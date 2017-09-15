package in.sportscafe.nostragamus.module.newChallenges.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by sandip on 30/08/17.
 */
@Parcel
public class MatchParty {

    @SerializedName("party_id")
    private int partyId;

    @SerializedName("party_name")
    private String partyName;

    @SerializedName("party_img_url")
    private String partyImgUrl;

    public int getPartyId() {
        return partyId;
    }

    public void setPartyId(int partyId) {
        this.partyId = partyId;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getPartyImgUrl() {
        return partyImgUrl;
    }

    public void setPartyImgUrl(String partyImgUrl) {
        this.partyImgUrl = partyImgUrl;
    }
}
