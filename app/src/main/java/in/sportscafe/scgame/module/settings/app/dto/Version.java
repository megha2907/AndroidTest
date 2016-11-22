package in.sportscafe.scgame.module.settings.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jeeva on 25/8/16.
 */
public class Version {

    @JsonProperty("version")
    private int version;

    @JsonProperty("message")
    private String message;

    @JsonProperty("version")
    public int getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(int version) {
        this.version = version;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }
}