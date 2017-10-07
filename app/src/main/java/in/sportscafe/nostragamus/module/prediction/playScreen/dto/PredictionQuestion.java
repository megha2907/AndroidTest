package in.sportscafe.nostragamus.module.prediction.playScreen.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

import in.sportscafe.nostragamus.Constants;

/**
 * Created by sandip on 15/09/17.
 */
@Parcel
public class PredictionQuestion {

    @SerializedName("question_id")
    private int questionId;

    @SerializedName("match_id")
    private int matchId;

    @SerializedName("question_points")
    private int questionPoints;

    @SerializedName("question_neg_points")
    private int questionNegPoints;

    @SerializedName("question_text")
    private String questionText;

    @SerializedName("question_context")
    private String questionContext;

    @SerializedName("question_option_1")
    private String questionOption1;

    @SerializedName("question_image_1")
    private String questionImage1;

    @SerializedName("question_option_2")
    private String questionOption2;

    @SerializedName("question_image_2")
    private String questionImage2;

    @SerializedName("question_option_3")
    private String questionOption3;

    @SerializedName("question_answer")
    private String questionAnswer;

    @SerializedName("poll")
    private List<PlayersPoll> playersPollList;

    private int minorityAnswerId = -1;
    private int positivePoints = Constants.PredictionPoints.POSITIVE_POINTS;
    private int negativePoints = Constants.PredictionPoints.NEGATIVE_POINTS;
    private PowerUp powerUp = new PowerUp();
    private boolean isAnsweredSuccessfully = false;

    /* ------------------------------------------------------------------- */

    public int getQuestionId() {
        return questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getQuestionPoints() {
        return questionPoints;
    }

    public void setQuestionPoints(int questionPoints) {
        this.questionPoints = questionPoints;
    }

    public int getQuestionNegPoints() {
        return questionNegPoints;
    }

    public void setQuestionNegPoints(int questionNegPoints) {
        this.questionNegPoints = questionNegPoints;
    }

    public String getQuestionContext() {
        return questionContext;
    }

    public void setQuestionContext(String questionContext) {
        this.questionContext = questionContext;
    }

    public String getQuestionOption1() {
        return questionOption1;
    }

    public void setQuestionOption1(String questionOption1) {
        this.questionOption1 = questionOption1;
    }

    public String getQuestionImage1() {
        return questionImage1;
    }

    public void setQuestionImage1(String questionImage1) {
        this.questionImage1 = questionImage1;
    }

    public String getQuestionOption2() {
        return questionOption2;
    }

    public void setQuestionOption2(String questionOption2) {
        this.questionOption2 = questionOption2;
    }

    public String getQuestionImage2() {
        return questionImage2;
    }

    public void setQuestionImage2(String questionImage2) {
        this.questionImage2 = questionImage2;
    }

    public String getQuestionOption3() {
        return questionOption3;
    }

    public void setQuestionOption3(String questionOption3) {
        this.questionOption3 = questionOption3;
    }

    public String getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer;
    }

    public PowerUp getPowerUp() {
        return powerUp;
    }

    public void setPowerUp(PowerUp powerUp) {
        this.powerUp = powerUp;
    }

    public List<PlayersPoll> getPlayersPollList() {
        return playersPollList;
    }

    public void setPlayersPollList(List<PlayersPoll> playersPollList) {
        this.playersPollList = playersPollList;
    }

    public int getMinorityAnswerId() {
        return minorityAnswerId;
    }

    public void setMinorityAnswerId(int minorityAnswerId) {
        this.minorityAnswerId = minorityAnswerId;
    }

    /**
     * As per older logic (copied)
     * @param answerId
     * @return
     */
    public boolean isMinorityAnswer(int answerId) {
        return -1 != minorityAnswerId && answerId == minorityAnswerId;
    }

    public int getPositivePoints() {
        return positivePoints;
    }

    public void setPositivePoints(int positivePoints) {
        this.positivePoints = positivePoints;
    }

    public int getNegativePoints() {
        return negativePoints;
    }

    public void setNegativePoints(int negativePoints) {
        this.negativePoints = negativePoints;
    }

    public boolean isAnsweredSuccessfully() {
        return isAnsweredSuccessfully;
    }

    public void setAnsweredSuccessfully(boolean answeredSuccessfully) {
        isAnsweredSuccessfully = answeredSuccessfully;
    }
}
