package in.sportscafe.nostragamus.module.prediction.editAnswer.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sc on 29/12/17.
 */

public class QuestionForEditAnswerRequest {

    @SerializedName("question_id")
    private int questionId;

    @SerializedName("match_id")
    private int matchId;

    @SerializedName("room_id")
    private int roomId;

    public int getQuestionId() {
        return questionId;
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

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
