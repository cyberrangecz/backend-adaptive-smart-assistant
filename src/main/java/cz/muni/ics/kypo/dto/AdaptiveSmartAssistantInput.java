package cz.muni.ics.kypo.dto;

import java.util.List;

public class AdaptiveSmartAssistantInput {

    private Long trainingRunId;
    private Long phaseX;
    private Integer phaseXTasks;
    private List<Long> phaseIds;
    private List<DecisionMatrixRowDTO> decisionMatrix;
    private Boolean questionnaireCorrectlyAnswered;

    public Long getTrainingRunId() {
        return trainingRunId;
    }

    public void setTrainingRunId(Long trainingRunId) {
        this.trainingRunId = trainingRunId;
    }

    public Long getPhaseX() {
        return phaseX;
    }

    public void setPhaseX(Long phaseX) {
        this.phaseX = phaseX;
    }

    public Integer getPhaseXTasks() {
        return phaseXTasks;
    }

    public void setPhaseXTasks(Integer phaseXTasks) {
        this.phaseXTasks = phaseXTasks;
    }

    public List<Long> getPhaseIds() {
        return phaseIds;
    }

    public void setPhaseIds(List<Long> phaseIds) {
        this.phaseIds = phaseIds;
    }

    public List<DecisionMatrixRowDTO> getDecisionMatrix() {
        return decisionMatrix;
    }

    public void setDecisionMatrix(List<DecisionMatrixRowDTO> decisionMatrix) {
        this.decisionMatrix = decisionMatrix;
    }

    public Boolean getQuestionnaireCorrectlyAnswered() {
        return questionnaireCorrectlyAnswered;
    }

    public void setQuestionnaireCorrectlyAnswered(Boolean questionnaireCorrectlyAnswered) {
        this.questionnaireCorrectlyAnswered = questionnaireCorrectlyAnswered;
    }
}
