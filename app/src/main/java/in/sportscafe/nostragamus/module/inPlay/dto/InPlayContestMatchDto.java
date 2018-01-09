package in.sportscafe.nostragamus.module.inPlay.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by sandip on 12/09/17.
 */
@Parcel
public class InPlayContestMatchDto {

    @SerializedName("is_played")
    private int isPlayed;

    @SerializedName("score")
    private int score;

    @SerializedName("match_starttime")
    private String startTime;

    @SerializedName("status")
    private String status;

    @SerializedName("parties")
    private String parties;

    public int isPlayed() {
        return isPlayed;
    }

    public void setPlayed(int played) {
        isPlayed = played;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getParties() {
        return parties;
    }

    public void setParties(String parties) {
        this.parties = parties;
    }
}
