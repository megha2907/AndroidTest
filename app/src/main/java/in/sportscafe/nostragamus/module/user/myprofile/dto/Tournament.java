package in.sportscafe.nostragamus.module.user.myprofile.dto;

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

    @JsonProperty("tournament_short_name")
    private String tournamentShortName;

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

    @JsonProperty("tournament_short_name")
    public String getTournamentShortName() {
        return tournamentShortName;
    }

    @JsonProperty("tournament_short_name")
    public void setTournamentShortName(String tournamentShortName) {
        this.tournamentShortName = tournamentShortName;
    }
}