package in.sportscafe.nostragamus.module.navigation.wallet.withdrawMoney.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 13/06/17.
 */

public class WithdrawFromWalletResponse {

    @SerializedName("msg")
    private String msg;

    @SerializedName("code")
    private Integer code;

    @SerializedName("msg")
    public String getMsg() {
        return msg;
    }

    @SerializedName("msg")
    public void setMsg(String msg) {
        this.msg = msg;
    }

    @SerializedName("code")
    public Integer getCode() {
        return code;
    }

    @SerializedName("code")
    public void setCode(Integer code) {
        this.code = code;
    }

}
