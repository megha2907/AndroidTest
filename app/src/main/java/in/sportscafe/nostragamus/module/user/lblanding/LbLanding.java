package in.sportscafe.nostragamus.module.user.lblanding;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.Comparator;

/**
 * Created by Jeeva on 19/01/17.
 */
@Parcel
public class LbLanding {

    private Integer id = 1;

    @JsonProperty("challenge_id")
    private Integer challengeId;

    @JsonProperty("group_id")
    private Integer groupId;

    @JsonProperty("challenge_name")
    private String name = "Cricket";

    private Integer rank;

    @JsonProperty("rank_change")
    private Integer rankChange;

    @JsonProperty("count_participants")
    private Integer countParticipants;

    @JsonProperty("count_played")
    private Integer countPlayed;

    @JsonProperty("challenge_img_url")
    private String imgUrl;

    @JsonProperty("challenge_starttime")
    private String startTime;

    @JsonProperty("challenge_endtime")
    private String endTime;

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

    public LbLanding(Integer challengeId,Integer groupId, String name, String imgUrl, String type) {
        this.challengeId = challengeId;
        this.groupId = groupId;
        this.name = name;
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

    @JsonProperty("challenge_name")
    public String getName() {
        return name;
    }

    @JsonProperty("challenge_name")
    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("challenge_img_url")
    public String getImgUrl() {
        return imgUrl;
    }

    @JsonProperty("challenge_img_url")
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

    @JsonProperty("challenge_id")
    public Integer getChallengeId() {
        return challengeId;
    }

    @JsonProperty("challenge_id")
    public void setChallengeId(Integer challengeId) {
        this.challengeId = challengeId;
    }

    @JsonProperty("group_id")
    public Integer getGroupId() {
        return groupId;
    }

    @JsonProperty("group_id")
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    @JsonProperty("challenge_starttime")
    public String getStartTime() {
        return startTime;
    }

    @JsonProperty("challenge_starttime")
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @JsonProperty("challenge_endtime")
    public String getEndTime() {
        return endTime;
    }

    @JsonProperty("challenge_endtime")
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @JsonProperty("count_participants")
    public Integer getCountParticipants() {
        return countParticipants;
    }

    @JsonProperty("count_participants")
    public void setCountParticipants(Integer countParticipants) {
        this.countParticipants = countParticipants;
    }



    /*Comparator for sorting the list by User Rank*/
    public static Comparator<LbLanding> LeaderBoardRankComparator = new Comparator<LbLanding>() {

        public int compare(LbLanding lhs, LbLanding rhs) {
            //ascending order
            if(null == lhs.rank || null == rhs.rank) {
                return -1;
            }
            return lhs.rank - rhs.rank;
        }
    };

    /*Comparator for sorting the list by Rank Change*/
    public static Comparator<LbLanding> LeaderBoardRankChangeComparator = new Comparator<LbLanding>() {

        public int compare(LbLanding lhs, LbLanding rhs) {
            //ascending order
            if(null == lhs.rankChange || null == rhs.rankChange) {
                return -1;
            }
            return lhs.rankChange - rhs.rankChange;
        }
    };

    /*Comparator for sorting the list by User Rank*/
    public static Comparator<LbLanding> LeaderBoardDateComparator = new Comparator<LbLanding>() {

        public int compare(LbLanding lhs, LbLanding rhs) {
            //ascending order
            if(null == lhs.rank || null == rhs.rank) {
                return -1;
            }
            return lhs.rank - rhs.rank;
        }
    };


    /*Comparator for sorting the list by Played Matches*/
    public static Comparator<LbLanding> LeaderBoardPlayedMatchesComparator = new Comparator<LbLanding>() {

        public int compare(LbLanding lhs, LbLanding rhs) {
            //ascending order
            if(null == lhs.countPlayed || null == rhs.countPlayed) {
                return -1;
            }
            return lhs.countPlayed - rhs.countPlayed;
        }
    };

}