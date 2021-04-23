package cz.muni.ics.kypo.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * This class provides additional info about phase taken by the participant.
 */
@ApiModel(value = "DecisionMatrixRowDTO")
public class RelatedPhaseInfoDTO {

    @ApiModelProperty(value = "ID of a phase", example = "1")
    private Long id;
    @ApiModelProperty(value = "The information if the questionnaire was correctly answered for a given phase.", example = "true")
    private boolean correctlyAnsweredRelatedQuestions;
    @ApiModelProperty(value = "Estimated time (minutes) taken by the player to solve the training phase", example = "1614803536837")
    private int estimatedPhaseTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isCorrectlyAnsweredRelatedQuestions() {
        return correctlyAnsweredRelatedQuestions;
    }

    public void setCorrectlyAnsweredRelatedQuestions(boolean correctlyAnsweredRelatedQuestions) {
        this.correctlyAnsweredRelatedQuestions = correctlyAnsweredRelatedQuestions;
    }

    public int getEstimatedPhaseTime() {
        return estimatedPhaseTime;
    }

    public void setEstimatedPhaseTime(int estimatedPhaseTime) {
        this.estimatedPhaseTime = estimatedPhaseTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RelatedPhaseInfoDTO)) return false;
        RelatedPhaseInfoDTO that = (RelatedPhaseInfoDTO) o;
        return isCorrectlyAnsweredRelatedQuestions() == that.isCorrectlyAnsweredRelatedQuestions() &&
                getEstimatedPhaseTime() == that.getEstimatedPhaseTime() &&
                Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), isCorrectlyAnsweredRelatedQuestions(), getEstimatedPhaseTime());
    }

    @Override
    public String toString() {
        return "PhaseInfoDTO{" +
                "phaseId=" + id +
                ", correctlyAnsweredRelatedQuestions=" + correctlyAnsweredRelatedQuestions +
                ", estimatedPhaseTime=" + estimatedPhaseTime +
                '}';
    }
}
