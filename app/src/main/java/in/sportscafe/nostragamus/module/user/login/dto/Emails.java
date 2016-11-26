package in.sportscafe.nostragamus.module.user.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepanshi on 11/26/16.
 */

public class Emails {

    @JsonProperty("value")
    private List<String> email = new ArrayList<>();

    @JsonProperty("value")
    public List<String> getEmail() {
        return email;
    }

    @JsonProperty("value")
    public void setEmail(List<String> email) {
        this.email = email;
    }
}
