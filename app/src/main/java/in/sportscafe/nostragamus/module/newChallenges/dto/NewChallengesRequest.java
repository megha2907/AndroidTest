package in.sportscafe.nostragamus.module.newChallenges.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sandip on 30/08/17.
 */

public class NewChallengesRequest {

    @JsonProperty("challenge_id")
    private int id;

}
