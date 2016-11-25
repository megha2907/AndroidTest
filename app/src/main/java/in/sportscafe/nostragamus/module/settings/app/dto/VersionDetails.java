package in.sportscafe.nostragamus.module.settings.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by deepanshu on 4/8/16.
 */
public class VersionDetails {

    @JsonProperty("forceUpdateVersion")
    private Version forceUpdateVersion;

    @JsonProperty("normalUpdateVersion")
    private Version normalUpdateVersion;

    @JsonProperty("forceUpdateVersion")
    public Version getForceUpdateVersion() {
        return forceUpdateVersion;
    }

    @JsonProperty("forceUpdateVersion")
    public void setForceUpdateVersion(Version forceUpdateVersion) {
        this.forceUpdateVersion = forceUpdateVersion;
    }

    @JsonProperty("normalUpdateVersion")
    public Version getNormalUpdateVersion() {
        return normalUpdateVersion;
    }

    @JsonProperty("normalUpdateVersion")
    public void setNormalUpdateVersion(Version normalUpdateVersion) {
        this.normalUpdateVersion = normalUpdateVersion;
    }
}