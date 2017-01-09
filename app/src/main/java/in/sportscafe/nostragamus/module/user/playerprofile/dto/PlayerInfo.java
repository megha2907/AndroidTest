package in.sportscafe.nostragamus.module.user.playerprofile.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroups;
import in.sportscafe.nostragamus.module.user.group.mutualgroups.MutualGroups;
import in.sportscafe.nostragamus.module.user.login.dto.PowerUpInfo;
import in.sportscafe.nostragamus.module.user.myprofile.myposition.dto.GroupSummary;

/**
 * Created by deepanshi on 12/22/16.
 */

public class PlayerInfo implements Serializable{

    @JsonProperty("user_id")
    private Integer id;

    @JsonProperty("user_email")
    private String email;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("user_photo")
    private String photo;

    @JsonProperty("total_points")
    private Long points;

    @JsonProperty("user_nick")
    private String userNickName;

    @JsonProperty("sports_preferences")
    private List<Integer> userSports = new ArrayList<>();

    @JsonProperty("count_groups")
    private Integer numberofgroups;

    @JsonProperty("info")
    private BadgesInfo badgeInfo;

    @JsonProperty("groups")
    private List<GroupSummary> groups = new ArrayList<>();

    @JsonProperty("groups")
    private List<MutualGroups> mutualGroups;

    @JsonProperty("count_matches")
    private Integer totalMatchesPlayed = 0;

    /**
     * @return The id
     */
    @JsonProperty("user_id")
    public Integer getId() {
        return id;
    }

    /**
     * @param id The user_id
     */
    @JsonProperty("user_id")
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The email
     */
    @JsonProperty("user_email")
    public String getEmail() {
        return email;
    }

    /**
     * @param email The user_email
     */
    @JsonProperty("user_email")
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The userName
     */
    @JsonProperty("user_name")
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName The user_firstname
     */
    @JsonProperty("user_name")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return The photo
     */
    @JsonProperty("user_photo")
    public String getPhoto() {
        return photo;
    }

    /**
     * @param photo The user_photo
     */
    @JsonIgnore
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @JsonProperty("total_points")
    public Long getPoints() {
        return points;
    }

    @JsonProperty("total_points")
    public void setPoints(Long points) {
        this.points = points;
    }

    @JsonProperty("user_nick")
    public String getUserNickName() {
        return userNickName;
    }

    @JsonProperty("user_nick")
    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    @JsonProperty("sports_preferences")
    public List<Integer> getUserSports() {
        if(null == userSports) {
            userSports = new ArrayList<>();
        }
        return userSports;
    }


    @JsonProperty("count_groups")
    public Integer getNumberofgroups() {
        return numberofgroups;
    }

    @JsonProperty("count_groups")
    public void setNumberofgroups(Integer numberofgroups) {
        this.numberofgroups = numberofgroups;
    }

    @JsonProperty("sports_preferences")
    public void setUserSports(List<Integer> userSports) {
        this.userSports = userSports;
    }

    @JsonIgnore
    public List<String> getBadges()
    {
        return badgeInfo.getBadges();
    }


    @JsonProperty("groups")
    public List<MutualGroups> getMutualGroups() {
        return mutualGroups;
    }

    @JsonProperty("groups")
    public void setMutualGroups(List<MutualGroups> mutualGroups) {
        this.mutualGroups = mutualGroups;
    }

    @JsonProperty("count_matches")
    public Integer getTotalMatchesPlayed() {
        return totalMatchesPlayed;
    }

    @JsonProperty("count_matches")
    public void setTotalMatchesPlayed(Integer totalMatchesPlayed) {
        this.totalMatchesPlayed = totalMatchesPlayed;
    }
}
