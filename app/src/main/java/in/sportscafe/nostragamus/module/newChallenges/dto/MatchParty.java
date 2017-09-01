package in.sportscafe.nostragamus.module.newChallenges.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sandip on 30/08/17.
 */

public class MatchParty {

    @JsonProperty("party_id")
    private int partyId;

    @JsonProperty("party_name")
    private String partyName;

    @JsonProperty("party_img_url")
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
