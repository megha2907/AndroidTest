package in.sportscafe.nostragamus.webservice;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.navigation.referfriends.referralcredits.ReferralHistory;

/**
 * Created by deepanshi on 6/27/17.
 */

@Parcel
public class UserReferralHistoryResponse {

    @SerializedName("data")
    private List<ReferralHistory> referralHistoryList = new ArrayList<>();

    @SerializedName("data")
    public List<ReferralHistory> getReferralHistoryList() {
        return referralHistoryList;
    }

    @SerializedName("data")
    public void setReferralHistoryList(List<ReferralHistory> referralHistoryList) {
        this.referralHistoryList = referralHistoryList;
    }

}
