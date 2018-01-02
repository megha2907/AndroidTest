package in.sportscafe.nostragamus.module.prediction.editAnswer.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sc on 29/12/17.
 */

public class SaveEditAnswerResponse {

    @SerializedName("data")
    private SaveEditAnswerDataResponse editAnswerDataResponse;

    public SaveEditAnswerDataResponse getEditAnswerDataResponse() {
        return editAnswerDataResponse;
    }

    public void setEditAnswerDataResponse(SaveEditAnswerDataResponse editAnswerDataResponse) {
        this.editAnswerDataResponse = editAnswerDataResponse;
    }
}
