package in.sportscafe.nostragamus.module.allchallenges.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupPerson;

/**
 * Created by Jeeva on 31/03/17.
 */
@Parcel
public class ConfigPlayersDetails {

    @SerializedName("max_count")
    private int maxCount;

    @SerializedName("joined_count")
    private int joinedCount;

    @SerializedName("players")
    private List<GroupPerson> players = new ArrayList<>();

    @SerializedName("max_count")
    public int getMaxCount() {
        return maxCount;
    }

    @SerializedName("max_count")
    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    @SerializedName("joined_count")
    public int getJoinedCount() {
        return joinedCount;
    }

    @SerializedName("joined_count")
    public void setJoinedCount(int joinedCount) {
        this.joinedCount = joinedCount;
    }

    @SerializedName("players")
    public List<GroupPerson> getPlayers() {
        return players;
    }

    @SerializedName("players")
    public void setPlayers(List<GroupPerson> players) {
        this.players = players;
    }
}