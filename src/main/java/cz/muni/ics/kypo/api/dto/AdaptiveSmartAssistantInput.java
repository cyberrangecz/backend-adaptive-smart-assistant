package cz.muni.ics.kypo.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "AdaptiveSmartAssistantInput")
@Data
public class AdaptiveSmartAssistantInput {

    @ApiModelProperty(value = "The identifier of a given training run representing a given participant", example = "1")
    @NotNull(message = "{smartAssistantInput.trainingRunId.NotNull.message}")
    private Long trainingRunId;
    @ApiModelProperty(value = "The id of a phase X.", example = "5")
    @NotNull(message = "{smartAssistantInput.phaseX.NotNull.message}")
    private Long phaseX;
    @ApiModelProperty(value = "The number of tasks in a phase X.", example = "3")
    @NotNull(message = "{smartAssistantInput.phaseXTasks.NotNull.message}")
    @Min(value = 1, message = "{smartAssistantInput.phaseXTasks.Min.message}")
    private Integer phaseXTasks;
    @ApiModelProperty(value = "The list of phaseIds (the given phase including the given phases).", example = "[1,2,3,4,5]")
    private List<Long> phaseIds = new ArrayList<>();
    @ApiModelProperty(value = "The decision matrix with weights to compute the students' performance.")
    @NotEmpty(message = "{smartAssistantInput.decisionMatrix.NotEmpty.message}")
    private List<DecisionMatrixRowDTO> decisionMatrix = new ArrayList<>();
}
