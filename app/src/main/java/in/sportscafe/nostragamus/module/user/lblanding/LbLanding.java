package in.sportscafe.nostragamus.module.user.lblanding;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Comparator;

import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoard;

/**
 * Created by Jeeva on 19/01/17.
 */

public class LbLanding implements Serializable,Comparable<LbLanding> {

    private Integer id =1;

    private String name = "Cricket";

    private Integer rank ;

    @JsonProperty("rank_change")
    private Integer rankChange ;

    @JsonProperty("count_played")
    private Integer countPlayed;

    @JsonProperty("img_url")
    private String imgUrl;

    private String type;

    public LbLanding() {
    }

    public LbLanding(Integer id, String name, Integer rank, Integer rankChange, String imgUrl, String type) {
        this.id = id;
        this.name = name;
        this.rank = rank;
        this.rankChange = rankChange;
        this.imgUrl = imgUrl;
        this.type = type;
    }

    public Integer getRankChange() {
        return rankChange;
    }

    public void setRankChange(Integer rankChange) {
        this.rankChange = rankChange;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("img_url")
    public String getImgUrl() {
        return imgUrl;
    }

    @JsonProperty("img_url")
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("count_played")
    public Integer getCountPlayed() {
        return countPlayed;
    }

    @JsonProperty("count_played")
    public void setCountPlayed(Integer countPlayed) {
        this.countPlayed = countPlayed;
    }


    /*Comparator for sorting the list by User Rank*/
    public static Comparator<LbLanding> LeaderBoardRankComparator = new Comparator<LbLanding>() {

        public int compare(LbLanding l1, LbLanding l2) {

            Integer userLeaderBoard1 = null;
            Integer userLeaderBoard2 = null;

            userLeaderBoard1 = l1.getRank();
            userLeaderBoard2 = l2.getRank();

            if (null==userLeaderBoard1){
                userLeaderBoard1 = Integer.MAX_VALUE;
            }
            if(null == userLeaderBoard2){
                userLeaderBoard2 = Integer.MAX_VALUE;
            }

            //ascending order
            return userLeaderBoard1.compareTo(userLeaderBoard2);

        }};

    /*Comparator for sorting the list by Rank Change*/
    public static Comparator<LbLanding> LeaderBoardRankChangeComparator = new Comparator<LbLanding>() {

        public int compare(LbLanding l1, LbLanding l2) {

            Integer userLeaderBoard1 = null;
            Integer userLeaderBoard2 = null;

            userLeaderBoard1 = l1.getRankChange();
            userLeaderBoard2 = l2.getRankChange();

            if (null==userLeaderBoard1){
                userLeaderBoard1 = Integer.MAX_VALUE;
            }
            if(null == userLeaderBoard2){
                userLeaderBoard2 = Integer.MAX_VALUE;
            }

            //ascending order
            return userLeaderBoard1.compareTo(userLeaderBoard2);

        }};

    /*Comparator for sorting the list by User Rank*/
    public static Comparator<LbLanding> LeaderBoardDateComparator = new Comparator<LbLanding>() {

        public int compare(LbLanding l1, LbLanding l2) {

            Integer userLeaderBoard1 = null;
            Integer userLeaderBoard2 = null;

            userLeaderBoard1 = l1.getId();
            userLeaderBoard2 = l2.getId();

            if (null==userLeaderBoard1){
                userLeaderBoard1 = Integer.MAX_VALUE;
            }
            if(null == userLeaderBoard2){
                userLeaderBoard2 = Integer.MAX_VALUE;
            }

            //ascending order
            return userLeaderBoard1.compareTo(userLeaderBoard2);

        }};


    /*Comparator for sorting the list by Played Matches*/
    public static Comparator<LbLanding> LeaderBoardPlayedMatchesComparator = new Comparator<LbLanding>() {

        public int compare(LbLanding l1, LbLanding l2) {

            Integer userLeaderBoard1 = null;
            Integer userLeaderBoard2 = null;

            userLeaderBoard1 = l1.getCountPlayed();
            userLeaderBoard2 = l2.getCountPlayed();

            if (null==userLeaderBoard1){
                userLeaderBoard1 = Integer.MAX_VALUE;
            }
            if(null == userLeaderBoard2){
                userLeaderBoard2 = Integer.MAX_VALUE;
            }

            //ascending order
            return userLeaderBoard1.compareTo(userLeaderBoard2);

        }};

    @Override
    public int compareTo(LbLanding lbLanding) {
        Integer Lbid=((LbLanding)lbLanding).getId();
        return (int)(this.id-Lbid);
    }

}