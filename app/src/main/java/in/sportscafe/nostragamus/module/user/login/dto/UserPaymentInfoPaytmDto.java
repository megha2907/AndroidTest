package in.sportscafe.nostragamus.module.user.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

/**
 * Created by sandip on 12/04/17.
 */
@Parcel
public class UserPaymentInfoPaytmDto {

    @JsonProperty("mobile")
    private Integer mobile;

    public Integer getMobile() {
        return mobile;
    }

    public void setMobile(Integer mobile) {
        this.mobile = mobile;
    }
}
