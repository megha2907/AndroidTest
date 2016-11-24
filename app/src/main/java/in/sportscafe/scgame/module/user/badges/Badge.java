package in.sportscafe.scgame.module.user.badges;

/**
 * Created by deepanshi on 25/8/16.
 */
public class Badge {

    private String name;

    private String desc;

    public Badge() {
    }

    public Badge(String name) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
