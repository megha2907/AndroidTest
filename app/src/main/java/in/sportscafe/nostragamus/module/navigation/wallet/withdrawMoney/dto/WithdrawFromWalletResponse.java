package in.sportscafe.nostragamus.module.navigation.wallet.withdrawMoney.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sandip on 13/06/17.
 */

public class WithdrawFromWalletResponse {

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("code")
    private Integer code;

    @JsonProperty("msg")
    public String getMsg() {
        return msg;
    }

    @JsonProperty("msg")
    public void setMsg(String msg) {
        this.msg = msg;
    }

    @JsonProperty("code")
    public Integer getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(Integer code) {
        this.code = code;
    }

}
