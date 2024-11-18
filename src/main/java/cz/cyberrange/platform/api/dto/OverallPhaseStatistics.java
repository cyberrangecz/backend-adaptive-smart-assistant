package cz.cyberrange.platform.api.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is taken from project elasticsearch-service.
 */
@ApiModel(value = "OverallPhaseStatistics")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class OverallPhaseStatistics {
    @ApiModelProperty(value = "ID of a phase", example = "1")
    private Long phaseId;
    @ApiModelProperty(value = "ID of a task", example = "3")
    private Long taskId;
    @ApiModelProperty(value = "ID of a sandbox", example = "1")
    private Long sandboxId;
    @ApiModelProperty(value = "Order of a phase", example = "0")
    private Long phaseOrder;
    @ApiModelProperty(value = "ID of a task", example = "1614803536837")
    private Long phaseTime;
    @ApiModelProperty(value = "The list of answers (flags) that participant submitted", example = "[\"nmap 123\", \"nmap 123\"]")
    private List<String> wrongAnswers = new ArrayList<>();
    @ApiModelProperty(value = "The information if the solution was displayed", example = "true")
    private Boolean solutionDisplayed;
    @ApiModelProperty(value = "The number of submitted commands", example = "5")
    private Long numberOfCommands;
    @ApiModelProperty(value = "The map containing the mapping if the commands contains the right keywords")
    private Map<String, Long> keywordsInCommands = new HashMap<>();
    @ApiModelProperty(value = "List of trainees answers", example = "[true,false,true,true]")
    private List<Boolean> questionsAnswer = new ArrayList<>();
}
