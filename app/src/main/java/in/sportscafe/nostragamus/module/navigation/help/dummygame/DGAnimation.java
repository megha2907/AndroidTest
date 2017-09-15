package in.sportscafe.nostragamus.module.navigation.help.dummygame;

import com.google.gson.annotations.SerializedName;

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

    @SerializedName("start")
    private float start;

    @SerializedName("end")
    private float end;

    @SerializedName("duration")
    private long duration;

    @SerializedName("type")
    private String type;

    @SerializedName("start")
    public float getStart() {
        return start;
    }

    @SerializedName("start")
    public void setStart(float start) {
        this.start = start;
    }

    @SerializedName("end")
    public float getEnd() {
        return end;
    }

    @SerializedName("end")
    public void setEnd(float end) {
        this.end = end;
    }

    @SerializedName("duration")
    public long getDuration() {
        return duration;
    }

    @SerializedName("duration")
    public void setDuration(long duration) {
        this.duration = duration;
    }

    @SerializedName("type")
    public String getType() {
        return type;
    }

    @SerializedName("type")
    public void setType(String type) {
        this.type = type;
    }
}