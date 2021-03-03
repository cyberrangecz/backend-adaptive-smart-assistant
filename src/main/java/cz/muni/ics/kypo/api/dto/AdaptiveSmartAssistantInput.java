package cz.muni.ics.kypo.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "AdaptiveSmartAssistantInput")
public class AdaptiveSmartAssistantInput {

    @ApiModelProperty(value = "The identifier of a given training run representing a given participant", example = "1")
    private Long trainingRunId;
    @ApiModelProperty(value = "The id of a phase X.", example = "5")
    private Long phaseX;
    @ApiModelProperty(value = "The number of tasks in a phase X.", example = "3")
    private Integer phaseXTasks;
    @ApiModelProperty(value = "The list of phaseIds (the given phase including the given phases).", example = "[1,2,3,4,5]")
    private List<Long> phaseIds;
    @ApiModelProperty(value = "The decision matrix with weights to compute the students' performance.")
    private List<DecisionMatrixRowDTO> decisionMatrix;
    @ApiModelProperty(value = "The information if the questionnaire was correctly answered for a given phase.", example = "true")
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

    @Override
    public String toString() {
        return "AdaptiveSmartAssistantInput{" +
                "trainingRunId=" + trainingRunId +
                ", phaseX=" + phaseX +
                ", phaseXTasks=" + phaseXTasks +
                ", phaseIds=" + phaseIds +
                ", decisionMatrix=" + decisionMatrix +
                ", questionnaireCorrectlyAnswered=" + questionnaireCorrectlyAnswered +
                '}';
    }
}
