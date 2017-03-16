package in.sportscafe.nostragamus.module.play.dummygame;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

/**
 * Created by Jeeva on 15/03/17.
 */
@Parcel
public class DGAnimation {

    public interface AnimationType {
        String ALPHA = "alpha";
        String SCALE = "scale";
    }

    @JsonProperty("start")
    private float start;

    @JsonProperty("end")
    private float end;

    @JsonProperty("duration")
    private long duration;

    @JsonProperty("type")
    private String type;

    @JsonProperty("start")
    public float getStart() {
        return start;
    }

    @JsonProperty("start")
    public void setStart(float start) {
        this.start = start;
    }

    @JsonProperty("end")
    public float getEnd() {
        return end;
    }

    @JsonProperty("end")
    public void setEnd(float end) {
        this.end = end;
    }

    @JsonProperty("duration")
    public long getDuration() {
        return duration;
    }

    @JsonProperty("duration")
    public void setDuration(long duration) {
        this.duration = duration;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }
}