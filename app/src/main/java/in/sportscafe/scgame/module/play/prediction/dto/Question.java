package in.sportscafe.scgame.module.play.prediction.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

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

    @JsonProperty("question_live")
    private boolean questionLive;

    @JsonProperty("question_answer")
    private Integer questionAnswer;

    @JsonProperty("answer")
    private String answerId;

    @JsonIgnore
    private int questionTime = 30;

    @JsonIgnore
    private int questionNumber;

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
    public String getAnswerId() {
        return answerId;
    }

    /**
     * @param answerId The answer
     */
    @JsonProperty("answer")
    public void setAnswerId(String answerId) {
        this.answerId = answerId;
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
}