package cz.cyberrange.platform.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/** This class provides additional info about phase taken by the participant. */
@ApiModel(value = "DecisionMatrixRowDTO")
@Data
public class RelatedPhaseInfoDTO {

  @ApiModelProperty(value = "ID of a phase", example = "1")
  private Long id;

  @ApiModelProperty(
      value = "The information if the questionnaire was correctly answered for a given phase.",
      example = "true")
  private boolean correctlyAnsweredRelatedQuestions;

  @ApiModelProperty(
      value = "Estimated time (minutes) taken by the player to solve the training phase",
      example = "1614803536837")
  private int estimatedPhaseTime;
}
