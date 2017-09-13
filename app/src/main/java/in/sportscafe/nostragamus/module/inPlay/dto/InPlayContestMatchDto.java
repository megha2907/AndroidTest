package in.sportscafe.nostragamus.module.inPlay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

/**
 * Created by sandip on 12/09/17.
 */
@Parcel
public class InPlayContestMatchDto {

    @JsonProperty("is_played")
    private boolean isPlayed;

    @JsonProperty("score")
    private int score;

    @JsonProperty("start_time")
    private String startTime;

    @JsonProperty("isCompleted")
    private boolean isCompleted;

    public boolean isPlayed() {
        return isPlayed;
    }

    public void setPlayed(boolean played) {
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

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
