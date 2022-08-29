package cz.muni.ics.kypo.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiModel(value = "OverallInstancePerformance")
public class OverallInstancePerformance {

    @ApiModelProperty(value = "The identifier of a given training run representing a given participant", example = "1")
    private Long traineeId;
    @ApiModelProperty(value = "List of input values for adaptive smart assistant for each phase together with decision matrix")
    private List<AdaptiveSmartAssistantInput> smartAssistantInput;
    @ApiModelProperty(value = "Performance statistics for each phase")
    private Map<Long, OverallPhaseStatistics> phasesSmartAssistantInput = new HashMap<>();

    public OverallInstancePerformance(Long traineeId, List<AdaptiveSmartAssistantInput> smartAssistantInput, Map<Long, OverallPhaseStatistics> phasesSmartAssistantInput) {
        this.traineeId = traineeId;
        this.smartAssistantInput = smartAssistantInput;
        this.phasesSmartAssistantInput = phasesSmartAssistantInput;
    }

    public OverallInstancePerformance() {
        super();
    }

    public Long getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(Long traineeId) {
        this.traineeId = traineeId;
    }

    public List<AdaptiveSmartAssistantInput> getSmartAssistantInput() {
        return smartAssistantInput;
    }

    public void setSmartAssistantInput(List<AdaptiveSmartAssistantInput> smartAssistantInput) {
        this.smartAssistantInput = smartAssistantInput;
    }

    public Map<Long, OverallPhaseStatistics> getPhasesSmartAssistantInput() {
        return phasesSmartAssistantInput;
    }

    public void setPhasesSmartAssistantInput(Map<Long, OverallPhaseStatistics> phasesSmartAssistantInput) {
        this.phasesSmartAssistantInput = phasesSmartAssistantInput;
    }

    @Override
    public String toString() {
        return "OverallInstancePerformance{" +
                "traineeId=" + traineeId +
                ", smartAssistantInput=" + smartAssistantInput +
                ", phasesSmartAssistantInput=" + phasesSmartAssistantInput +
                '}';
    }
}
