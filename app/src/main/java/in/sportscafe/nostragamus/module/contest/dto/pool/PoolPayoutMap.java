package in.sportscafe.nostragamus.module.contest.dto.pool;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by sc on 7/12/17.
 */
@Parcel
public class PoolPayoutMap {

    @SerializedName("percent")
    private float percent;

    @SerializedName("share")
    private float share;

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public float getShare() {
        return share;
    }

    public void setShare(float share) {
        this.share = share;
    }
}
