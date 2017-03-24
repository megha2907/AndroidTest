package in.sportscafe.nostragamus.module.user.myprofile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by deepanshi on 11/14/16.
 */
@Parcel
public class TournamentInfo {

    @JsonProperty("sports_id")
    private Integer sportsId;

    @JsonProperty("sports_name")
    private String sportsName;

    @JsonProperty("tournaments")
    private List<TournamentFeedInfo> tournamentFeedInfoList;

    /**
     * @return The sportsId
     */
    @JsonProperty("sports_id")
    public Integer getSportsId() {
        return sportsId;
    }

    /**
     * @param sportsId The user_id
     */
    @JsonProperty("sports_id")
    public void setSportsId(Integer sportsId) {
        this.sportsId = sportsId;
    }

    @JsonProperty("sports_name")
    public String getSportsName() {
        return sportsName;
    }

    @JsonProperty("sports_name")
    public void setSportsName(String sportsName) {
        this.sportsName = sportsName;
    }


    @JsonProperty("tournaments")
    public List<TournamentFeedInfo> getTournamentFeedInfoList() {
        return tournamentFeedInfoList;
    }

    @JsonProperty("tournaments")
    public void setTournamentFeedInfoList(List<TournamentFeedInfo> tournamentFeedInfoList) {
        this.tournamentFeedInfoList = tournamentFeedInfoList;
    }

}