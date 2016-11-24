package in.sportscafe.scgame.module.user.myprofile.myposition.dto;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import in.sportscafe.scgame.R;

/**
 * Created by Jeeva on 11/7/16.
 */
public class RankSummary implements Serializable, ParentListItem {

    @JsonProperty("sports_id")
    private Integer sportId;

    @JsonProperty("sports_name")
    private String sportName;

    @JsonProperty("rank")
    private Integer rank;

    @JsonProperty("rank_change")
    private Integer rankChange;

    @JsonProperty("user_points")
    private Long userPoints;

//    @JsonProperty("tour_summary_list")
//    private List<TourSummary> tourSummaryList;

    @JsonIgnore
    private List<RankSummary> tourSummaryList;

    @JsonIgnore
    private Long groupId;

    public RankSummary() {
    }

    public RankSummary(Integer sportId, String sportName) {
        this.sportId = sportId;
        this.sportName = sportName;
    }

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

    @JsonProperty("rank")
    public Integer getRank() {
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

    @JsonProperty("user_points")
    public Long getUserPoints() {
        return userPoints;
    }

    @JsonProperty("user_points")
    public void setUserPoints(Long userPoints) {
        this.userPoints = userPoints;
    }

//   // @JsonProperty("tour_summary_list")
//    public List<TourSummary> getTourSummaryList() {
//        return tourSummaryList;
//    }
//
//   // @JsonProperty("tour_summary_list")
//    public void setTourSummaryList(List<TourSummary> tourSummaryList) {
//        this.tourSummaryList = tourSummaryList;
//    }


    @JsonIgnore
    public List<RankSummary> getSummaryList() {
        return tourSummaryList;
    }

    @JsonIgnore
    public void setSummaryList(List<RankSummary> tourSummaryList) {
        this.tourSummaryList = tourSummaryList;
    }


    @JsonIgnore
    public Long getGroupId() {
        return groupId;
    }

    @JsonIgnore
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @JsonIgnore
    public int getSportImage() {
        switch (sportId) {
            case 5:
                return R.drawable.badminton_menu_colored_icon;
            case 8:
                return R.drawable.boxing_menu_colored_icon;
            case 1:
                return R.drawable.cricket_menu_colored_icon;
            case 4:
                return R.drawable.football_menu_colored_icon;
            case 2:
                return R.drawable.hockey_menu_colored_icon;
            case 6:
                return R.drawable.kabaddi_menu_icon;
            case 7:
                return R.drawable.basketball_menu_colored_icon;
            case 3:
                return R.drawable.tennis_menu_colored_icon;
            case 10:
                return R.drawable.table_tennis_menu_colored_icon;
            default:
                return R.drawable.ic_launcher;
        }
    }

    @Override
    public List<?> getChildItemList() {
        return tourSummaryList;
        //return new ArrayList<>();
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}