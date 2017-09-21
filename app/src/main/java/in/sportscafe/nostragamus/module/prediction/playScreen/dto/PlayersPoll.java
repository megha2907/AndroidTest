package in.sportscafe.nostragamus.module.prediction.playScreen.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by sandip on 19/09/17.
 */
@Parcel
public class PlayersPoll {

    @SerializedName("answer")
    private int answerId;

    @SerializedName("percentage")
    private String answerPercentage;

    @SerializedName("count")
    private int count;

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public String getAnswerPercentage() {
        return answerPercentage;
    }

    public void setAnswerPercentage(String answerPercentage) {
        this.answerPercentage = answerPercentage;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
