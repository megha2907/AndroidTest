package in.sportscafe.nostragamus.module.user.myprofile.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Jeeva on 24/6/16.
 */
@Parcel
public class TournamentFeedInfo {

    @SerializedName("tournament_id")
    private Integer tournamentId;

    @SerializedName("tournament_name")
    private String tournamentName;

    @SerializedName("tournament_img_url")
    private String tournamentPhoto;

    @SerializedName("sports_name")
    private String SportsName;

    @SerializedName("count_unplayed")
    private String CountsUnplayed;

    @SerializedName("tournament_subtext")
    private String tournamentSubtext;

    public TournamentFeedInfo() {
    }

    public TournamentFeedInfo(int tournamentId) {
        this.tournamentId = tournamentId;
    }


    @SerializedName("tournament_id")
    public Integer getTournamentId() {
        return tournamentId;
    }

    @SerializedName("tournament_id")
    public void setTournamentId(Integer tournamentId) {
        this.tournamentId = tournamentId;
    }

    @SerializedName("tournament_name")
    public String getTournamentName() {
        return tournamentName;
    }

    @SerializedName("tournament_name")
    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    @SerializedName("tournament_img_url")
    public String getTournamentPhoto() {
        return tournamentPhoto;
    }

    @SerializedName("tournament_img_url")
    public void setTournamentPhoto(String tournamentPhoto) {
        this.tournamentPhoto = tournamentPhoto;
    }

    @SerializedName("sports_name")
    public String getSportsName() {
        return SportsName;
    }

    @SerializedName("sports_name")
    public void setSportsName(String sportsName) {
        SportsName = sportsName;
    }

    @SerializedName("count_unplayed")
    public String getCountsUnplayed() {
        return CountsUnplayed;
    }

    @SerializedName("count_unplayed")
    public void setCountsUnplayed(String countsUnplayed) {
        CountsUnplayed = countsUnplayed;
    }

    @SerializedName("tournament_subtext")
    public String getTournamentSubtext() {
        return tournamentSubtext;
    }

    @SerializedName("tournament_subtext")
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
