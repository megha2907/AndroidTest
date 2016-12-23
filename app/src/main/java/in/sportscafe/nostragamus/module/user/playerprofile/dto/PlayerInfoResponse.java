package in.sportscafe.nostragamus.module.user.playerprofile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;

/**
 * Created by deepanshi on 12/22/16.
 */

public class PlayerInfoResponse {

        @JsonProperty("data")
        private PlayerInfo playerInfo;

        @JsonProperty("data")
        public PlayerInfo getPlayerInfo() {
            return playerInfo;
        }

        @JsonProperty("data")
        public void setPlayerInfo(PlayerInfo playerInfo) {
            this.playerInfo = playerInfo;
        }

}
