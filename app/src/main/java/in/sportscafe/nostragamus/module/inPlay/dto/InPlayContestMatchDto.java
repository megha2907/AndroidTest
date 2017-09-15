package in.sportscafe.nostragamus.module.inPlay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

/**
 * Created by sandip on 12/09/17.
 */
@Parcel
public class InPlayContestMatchDto {

    @JsonProperty("is_played")
    private int isPlayed;

    @JsonProperty("score")
    private int score;

    @JsonProperty("start_time")
    private String startTime;

    @JsonProperty("status")
    private String status;

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

    public String isStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
