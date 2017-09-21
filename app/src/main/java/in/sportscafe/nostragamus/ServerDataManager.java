package in.sportscafe.nostragamus;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.navigation.wallet.dto.UserWalletResponse;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;

/**
 * Created by sandip on 24/04/17.
 */

public class ServerDataManager {

    private List<Challenge> challengeList;

    private List<Challenge> completedChallenges = new ArrayList<>();

    private Challenge challengeInfo;

    private Match matchInfo;

    private UserWalletResponse userWalletResponse;

    private UserInfo userInfo;

    public @Nullable List<Challenge> getChallengeList() {
        return challengeList;
    }

    public void setChallengeList(List<Challenge> challengeList) {
        this.challengeList = challengeList;
    }

    public @Nullable List<Challenge> getCompletedChallenges() {
        return completedChallenges;
    }

    public void setCompletedChallenges(List<Challenge> completedChallenges) {
        this.completedChallenges = completedChallenges;
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

    public UserWalletResponse getUserWalletResponse() {
        return userWalletResponse;
    }

    public void setUserWalletResponse(UserWalletResponse userWalletResponse) {
        this.userWalletResponse = userWalletResponse;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
