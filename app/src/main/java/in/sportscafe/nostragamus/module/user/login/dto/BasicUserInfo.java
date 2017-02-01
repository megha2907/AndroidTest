package in.sportscafe.nostragamus.module.user.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

/**
 * Created by Jeeva on 10/01/17.
 */
@Parcel
public class BasicUserInfo {

    @JsonProperty("user_id")
    private Integer id;

    @JsonProperty("user_nick")
    private String userNickName;

    @JsonProperty("user_id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("user_id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("user_nick")
    public String getUserNickName() {
        return userNickName;
    }

    @JsonProperty("user_nick")
    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }
}