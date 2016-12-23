package in.sportscafe.nostragamus.module.user.playerprofile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by deepanshi on 12/22/16.
 */
public class BadgesInfo {

    @JsonProperty("badges")
    private List<String> badges = new ArrayList<>();

    @JsonProperty("badges")
    public List<String> getBadges() {
        return badges;
    }

    @JsonProperty("badges")
    public void setBadges(List<String> badges) {
        this.badges = badges;
    }

}
