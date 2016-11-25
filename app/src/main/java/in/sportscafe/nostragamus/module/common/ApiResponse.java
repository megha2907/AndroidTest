package in.sportscafe.nostragamus.module.common;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jeeva on 30/6/16.
 */
public class ApiResponse {

    @JsonProperty("data")
    private String message;

    @JsonProperty("data")
    public String getMessage() {
        return message;
    }

    @JsonProperty("data")
    public void setMessage(String message) {
        this.message = message;
    }
}