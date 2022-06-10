package cz.muni.ics.kypo.service;

import cz.muni.ics.kypo.api.dto.*;
import cz.muni.ics.kypo.api.exceptions.EntityErrorDetail;
import cz.muni.ics.kypo.api.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdaptivePhasesService {

    private static final double ZERO = 0.0;
    private final ElasticSearchApiService elasticSearchApiService;

    /**
     * Computes suitable tasks for given participants based on their performance.
     * @param overallInstancePerformances Overall performances of all participants.
     * @return the suitable tasks in a phases for all trainees
     */
    public List<List<SuitableTaskResponseDto>> computeSuitableTask(List<OverallInstancePerformance> overallInstancePerformances) {
        List<List<SuitableTaskResponseDto>> suitableTasksForTrainees = new ArrayList<>();
        overallInstancePerformances.forEach(trainee -> {
            List<SuitableTaskResponseDto> suitableTasksResponse = new ArrayList<>();
            trainee.getSmartAssistantInput().forEach(smartAssistantInput -> {
                double participantsPerformance = computeParticipantsPerformance(smartAssistantInput, trainee.getPhasesSmartAssistantInput());
                suitableTasksResponse.add(findSuitableTask(participantsPerformance, smartAssistantInput));
            });
            suitableTasksForTrainees.add(suitableTasksResponse);
        });
        return suitableTasksForTrainees;
    }

    /**
     *
     * @param smartAssistantInput input for smart assistant, especially phase details
     * @return the suitable task in a phase x
     */
    public SuitableTaskResponseDto computeSuitableTask(AdaptiveSmartAssistantInput smartAssistantInput, String accessToken, Long userId) {
        double participantsPerformance = computeParticipantsPerformance(smartAssistantInput, accessToken, userId); // must be in interval <0,1>
        return findSuitableTask(participantsPerformance, smartAssistantInput);
    }

    private double computeParticipantsPerformance(AdaptiveSmartAssistantInput smartAssistantInput, String accessToken, Long userId) {
        Map<Long, OverallPhaseStatistics> overAllPhaseStatistics = elasticSearchApiService.getOverAllPhaseStatistics(
                        smartAssistantInput.getTrainingRunId(),
                        smartAssistantInput.getPhaseIds(),
                        accessToken,
                        userId
                )
                .stream().collect(Collectors.toMap(OverallPhaseStatistics::getPhaseId, Function.identity()));
        log.debug("For training run (ID: " + smartAssistantInput.getTrainingRunId() + ") of the user (ID: " + userId + ") " +
                "the following statistics were used to compute theirs performance: \n " + overAllPhaseStatistics);
        log.error(overAllPhaseStatistics.toString());
        log.error(smartAssistantInput.toString());
        return evaluateParticipantPerformance(smartAssistantInput, overAllPhaseStatistics);
    }

    private double computeParticipantsPerformance(AdaptiveSmartAssistantInput smartAssistantInput, Map<Long,OverallPhaseStatistics> overallPhaseStatistics) {
        return evaluateParticipantPerformance(smartAssistantInput, overallPhaseStatistics);
    }

    /**
     * This method is equal to the third equation in the paper: https://www.muni.cz/en/research/publications/1783806
     */
    private SuitableTaskResponseDto findSuitableTask(double participantsPerformance, AdaptiveSmartAssistantInput smartAssistantInput) {
        SuitableTaskResponseDto suitableTaskResponseDto = new SuitableTaskResponseDto();
        if (participantsPerformance == ZERO) {
            suitableTaskResponseDto.setSuitableTask(smartAssistantInput.getPhaseXTasks());
        } else {
            // the (int) cast is equal to the trunc function in the third equation in the above-mentioned paper
            int suitableTask = ((int) (smartAssistantInput.getPhaseXTasks() * (1 - participantsPerformance))) + 1;
            suitableTaskResponseDto.setSuitableTask(suitableTask);
        }
        return suitableTaskResponseDto;
    }

    private double evaluateParticipantPerformance(AdaptiveSmartAssistantInput smartAssistantInput, Map<Long, OverallPhaseStatistics> overAllPhaseStatistics) {
        double sumOfAllWeights = ZERO;
        double participantWeightedPerformance = ZERO;
        long currentPhaseId = smartAssistantInput.getDecisionMatrix().get(smartAssistantInput.getDecisionMatrix().size() - 1).getRelatedPhaseInfo().getId();
        for (DecisionMatrixRowDTO decisionMatrixRow : smartAssistantInput.getDecisionMatrix()) {
            RelatedPhaseInfoDTO relatedPhaseInfo = decisionMatrixRow.getRelatedPhaseInfo();
            sumOfAllWeights += decisionMatrixRow.getQuestionnaireAnswered();
            participantWeightedPerformance += decisionMatrixRow.getQuestionnaireAnswered() * convertBooleanToBinaryDouble(relatedPhaseInfo.isCorrectlyAnsweredRelatedQuestions());
            if (decisionMatrixRow.getRelatedPhaseInfo().getId() == currentPhaseId) {
                break;
            }
            if (!elasticSearchDataAreNeeded(decisionMatrixRow)) {
                continue;
            }
            OverallPhaseStatistics relatedPhaseStatistics = Optional.ofNullable(overAllPhaseStatistics.get(relatedPhaseInfo.getId()))
                    .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(OverallPhaseStatistics.class, "id", Long.class, relatedPhaseInfo.getId(), "Statistics for phase not found")));
            if (!relatedPhaseStatistics.getSolutionDisplayed()) {
                participantWeightedPerformance += evaluateSolutionDisplayed(decisionMatrixRow, relatedPhaseStatistics);
                participantWeightedPerformance += evaluateKeywordUsed(decisionMatrixRow, relatedPhaseStatistics);
                participantWeightedPerformance += evaluateCompletedInTime(decisionMatrixRow, relatedPhaseStatistics, relatedPhaseInfo.getEstimatedPhaseTime());
                participantWeightedPerformance += evaluateWrongAnswers(decisionMatrixRow, relatedPhaseStatistics);
            }
            sumOfAllWeights += decisionMatrixRow.getCompletedInTime() + decisionMatrixRow.getSolutionDisplayed() +
                    decisionMatrixRow.getKeywordUsed() + decisionMatrixRow.getWrongAnswers();

        }
        if (sumOfAllWeights == 0) {
            log.error("No weights found for adaptive smart assistant input {}. The easiest task will be picked", smartAssistantInput);
            return 0.0;
        }
        return participantWeightedPerformance / sumOfAllWeights;
    }

    private double evaluateCompletedInTime(DecisionMatrixRowDTO decisionMatrixRow,
                                           OverallPhaseStatistics phaseStatistics,
                                           long estimatedPhaseTime) {
        Long estimatedTimeInMillis = TimeUnit.MINUTES.toMillis(estimatedPhaseTime);
        return decisionMatrixRow.getCompletedInTime() * convertBooleanToBinaryDouble(phaseStatistics.getPhaseTime() < estimatedTimeInMillis);
    }

    private double evaluateKeywordUsed(DecisionMatrixRowDTO decisionMatrixRow, OverallPhaseStatistics phaseStatistics) {
        return decisionMatrixRow.getKeywordUsed() * convertBooleanToBinaryDouble(phaseStatistics.getNumberOfCommands() < decisionMatrixRow.getAllowedCommands());
    }

    private double evaluateSolutionDisplayed(DecisionMatrixRowDTO decisionMatrixRow, OverallPhaseStatistics phaseStatistics) {
        return decisionMatrixRow.getSolutionDisplayed() * convertBooleanToBinaryDoubleNegated(phaseStatistics.getSolutionDisplayed());
    }

    private double evaluateWrongAnswers(DecisionMatrixRowDTO decisionMatrixRow, OverallPhaseStatistics phaseStatistics) {
        return decisionMatrixRow.getWrongAnswers() * convertBooleanToBinaryDouble(phaseStatistics.getWrongAnswers().size() < decisionMatrixRow.getAllowedWrongAnswers());
    }

    private double convertBooleanToBinaryDouble(Boolean isCorrect) {
        return Boolean.TRUE.equals(isCorrect) ? 1.0 : ZERO;
    }

    private double convertBooleanToBinaryDoubleNegated(Boolean isCorrect) {
        return Boolean.TRUE.equals(isCorrect) ? ZERO : 1.0;
    }

    private boolean elasticSearchDataAreNeeded(DecisionMatrixRowDTO decisionMatrixRow) {
        return decisionMatrixRow.getCompletedInTime() > ZERO ||
                decisionMatrixRow.getKeywordUsed() > ZERO ||
                decisionMatrixRow.getSolutionDisplayed() > ZERO ||
                decisionMatrixRow.getWrongAnswers() > ZERO;
    }
}
