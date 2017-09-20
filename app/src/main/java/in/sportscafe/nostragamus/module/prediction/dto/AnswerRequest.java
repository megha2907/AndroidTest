package in.sportscafe.nostragamus.module.prediction.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 19/09/17.
 */

public class AnswerRequest {

    @SerializedName("room_id")
    private int roomId;

    @SerializedName("match_id")
    private int matchId;

    @SerializedName("question_id")
    private int questionId;

    @SerializedName("answer_id")
    private int answerId;

    @SerializedName("answer_time")
    private String answerTime;

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

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public String getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(String answerTime) {
        this.answerTime = answerTime;
    }
}
