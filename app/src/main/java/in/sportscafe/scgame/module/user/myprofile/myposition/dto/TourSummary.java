package in.sportscafe.scgame.module.user.myprofile.myposition.dto;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jeeva.android.Log;

import java.io.Serializable;
import java.util.List;

/**
 * Created by deepanshi on 9/21/16.
 */
public class TourSummary extends BaseSummary implements Serializable {

    public TourSummary() {
    }

    public TourSummary(Integer tournamentId, String tournamentName, String tournamentImageUrl, Integer rank, Integer rankChange, Integer overallRank, Integer overallRankChange, Integer sportId, String sportName,String sportPhoto) {
        super(sportName,sportPhoto, tournamentId, tournamentName, tournamentImageUrl, rank, rankChange, overallRank, overallRankChange);
        this.sportId = sportId;
        this.sportName = sportName;
        this.sportPhoto = sportPhoto;
    }

    @JsonProperty("sports_id")
    private Integer sportId;

    @JsonProperty("sports_name")
    private String sportName;

    @JsonIgnore
    private String sportPhoto;

    @JsonProperty("sports_id")
    public Integer getSportId() {
        return sportId;
    }

    @JsonProperty("sports_id")
    public void setSportId(Integer sportId) {
        this.sportId = sportId;
    }

    @JsonProperty("sports_name")
    public String getSportName() {
        return sportName;
    }

    @JsonProperty("sports_name")
    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    @JsonIgnore
    public String getSportPhoto() {
        return sportPhoto;
    }

    @JsonIgnore
    public void setSportPhoto(String sportPhoto) {
        this.sportPhoto = sportPhoto;
    }




}