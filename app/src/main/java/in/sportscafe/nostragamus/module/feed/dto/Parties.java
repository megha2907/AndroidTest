package in.sportscafe.nostragamus.module.feed.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 9/26/16.
 */
@Parcel
public class Parties {

    @SerializedName("party_name")
    private String partyName;

    @SerializedName("party_id")
    private int partyId;

    @SerializedName("party_img_url")
    private String partyImageUrl;

    @SerializedName("party_id")
    public int getPartyId() {
        return partyId;
    }

    @SerializedName("party_id")
    public void setPartyId(int partyId) {
        this.partyId = partyId;
    }

    @SerializedName("party_name")
    public String getPartyName() {
        return partyName;
    }

    @SerializedName("party_name")
    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    @SerializedName("party_img_url")
    public String getPartyImageUrl() {
        return partyImageUrl;
    }

    @SerializedName("party_img_url")
    public void setPartyImageUrl(String partyImageUrl) {
        this.partyImageUrl = partyImageUrl;
    }

}