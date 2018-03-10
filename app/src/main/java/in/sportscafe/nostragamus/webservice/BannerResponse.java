package in.sportscafe.nostragamus.webservice;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import in.sportscafe.nostragamus.module.newChallenges.dto.BannerResponseData;

/**
 * Created by deepanshi on 2/1/18.
 */

public class BannerResponse {

    @SerializedName("data")
    List<BannerResponseData> bannerResponseDataList;

    public List<BannerResponseData> getBannerResponseDataList() {
        return bannerResponseDataList;
    }

    public void setBannerResponseDataList(List<BannerResponseData> bannerResponseDataList) {
        this.bannerResponseDataList = bannerResponseDataList;
    }

}
