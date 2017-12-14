package in.sportscafe.nostragamus.module.user.login.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sc on 30/11/17.
 */

public class Device {

    @SerializedName("imei")
    private String imei;

    @SerializedName("androidId")
    private String androidId;

    @SerializedName("macId")
    private String macId;

    @SerializedName("manufecturer")
    private String manufecturer;

    @SerializedName("model")
    private String model;

    @SerializedName("user_accounts")
    private String userAccounts;

    @SerializedName("screen_dpi")
    private float screenDpi;

    @SerializedName("screen_height")
    private float screenHeight;

    @SerializedName("screen_width")
    private float screenWidth;

    @SerializedName("os_api_level")
    private int osApiLevel;

    @SerializedName("app_version")
    private int appVersion;

    @SerializedName("RAM")
    private long ram;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getMacId() {
        return macId;
    }

    public void setMacId(String macId) {
        this.macId = macId;
    }

    public String getManufecturer() {
        return manufecturer;
    }

    public void setManufecturer(String manufecturer) {
        this.manufecturer = manufecturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public float getScreenDpi() {
        return screenDpi;
    }

    public void setScreenDpi(float screenDpi) {
        this.screenDpi = screenDpi;
    }

    public float getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(float screenHeight) {
        this.screenHeight = screenHeight;
    }

    public float getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(float screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getOsApiLevel() {
        return osApiLevel;
    }

    public void setOsApiLevel(int osApiLevel) {
        this.osApiLevel = osApiLevel;
    }

    public int getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(int appVersion) {
        this.appVersion = appVersion;
    }

    public long getRam() {
        return ram;
    }

    public void setRam(long ram) {
        this.ram = ram;
    }

    public String getUserAccounts() {
        return userAccounts;
    }

    public void setUserAccounts(String userAccounts) {
        this.userAccounts = userAccounts;
    }
}
