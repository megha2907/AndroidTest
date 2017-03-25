package in.sportscafe.nostragamus.module.user.myprofile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

/**
 * Created by Jeeva on 24/6/16.
 */
@Parcel
public class TournamentFeedInfo {

    @JsonProperty("tournament_id")
    private Integer tournamentId;

    @JsonProperty("tournament_name")
    private String tournamentName;

    @JsonProperty("tournament_img_url")
    private String tournamentPhoto;

    @JsonProperty("sports_name")
    private String SportsName;

    @JsonProperty("count_unplayed")
    private String CountsUnplayed;

    @JsonProperty("tournament_subtext")
    private String tournamentSubtext;

    public TournamentFeedInfo() {
    }

    public TournamentFeedInfo(int tournamentId) {
        this.tournamentId = tournamentId;
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
    public String getTournamentPhoto() {
        return tournamentPhoto;
    }

    @JsonProperty("tournament_img_url")
    public void setTournamentPhoto(String tournamentPhoto) {
        this.tournamentPhoto = tournamentPhoto;
    }

    @JsonProperty("sports_name")
    public String getSportsName() {
        return SportsName;
    }

    @JsonProperty("sports_name")
    public void setSportsName(String sportsName) {
        SportsName = sportsName;
    }

    @JsonProperty("count_unplayed")
    public String getCountsUnplayed() {
        return CountsUnplayed;
    }

    @JsonProperty("count_unplayed")
    public void setCountsUnplayed(String countsUnplayed) {
        CountsUnplayed = countsUnplayed;
    }

    @JsonProperty("tournament_subtext")
    public String getTournamentSubtext() {
        return tournamentSubtext;
    }

    @JsonProperty("tournament_subtext")
    public void setTournamentSubtext(String tournamentSubtext) {
        this.tournamentSubtext = tournamentSubtext;
    }

    @Override
    public boolean equals(Object o) {
        TournamentFeedInfo tournamentInfo = (TournamentFeedInfo) o;
        return tournamentId == tournamentInfo.getTournamentId();
    }

    @Override
    public String toString() {
        return tournamentName;
    }

}
