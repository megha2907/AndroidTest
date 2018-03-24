package in.sportscafe.nostragamus.module.privateContest.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sc on 21/3/18.
 */

public class PrivateContestPrizeTemplateResponse {

    @SerializedName("name")
    private String name;

    @SerializedName("prizes")
    private List<PrivateContestPrizeResponse> prizes = null;

    @SerializedName("shareType")
    private String shareType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PrivateContestPrizeResponse> getPrizes() {
        return prizes;
    }

    public void setPrizes(List<PrivateContestPrizeResponse> prizes) {
        this.prizes = prizes;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    /**
     * Used for Spinner adapter to show name as spinner item
     * @return
     */
    @Override
    public String toString() {
        return name;
    }
}
