package in.sportscafe.nostragamus.module.user.playerprofile.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.user.badges.Badge;
import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroups;
import in.sportscafe.nostragamus.module.user.login.dto.BasicUserInfo;
import in.sportscafe.nostragamus.module.user.login.dto.InfoDetails;

/**
 * Created by deepanshi on 12/22/16.
 */
@Parcel
public class PlayerInfo extends BasicUserInfo {

    @JsonProperty("user_photo")
    private String photo;

    @JsonProperty("sports_preferences")
    private List<Integer> userSports = new ArrayList<>();

    @JsonProperty("info")
    private InfoDetails infoDetails;

    @JsonProperty("count_matches")
    private Integer totalMatchesPlayed = 0;

    @JsonProperty("total_points")
    private Long points;

    @JsonProperty("total_powerups")
    private Long totalPowerups;

    @JsonProperty("count_predictions")
    private Integer predictionCount;

    @JsonProperty("count_powerups")
    private Integer powerupsUsedCount;

    @JsonProperty("accuracy")
    private Integer accuracy;

    @JsonProperty("mutual_groups")
    private List<AllGroups> mutualGroups = new ArrayList<>();

    @JsonProperty("user_photo")
    public String getPhoto() {
        return photo;
    }

    @JsonProperty("user_photo")
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @JsonProperty("sports_preferences")
    public List<Integer> getUserSports() {
        if (null == userSports) {
            userSports = new ArrayList<>();
        }
        return userSports;
    }

    @JsonProperty("sports_preferences")
    public void setUserSports(List<Integer> userSports) {
        this.userSports = userSports;
    }

    @JsonProperty("info")
    public InfoDetails getInfoDetails() {
        return infoDetails;
    }

    @JsonProperty("info")
    public void setInfoDetails(InfoDetails infoDetails) {
        this.infoDetails = infoDetails;
    }

    @JsonProperty("count_matches")
    public Integer getTotalMatchesPlayed() {
        return totalMatchesPlayed;
    }

    @JsonProperty("count_matches")
    public void setTotalMatchesPlayed(Integer totalMatchesPlayed) {
        this.totalMatchesPlayed = totalMatchesPlayed;
    }

    @JsonProperty("total_points")
    public Long getTotalPoints() {
        if(null == points) {
            return 0L;
        }
        return points;
    }

    @JsonProperty("total_points")
    public void setTotalPoints(Long points) {
        this.points = points;
    }

    @JsonProperty("total_powerups")
    public Long getTotalPowerups() {
        return totalPowerups;
    }

    @JsonProperty("total_powerups")
    public void setTotalPowerups(Long totalPowerups) {
        this.totalPowerups = totalPowerups;
    }

    @JsonProperty("mutual_groups")
    public List<AllGroups> getMutualGroups() {
        if(null == mutualGroups) {
            mutualGroups = new ArrayList<>();
        }
        return mutualGroups;
    }

    @JsonProperty("mutual_groups")
    public void setMutualGroups(List<AllGroups> mutualGroups) {
        this.mutualGroups = mutualGroups;
    }

    @JsonIgnore
    public List<Badge> getBadges() {
        return infoDetails.getBadges();
    }

    @JsonProperty("count_predictions")
    public Integer getPredictionCount() {
        return predictionCount;
    }

    @JsonProperty("count_predictions")
    public void setPredictionCount(Integer predictionCount) {
        this.predictionCount = predictionCount;
    }

    @JsonProperty("accuracy")
    public Integer getAccuracy() {
        return accuracy;
    }

    @JsonProperty("accuracy")
    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    @JsonProperty("count_powerups")
    public Integer getPowerupsUsedCount() {
        return powerupsUsedCount;
    }

    @JsonProperty("count_powerups")
    public void setPowerupsUsedCount(Integer powerupsUsedCount) {
        this.powerupsUsedCount = powerupsUsedCount;
    }

}