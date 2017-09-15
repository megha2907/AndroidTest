package in.sportscafe.nostragamus.module.fuzzyplayers;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.user.login.dto.BasicUserInfo;

/**
 * Created by Jeeva on 10/01/17.
 */

public class FuzzyPlayerResponse {

    @SerializedName("data")
    private List<BasicUserInfo> players = new ArrayList<>();

    @SerializedName("data")
    public List<BasicUserInfo> getPlayers() {
        return players;
    }

    @SerializedName("data")
    public void setPlayers(List<BasicUserInfo> players) {
        this.players = players;
    }
}