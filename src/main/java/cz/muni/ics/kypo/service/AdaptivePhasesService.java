package cz.muni.ics.kypo.service;

import cz.muni.ics.kypo.api.SuitableTaskResponseDto;
import cz.muni.ics.kypo.dto.AdaptiveSmartAssistantInput;
import cz.muni.ics.kypo.dto.DecisionMatrixRowDTO;
import cz.muni.ics.kypo.dto.OverallPhaseStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdaptivePhasesService {

    private final ElasticSearchApiService elasticSearchApiService;

    @Autowired
    public AdaptivePhasesService(ElasticSearchApiService elasticSearchApiService) {
        this.elasticSearchApiService = elasticSearchApiService;
    }

    private double computeParticipantsPerformance(AdaptiveSmartAssistantInput smartAssistantInput) {
        List<OverallPhaseStatistics> overAllPhaseStatistics = elasticSearchApiService.getOverAllPhaseStatistics(smartAssistantInput.getTrainingRunId(), smartAssistantInput.getPhaseIds());
        return evaluateParticipantPerformance(smartAssistantInput, overAllPhaseStatistics);
    }

    /**
     * @param smartAssistantInput input for smart assistant, especially phase details
     * @return the suitable task in a phase x
     */
    public SuitableTaskResponseDto computeSuitableTask(AdaptiveSmartAssistantInput smartAssistantInput) {
        SuitableTaskResponseDto suitableTaskResponseDto = new SuitableTaskResponseDto();
        double participantsPerformance = computeParticipantsPerformance(smartAssistantInput); // must be in interval <0,1>
        if (participantsPerformance == 0.0) {
            suitableTaskResponseDto.setSuitableTask(smartAssistantInput.getPhaseXTasks());
            return suitableTaskResponseDto;
        } else {
            int suitableTask = ((int) (smartAssistantInput.getPhaseXTasks() * (1 - participantsPerformance))) + 1;
            suitableTaskResponseDto.setSuitableTask(suitableTask);
            return suitableTaskResponseDto;
        }
    }

    private double evaluateParticipantPerformance(AdaptiveSmartAssistantInput smartAssistantInput, List<OverallPhaseStatistics> overAllPhaseStatistics) {
        double sumOfAllWeights = 0.0;
        double participantWeightedPerformance = 0.0;
        for (DecisionMatrixRowDTO decisionMatrixRow : smartAssistantInput.getDecisionMatrix()) {
            if (decisionMatrixRow.getQuestionnaireAnswered() > 0.0) {
                sumOfAllWeights += decisionMatrixRow.getQuestionnaireAnswered();
                participantWeightedPerformance += decisionMatrixRow.getQuestionnaireAnswered() *  convertBooleanToBinaryDouble(smartAssistantInput.getQuestionnaireCorrectlyAnswered());
            }
            if (decisionMatrixRow.getCompletedInTime() > 0.0) {
                sumOfAllWeights += decisionMatrixRow.getCompletedInTime();
                participantWeightedPerformance += evaluateCompletedInTime(decisionMatrixRow, overAllPhaseStatistics);
            }
            if (decisionMatrixRow.getKeywordUsed() > 0.0) {
                sumOfAllWeights += decisionMatrixRow.getKeywordUsed();
                participantWeightedPerformance += evaluateKeywordUsed(decisionMatrixRow, overAllPhaseStatistics);
            }
            if (decisionMatrixRow.getSolutionDisplayed() > 0.0) {
                sumOfAllWeights += decisionMatrixRow.getSolutionDisplayed();
                participantWeightedPerformance += evaluateSolutionDisplayed(decisionMatrixRow, overAllPhaseStatistics);
            }
            if (decisionMatrixRow.getWrongAnswers() > 0.0) {
                sumOfAllWeights += decisionMatrixRow.getWrongAnswers();
                participantWeightedPerformance += evaluateWrongAnswers(decisionMatrixRow, overAllPhaseStatistics);
            }
        }
        return participantWeightedPerformance / sumOfAllWeights;
    }

    private double evaluateCompletedInTime(DecisionMatrixRowDTO decisionMatrixRow, List<OverallPhaseStatistics> overAllPhaseStatistics) {
        OverallPhaseStatistics phaseStatistics = findPhaseStatisticsByPhaseId(decisionMatrixRow.getRelatedPhaseId(), overAllPhaseStatistics);
        return decisionMatrixRow.getCompletedInTime() * convertBooleanToBinaryDouble(phaseStatistics.getPhaseTime() < 0);
    }

    private double evaluateKeywordUsed(DecisionMatrixRowDTO decisionMatrixRow, List<OverallPhaseStatistics> overAllPhaseStatistics) {
        OverallPhaseStatistics phaseStatistics = findPhaseStatisticsByPhaseId(decisionMatrixRow.getRelatedPhaseId(), overAllPhaseStatistics);

        // TODO currently not sure what is supposed to happen here
        return 0.0;
    }

    private double evaluateSolutionDisplayed(DecisionMatrixRowDTO decisionMatrixRow, List<OverallPhaseStatistics> overAllPhaseStatistics) {
        OverallPhaseStatistics phaseStatistics = findPhaseStatisticsByPhaseId(decisionMatrixRow.getRelatedPhaseId(), overAllPhaseStatistics);
        return  decisionMatrixRow.getSolutionDisplayed() * convertBooleanToBinaryDoubleNegated(phaseStatistics.getSolutionDisplayed());
    }

    private double evaluateWrongAnswers(DecisionMatrixRowDTO decisionMatrixRow, List<OverallPhaseStatistics> overAllPhaseStatistics) {
        OverallPhaseStatistics phaseStatistics = findPhaseStatisticsByPhaseId(decisionMatrixRow.getRelatedPhaseId(), overAllPhaseStatistics);
        // TODO limit of wrong answers must be taken from training definition
        return decisionMatrixRow.getCompletedInTime() * convertBooleanToBinaryDouble(phaseStatistics.getWrongAnswers().size() < 10);
    }

    private double convertBooleanToBinaryDouble(Boolean isCorrect) {
        return Boolean.TRUE.equals(isCorrect) ? 1.0 : 0.0;
    }

    private double convertBooleanToBinaryDoubleNegated(Boolean isCorrect) {
        return Boolean.TRUE.equals(isCorrect) ? 0.0 : 1.0;
    }

    private OverallPhaseStatistics findPhaseStatisticsByPhaseId(long phaseId, List<OverallPhaseStatistics> overAllPhaseStatistics) {
        return overAllPhaseStatistics.stream()
                .filter(x -> x.getPhaseId().equals(phaseId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Phase not found for id " + phaseId)); // TODO throw a proper exception
    }
}
