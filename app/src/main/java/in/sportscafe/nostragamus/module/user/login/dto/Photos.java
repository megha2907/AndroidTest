package in.sportscafe.nostragamus.module.user.login.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepanshi on 11/26/16.
 */

public class Photos {

    @SerializedName("value")
    private List<String> photo;

    @SerializedName("value")
    public List<String> getPhoto() {
        return photo;
    }

    @SerializedName("value")
    public void setPhoto(List<String> photo) {
        this.photo = photo;
    }
}
