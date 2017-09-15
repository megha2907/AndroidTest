package in.sportscafe.nostragamus.module.user.lblanding;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.Comparator;

/**
 * Created by Jeeva on 19/01/17.
 */
@Parcel
public class LbLanding {

    private Integer id = 1;

    @SerializedName("challenge_id")
    private Integer challengeId;

    @SerializedName("group_id")
    private Integer groupId;

    @SerializedName("challenge_name")
    private String name = "Cricket";

    private Integer rank;

    @SerializedName("rank_change")
    private Integer rankChange;

    @SerializedName("count_participants")
    private Integer countParticipants;

    @SerializedName("count_played")
    private Integer countPlayed;

    @SerializedName("challenge_img_url")
    private String imgUrl;

    @SerializedName("challenge_starttime")
    private String startTime;

    @SerializedName("challenge_endtime")
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

    @SerializedName("challenge_name")
    public String getName() {
        return name;
    }

    @SerializedName("challenge_name")
    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @SerializedName("challenge_img_url")
    public String getImgUrl() {
        return imgUrl;
    }

    @SerializedName("challenge_img_url")
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @SerializedName("count_played")
    public Integer getCountPlayed() {
        return countPlayed;
    }

    @SerializedName("count_played")
    public void setCountPlayed(Integer countPlayed) {
        this.countPlayed = countPlayed;
    }

    @SerializedName("challenge_id")
    public Integer getChallengeId() {
        return challengeId;
    }

    @SerializedName("challenge_id")
    public void setChallengeId(Integer challengeId) {
        this.challengeId = challengeId;
    }

    @SerializedName("group_id")
    public Integer getGroupId() {
        return groupId;
    }

    @SerializedName("group_id")
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    @SerializedName("challenge_starttime")
    public String getStartTime() {
        return startTime;
    }

    @SerializedName("challenge_starttime")
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @SerializedName("challenge_endtime")
    public String getEndTime() {
        return endTime;
    }

    @SerializedName("challenge_endtime")
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @SerializedName("count_participants")
    public Integer getCountParticipants() {
        return countParticipants;
    }

    @SerializedName("count_participants")
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