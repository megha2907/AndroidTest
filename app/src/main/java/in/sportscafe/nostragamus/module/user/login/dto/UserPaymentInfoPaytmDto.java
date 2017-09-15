package in.sportscafe.nostragamus.module.user.login.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by sandip on 12/04/17.
 */
@Parcel
public class UserPaymentInfoPaytmDto {

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("mobile")
    public String getMobile() {
        return mobile;
    }

    @SerializedName("mobile")
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
