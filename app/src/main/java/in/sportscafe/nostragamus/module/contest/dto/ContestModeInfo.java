package in.sportscafe.nostragamus.module.contest.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 9/13/17.
 */

@Parcel
public class ContestModeInfo {

    @SerializedName("name")
    private String name;

    @SerializedName("desc")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
