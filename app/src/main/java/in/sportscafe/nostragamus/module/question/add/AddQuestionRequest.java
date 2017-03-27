package in.sportscafe.nostragamus.module.question.add;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jeeva on 24/03/17.
 */
public class AddQuestionRequest {

    @JsonProperty("match_id")
    private int matchId;

    @JsonProperty("question_text")
    private String question;

    @JsonProperty("question_context")
    private String context;

    @JsonProperty("question_option_1")
    private String leftOption;

    @JsonProperty("question_option_2")
    private String rightOption;

    @JsonProperty("question_option_3")
    private String neitherOption;

    @JsonProperty("question_submitted_by")
    private QuestionSubmitBy createdBy;

    @JsonProperty("match_id")
    public int getMatchId() {
        return matchId;
    }

    @JsonProperty("match_id")
    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    @JsonProperty("question_text")
    public String getQuestion() {
        return question;
    }

    @JsonProperty("question_text")
    public void setQuestion(String question) {
        this.question = question;
    }

    @JsonProperty("question_context")
    public String getContext() {
        return context;
    }

    @JsonProperty("question_context")
    public void setContext(String context) {
        this.context = context;
    }

    @JsonProperty("question_option_1")
    public String getLeftOption() {
        return leftOption;
    }

    @JsonProperty("question_option_1")
    public void setLeftOption(String leftOption) {
        this.leftOption = leftOption;
    }

    @JsonProperty("question_option_2")
    public String getRightOption() {
        return rightOption;
    }

    @JsonProperty("question_option_2")
    public void setRightOption(String rightOption) {
        this.rightOption = rightOption;
    }

    @JsonProperty("question_option_3")
    public String getNeitherOption() {
        return neitherOption;
    }

    @JsonProperty("question_option_3")
    public void setNeitherOption(String neitherOption) {
        this.neitherOption = neitherOption;
    }

    @JsonProperty("question_submitted_by")
    public QuestionSubmitBy getCreatedBy() {
        return createdBy;
    }

    @JsonProperty("question_submitted_by")
    public void setCreatedBy(QuestionSubmitBy createdBy) {
        this.createdBy = createdBy;
    }
}