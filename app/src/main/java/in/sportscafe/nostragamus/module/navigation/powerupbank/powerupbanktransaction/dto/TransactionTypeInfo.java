package in.sportscafe.nostragamus.module.navigation.powerupbank.powerupbanktransaction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 7/29/17.
 */

@Parcel
public class TransactionTypeInfo {

    @JsonProperty("name")
    private String name;

    @JsonProperty("image")
    private String image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
