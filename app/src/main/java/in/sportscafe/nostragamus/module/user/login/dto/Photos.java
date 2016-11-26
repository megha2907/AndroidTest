package in.sportscafe.nostragamus.module.user.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepanshi on 11/26/16.
 */

public class Photos {

    @JsonProperty("value")
    private List<String> photo;

    @JsonProperty("value")
    public List<String> getPhoto() {
        return photo;
    }

    @JsonProperty("value")
    public void setPhoto(List<String> photo) {
        this.photo = photo;
    }
}
