package in.sportscafe.nostragamus.module.prediction.copyAnswer.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sc on 24/1/18.
 */

public class CopyAnswerContestsResponse {

    @SerializedName("data")
    private List<CopyAnswerContest> data = null;

    public List<CopyAnswerContest> getData() {
        return data;
    }

    public void setData(List<CopyAnswerContest> data) {
        this.data = data;
    }
}
