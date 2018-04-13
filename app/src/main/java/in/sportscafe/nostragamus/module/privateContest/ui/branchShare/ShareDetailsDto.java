package in.sportscafe.nostragamus.module.privateContest.ui.branchShare;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by sc on 29/3/18.
 */
@Parcel
public class ShareDetailsDto {

    @SerializedName("private_code")
    private String privateCode;

    @SerializedName("user_nick")
    private String userNick;

    @SerializedName("user_photo_url")
    private String userPhotoUrl;

    public String getPrivateCode() {
        return privateCode;
    }

    public void setPrivateCode(String privateCode) {
        this.privateCode = privateCode;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }
}
