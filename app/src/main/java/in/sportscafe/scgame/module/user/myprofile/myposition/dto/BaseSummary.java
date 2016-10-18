package in.sportscafe.scgame.module.user.myprofile.myposition.dto;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by deepanshi on 9/21/16.
 */
public class BaseSummary implements Serializable, ParentListItem {

    @JsonIgnore
    private String name;

    @JsonProperty("tournament_id")
    private Integer tournamentId;

    @JsonProperty("tournament_name")
    private String tournamentName;

    @JsonProperty("tournament_img_url")
    private String tournamentImageUrl;

    @JsonProperty("rank")
    private Integer rank;

    @JsonProperty("rank_change")
    private Integer rankChange;

    @JsonIgnore
    private Integer overallRank;

    @JsonIgnore
    private Integer overallRankChange;

    @JsonProperty("total_points")
    private String tournamentTotalPoints;

    @JsonIgnore
    private List<BaseSummary> tourSummaryList;

    public BaseSummary() {
    }

    public BaseSummary(String name, Integer tournamentId, String tournamentName, String tournamentImageUrl, Integer rank, Integer rankChange, Integer overallRank, Integer overallRankChange) {
        this.name = name;
        this.tournamentId = tournamentId;
        this.tournamentName = tournamentName;
        this.tournamentImageUrl=tournamentImageUrl;
        this.rank=rank;
        this.rankChange=rankChange;
        this.overallRank=overallRank;
        this.overallRankChange=overallRankChange;
    }

    @JsonIgnore
    public String getName() {
        return name;
    }

    @JsonIgnore
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("tournament_id")
    public Integer getTournamentId() {
        return tournamentId;
    }

    @JsonProperty("tournament_id")
    public void setTournamentId(Integer tournamentId) {
        this.tournamentId = tournamentId;
    }

    @JsonProperty("tournament_name")
    public String getTournamentName() {
        return tournamentName;
    }

    @JsonProperty("tournament_name")
    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    @JsonProperty("tournament_img_url")
    public String getTournamentImageUrl() {
        return tournamentImageUrl;
    }

    @JsonProperty("tournament_img_url")
    public void setTournamentImageUrl(String tournamentImageUrl) {
        this.tournamentImageUrl = tournamentImageUrl;
    }

    @JsonProperty("rank")
    public Integer getRank()
    {
        return rank;
    }

    @JsonProperty("rank")
    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @JsonProperty("rank_change")
    public Integer getRankChange() {
        return rankChange;
    }

    @JsonProperty("rank_change")
    public void setRankChange(Integer rankChange) {
        this.rankChange = rankChange;
    }

    @JsonProperty("total_points")
    public String getTournamentTotalPoints() {
        return tournamentTotalPoints;
    }

    @JsonProperty("total_points")
    public void setTournamentTotalPoints(String tournamentTotalPoints) {
        this.tournamentTotalPoints = tournamentTotalPoints;
    }

    @JsonIgnore
    public List<BaseSummary> getSummaryList() {
        return tourSummaryList;
    }

    @JsonIgnore
    public void setSummaryList(List<BaseSummary> tourSummaryList) {
        this.tourSummaryList = tourSummaryList;
    }

    @Override
    public List<?> getChildItemList() {
        return tourSummaryList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public Integer getOverallRank() {
        return overallRank;
    }

    public void setOverallRank(Integer overallRank) {
        this.overallRank = overallRank;
    }

    public Integer getOverallRankChange() {
        return overallRankChange;
    }

    public void setOverallRankChange(Integer overallRankChange) {
        this.overallRankChange = overallRankChange;
    }


}