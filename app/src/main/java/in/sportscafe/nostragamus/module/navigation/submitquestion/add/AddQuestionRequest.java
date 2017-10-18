package in.sportscafe.nostragamus.module.navigation.submitquestion.add;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jeeva on 24/03/17.
 */
public class AddQuestionRequest {

    @SerializedName("match_id")
    private int matchId;

    @SerializedName("question_text")
    private String question;

    @SerializedName("question_context")
    private String context;

    @SerializedName("question_option_1")
    private String leftOption;

    @SerializedName("question_option_2")
    private String rightOption;

    @SerializedName("question_option_3")
    private String neitherOption;

    @SerializedName("question_submitted_by")
    private QuestionSubmitBy createdBy;

    @SerializedName("match_id")
    public int getMatchId() {
        return matchId;
    }

    @SerializedName("match_id")
    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    @SerializedName("question_text")
    public String getQuestion() {
        return question;
    }

    @SerializedName("question_text")
    public void setQuestion(String question) {
        this.question = question;
    }

    @SerializedName("question_context")
    public String getContext() {
        return context;
    }

    @SerializedName("question_context")
    public void setContext(String context) {
        this.context = context;
    }

    @SerializedName("question_option_1")
    public String getLeftOption() {
        return leftOption;
    }

    @SerializedName("question_option_1")
    public void setLeftOption(String leftOption) {
        this.leftOption = leftOption;
    }

    @SerializedName("question_option_2")
    public String getRightOption() {
        return rightOption;
    }

    @SerializedName("question_option_2")
    public void setRightOption(String rightOption) {
        this.rightOption = rightOption;
    }

    @SerializedName("question_option_3")
    public String getNeitherOption() {
        return neitherOption;
    }

    @SerializedName("question_option_3")
    public void setNeitherOption(String neitherOption) {
        this.neitherOption = neitherOption;
    }

    @SerializedName("question_submitted_by")
    public QuestionSubmitBy getCreatedBy() {
        return createdBy;
    }

    @SerializedName("question_submitted_by")
    public void setCreatedBy(QuestionSubmitBy createdBy) {
        this.createdBy = createdBy;
    }
}