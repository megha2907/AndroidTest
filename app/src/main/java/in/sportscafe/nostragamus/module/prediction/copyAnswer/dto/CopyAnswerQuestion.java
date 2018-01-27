package in.sportscafe.nostragamus.module.prediction.copyAnswer.dto;

import com.google.gson.annotations.SerializedName;

import in.sportscafe.nostragamus.module.prediction.playScreen.dto.AnswerPowerUpDto;

/**
 * Created by sc on 24/1/18.
 */

public class CopyAnswerQuestion {

    @SerializedName("room_id")
    private int roomId;

    @SerializedName("question_text")
    private String questionText;

    @SerializedName("question_option_1")
    private String questionOption1;

    @SerializedName("question_option_2")
    private String questionOption2;

    @SerializedName("question_option_3")
    private String questionOption3;

    @SerializedName("answer")
    private int answer;

    @SerializedName("appliedPowerups")
    private AnswerPowerUpDto appliedPowerUp;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionOption1() {
        return questionOption1;
    }

    public void setQuestionOption1(String questionOption1) {
        this.questionOption1 = questionOption1;
    }

    public String getQuestionOption2() {
        return questionOption2;
    }

    public void setQuestionOption2(String questionOption2) {
        this.questionOption2 = questionOption2;
    }

    public String getQuestionOption3() {
        return questionOption3;
    }

    public void setQuestionOption3(String questionOption3) {
        this.questionOption3 = questionOption3;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public AnswerPowerUpDto getAppliedPowerUp() {
        return appliedPowerUp;
    }

    public void setAppliedPowerUp(AnswerPowerUpDto appliedPowerUp) {
        this.appliedPowerUp = appliedPowerUp;
    }
}
