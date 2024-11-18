package cz.cyberrange.platform.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * This class is taken from project adaptive-training.
 */
@ApiModel(value = "DecisionMatrixRowDTO")
@Data
public class DecisionMatrixRowDTO {

    @ApiModelProperty(value = "ID of decision matrix row", required = true, example = "1")
    private long id;
    @ApiModelProperty(value = "Order of row in a decision matrix", required = true, example = "1")
    private int order;
    @ApiModelProperty(value = "It determines how important the answers of the questions in questionnaires are", required = true, example = "0.5")
    private double questionnaireAnswered;
    @ApiModelProperty(value = "It determines how important it is whether the player used the keyword", required = true, example = "0.5")
    private double keywordUsed;
    @ApiModelProperty(value = "It determines how important it is whether the player completed the task in time", required = true, example = "0.5")
    private double completedInTime;
    @ApiModelProperty(value = "It determines how important it is whether the player displayed the solution of the task they were solving", required = true, example = "0.5")
    private double solutionDisplayed;
    @ApiModelProperty(value = "It determines how important the number of wrong answers are", required = true, example = "0.5")
    private double wrongAnswers;
    @ApiModelProperty(value = "Info about phase (usually training phase) the decision matrix row is related to", required = true, example = "1")
    private RelatedPhaseInfoDTO relatedPhaseInfo;
    @ApiModelProperty(value = "Number of commands that are allowed to use in a training phase", example = "10")
    private long allowedCommands;
    @ApiModelProperty(value = "Number of wrong answers that are allowed in a training phase", example = "10")
    private long allowedWrongAnswers;
}
