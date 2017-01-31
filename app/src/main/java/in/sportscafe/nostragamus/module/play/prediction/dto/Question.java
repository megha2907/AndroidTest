package in.sportscafe.nostragamus.module.play.prediction.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import in.sportscafe.nostragamus.module.othersanswers.AnswerPercentage;

/**
 * Created by Jeeva on 27/5/16.
 */
public class Question implements Serializable {

    @JsonProperty("question_id")
    private Integer questionId;

    @JsonProperty("match_id")
    private Integer matchId;

    @JsonProperty("question_text")
    private String questionText;

    @JsonProperty("question_context")
    private String questionContext;

    @JsonProperty("question_option_1")
    private String questionOption1;

    @JsonProperty("question_image_1")
    private String questionImage1;

    @JsonProperty("question_option_2")
    private String questionOption2;

    @JsonProperty("question_image_2")
    private String questionImage2;

    @JsonProperty("question_option_3")
    private String questionOption3;

    @JsonProperty("question_live")
    private boolean questionLive;

    @JsonProperty("question_answer")
    private Integer questionAnswer;

    @JsonProperty("answer")
    private Integer answerId;

    @JsonProperty("question_value")
    private String questionValue;

    @JsonProperty("powerup_id")
    private String AnswerPowerUpId;

    @JsonProperty("user_answer_points")
    private Integer answerPoints;

    @JsonProperty("user_answer_points")
    private Integer questionPoints;

    @JsonProperty("question_points")
    private Integer questionPositivePoints;

    @JsonProperty("question_neg_points")
    private Integer questionNegativePoints;

    @JsonIgnore
    private int questionTime = 30;

    @JsonIgnore
    private int questionNumber;

    @JsonIgnore
    private String powerUpId = null;

    @JsonIgnore
    private String option1AudPollPer;

    @JsonIgnore
    private String option2AudPollPer;

    @JsonIgnore
    private String option3AudPollPer;

    /**
     * @return The questionId
     */
    @JsonProperty("question_id")
    public Integer getQuestionId() {
        return questionId;
    }

    /**
     * @param questionId The question_id
     */
    @JsonProperty("question_id")
    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    /**
     * @return The matchId
     */
    @JsonProperty("match_id")
    public Integer getMatchId() {
        return matchId;
    }

    /**
     * @param matchId The match_id
     */
    @JsonProperty("match_id")
    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }

    /**
     * @return The questionText
     */
    @JsonProperty("question_text")
    public String getQuestionText() {
        return questionText;
    }

    /**
     * @param questionText The question_text
     */
    @JsonProperty("question_text")
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    @JsonProperty("question_context")
    public String getQuestionContext() {
        return questionContext;
    }

    @JsonProperty("question_context")
    public void setQuestionContext(String questionContext) {
        this.questionContext = questionContext;
    }

    /**
     * @return The questionOption1
     */
    @JsonProperty("question_option_1")
    public String getQuestionOption1() {
        return questionOption1;
    }

    /**
     * @param questionOption1 The question_option_1
     */
    @JsonProperty("question_option_1")
    public void setQuestionOption1(String questionOption1) {
        this.questionOption1 = questionOption1;
    }

    /**
     * @return The questionImage1
     */
    @JsonProperty("question_image_1")
    public String getQuestionImage1() {
        return questionImage1;
    }

    /**
     * @param questionImage1 The question_image_1
     */
    @JsonProperty("question_image_1")
    public void setQuestionImage1(String questionImage1) {
        this.questionImage1 = questionImage1;
    }

    /**
     * @return The questionOption2
     */
    @JsonProperty("question_option_2")
    public String getQuestionOption2() {
        return questionOption2;
    }

    /**
     * @param questionOption2 The question_option_2
     */
    @JsonProperty("question_option_2")
    public void setQuestionOption2(String questionOption2) {
        this.questionOption2 = questionOption2;
    }

    /**
     * @return The questionImage2
     */
    @JsonProperty("question_image_2")
    public String getQuestionImage2() {
        return questionImage2;
    }

    /**
     * @param questionImage2 The question_image_2
     */
    @JsonProperty("question_image_2")
    public void setQuestionImage2(String questionImage2) {
        this.questionImage2 = questionImage2;
    }

    /**
     * @return The questionOption3
     */
    @JsonProperty("question_option_3")
    public String getQuestionOption3() {
        return questionOption3;
    }

    /**
     * @param questionOption3 The question_option_3
     */
    @JsonProperty("question_option_3")
    public void setQuestionOption3(String questionOption3) {
        this.questionOption3 = questionOption3;
    }

    /**
     * @return The questionLive
     */
    @JsonProperty("question_live")
    public boolean getQuestionLive() {
        return questionLive;
    }

    /**
     * @param questionLive The question_live
     */
    @JsonProperty("question_live")
    public void setQuestionLive(boolean questionLive) {
        this.questionLive = questionLive;
    }

    /**
     * @return The questionAnswer
     */
    @JsonProperty("question_answer")
    public Integer getQuestionAnswer() {
        return questionAnswer;
    }

    /**
     * @param questionAnswer The question_answer
     */
    @JsonProperty("question_answer")
    public void setQuestionAnswer(Integer questionAnswer) {
        this.questionAnswer = questionAnswer;
    }

    /**
     * @return The answerId
     */
    @JsonProperty("answer")
    public Integer getAnswerId() {
        return answerId;
    }

    /**
     * @param answerId The answer
     */
    @JsonProperty("answer")
    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }


    /**
     * @return The answerPowerUpId
     */
    @JsonProperty("powerup_id")
    public String getAnswerPowerUpId() {
        return AnswerPowerUpId;
    }

    /**
     * @param AnswerPowerUpId The answerPowerUpId
     */
    @JsonProperty("powerup_id")
    public void setAnswerPowerUpId(String AnswerPowerUpId ) {
        this.AnswerPowerUpId = AnswerPowerUpId;
    }

    @JsonProperty("questionTime")
    public int getQuestionTime() {
        return questionTime;
    }

    @JsonProperty("questionTime")
    public void setQuestionTime(int questionTime) {
        this.questionTime = questionTime;
    }

    @JsonProperty("questionNumber")
    public int getQuestionNumber() {
        return questionNumber;
    }

    @JsonProperty("questionNumber")
    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    @JsonProperty("question_value")
    public String getQuestionValue() {
        return questionValue;
    }

    @JsonProperty("question_value")
    public void setQuestionValue(String questionValue) {
        this.questionValue = questionValue;
    }

    @JsonIgnore
    public String getPowerUpId() {
        return powerUpId;
    }

    @JsonIgnore
    public void setPowerUpId(String powerUpId) {
        this.powerUpId = powerUpId;
    }

    @JsonProperty("user_answer_points")
    public Integer getAnswerPoints() {
        return answerPoints;
    }

    @JsonProperty("user_answer_points")
    public void setAnswerPoints(Integer answerPoints) {
        this.answerPoints = answerPoints;
    }

    @JsonProperty("question_points")
    public Integer getQuestionPositivePoints() {
        return questionPositivePoints;
    }

    @JsonProperty("question_points")
    public void setQuestionPositivePoints(Integer questionPositivePoints) {
        this.questionPositivePoints = questionPositivePoints;
    }

    @JsonProperty("question_neg_points")
    public Integer getQuestionNegativePoints() {
        return questionNegativePoints;
    }

    @JsonProperty("question_neg_points")
    public void setQuestionNegativePoints(Integer questionNegativePoints) {
        this.questionNegativePoints = questionNegativePoints;
    }

    @JsonIgnore
    public String getOption1AudPollPer() {
        return option1AudPollPer;
    }

    @JsonIgnore
    public void setOption1AudPollPer(String option1AudPollPer) {
        this.option1AudPollPer = option1AudPollPer;
    }

    @JsonIgnore
    public String getOption2AudPollPer() {
        return option2AudPollPer;
    }

    @JsonIgnore
    public void setOption2AudPollPer(String option2AudPollPer) {
        this.option2AudPollPer = option2AudPollPer;
    }

    @JsonIgnore
    public String getOption3AudPollPer() {
        return option3AudPollPer;
    }

    @JsonIgnore
    public void setOption3AudPollPer(String option3AudPollPer) {
        this.option3AudPollPer = option3AudPollPer;
    }

    @JsonIgnore
    public void updatePollPercentage(AnswerPercentage answerPercentage) {
        if(null != answerPercentage) {
            option1AudPollPer = answerPercentage.getAnswer1() + "%";
            option2AudPollPer = answerPercentage.getAnswer2() + "%";
            option3AudPollPer = answerPercentage.getAnswer0() + "%";
        }
    }
}