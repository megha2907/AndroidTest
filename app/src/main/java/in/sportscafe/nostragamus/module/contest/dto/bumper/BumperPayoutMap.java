package in.sportscafe.nostragamus.module.contest.dto.bumper;

import com.google.gson.annotations.SerializedName;

/**
 * Created by deepanshi on 2/2/18.
 */

public class BumperPayoutMap {

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
