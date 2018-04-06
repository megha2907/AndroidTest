package in.sportscafe.nostragamus.module.user.playerprofile.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.user.badges.Badge;
import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroups;
import in.sportscafe.nostragamus.module.user.login.dto.BasicUserInfo;
import in.sportscafe.nostragamus.module.user.login.dto.InfoDetails;
import in.sportscafe.nostragamus.module.user.login.dto.UserPaymentInfo;

/**
 * Created by deepanshi on 12/22/16.
 */
@Parcel
public class PlayerInfo extends BasicUserInfo {

    @SerializedName("user_photo")
    private String photo;

    @SerializedName("sports_preferences")
    private List<Integer> userSports = new ArrayList<>();

    @SerializedName("info")
    private InfoDetails infoDetails;

    @SerializedName("payment_info")
    private UserPaymentInfo userPaymentInfo;

    @SerializedName("count_matches")
    private Integer totalMatchesPlayed = 0;

    @SerializedName("total_points")
    private Long points;

    @SerializedName("total_powerups")
    private Long totalPowerups;

    @SerializedName("count_predictions")
    private Integer predictionCount;

    @SerializedName("count_powerups")
    private Integer powerupsUsedCount;

    @SerializedName("accuracy")
    private Integer accuracy;

    @SerializedName("otp_number")
    private String otpMobileNumber;

    @SerializedName("count_deposits")
    private int depositCount;

    @SerializedName("count_referrals")
    private int referralCount;

    @SerializedName("count_contests_joined")
    private int contestJoinedCount;

    @SerializedName("count_paid_contests_joined")
    private int paidContestJoinedCount;

    @SerializedName("most_played_sport")
    private String mostPlayedSport;

    @SerializedName("has_referred")
    private boolean hasReferred;

    @SerializedName("has_deposited")
    private boolean hasDeposited;

    @SerializedName("mutual_groups")
    private List<AllGroups> mutualGroups = new ArrayList<>();

    @SerializedName("user_photo")
    public String getPhoto() {
        return photo;
    }

    @SerializedName("user_photo")
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @SerializedName("sports_preferences")
    public List<Integer> getUserSports() {
        if (null == userSports) {
            userSports = new ArrayList<>();
        }
        return userSports;
    }

    @SerializedName("sports_preferences")
    public void setUserSports(List<Integer> userSports) {
        this.userSports = userSports;
    }

    @SerializedName("info")
    public InfoDetails getInfoDetails() {
        return infoDetails;
    }

    @SerializedName("info")
    public void setInfoDetails(InfoDetails infoDetails) {
        this.infoDetails = infoDetails;
    }

    @SerializedName("count_matches")
    public Integer getTotalMatchesPlayed() {
        return totalMatchesPlayed;
    }

    @SerializedName("count_matches")
    public void setTotalMatchesPlayed(Integer totalMatchesPlayed) {
        this.totalMatchesPlayed = totalMatchesPlayed;
    }

    @SerializedName("total_points")
    public Long getTotalPoints() {
        if(null == points) {
            return 0L;
        }
        return points;
    }

    @SerializedName("total_points")
    public void setTotalPoints(Long points) {
        this.points = points;
    }

    @SerializedName("total_powerups")
    public Long getTotalPowerups() {
        return totalPowerups;
    }

    @SerializedName("total_powerups")
    public void setTotalPowerups(Long totalPowerups) {
        this.totalPowerups = totalPowerups;
    }

    @SerializedName("mutual_groups")
    public List<AllGroups> getMutualGroups() {
        if(null == mutualGroups) {
            mutualGroups = new ArrayList<>();
        }
        return mutualGroups;
    }

    @SerializedName("mutual_groups")
    public void setMutualGroups(List<AllGroups> mutualGroups) {
        this.mutualGroups = mutualGroups;
    }


    public List<Badge> getBadges() {
        return infoDetails.getBadges();
    }

    @SerializedName("count_predictions")
    public Integer getPredictionCount() {
        if(null == predictionCount) {
            predictionCount = 0;
        }
        return predictionCount;
    }

    @SerializedName("count_predictions")
    public void setPredictionCount(Integer predictionCount) {
        this.predictionCount = predictionCount;
    }

    @SerializedName("accuracy")
    public Integer getAccuracy() {
        if(null == accuracy) {
            accuracy = 0;
        }
        return accuracy;
    }

    @SerializedName("accuracy")
    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    @SerializedName("count_powerups")
    public Integer getPowerupsUsedCount() {
        return powerupsUsedCount;
    }

    @SerializedName("count_powerups")
    public void setPowerupsUsedCount(Integer powerupsUsedCount) {
        this.powerupsUsedCount = powerupsUsedCount;
    }

    @SerializedName("payment_info")
    public UserPaymentInfo getUserPaymentInfo() {
        return userPaymentInfo;
    }

    @SerializedName("payment_info")
    public void setUserPaymentInfo(UserPaymentInfo userPaymentInfo) {
        this.userPaymentInfo = userPaymentInfo;
    }

    @SerializedName("otp_number")
    public String getOtpMobileNumber() {
        return otpMobileNumber;
    }

    @SerializedName("otp_number")
    public void setOtpMobileNumber(String otpMobileNumber) {
        this.otpMobileNumber = otpMobileNumber;
    }

    public int getDepositCount() {
        return depositCount;
    }

    public void setDepositCount(int depositCount) {
        this.depositCount = depositCount;
    }

    public int getReferralCount() {
        return referralCount;
    }

    public void setReferralCount(int referralCount) {
        this.referralCount = referralCount;
    }

    public int getContestJoinedCount() {
        return contestJoinedCount;
    }

    public void setContestJoinedCount(int contestJoinedCount) {
        this.contestJoinedCount = contestJoinedCount;
    }

    public int getPaidContestJoinedCount() {
        return paidContestJoinedCount;
    }

    public void setPaidContestJoinedCount(int paidContestJoinedCount) {
        this.paidContestJoinedCount = paidContestJoinedCount;
    }

    public String getMostPlayedSport() {
        return mostPlayedSport;
    }

    public void setMostPlayedSport(String mostPlayedSport) {
        this.mostPlayedSport = mostPlayedSport;
    }

    public boolean isHasReferred() {
        return hasReferred;
    }

    public void setHasReferred(boolean hasReferred) {
        this.hasReferred = hasReferred;
    }

    public boolean isHasDeposited() {
        return hasDeposited;
    }

    public void setHasDeposited(boolean hasDeposited) {
        this.hasDeposited = hasDeposited;
    }

}