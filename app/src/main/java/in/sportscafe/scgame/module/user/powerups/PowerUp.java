package in.sportscafe.scgame.module.user.powerups;

/**
 * Created by deepanshi on 24/8/16.
 */
public class PowerUp {

    private String id;

    private String desc;

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
        if(id=="2x"){
            desc="Double your returns when you are confident about a prediction";
        }
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
