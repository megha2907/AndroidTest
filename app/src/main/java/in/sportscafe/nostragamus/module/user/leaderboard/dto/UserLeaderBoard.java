package in.sportscafe.nostragamus.module.user.leaderboard.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.Comparator;

/**
 * Created by deepanshi on 10/14/16.
 */
@Parcel
public class UserLeaderBoard {

    @SerializedName("user_id")
    private Integer userId;

    @SerializedName("user_nick")
    private String userName;

    @SerializedName("user_photo")
    private String userPhoto;

    @SerializedName("accuracy")
    private Integer accuracy;

    @SerializedName("rank")
    private Integer rank;

    @SerializedName("rank_change")
    private Integer rankChange;

    @SerializedName("count_plays")
    private Integer countPlayed;

    @SerializedName("total_points")
    private Long points;

    @SerializedName("this_match_points")
    private Long matchPoints;

    @SerializedName("count_powerups")
    private Integer userPowerUps;

    @SerializedName("is_winning")
    private boolean isWinning = false;

    /**
     * @return The userId
     */
    @SerializedName("user_id")
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId The user_id
     */
    @SerializedName("user_id")
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @SerializedName("user_name")
    public String getUserName() {
        return userName;
    }

    @SerializedName("user_name")
    public void setUserName(String userName) {
        this.userName = userName;
    }


    @SerializedName("user_photo")
    public String getUserPhoto() {
        return userPhoto;
    }

    @SerializedName("user_photo")
    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    /**
     * @return The rank
     */
    @SerializedName("rank")
    public Integer getRank() {
        return rank;
    }

    /**
     * @param rank The rank
     */
    @SerializedName("rank")
    public void setRank(Integer rank) {
        this.rank = rank;
    }

    /**
     * @return The rankChange
     */
    @SerializedName("rank_change")
    public Integer getRankChange() {
        return rankChange;
    }

    /**
     * @param rankChange The rank_change
     */
    @SerializedName("rank_change")
    public void setRankChange(Integer rankChange) {
        this.rankChange = rankChange;
    }

    @SerializedName("user_points")
    public Long getPoints() {
        return points;
    }

    @SerializedName("user_points")
    public void setPoints(Long points) {
        this.points = points;
    }


    @SerializedName("count_plays")
    public Integer getCountPlayed() {
        return countPlayed;
    }

    @SerializedName("count_plays")
    public void setCountPlayed(Integer countPlayed) {
        this.countPlayed = countPlayed;
    }

    @SerializedName("accuracy")
    public Integer getAccuracy() {
        return accuracy;
    }

    @SerializedName("accuracy")
    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    @SerializedName("this_match_points")
    public Long getMatchPoints() {
        return matchPoints;
    }

    @SerializedName("this_match_points")
    public void setMatchPoints(Long matchPoints) {
        this.matchPoints = matchPoints;
    }

    @SerializedName("count_powerups")
    public Integer getUserPowerUps() {
        return userPowerUps;
    }

    @SerializedName("count_powerups")
    public void setUserPowerUps(Integer userPowerUps) {
        this.userPowerUps = userPowerUps;
    }

    public boolean isWinning() {
        return isWinning;
    }

    public void setWinning(boolean winning) {
        isWinning = winning;
    }


    /*Comparator for sorting the list by User Accuracy*/
    public static Comparator<UserLeaderBoard> UserAccuracyComparator = new Comparator<UserLeaderBoard>() {

        public int compare(UserLeaderBoard lhs, UserLeaderBoard rhs) {
            //descending order
            if (null == lhs.accuracy || null == rhs.accuracy) {
                return -1;
            }
            return rhs.accuracy - lhs.accuracy;
        }
    };

    /*Comparator for sorting the list by User Rank*/
    public static Comparator<UserLeaderBoard> UserRankComparator = new Comparator<UserLeaderBoard>() {

        public int compare(UserLeaderBoard lhs, UserLeaderBoard rhs) {
            //ascending order
            if (null == lhs.rank || null == rhs.rank) {
                return -1;
            }
            return lhs.rank - rhs.rank;
        }
    };


    /*Comparator for sorting the list by Total Points*/
    public static Comparator<UserLeaderBoard> UserTotalPointsComparator = new Comparator<UserLeaderBoard>() {

        public int compare(UserLeaderBoard lhs, UserLeaderBoard rhs) {

            //descending order
            if (null == lhs.points || null == rhs.points) {
                return -1;
            }
            return (int) (rhs.points - lhs.points);
        }
    };

    /*Comparator for sorting the list by Match Points*/
    public static Comparator<UserLeaderBoard> UserMatchPointsComparator = new Comparator<UserLeaderBoard>() {

        public int compare(UserLeaderBoard lhs, UserLeaderBoard rhs) {

            //descending order
            if (null == lhs.matchPoints || null == rhs.matchPoints) {
                return -1;
            }
            return (int) (rhs.matchPoints - lhs.matchPoints);
        }
    };

    /*Comparator for sorting the list by Powerups*/
    public static Comparator<UserLeaderBoard> UserPowerUpsComparator = new Comparator<UserLeaderBoard>() {

        public int compare(UserLeaderBoard lhs, UserLeaderBoard rhs) {

            //descending order
            if (null == lhs.userPowerUps || null == rhs.userPowerUps) {
                return -1;
            }
            return rhs.userPowerUps - lhs.userPowerUps;
        }
    };
}