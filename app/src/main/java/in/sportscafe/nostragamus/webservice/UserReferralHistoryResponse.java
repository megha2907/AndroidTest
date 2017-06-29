package in.sportscafe.nostragamus.webservice;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.navigation.referfriends.referralcredits.ReferralHistory;

/**
 * Created by deepanshi on 6/27/17.
 */

@Parcel
public class UserReferralHistoryResponse {

    @JsonProperty("data")
    private List<ReferralHistory> referralHistoryList = new ArrayList<>();

    @JsonProperty("data")
    public List<ReferralHistory> getReferralHistoryList() {
        return referralHistoryList;
    }

    @JsonProperty("data")
    public void setReferralHistoryList(List<ReferralHistory> referralHistoryList) {
        this.referralHistoryList = referralHistoryList;
    }

}
