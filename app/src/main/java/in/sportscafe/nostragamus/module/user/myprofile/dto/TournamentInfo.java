package in.sportscafe.nostragamus.module.user.myprofile.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by deepanshi on 11/14/16.
 */
@Parcel
public class TournamentInfo {

    @SerializedName("sports_id")
    private Integer sportsId;

    @SerializedName("sports_name")
    private String sportsName;

    @SerializedName("tournaments")
    private List<TournamentFeedInfo> tournamentFeedInfoList;

    /**
     * @return The sportsId
     */
    @SerializedName("sports_id")
    public Integer getSportsId() {
        return sportsId;
    }

    /**
     * @param sportsId The user_id
     */
    @SerializedName("sports_id")
    public void setSportsId(Integer sportsId) {
        this.sportsId = sportsId;
    }

    @SerializedName("sports_name")
    public String getSportsName() {
        return sportsName;
    }

    @SerializedName("sports_name")
    public void setSportsName(String sportsName) {
        this.sportsName = sportsName;
    }


    @SerializedName("tournaments")
    public List<TournamentFeedInfo> getTournamentFeedInfoList() {
        return tournamentFeedInfoList;
    }

    @SerializedName("tournaments")
    public void setTournamentFeedInfoList(List<TournamentFeedInfo> tournamentFeedInfoList) {
        this.tournamentFeedInfoList = tournamentFeedInfoList;
    }

}