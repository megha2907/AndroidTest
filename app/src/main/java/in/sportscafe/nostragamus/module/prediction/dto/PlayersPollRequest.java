package in.sportscafe.nostragamus.module.prediction.dto;

import com.google.gson.annotations.SerializedName;

public class PlayersPollRequest {

    @SerializedName("question_id")
    private int questionId;

    @SerializedName("room_id")
    private int roomId;

    @SerializedName("question_id")
    public int getQuestionId() {
        return questionId;
    }

    @SerializedName("question_id")
    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    @SerializedName("challenge_id")
    public int getRoomId() {
        return roomId;
    }

    @SerializedName("challenge_id")
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}