package cz.muni.ics.kypo.dto;

// TODO this class should be taken from dependency on kypo-adaptive-training
public class DecisionMatrixRowDTO {

    private long id;
    private int order;
    private double questionnaireAnswered;
    private double keywordUsed;
    private double completedInTime;
    private double solutionDisplayed;
    private double wrongAnswers;
    private long relatedPhaseId;

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

    public long getRelatedPhaseId() {
        return relatedPhaseId;
    }

    public void setRelatedPhaseId(long relatedPhaseId) {
        this.relatedPhaseId = relatedPhaseId;
    }
}
