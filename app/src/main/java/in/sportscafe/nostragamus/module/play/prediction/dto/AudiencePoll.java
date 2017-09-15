package in.sportscafe.nostragamus.module.play.prediction.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 11/24/16.
 */

@Parcel
public class AudiencePoll {

    @SerializedName("answer")
    private Integer answerId;

    @SerializedName("percentage")
    private String answerPercentage;

    @SerializedName("answer")
    public Integer getAnswerId() {
        return answerId;
    }

    @SerializedName("answer")
    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    @SerializedName("percentage")
    public String getAnswerPercentage() {
        return answerPercentage;
    }

    @SerializedName("percentage")
    public void setAnswerPercentage(String answerPercentage) {
        this.answerPercentage = answerPercentage;
    }

}