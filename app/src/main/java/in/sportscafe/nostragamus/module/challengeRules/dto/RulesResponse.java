package in.sportscafe.nostragamus.module.challengeRules.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 9/15/17.
 */

@Parcel
public class RulesResponse {

    @JsonProperty("data")
    private Rules rules;

    public Rules getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }


}
