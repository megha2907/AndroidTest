package in.sportscafe.nostragamus.webservice;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.HashMap;

/**
 * Created by deepanshi on 6/23/17.
 */
@Parcel
public class UserReferralInfo {

    @SerializedName("refer_heading")
    private String referHeading;

    @SerializedName("refer_subheading_one")
    private String referSubHeadingOne;

    @SerializedName("refer_subheading_two")
    private String referSubHeadingTwo;

    @SerializedName("referral_code")
    private String referralCode;

    @SerializedName("referral_credits")
    private Integer referralCredits;

    @SerializedName("total_powerups")
    private Integer totalPowerUps;

    @SerializedName("friends_referred")
    private Integer friendsReferred;

    @SerializedName("wallet_init")
    private Integer walletInitialAmount;

    @SerializedName("download_link")
    private String appDownloadLink;

    @SerializedName("powerups")
    private HashMap<String, Integer> powerUps = new HashMap<>();

    @SerializedName("refer_heading")
    public String getReferHeading() {
        return referHeading;
    }

    @SerializedName("refer_heading")
    public void setReferHeading(String referHeading) {
        this.referHeading = referHeading;
    }

    @SerializedName("refer_subheading_one")
    public String getReferSubHeadingOne() {
        return referSubHeadingOne;
    }

    @SerializedName("refer_subheading_one")
    public void setReferSubHeadingOne(String referSubHeadingOne) {
        this.referSubHeadingOne = referSubHeadingOne;
    }

    @SerializedName("refer_subheading_two")
    public String getReferSubHeadingTwo() {
        return referSubHeadingTwo;
    }

    @SerializedName("refer_subheading_two")
    public void setReferSubHeadingTwo(String referSubHeadingTwo) {
        this.referSubHeadingTwo = referSubHeadingTwo;
    }

    @SerializedName("referral_code")
    public String getReferralCode() {
        return referralCode;
    }

    @SerializedName("referral_code")
    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    @SerializedName("referral_credits")
    public Integer getReferralCredits() {
        if (null==referralCredits){
            referralCredits=0;
        }
        return referralCredits;
    }

    @SerializedName("referral_credits")
    public void setReferralCredits(Integer referralCredits) {
        this.referralCredits = referralCredits;
    }

    @SerializedName("total_powerups")
    public Integer getTotalPowerUps() {
        if (null==totalPowerUps){
            totalPowerUps=0;
        }
        return totalPowerUps;
    }

    @SerializedName("total_powerups")
    public void setTotalPowerUps(Integer totalPowerUps) {
        this.totalPowerUps = totalPowerUps;
    }

    @SerializedName("friends_referred")
    public Integer getFriendsReferred() {
        if (null==friendsReferred){
            friendsReferred=0;
        }
        return friendsReferred;
    }

    @SerializedName("friends_referred")
    public void setFriendsReferred(Integer friendsReferred) {
        this.friendsReferred = friendsReferred;
    }

    @SerializedName("powerups")
    public HashMap<String, Integer> getPowerUps() {
        return powerUps;
    }

    @SerializedName("powerups")
    public void setPowerUps(HashMap<String, Integer> powerUps) {
        this.powerUps = powerUps;
    }

    @SerializedName("wallet_init")
    public Integer getWalletInitialAmount() {
        return walletInitialAmount;
    }

    @SerializedName("wallet_init")
    public void setWalletInitialAmount(Integer walletInitialAmount) {
        this.walletInitialAmount = walletInitialAmount;
    }

    @SerializedName("download_link")
    public String getAppDownloadLink() {
        return appDownloadLink;
    }

    @SerializedName("download_link")
    public void setAppDownloadLink(String appDownloadLink) {
        this.appDownloadLink = appDownloadLink;
    }

}
