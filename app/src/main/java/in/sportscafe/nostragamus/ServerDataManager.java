package in.sportscafe.nostragamus;

import android.support.annotation.Nullable;

import java.util.List;

import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;

/**
 * Created by sandip on 24/04/17.
 */

public class ServerDataManager {

    private List<Challenge> challengeList;

    public @Nullable List<Challenge> getChallengeList() {
        return challengeList;
    }

    public void setChallengeList(List<Challenge> challengeList) {
        this.challengeList = challengeList;
    }
}
