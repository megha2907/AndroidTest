package in.sportscafe.nostragamus.module.user.leaderboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by deepanshi on 10/14/16.
 */

public class UserLeaderBoard implements Serializable,Comparable<UserLeaderBoard>{

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("user_nick")
    private String userName;

    @JsonProperty("user_photo")
    private String userPhoto;

    @JsonProperty("rank")
    private Integer rank;

    @JsonProperty("rank_change")
    private Integer rankChange;

    @JsonProperty("count_played")
    private Integer countPlayed;

    @JsonProperty("total_points")
    private Long points;

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


    @JsonProperty("count_played")
    public Integer getCountPlayed() {
        return countPlayed;
    }

    @JsonProperty("count_played")
    public void setCountPlayed(Integer countPlayed) {
        this.countPlayed = countPlayed;
    }

    @Override
    public int compareTo(UserLeaderBoard userLeaderBoard) {
        Long userPoints=((UserLeaderBoard)userLeaderBoard).getPoints();
        /* For Ascending order*/
        return (int)(this.points-userPoints);
    }

    /*Comparator for sorting the list by User Rank*/
    public static Comparator<UserLeaderBoard> UserRankComparator = new Comparator<UserLeaderBoard>() {

        public int compare(UserLeaderBoard u1, UserLeaderBoard u2) {

            Integer user1 = null;
            Integer user2 = null;

            user1 = u1.getRank();
            user2 = u2.getRank();

            if (null==user1){
                user1 = Integer.MAX_VALUE;
            }
            if(null == user2){
                user2 = Integer.MAX_VALUE;
            }

            //descending order
            return user2.compareTo(user1);
        }};



    /*Comparator for sorting the list by User Rank*/
    public static Comparator<UserLeaderBoard> UserAccuracyComparator = new Comparator<UserLeaderBoard>() {

        public int compare(UserLeaderBoard u1, UserLeaderBoard u2) {

            Integer user1 = null;
            Integer user2 = null;

            user1 = u1.getRank();
            user2 = u2.getRank();

            if (null==user1){
                user1 = Integer.MAX_VALUE;
            }
            if(null == user2){
                user2 = Integer.MAX_VALUE;
            }

            //ascending order
            return user1.compareTo(user2);

        }};

}
