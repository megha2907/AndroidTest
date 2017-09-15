package in.sportscafe.nostragamus.module.user.myprofile.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 9/28/16.
 */
@Parcel
public class Tournament {

    @SerializedName("tournament_id")
    private int id;

    @SerializedName("tournament_name")
    private String name;

    @SerializedName("tournament_short_name")
    private String tournamentShortName;

    @SerializedName("tournament_id")
    public int getId() {
        return id;
    }

    @SerializedName("tournament_id")
    public void setId(int id) {
        this.id = id;
    }

    @SerializedName("tournament_name")
    public String getName() {
        return name;
    }

    @SerializedName("tournament_name")
    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("tournament_short_name")
    public String getTournamentShortName() {
        return tournamentShortName;
    }

    @SerializedName("tournament_short_name")
    public void setTournamentShortName(String tournamentShortName) {
        this.tournamentShortName = tournamentShortName;
    }
}