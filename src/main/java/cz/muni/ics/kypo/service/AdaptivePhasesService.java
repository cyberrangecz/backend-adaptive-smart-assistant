package cz.muni.ics.kypo.service;

import cz.muni.ics.kypo.api.dto.*;
import cz.muni.ics.kypo.api.exceptions.EntityErrorDetail;
import cz.muni.ics.kypo.api.exceptions.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AdaptivePhasesService {

    private static final Logger LOG = LoggerFactory.getLogger(AdaptivePhasesService.class);

    private static final double ZERO = 0.0;
    private final ElasticSearchApiService elasticSearchApiService;

    @Autowired
    public AdaptivePhasesService(ElasticSearchApiService elasticSearchApiService) {
        this.elasticSearchApiService = elasticSearchApiService;
    }

    private double computeParticipantsPerformance(AdaptiveSmartAssistantInput smartAssistantInput) {
        Map<Long, OverallPhaseStatistics> overAllPhaseStatistics = elasticSearchApiService.getOverAllPhaseStatistics(smartAssistantInput.getTrainingRunId(), smartAssistantInput.getPhaseIds())
                .stream().collect(Collectors.toMap(OverallPhaseStatistics::getPhaseId, Function.identity()));
        return evaluateParticipantPerformance(smartAssistantInput, overAllPhaseStatistics);
    }

    /**
     * @param smartAssistantInput input for smart assistant, especially phase details
     * @return the suitable task in a phase x
     */
    public SuitableTaskResponseDto computeSuitableTask(AdaptiveSmartAssistantInput smartAssistantInput) {
        SuitableTaskResponseDto suitableTaskResponseDto = new SuitableTaskResponseDto();
        double participantsPerformance = computeParticipantsPerformance(smartAssistantInput); // must be in interval <0,1>
        if (participantsPerformance == ZERO) {
            suitableTaskResponseDto.setSuitableTask(smartAssistantInput.getPhaseXTasks());
            return suitableTaskResponseDto;
        } else {
            int suitableTask = ((int) (smartAssistantInput.getPhaseXTasks() * (1 - participantsPerformance))) + 1;
            suitableTaskResponseDto.setSuitableTask(suitableTask);
            return suitableTaskResponseDto;
        }
    }

    private double evaluateParticipantPerformance(AdaptiveSmartAssistantInput smartAssistantInput, Map<Long, OverallPhaseStatistics> overAllPhaseStatistics) {
        double sumOfAllWeights = ZERO;
        double participantWeightedPerformance = ZERO;
        long currentPhaseId = smartAssistantInput.getDecisionMatrix().get(smartAssistantInput.getDecisionMatrix().size() - 1).getRelatedPhaseInfo().getId();
        for (DecisionMatrixRowDTO decisionMatrixRow : smartAssistantInput.getDecisionMatrix()) {
            RelatedPhaseInfoDTO relatedPhaseInfo = decisionMatrixRow.getRelatedPhaseInfo();
            sumOfAllWeights += decisionMatrixRow.getQuestionnaireAnswered();
            participantWeightedPerformance += decisionMatrixRow.getQuestionnaireAnswered() * convertBooleanToBinaryDouble(relatedPhaseInfo.isCorrectlyAnsweredRelatedQuestions());
            if(decisionMatrixRow.getRelatedPhaseInfo().getId() == currentPhaseId) {
                break;
            }
            if (!elasticSearchDataAreNeeded(decisionMatrixRow)) {
                continue;
            }
            OverallPhaseStatistics relatedPhaseStatistics = Optional.ofNullable(overAllPhaseStatistics.get(relatedPhaseInfo.getId()))
                    .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(OverallPhaseStatistics.class, "id", Long.class, relatedPhaseInfo.getId(), "Statistics for phase not found")));
            if (decisionMatrixRow.getSolutionDisplayed() > ZERO) {
                participantWeightedPerformance += evaluateSolutionDisplayed(decisionMatrixRow, relatedPhaseStatistics);
                participantWeightedPerformance += evaluateKeywordUsed(decisionMatrixRow, relatedPhaseStatistics);
                participantWeightedPerformance += evaluateCompletedInTime(decisionMatrixRow, relatedPhaseStatistics, relatedPhaseInfo.getEstimatedPhaseTime());
                participantWeightedPerformance += evaluateWrongAnswers(decisionMatrixRow, relatedPhaseStatistics);
            }
            sumOfAllWeights += decisionMatrixRow.getCompletedInTime() + decisionMatrixRow.getSolutionDisplayed() +
                    decisionMatrixRow.getKeywordUsed() + decisionMatrixRow.getWrongAnswers();

        }
        if (sumOfAllWeights == 0) {
            LOG.error("No weights found for adaptive smart assistant input {}. The easiest task will be picked", smartAssistantInput);
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
