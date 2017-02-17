package in.sportscafe.nostragamus.module.tournamentFeed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 9/28/16.
 */
@Parcel
public class Tournament {

    @JsonProperty("tournament_id")
    private int id;

    @JsonProperty("tournament_name")
    private String name;

    @JsonProperty("tournament_id")
    public int getId() {
        return id;
    }

    @JsonProperty("tournament_id")
    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty("tournament_name")
    public String getName() {
        return name;
    }

    @JsonProperty("tournament_name")
    public void setName(String name) {
        this.name = name;
    }
}