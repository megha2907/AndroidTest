package in.sportscafe.nostragamus.webservice;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.HashMap;

/**
 * Created by deepanshi on 6/23/17.
 */
@Parcel
public class UserReferralInfo {

    @JsonProperty("refer_heading")
    private String referHeading;

    @JsonProperty("refer_subheading_one")
    private String referSubHeadingOne;

    @JsonProperty("refer_subheading_two")
    private String referSubHeadingTwo;

    @JsonProperty("referral_code")
    private String referralCode;

    @JsonProperty("referral_credits")
    private Integer referralCredits;

    @JsonProperty("total_powerups")
    private Integer totalPowerUps;

    @JsonProperty("friends_referred")
    private Integer friendsReferred;

    @JsonProperty("wallet_init")
    private Integer walletInitialAmount;

    @JsonProperty("powerups")
    private HashMap<String, Integer> powerUps = new HashMap<>();

    @JsonProperty("refer_heading")
    public String getReferHeading() {
        return referHeading;
    }

    @JsonProperty("refer_heading")
    public void setReferHeading(String referHeading) {
        this.referHeading = referHeading;
    }

    @JsonProperty("refer_subheading_one")
    public String getReferSubHeadingOne() {
        return referSubHeadingOne;
    }

    @JsonProperty("refer_subheading_one")
    public void setReferSubHeadingOne(String referSubHeadingOne) {
        this.referSubHeadingOne = referSubHeadingOne;
    }

    @JsonProperty("refer_subheading_two")
    public String getReferSubHeadingTwo() {
        return referSubHeadingTwo;
    }

    @JsonProperty("refer_subheading_two")
    public void setReferSubHeadingTwo(String referSubHeadingTwo) {
        this.referSubHeadingTwo = referSubHeadingTwo;
    }

    @JsonProperty("referral_code")
    public String getReferralCode() {
        return referralCode;
    }

    @JsonProperty("referral_code")
    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    @JsonProperty("referral_credits")
    public Integer getReferralCredits() {
        if (null==referralCredits){
            referralCredits=0;
        }
        return referralCredits;
    }

    @JsonProperty("referral_credits")
    public void setReferralCredits(Integer referralCredits) {
        this.referralCredits = referralCredits;
    }

    @JsonProperty("total_powerups")
    public Integer getTotalPowerUps() {
        if (null==totalPowerUps){
            totalPowerUps=0;
        }
        return totalPowerUps;
    }

    @JsonProperty("total_powerups")
    public void setTotalPowerUps(Integer totalPowerUps) {
        this.totalPowerUps = totalPowerUps;
    }

    @JsonProperty("friends_referred")
    public Integer getFriendsReferred() {
        if (null==friendsReferred){
            friendsReferred=0;
        }
        return friendsReferred;
    }

    @JsonProperty("friends_referred")
    public void setFriendsReferred(Integer friendsReferred) {
        this.friendsReferred = friendsReferred;
    }

    @JsonProperty("powerups")
    public HashMap<String, Integer> getPowerUps() {
        return powerUps;
    }

    @JsonProperty("powerups")
    public void setPowerUps(HashMap<String, Integer> powerUps) {
        this.powerUps = powerUps;
    }

    @JsonProperty("wallet_init")
    public Integer getWalletInitialAmount() {
        return walletInitialAmount;
    }

    @JsonProperty("wallet_init")
    public void setWalletInitialAmount(Integer walletInitialAmount) {
        this.walletInitialAmount = walletInitialAmount;
    }

}
