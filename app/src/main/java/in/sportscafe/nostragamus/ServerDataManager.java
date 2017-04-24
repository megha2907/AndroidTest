package in.sportscafe.nostragamus;

import android.support.annotation.Nullable;

import java.util.List;

import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.feed.dto.Match;

/**
 * Created by sandip on 24/04/17.
 */

public class ServerDataManager {

    private List<Challenge> challengeList;

    private Challenge challengeInfo;

    private Match matchInfo;

    public @Nullable List<Challenge> getChallengeList() {
        return challengeList;
    }

    public void setChallengeList(List<Challenge> challengeList) {
        this.challengeList = challengeList;
    }

    public Challenge getChallengeInfo() {
        return challengeInfo;
    }

    public void setChallengeInfo(Challenge challengeInfo) {
        this.challengeInfo = challengeInfo;
    }

    public Match getMatchInfo() {
        return matchInfo;
    }

    public void setMatchInfo(Match matchInfo) {
        this.matchInfo = matchInfo;
    }

}
