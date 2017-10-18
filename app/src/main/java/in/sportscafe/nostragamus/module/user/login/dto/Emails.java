package in.sportscafe.nostragamus.module.user.login.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepanshi on 11/26/16.
 */

public class Emails {

    @SerializedName("value")
    private List<String> email = new ArrayList<>();

    @SerializedName("value")
    public List<String> getEmail() {
        return email;
    }

    @SerializedName("value")
    public void setEmail(List<String> email) {
        this.email = email;
    }
}
