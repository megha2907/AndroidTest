package in.sportscafe.nostragamus.module.settings.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jeeva on 3/10/16.
 */

public class AppSettingsResponse {

    @JsonProperty("version")
    private VersionDetails version;

    @JsonProperty("version")
    public VersionDetails getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(VersionDetails version) {
        this.version = version;
    }
}