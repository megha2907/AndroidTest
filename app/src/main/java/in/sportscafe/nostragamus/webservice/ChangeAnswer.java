package in.sportscafe.nostragamus.webservice;

import com.google.gson.annotations.SerializedName;

/**
 * Created by deepanshi on 5/17/17.
 */

public class ChangeAnswer {

    @SerializedName("match_id")
    private Integer matchId;

    @SerializedName("question_id")
    private Integer questionId;

    @SerializedName("answer")
    private Integer answerId;

    @SerializedName("room_id")
    private Integer roomId;

    public ChangeAnswer(Integer matchId,Integer questionId, Integer answerId,Integer roomId) {
        this.matchId = matchId;
        this.questionId = questionId;
        this.answerId = answerId;
        this.roomId = roomId;
    }

    @SerializedName("question_id")
    public Integer getQuestionId() {
        return questionId;
    }

    @SerializedName("question_id")
    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    @SerializedName("answer")
    public Integer getAnswerId() {
        return answerId;
    }

    @SerializedName("answer")
    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getMatchId() {
        return matchId;
    }

    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }
}
