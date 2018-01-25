package in.sportscafe.nostragamus.module.prediction.copyAnswer.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sc on 24/1/18.
 */

public class CopyAnswerContest {

    @SerializedName("room_id")
    private String roomId;

    @SerializedName("challenge_id")
    private int challengeId;

    @SerializedName("challenge_name")
    private String challengeName;

    @SerializedName("config_name")
    private String configName;

    @SerializedName("hours_since_played")
    private int hoursSincePlayed;

    @SerializedName("answers")
    private List<CopyAnswerQuestion> answers = null;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public int getHoursSincePlayed() {
        return hoursSincePlayed;
    }

    public void setHoursSincePlayed(int hoursSincePlayed) {
        this.hoursSincePlayed = hoursSincePlayed;
    }

    public List<CopyAnswerQuestion> getAnswers() {
        return answers;
    }

    public void setAnswers(List<CopyAnswerQuestion> answers) {
        this.answers = answers;
    }
}
