package cz.muni.ics.kypo.api.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.*;

/**
 * This class is taken from project kypo-elasticsearch-service.
 */
@ApiModel(value = "OverallPhaseStatistics")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OverallPhaseStatistics {

    @ApiModelProperty(value = "ID of a phase", example = "1")
    private Long phaseId;
    @ApiModelProperty(value = "ID of a task", example = "3")
    private Long taskId;
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

    public Long getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(Long phaseId) {
        this.phaseId = phaseId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getPhaseTime() {
        return phaseTime;
    }

    public void setPhaseTime(Long phaseTime) {
        this.phaseTime = phaseTime;
    }

    public List<String> getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(List<String> wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }

    public Boolean getSolutionDisplayed() {
        return solutionDisplayed;
    }

    public void setSolutionDisplayed(Boolean solutionDisplayed) {
        this.solutionDisplayed = solutionDisplayed;
    }

    public Long getNumberOfCommands() {
        return numberOfCommands;
    }

    public void setNumberOfCommands(Long numberOfCommands) {
        this.numberOfCommands = numberOfCommands;
    }

    public Map<String, Long> getKeywordsInCommands() {
        return keywordsInCommands;
    }

    public void setKeywordsInCommands(Map<String, Long> keywordsInCommands) {
        this.keywordsInCommands = keywordsInCommands;
    }

    public void addKeyword(String keyword, Long keywordOccurrences) {
        this.keywordsInCommands.put(keyword, keywordOccurrences);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OverallPhaseStatistics)) return false;
        OverallPhaseStatistics that = (OverallPhaseStatistics) o;
        return Objects.equals(getPhaseId(), that.getPhaseId()) &&
                Objects.equals(getTaskId(), that.getTaskId()) &&
                Objects.equals(getPhaseTime(), that.getPhaseTime()) &&
                Objects.equals(getWrongAnswers(), that.getWrongAnswers()) &&
                Objects.equals(getSolutionDisplayed(), that.getSolutionDisplayed()) &&
                Objects.equals(getNumberOfCommands(), that.getNumberOfCommands()) &&
                Objects.equals(getKeywordsInCommands(), that.getKeywordsInCommands());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPhaseId(), getTaskId(), getPhaseTime(), getWrongAnswers(), getSolutionDisplayed(), getNumberOfCommands(), getKeywordsInCommands());
    }

    @Override
    public String toString() {
        return "OverallPhaseStatistics{" +
                "phaseId=" + phaseId +
                ", taskId=" + taskId +
                ", phaseTime=" + phaseTime +
                ", wrongAnswers=" + wrongAnswers +
                ", solutionDisplayed=" + solutionDisplayed +
                ", numberOfCommands=" + numberOfCommands +
                ", keywordsInCommands=" + keywordsInCommands +
                '}';
    }
}
