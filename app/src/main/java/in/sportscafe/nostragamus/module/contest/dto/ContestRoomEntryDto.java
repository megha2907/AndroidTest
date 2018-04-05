package in.sportscafe.nostragamus.module.contest.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 06/09/17.
 */

public class ContestRoomEntryDto {

    @SerializedName("user_nick")
    private String userName;

    @SerializedName("user_photo")
    private String userPicUrl;

    @SerializedName("is_creator")
    private boolean isPrivateContestCreator = false;

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

    public boolean isPrivateContestCreator() {
        return isPrivateContestCreator;
    }

    public void setPrivateContestCreator(boolean privateContestCreator) {
        isPrivateContestCreator = privateContestCreator;
    }
}
