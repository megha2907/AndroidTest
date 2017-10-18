package in.sportscafe.nostragamus.webservice;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by deepanshi on 10/18/17.
 */

public class SportsInfo {

    @SerializedName("sports_id")
    private int sportsId;

    @SerializedName("sports_name")
    private String sportsName;

    @SerializedName("sports_selected_icon")
    private String sportsSelectedIcon;

    @SerializedName("sports_unselected_icon")
    private String sportsUnSelectedIcon;

    public int getSportsId() {
        return sportsId;
    }

    public void setSportsId(int sportsId) {
        this.sportsId = sportsId;
    }

    public String getSportsName() {
        return sportsName;
    }

    public void setSportsName(String sportsName) {
        this.sportsName = sportsName;
    }

    public String getSportsSelectedIcon() {
        return sportsSelectedIcon;
    }

    public void setSportsSelectedIcon(String sportsSelectedIcon) {
        this.sportsSelectedIcon = sportsSelectedIcon;
    }

    public String getSportsUnSelectedIcon() {
        return sportsUnSelectedIcon;
    }

    public void setSportsUnSelectedIcon(String sportsUnSelectedIcon) {
        this.sportsUnSelectedIcon = sportsUnSelectedIcon;
    }
}
