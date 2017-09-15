package in.sportscafe.nostragamus.module.contest.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 06/09/17.
 */

public class ContestRoomEntryDto {

    @SerializedName("user_name")
    private String userName;

    @SerializedName("user_pic_url")
    private String userPicUrl;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPicUrl() {
        return userPicUrl;
    }

    public void setUserPicUrl(String userPicUrl) {
        this.userPicUrl = userPicUrl;
    }
}
