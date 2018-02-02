package in.sportscafe.nostragamus.module.newChallenges.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by deepanshi on 2/1/18.
 */

public class BannerInfo {

    @SerializedName("sportId")
    private int sportId;

    public int getSportId() {
        return sportId;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

}
