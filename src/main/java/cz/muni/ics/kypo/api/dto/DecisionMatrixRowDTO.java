package cz.muni.ics.kypo.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * This class is taken from project kypo-adaptive-training.
 */
@ApiModel(value = "DecisionMatrixRowDTO")
public class DecisionMatrixRowDTO {

    @ApiModelProperty(value = "ID of decision matrix row", required = true, position = 0, example = "1")
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public double getQuestionnaireAnswered() {
        return questionnaireAnswered;
    }

    public void setQuestionnaireAnswered(double questionnaireAnswered) {
        this.questionnaireAnswered = questionnaireAnswered;
    }

    public double getKeywordUsed() {
        return keywordUsed;
    }

    public void setKeywordUsed(double keywordUsed) {
        this.keywordUsed = keywordUsed;
    }

    public double getCompletedInTime() {
        return completedInTime;
    }

    public void setCompletedInTime(double completedInTime) {
        this.completedInTime = completedInTime;
    }

    public double getSolutionDisplayed() {
        return solutionDisplayed;
    }

    public void setSolutionDisplayed(double solutionDisplayed) {
        this.solutionDisplayed = solutionDisplayed;
    }

    public double getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(double wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }

    public RelatedPhaseInfoDTO getRelatedPhaseInfo() {
        return relatedPhaseInfo;
    }

    public void setRelatedPhaseInfo(RelatedPhaseInfoDTO relatedPhaseInfo) {
        this.relatedPhaseInfo = relatedPhaseInfo;
    }

    public long getAllowedCommands() {
        return allowedCommands;
    }

    public void setAllowedCommands(long allowedCommands) {
        this.allowedCommands = allowedCommands;
    }

    public long getAllowedWrongAnswers() {
        return allowedWrongAnswers;
    }

    public void setAllowedWrongAnswers(long allowedWrongAnswers) {
        this.allowedWrongAnswers = allowedWrongAnswers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DecisionMatrixRowDTO)) return false;
        DecisionMatrixRowDTO that = (DecisionMatrixRowDTO) o;
        return id == that.id &&
                order == that.order &&
                Double.compare(that.questionnaireAnswered, questionnaireAnswered) == 0 &&
                Double.compare(that.keywordUsed, keywordUsed) == 0 &&
                Double.compare(that.completedInTime, completedInTime) == 0 &&
                Double.compare(that.solutionDisplayed, solutionDisplayed) == 0 &&
                Double.compare(that.wrongAnswers, wrongAnswers) == 0 &&
                allowedCommands == that.allowedCommands &&
                allowedWrongAnswers == that.allowedWrongAnswers;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order, questionnaireAnswered, keywordUsed, completedInTime, solutionDisplayed, wrongAnswers, allowedCommands, allowedWrongAnswers);
    }

    @Override
    public String toString() {
        return "DecisionMatrixRow{" +
                "id=" + id +
                ", order=" + order +
                ", questionnaireAnswered=" + questionnaireAnswered +
                ", keywordUsed=" + keywordUsed +
                ", completedInTime=" + completedInTime +
                ", solutionDisplayed=" + solutionDisplayed +
                ", wrongAnswers=" + wrongAnswers +
                ", allowedCommands=" + allowedCommands +
                ", allowedWrongAnswers=" + allowedWrongAnswers +
                '}';
    }
}
