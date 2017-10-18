package in.sportscafe.nostragamus.module.user.login.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.user.badges.Badge;


/**
 * Created by deepanshi on 25/8/16.
 */
public class BadgeInfo {

    @SerializedName("badges")
         List<Badge> badges = new ArrayList<>();

        @SerializedName("badges")
        public List<Badge> getBadges() {
            return badges;
        }

        @SerializedName("badges")
        public void setBadges(List<Badge> badges) {
            this.badges = badges;
        }
}
