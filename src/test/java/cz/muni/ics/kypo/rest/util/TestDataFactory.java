package cz.muni.ics.kypo.rest.util;

import cz.muni.ics.kypo.api.dto.AdaptiveSmartAssistantInput;
import cz.muni.ics.kypo.api.dto.DecisionMatrixRowDTO;
import cz.muni.ics.kypo.api.dto.OverallPhaseStatistics;
import cz.muni.ics.kypo.api.dto.RelatedPhaseInfoDTO;
import cz.muni.ics.kypo.api.dto.SuitableTaskResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class TestDataFactory {

    public List<OverallPhaseStatistics> getOverallPhaseStatisticsAllCorrect() {
        OverallPhaseStatistics overallPhaseStatistics = new OverallPhaseStatistics();
        overallPhaseStatistics.setPhaseId(2L);
        overallPhaseStatistics.setNumberOfCommands(3L);
        overallPhaseStatistics.setPhaseTime(-10L);
        overallPhaseStatistics.setSolutionDisplayed(false);
        overallPhaseStatistics.setTaskId(1L);
        overallPhaseStatistics.setWrongAnswers(List.of());
        overallPhaseStatistics.setKeywordsInCommands(Map.of());
        return List.of(overallPhaseStatistics);
    }

    public List<OverallPhaseStatistics> getOverallPhaseStatisticsAllWrong() {
        OverallPhaseStatistics overallPhaseStatistics = new OverallPhaseStatistics();
        overallPhaseStatistics.setPhaseId(2L);
        overallPhaseStatistics.setNumberOfCommands(20L);
        overallPhaseStatistics.setPhaseTime(30L);
        overallPhaseStatistics.setSolutionDisplayed(true);
        overallPhaseStatistics.setTaskId(2L);
        overallPhaseStatistics.setWrongAnswers(List.of("wrong", "completely wrong", "not even close", "false", "far far away"));
        overallPhaseStatistics.setKeywordsInCommands(Map.of("cat", 20L));
        return List.of(overallPhaseStatistics);
    }

    public List<OverallPhaseStatistics> getOverallPhaseStatisticsCombinedPerformance() {
        OverallPhaseStatistics overallPhaseStatistics = new OverallPhaseStatistics();
        overallPhaseStatistics.setPhaseId(2L);
        overallPhaseStatistics.setNumberOfCommands(5L);
        overallPhaseStatistics.setPhaseTime(30L);
        overallPhaseStatistics.setSolutionDisplayed(false);
        overallPhaseStatistics.setTaskId(2L);
        overallPhaseStatistics.setWrongAnswers(List.of("wrong", "a bit wrong"));
        overallPhaseStatistics.setKeywordsInCommands(Map.of("cat", 1L));
        return List.of(overallPhaseStatistics);
    }

    public List<OverallPhaseStatistics> getOverallPhaseStatisticsWrongPhaseId() {
        OverallPhaseStatistics overallPhaseStatistics = new OverallPhaseStatistics();
        overallPhaseStatistics.setPhaseId(12345L);
        overallPhaseStatistics.setNumberOfCommands(3L);
        overallPhaseStatistics.setPhaseTime(-10L);
        overallPhaseStatistics.setSolutionDisplayed(false);
        overallPhaseStatistics.setTaskId(1L);
        overallPhaseStatistics.setWrongAnswers(List.of());
        overallPhaseStatistics.setKeywordsInCommands(Map.of());
        return List.of(overallPhaseStatistics);
    }

    public AdaptiveSmartAssistantInput getAdaptiveSmartAssistantInput1() {
        AdaptiveSmartAssistantInput adaptiveSmartAssistantInput = new AdaptiveSmartAssistantInput();
        adaptiveSmartAssistantInput.setPhaseX(1L);
        adaptiveSmartAssistantInput.setPhaseXTasks(3);
        adaptiveSmartAssistantInput.setPhaseIds(List.of(1L));
        adaptiveSmartAssistantInput.setTrainingRunId(1L);
        adaptiveSmartAssistantInput.setDecisionMatrix(List.of(getDecisionMatrixRowOnlyQuestionnaireMatters()));
        return adaptiveSmartAssistantInput;
    }

    public AdaptiveSmartAssistantInput getAdaptiveSmartAssistantInput2() {
        AdaptiveSmartAssistantInput adaptiveSmartAssistantInput = new AdaptiveSmartAssistantInput();
        adaptiveSmartAssistantInput.setPhaseX(2L);
        adaptiveSmartAssistantInput.setPhaseXTasks(3);
        adaptiveSmartAssistantInput.setPhaseIds(List.of(2L));
        adaptiveSmartAssistantInput.setTrainingRunId(1L);
        adaptiveSmartAssistantInput.setDecisionMatrix(List.of(getDecisionMatrixRowOnlyQuestionnaireMatters(), getDecisionMatrixRowEverythingMatters()));
        return adaptiveSmartAssistantInput;
    }

    public AdaptiveSmartAssistantInput getAdaptiveSmartAssistantInput3() {
        AdaptiveSmartAssistantInput adaptiveSmartAssistantInput = new AdaptiveSmartAssistantInput();
        adaptiveSmartAssistantInput.setPhaseX(2L);
        adaptiveSmartAssistantInput.setPhaseXTasks(5);
        adaptiveSmartAssistantInput.setPhaseIds(List.of(2L));
        adaptiveSmartAssistantInput.setTrainingRunId(1L);
        adaptiveSmartAssistantInput.setDecisionMatrix(List.of(getDecisionMatrixRowOnlyQuestionnaireMatters(), getDecisionMatrixRowEverythingMatters()));
        return adaptiveSmartAssistantInput;
    }

    public AdaptiveSmartAssistantInput getAdaptiveSmartAssistantInput4() {
        AdaptiveSmartAssistantInput adaptiveSmartAssistantInput = new AdaptiveSmartAssistantInput();
        adaptiveSmartAssistantInput.setPhaseX(2L);
        adaptiveSmartAssistantInput.setPhaseXTasks(5);
        adaptiveSmartAssistantInput.setPhaseIds(List.of(2L));
        adaptiveSmartAssistantInput.setTrainingRunId(1L);
        adaptiveSmartAssistantInput.setDecisionMatrix(List.of(getDecisionMatrixRowOnlyQuestionnaireMatters(), getDecisionMatrixRowEverythingMattersInDifferentWeights()));
        return adaptiveSmartAssistantInput;
    }

    public AdaptiveSmartAssistantInput getAdaptiveSmartAssistantInputZeroWeightsInDecisionMatrix() {
        AdaptiveSmartAssistantInput adaptiveSmartAssistantInput = new AdaptiveSmartAssistantInput();
        adaptiveSmartAssistantInput.setPhaseX(1L);
        adaptiveSmartAssistantInput.setPhaseXTasks(3);
        adaptiveSmartAssistantInput.setPhaseIds(List.of(1L));
        adaptiveSmartAssistantInput.setTrainingRunId(1L);
        adaptiveSmartAssistantInput.setDecisionMatrix(List.of(getDecisionMatrixRowZeroWeights()));
        return adaptiveSmartAssistantInput;
    }

    public SuitableTaskResponseDto getSuitableTaskResponseOne() {
        SuitableTaskResponseDto suitableTaskResponseDto = new SuitableTaskResponseDto();
        suitableTaskResponseDto.setSuitableTask(1);
        return suitableTaskResponseDto;
    }

    public SuitableTaskResponseDto getSuitableTaskResponseTwo() {
        SuitableTaskResponseDto suitableTaskResponseDto = new SuitableTaskResponseDto();
        suitableTaskResponseDto.setSuitableTask(2);
        return suitableTaskResponseDto;
    }

    public SuitableTaskResponseDto getSuitableTaskResponseThree() {
        SuitableTaskResponseDto suitableTaskResponseDto = new SuitableTaskResponseDto();
        suitableTaskResponseDto.setSuitableTask(3);
        return suitableTaskResponseDto;
    }

    public SuitableTaskResponseDto getSuitableTaskResponseFive() {
        SuitableTaskResponseDto suitableTaskResponseDto = new SuitableTaskResponseDto();
        suitableTaskResponseDto.setSuitableTask(5);
        return suitableTaskResponseDto;
    }

    private DecisionMatrixRowDTO getDecisionMatrixRowOnlyQuestionnaireMatters() {
        DecisionMatrixRowDTO decisionMatrixRowDTO = new DecisionMatrixRowDTO();
        decisionMatrixRowDTO.setId(1);
        decisionMatrixRowDTO.setOrder(0);
        decisionMatrixRowDTO.setCompletedInTime(0);
        decisionMatrixRowDTO.setKeywordUsed(0);
        decisionMatrixRowDTO.setQuestionnaireAnswered(1);
        decisionMatrixRowDTO.setRelatedPhaseInfo(getRelatedPhaseInfo(1L, 5,false));
        decisionMatrixRowDTO.setSolutionDisplayed(0);
        decisionMatrixRowDTO.setWrongAnswers(0);
        return decisionMatrixRowDTO;
    }

    private DecisionMatrixRowDTO getDecisionMatrixRowEverythingMatters() {
        DecisionMatrixRowDTO decisionMatrixRowDTO = new DecisionMatrixRowDTO();
        decisionMatrixRowDTO.setId(2);
        decisionMatrixRowDTO.setOrder(1);
        decisionMatrixRowDTO.setCompletedInTime(1);
        decisionMatrixRowDTO.setKeywordUsed(1);
        decisionMatrixRowDTO.setQuestionnaireAnswered(1);
        decisionMatrixRowDTO.setRelatedPhaseInfo(getRelatedPhaseInfo(2L, 7, true));
        decisionMatrixRowDTO.setSolutionDisplayed(1);
        decisionMatrixRowDTO.setWrongAnswers(1);
        decisionMatrixRowDTO.setAllowedCommands(10);
        decisionMatrixRowDTO.setAllowedWrongAnswers(3);
        return decisionMatrixRowDTO;
    }

    private DecisionMatrixRowDTO getDecisionMatrixRowEverythingMattersInDifferentWeights() {
        DecisionMatrixRowDTO decisionMatrixRowDTO = new DecisionMatrixRowDTO();
        decisionMatrixRowDTO.setId(2);
        decisionMatrixRowDTO.setOrder(1);
        decisionMatrixRowDTO.setCompletedInTime(0.3);
        decisionMatrixRowDTO.setKeywordUsed(0.9);
        decisionMatrixRowDTO.setQuestionnaireAnswered(0.8);
        decisionMatrixRowDTO.setRelatedPhaseInfo(getRelatedPhaseInfo(2L, 10, false));
        decisionMatrixRowDTO.setSolutionDisplayed(0.75);
        decisionMatrixRowDTO.setWrongAnswers(0.25);
        decisionMatrixRowDTO.setAllowedCommands(10);
        decisionMatrixRowDTO.setAllowedWrongAnswers(3);
        return decisionMatrixRowDTO;
    }

    private DecisionMatrixRowDTO getDecisionMatrixRowZeroWeights() {
        DecisionMatrixRowDTO decisionMatrixRowDTO = new DecisionMatrixRowDTO();
        decisionMatrixRowDTO.setId(1);
        decisionMatrixRowDTO.setOrder(0);
        decisionMatrixRowDTO.setCompletedInTime(0);
        decisionMatrixRowDTO.setKeywordUsed(0);
        decisionMatrixRowDTO.setQuestionnaireAnswered(0);
        decisionMatrixRowDTO.setRelatedPhaseInfo(getRelatedPhaseInfo(1L, 5, true));
        decisionMatrixRowDTO.setSolutionDisplayed(0);
        decisionMatrixRowDTO.setWrongAnswers(0);
        decisionMatrixRowDTO.setAllowedCommands(10);
        decisionMatrixRowDTO.setAllowedWrongAnswers(3);
        return decisionMatrixRowDTO;
    }

    private RelatedPhaseInfoDTO getRelatedPhaseInfo(Long phaseId, int estimatedTime, boolean correctlyAnsweredRelatedQuestions) {
        RelatedPhaseInfoDTO relatedPhaseInfoDTO = new RelatedPhaseInfoDTO();
        relatedPhaseInfoDTO.setId(phaseId);
        relatedPhaseInfoDTO.setEstimatedPhaseTime(estimatedTime);
        relatedPhaseInfoDTO.setCorrectlyAnsweredRelatedQuestions(correctlyAnsweredRelatedQuestions);
        return relatedPhaseInfoDTO;
    }
}
