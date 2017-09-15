package in.sportscafe.nostragamus.module.user.leaderboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.Comparator;

/**
 * Created by deepanshi on 10/14/16.
 */
@Parcel
public class UserLeaderBoard {

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("user_nick")
    private String userName;

    @JsonProperty("user_photo")
    private String userPhoto;

    @JsonProperty("accuracy")
    private Integer accuracy;

    @JsonProperty("rank")
    private Integer rank;

    @JsonProperty("rank_change")
    private Integer rankChange;

    @JsonProperty("count_plays")
    private Integer countPlayed;

    @JsonProperty("total_points")
    private Long points;

    @JsonProperty("this_match_points")
    private Long matchPoints;

    @JsonProperty("count_powerups")
    private Integer userPowerUps;

    /**
     * @return The userId
     */
    @JsonProperty("user_id")
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId The user_id
     */
    @JsonProperty("user_id")
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @JsonProperty("user_name")
    public String getUserName() {
        return userName;
    }

    @JsonProperty("user_name")
    public void setUserName(String userName) {
        this.userName = userName;
    }


    @JsonProperty("user_photo")
    public String getUserPhoto() {
        return userPhoto;
    }

    @JsonProperty("user_photo")
    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    /**
     * @return The rank
     */
    @JsonProperty("rank")
    public Integer getRank() {
        return rank;
    }

    /**
     * @param rank The rank
     */
    @JsonProperty("rank")
    public void setRank(Integer rank) {
        this.rank = rank;
    }

    /**
     * @return The rankChange
     */
    @JsonProperty("rank_change")
    public Integer getRankChange() {
        return rankChange;
    }

    /**
     * @param rankChange The rank_change
     */
    @JsonProperty("rank_change")
    public void setRankChange(Integer rankChange) {
        this.rankChange = rankChange;
    }

    @JsonProperty("user_points")
    public Long getPoints() {
        return points;
    }

    @JsonProperty("user_points")
    public void setPoints(Long points) {
        this.points = points;
    }


    @JsonProperty("count_plays")
    public Integer getCountPlayed() {
        return countPlayed;
    }

    @JsonProperty("count_plays")
    public void setCountPlayed(Integer countPlayed) {
        this.countPlayed = countPlayed;
    }

    @JsonProperty("accuracy")
    public Integer getAccuracy() {
        return accuracy;
    }

    @JsonProperty("accuracy")
    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    @JsonProperty("this_match_points")
    public Long getMatchPoints() {
        return matchPoints;
    }

    @JsonProperty("this_match_points")
    public void setMatchPoints(Long matchPoints) {
        this.matchPoints = matchPoints;
    }

    @JsonProperty("count_powerups")
    public Integer getUserPowerUps() {
        return userPowerUps;
    }

    @JsonProperty("count_powerups")
    public void setUserPowerUps(Integer userPowerUps) {
        this.userPowerUps = userPowerUps;
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