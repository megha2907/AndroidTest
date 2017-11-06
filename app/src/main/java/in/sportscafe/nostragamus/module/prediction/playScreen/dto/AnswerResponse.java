package in.sportscafe.nostragamus.module.prediction.playScreen.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 19/09/17.
 */

public class AnswerResponse {

    @SerializedName("data")
    private AnswerResponseData answerResponseData;

    @SerializedName("error")
    private String error;

    public AnswerResponseData getAnswerResponseData() {
        return answerResponseData;
    }

    public void setAnswerResponseData(AnswerResponseData answerResponseData) {
        this.answerResponseData = answerResponseData;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
