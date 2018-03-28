package in.sportscafe.nostragamus.module.privateContest.ui.joinPrivateContest.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by sc on 27/3/18.
 */
@Parcel
public class FindPrivateContestResponse {

    @SerializedName("data")
    private FindPrivateContestResponseData data;

    public FindPrivateContestResponseData getData() {
        return data;
    }

    public void setData(FindPrivateContestResponseData data) {
        this.data = data;
    }
}
