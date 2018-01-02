package in.sportscafe.nostragamus.module.prediction.editAnswer.dto;

import com.google.gson.annotations.SerializedName;

import in.sportscafe.nostragamus.module.prediction.playScreen.dto.AnswerPowerUpDto;

/**
 * Created by deepanshi on 5/17/17.
 */

public class SaveEditAnswerRequest {

    @SerializedName("match_id")
    private int matchId;

    @SerializedName("question_id")
    private int questionId;

    @SerializedName("answer")
    private int answerId;

    @SerializedName("room_id")
    private int roomId;

    @SerializedName("answer_time")
    private String answerTime;

    @SerializedName("powerups")
    private AnswerPowerUpDto answerPowerup;

    @SerializedName("question_id")
    public int getQuestionId() {
        return questionId;
    }

    @SerializedName("question_id")
    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    @SerializedName("answer")
    public int getAnswerId() {
        return answerId;
    }

    @SerializedName("answer")
    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public AnswerPowerUpDto getAnswerPowerup() {
        return answerPowerup;
    }

    public void setAnswerPowerup(AnswerPowerUpDto answerPowerup) {
        this.answerPowerup = answerPowerup;
    }

    public String getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(String answerTime) {
        this.answerTime = answerTime;
    }
}
