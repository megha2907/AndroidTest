package in.sportscafe.nostragamus.module.allchallenges.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupPerson;

/**
 * Created by Jeeva on 31/03/17.
 */
@Parcel
public class ConfigPlayersDetails {

    @JsonProperty("max_count")
    private int maxCount;

    @JsonProperty("joined_count")
    private int joinedCount;

    @JsonProperty("players")
    private List<GroupPerson> players = new ArrayList<>();

    @JsonProperty("max_count")
    public int getMaxCount() {
        return maxCount;
    }

    @JsonProperty("max_count")
    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    @JsonProperty("joined_count")
    public int getJoinedCount() {
        return joinedCount;
    }

    @JsonProperty("joined_count")
    public void setJoinedCount(int joinedCount) {
        this.joinedCount = joinedCount;
    }

    @JsonProperty("players")
    public List<GroupPerson> getPlayers() {
        return players;
    }

    @JsonProperty("players")
    public void setPlayers(List<GroupPerson> players) {
        this.players = players;
    }
}