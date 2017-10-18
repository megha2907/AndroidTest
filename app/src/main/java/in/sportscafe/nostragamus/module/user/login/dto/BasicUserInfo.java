package in.sportscafe.nostragamus.module.user.login.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Jeeva on 10/01/17.
 */
@Parcel
public class BasicUserInfo {

    @SerializedName("user_id")
    private Integer id;

    @SerializedName("user_nick")
    private String userNickName;

    @SerializedName("user_id")
    public Integer getId() {
        return id;
    }

    @SerializedName("user_id")
    public void setId(Integer id) {
        this.id = id;
    }

    @SerializedName("user_nick")
    public String getUserNickName() {
        return userNickName;
    }

    @SerializedName("user_nick")
    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }
}