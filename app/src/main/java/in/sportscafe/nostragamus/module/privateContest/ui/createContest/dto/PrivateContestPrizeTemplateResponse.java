package in.sportscafe.nostragamus.module.privateContest.ui.createContest.dto;

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

    private int TemplateType = PrivateContestPrizeSpinnerItemType.PRIZE_TEMPLATE;

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

    public int getTemplateType() {
        return TemplateType;
    }

    public void setTemplateType(int templateType) {
        TemplateType = templateType;
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
