package in.sportscafe.scgame.module.feed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jeeva.android.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepanshi on 9/26/16.
 */

public class Parties implements Serializable {

    @JsonProperty("party_name")
    private String partyName;

    @JsonProperty("party_id")
    private String partyId;

    @JsonProperty("party_img_url")
    private String partyImageUrl;

    @JsonProperty("party_id")
    public String getPartyId() {
        return partyId;
    }

    @JsonProperty("party_id")
    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    @JsonProperty("party_name")
    public String getPartyName() {
        return partyName;
    }

    @JsonProperty("party_name")
    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    @JsonProperty("party_img_url")
    public String getPartyImageUrl() {
        return partyImageUrl;
    }

    @JsonProperty("party_img_url")
    public void setPartyImageUrl(String partyImageUrl) {
        this.partyImageUrl = partyImageUrl;
    }



}
