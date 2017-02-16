package in.sportscafe.nostragamus.module.user.powerups;

import android.text.TextUtils;

import org.parceler.Parcel;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.Powerups;
import in.sportscafe.nostragamus.R;

/**
 * Created by deepanshi on 24/8/16.
 */
@Parcel
public class PowerUp {

    private String id;

    private int count;

    public PowerUp() {
    }

    public PowerUp(String id, int count) {
        this.id = id;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static int getPlayPowerupIcons(String powerupId) {
        if(TextUtils.isEmpty(powerupId)) {
            return -1;
        }

        switch (powerupId) {
            case Powerups.XX:
            case Powerups.XX_GLOBAL:
                return R.drawable.powerup_2x_white;
            case Powerups.NO_NEGATIVE:
                return R.drawable.powerup_nonegs_white;
            case Powerups.AUDIENCE_POLL:
                return R.drawable.powerup_audience_poll_white;
            default:
                return -1;
        }
    }

    public static int getResultPowerupIcons(String powerupId) {
        if(TextUtils.isEmpty(powerupId)) {
            return -1;
        }

        switch (powerupId) {
            case Powerups.XX:
            case Powerups.XX_GLOBAL:
                return R.drawable.powerup_icon;
            case Powerups.NO_NEGATIVE:
                return R.drawable.powerup_nonegs;
            case Powerups.AUDIENCE_POLL:
                return R.drawable.powerup_audience_poll;
            case Powerups.MATCH_REPLAY:
                return R.drawable.replay_icon;
            case Powerups.ANSWER_FLIP:
                return R.drawable.powerup_flip;
            default:
                return -1;
        }
    }
}