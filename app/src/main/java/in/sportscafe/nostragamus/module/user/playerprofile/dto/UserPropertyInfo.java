package in.sportscafe.nostragamus.module.user.playerprofile.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 4/9/18.
 */

@Parcel
public class UserPropertyInfo {

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
