package in.sportscafe.nostragamus.module.user.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.user.badges.Badge;


/**
 * Created by deepanshi on 25/8/16.
 */
public class BadgeInfo {

    @JsonProperty("badges")
         List<Badge> badges = new ArrayList<>();

        @JsonProperty("badges")
        public List<Badge> getBadges() {
            return badges;
        }

        @JsonProperty("badges")
        public void setBadges(List<Badge> badges) {
            this.badges = badges;
        }
}
