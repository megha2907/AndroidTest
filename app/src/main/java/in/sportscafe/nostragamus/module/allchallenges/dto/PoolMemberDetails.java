package in.sportscafe.nostragamus.module.allchallenges.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupPerson;

/**
 * Created by Jeeva on 31/03/17.
 */
public class PoolMemberDetails {

    @JsonProperty("maximum_members_count")
    private int maxCount;

    @JsonProperty("joined_members_count")
    private int joinedCount;

    @JsonProperty("joined_members")
    private List<GroupPerson> joinedMembers = new ArrayList<>();

    @JsonProperty("maximum_members_count")
    public int getMaxCount() {
        return maxCount;
    }

    @JsonProperty("maximum_members_count")
    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    @JsonProperty("joined_members_count")
    public int getJoinedCount() {
        return joinedCount;
    }

    @JsonProperty("joined_members_count")
    public void setJoinedCount(int joinedCount) {
        this.joinedCount = joinedCount;
    }

    @JsonProperty("joined_members")
    public List<GroupPerson> getJoinedMembers() {
        return joinedMembers;
    }

    @JsonProperty("joined_members")
    public void setJoinedMembers(List<GroupPerson> joinedMembers) {
        this.joinedMembers = joinedMembers;
    }
}