package in.sportscafe.scgame.module.user.powerups;

/**
 * Created by deepanshi on 24/8/16.
 */
public class PowerUp {

    private String name;

    private int count;

    public PowerUp() {
    }

    public PowerUp(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
