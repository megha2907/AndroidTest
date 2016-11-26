package in.sportscafe.nostragamus.module.user.powerups;

import in.sportscafe.nostragamus.R;

/**
 * Created by deepanshi on 24/8/16.
 */
public class PowerUp {

    private String id;

    private String desc;

    private int icon;

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

    public String getDesc() {
        switch (id) {
            case "2x":
                id = "2x";
                desc="Double your returns when you are confident about a prediction";
                break;
            case "no_negs":
                id = "No Negative Points";
                desc="Avoid being penalised for an incorrect prediction";
                break;
            case "player_poll":
                id = "Player Poll";
                desc="Peak into how other Nostragamus players have predicted";
                break;
            default:
                desc="";
        }
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getIcon() {
        switch (id) {
            case "2x":
                return R.drawable.powerup_2x_white;
            case "no_negs":
                return R.drawable.powerup_nonegs_white;
            case "player_poll":
                return R.drawable.powerup_audience_poll_white;
            default:
                return R.drawable.placeholder_icon;
        }
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
