package in.sportscafe.nostragamus.module.settings.app.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jeeva on 25/8/16.
 */

public class Version {

    @SerializedName("version")
    private int version;

    @SerializedName("message")
    private String message;

    @SerializedName("apkLink")
    private String apkLink;

    @SerializedName("version")
    public int getVersion() {
        return version;
    }

    @SerializedName("version")
    public void setVersion(int version) {
        this.version = version;
    }

    @SerializedName("message")
    public String getMessage() {
        return message;
    }

    @SerializedName("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("apkLink")
    public String getApkLink() {
        return apkLink;
    }

    @SerializedName("apkLink")
    public void setApkLink(String apkLink) {
        this.apkLink = apkLink;
    }
}