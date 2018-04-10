package in.sportscafe.nostragamus.module.user.login.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.sportscafe.nostragamus.module.user.badges.Badge;

/**
 * Created by deepanshi on 10/8/16.
 */
@Parcel
public class InfoDetails {

    @SerializedName("powerups")
    private HashMap<String, Integer> powerUps = new HashMap<>();

    @SerializedName("badges")
    private List<Badge> badges = new ArrayList<>();

    @SerializedName("transient_badges")
    private List<Badge> transientBadges = new ArrayList<>();

    @SerializedName("level")
    private String level;

    @SerializedName("user_referral_code")
    private String ReferUserCode;

    @SerializedName("user_accepted")
    private Boolean disclaimerAccepted;

    @SerializedName("otp_verified")
    private Boolean otpVerified;

    @SerializedName("seen_whats_new")
    private Boolean whatsNewShown;

    @SerializedName("wallet_init")
    private Integer walletInit;

    @SerializedName("first_withdrawal_done")
    private Boolean firstWithdrawDone;

    @SerializedName("wallet_created")
    private boolean isWalletCreated;

    @SerializedName("kyc_status")
    private String kycStatus;

    @SerializedName("wallet_withdrawal")
    private boolean withdrawalBlocked = false;

    @SerializedName("powerups")
    public HashMap<String, Integer> getPowerUps() {
        return powerUps;
    }

    @SerializedName("powerups")
    public void setPowerUps(HashMap<String, Integer> powerUps) {
        this.powerUps = powerUps;
    }

    @SerializedName("badges")
    public List<Badge> getBadges() {
        return badges;
    }

    @SerializedName("badges")
    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }

    @SerializedName("level")
    public String getLevel() {
        return level;
    }

    @SerializedName("level")
    public void setLevel(String level) {
        this.level = level;
    }

    @SerializedName("transient_badges")
    public List<Badge> getTransientBadges() {
        return transientBadges;
    }

    @SerializedName("transient_badges")
    public void setTransientBadges(List<Badge> transientBadges) {
        this.transientBadges = transientBadges;
    }

    @SerializedName("user_accepted")
    public Boolean getDisclaimerAccepted() {
        return disclaimerAccepted;
    }

    @SerializedName("user_accepted")
    public void setDisclaimerAccepted(Boolean disclaimerAccepted) {
        this.disclaimerAccepted = disclaimerAccepted;
    }

    @SerializedName("wallet_init")
    public Integer getWalletInit() {
        return walletInit;
    }

    @SerializedName("wallet_init")
    public void setWalletInit(Integer walletInit) {
        this.walletInit = walletInit;
    }

    @SerializedName("first_withdrawal_done")
    public Boolean getFirstWithdrawDone() {
        return firstWithdrawDone;
    }

    @SerializedName("first_withdrawal_done")
    public void setFirstWithdrawDone(Boolean firstWithdrawDone) {
        this.firstWithdrawDone = firstWithdrawDone;
    }

    @SerializedName("wallet_created")
    public boolean isWalletCreated() {
        return isWalletCreated;
    }

    @SerializedName("wallet_created")
    public void setWalletCreated(boolean walletCreated) {
        isWalletCreated = walletCreated;
    }

    @SerializedName("user_referral_code")
    public String getReferUserCode() {
        return ReferUserCode;
    }

    @SerializedName("user_referral_code")
    public void setReferUserCode(String referUserCode) {
        ReferUserCode = referUserCode;
    }

    @SerializedName("seen_whats_new")
    public Boolean getWhatsNewShown() {
        return whatsNewShown;
    }

    @SerializedName("seen_whats_new")
    public void setWhatsNewShown(Boolean whatsNewShown) {
        this.whatsNewShown = whatsNewShown;
    }

    @SerializedName("otp_verified")
    public Boolean getOtpVerified() {
        return otpVerified;
    }

    @SerializedName("otp_verified")
    public void setOtpVerified(Boolean otpVerified) {
        this.otpVerified = otpVerified;
    }

    @SerializedName("kyc_status")
    public String getKycStatus() {
        return kycStatus;
    }
    @SerializedName("kyc_status")
    public void setKycStatus(String kycStatus) {
        this.kycStatus = kycStatus;
    }

    public boolean isWithdrawalBlocked() {
        return withdrawalBlocked;
    }

    public void setWithdrawalBlocked(boolean withdrawalBlocked) {
        this.withdrawalBlocked = withdrawalBlocked;
    }


}