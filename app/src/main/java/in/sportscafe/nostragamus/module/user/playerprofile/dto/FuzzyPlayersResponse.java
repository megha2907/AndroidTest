package in.sportscafe.nostragamus.module.user.playerprofile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.user.login.dto.BasicUserInfo;

/**
 * Created by Jeeva on 10/01/17.
 */

public class FuzzyPlayersResponse {

    @JsonProperty("data")
    private List<BasicUserInfo> players = new ArrayList<>();

    @JsonProperty("data")
    public List<BasicUserInfo> getPlayers() {
        return players;
    }

    @JsonProperty("data")
    public void setPlayers(List<BasicUserInfo> players) {
        this.players = players;
    }
}