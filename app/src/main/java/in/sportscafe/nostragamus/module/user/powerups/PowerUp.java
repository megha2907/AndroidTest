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
                desc="Double your returns when you are confident about a prediction";
            default:
                desc="Double your returns when you are confident about a prediction";
        }
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getIcon() {
        switch (id) {
            case "2x":
                return R.drawable.powerup_colored_icon;
            default:
                return R.drawable.placeholder_icon;
        }
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
