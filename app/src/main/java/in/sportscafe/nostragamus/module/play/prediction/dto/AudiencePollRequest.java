package in.sportscafe.nostragamus.module.play.prediction.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by deepanshi on 11/25/16.
 */

public class AudiencePollRequest {

    @SerializedName("question_id")
    private Integer questionId;

    @SerializedName("challenge_id")
    private int challengeId;

    @SerializedName("question_id")
    public Integer getQuestionId() {
        return questionId;
    }

    @SerializedName("question_id")
    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    @SerializedName("challenge_id")
    public int getChallengeId() {
        return challengeId;
    }

    @SerializedName("challenge_id")
    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }
}