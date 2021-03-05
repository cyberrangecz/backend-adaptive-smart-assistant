package cz.muni.ics.kypo.service;

import cz.muni.ics.kypo.api.dto.SuitableTaskResponseDto;
import cz.muni.ics.kypo.api.exceptions.EntityErrorDetail;
import cz.muni.ics.kypo.api.exceptions.EntityNotFoundException;
import cz.muni.ics.kypo.api.dto.AdaptiveSmartAssistantInput;
import cz.muni.ics.kypo.api.dto.DecisionMatrixRowDTO;
import cz.muni.ics.kypo.api.dto.OverallPhaseStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AdaptivePhasesService {

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
        for (DecisionMatrixRowDTO decisionMatrixRow : smartAssistantInput.getDecisionMatrix()) {
            OverallPhaseStatistics relatedPhaseStatistics = Optional.ofNullable(overAllPhaseStatistics.get(decisionMatrixRow.getRelatedPhaseId()))
                    .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(OverallPhaseStatistics.class, "id", Long.class, decisionMatrixRow.getRelatedPhaseId(), "Statistics for phase not found")));
            if (decisionMatrixRow.getQuestionnaireAnswered() > ZERO) {
                sumOfAllWeights += decisionMatrixRow.getQuestionnaireAnswered();
                participantWeightedPerformance += decisionMatrixRow.getQuestionnaireAnswered() * convertBooleanToBinaryDouble(smartAssistantInput.getQuestionnaireCorrectlyAnswered());
            }
            if (decisionMatrixRow.getCompletedInTime() > ZERO) {
                sumOfAllWeights += decisionMatrixRow.getCompletedInTime();
                participantWeightedPerformance += evaluateCompletedInTime(decisionMatrixRow, relatedPhaseStatistics);
            }
            if (decisionMatrixRow.getKeywordUsed() > ZERO) {
                sumOfAllWeights += decisionMatrixRow.getKeywordUsed();
                participantWeightedPerformance += evaluateKeywordUsed(decisionMatrixRow, relatedPhaseStatistics);
            }
            if (decisionMatrixRow.getSolutionDisplayed() > ZERO) {
                sumOfAllWeights += decisionMatrixRow.getSolutionDisplayed();
                participantWeightedPerformance += evaluateSolutionDisplayed(decisionMatrixRow, relatedPhaseStatistics);
            }
            if (decisionMatrixRow.getWrongAnswers() > ZERO) {
                sumOfAllWeights += decisionMatrixRow.getWrongAnswers();
                participantWeightedPerformance += evaluateWrongAnswers(decisionMatrixRow, relatedPhaseStatistics);
            }
        }
        return participantWeightedPerformance / sumOfAllWeights;
    }

    private double evaluateCompletedInTime(DecisionMatrixRowDTO decisionMatrixRow, OverallPhaseStatistics phaseStatistics) {
        return decisionMatrixRow.getCompletedInTime() * convertBooleanToBinaryDouble(phaseStatistics.getPhaseTime() < 0);
    }

    private double evaluateKeywordUsed(DecisionMatrixRowDTO decisionMatrixRow, OverallPhaseStatistics phaseStatistics) {
        long numberOfCommands = 0;
        if (!CollectionUtils.isEmpty(phaseStatistics.getKeywordsInCommands())) {
            numberOfCommands = phaseStatistics.getKeywordsInCommands()
                    .values()
                    .stream()
                    .mapToLong(Long::valueOf)
                    .sum();
        }
        return decisionMatrixRow.getKeywordUsed() * convertBooleanToBinaryDouble(numberOfCommands < decisionMatrixRow.getAllowedCommands());
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

}
