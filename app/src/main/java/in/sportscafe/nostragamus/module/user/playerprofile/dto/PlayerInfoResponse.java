package in.sportscafe.nostragamus.module.user.playerprofile.dto;

import com.google.gson.annotations.SerializedName;

import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;

/**
 * Created by deepanshi on 12/22/16.
 */

public class PlayerInfoResponse {

        @SerializedName("data")
        private PlayerInfo playerInfo;

        @SerializedName("data")
        public PlayerInfo getPlayerInfo() {
            return playerInfo;
        }

        @SerializedName("data")
        public void setPlayerInfo(PlayerInfo playerInfo) {
            this.playerInfo = playerInfo;
        }

}
