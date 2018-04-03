package in.sportscafe.nostragamus.module.navigation.wallet.doKYC.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import in.sportscafe.nostragamus.module.newChallenges.dto.BannerResponseData;

/**
 * Created by deepanshi on 3/27/18.
 */

public class KYCResponse {

    @SerializedName("info")
    KYCDetails kycDetails;

    public KYCDetails getKycDetails() {
        return kycDetails;
    }

    public void setKycDetails(KYCDetails kycDetails) {
        this.kycDetails = kycDetails;
    }
}
