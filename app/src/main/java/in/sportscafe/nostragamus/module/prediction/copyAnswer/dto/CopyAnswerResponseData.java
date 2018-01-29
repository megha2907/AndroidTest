package in.sportscafe.nostragamus.module.prediction.copyAnswer.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sc on 29/1/18.
 */

public class CopyAnswerResponseData {

    @SerializedName("msg")
    private String Message;

    @SerializedName("copy_powerups")
    private boolean isPowerupsCopied;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }

    public boolean isPowerupsCopied() {
        return isPowerupsCopied;
    }

    public void setPowerupsCopied(boolean powerupsCopied) {
        this.isPowerupsCopied = powerupsCopied;
    }
}
