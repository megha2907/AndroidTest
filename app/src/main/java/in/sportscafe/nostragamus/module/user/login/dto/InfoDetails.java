package in.sportscafe.nostragamus.module.user.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonProperty("powerups")
    private HashMap<String, Integer> powerUps = new HashMap<>();

    @JsonProperty("badges")
    private List<Badge> badges = new ArrayList<>();

    @JsonProperty("transient_badges")
    private List<Badge> transientBadges = new ArrayList<>();

    @JsonProperty("level")
    private String level;

    @JsonProperty("user_referral_code")
    private String ReferUserCode;

    @JsonProperty("user_accepted")
    private Boolean disclaimerAccepted;

    @JsonProperty("wallet_init")
    private Integer walletInit;

    @JsonProperty("first_withdrawal_done")
    private Boolean firstWithdrawDone;

    @JsonProperty("wallet_created")
    private boolean isWalletCreated;

    @JsonProperty("powerups")
    public HashMap<String, Integer> getPowerUps() {
        return powerUps;
    }

    @JsonProperty("powerups")
    public void setPowerUps(HashMap<String, Integer> powerUps) {
        this.powerUps = powerUps;
    }

    @JsonProperty("badges")
    public List<Badge> getBadges() {
        return badges;
    }

    @JsonProperty("badges")
    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }

    @JsonProperty("level")
    public String getLevel() {
        return level;
    }

    @JsonProperty("level")
    public void setLevel(String level) {
        this.level = level;
    }

    @JsonProperty("transient_badges")
    public List<Badge> getTransientBadges() {
        return transientBadges;
    }

    @JsonProperty("transient_badges")
    public void setTransientBadges(List<Badge> transientBadges) {
        this.transientBadges = transientBadges;
    }

    @JsonProperty("user_accepted")
    public Boolean getDisclaimerAccepted() {
        return disclaimerAccepted;
    }

    @JsonProperty("user_accepted")
    public void setDisclaimerAccepted(Boolean disclaimerAccepted) {
        this.disclaimerAccepted = disclaimerAccepted;
    }

    @JsonProperty("wallet_init")
    public Integer getWalletInit() {
        return walletInit;
    }

    @JsonProperty("wallet_init")
    public void setWalletInit(Integer walletInit) {
        this.walletInit = walletInit;
    }

    @JsonProperty("first_withdrawal_done")
    public Boolean getFirstWithdrawDone() {
        return firstWithdrawDone;
    }

    @JsonProperty("first_withdrawal_done")
    public void setFirstWithdrawDone(Boolean firstWithdrawDone) {
        this.firstWithdrawDone = firstWithdrawDone;
    }

    @JsonProperty("wallet_created")
    public boolean isWalletCreated() {
        return isWalletCreated;
    }

    @JsonProperty("wallet_created")
    public void setWalletCreated(boolean walletCreated) {
        isWalletCreated = walletCreated;
    }

    @JsonProperty("user_referral_code")
    public String getReferUserCode() {
        return ReferUserCode;
    }

    @JsonProperty("user_referral_code")
    public void setReferUserCode(String referUserCode) {
        ReferUserCode = referUserCode;
    }


}