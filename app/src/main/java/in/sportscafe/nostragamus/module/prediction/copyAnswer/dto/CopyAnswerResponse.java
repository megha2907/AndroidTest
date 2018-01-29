package in.sportscafe.nostragamus.module.prediction.copyAnswer.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sc on 25/1/18.
 */

public class CopyAnswerResponse {

    @SerializedName("data")
    private CopyAnswerResponseData data;

    public CopyAnswerResponseData getData() {
        return data;
    }

    public void setData(CopyAnswerResponseData data) {
        this.data = data;
    }
}
