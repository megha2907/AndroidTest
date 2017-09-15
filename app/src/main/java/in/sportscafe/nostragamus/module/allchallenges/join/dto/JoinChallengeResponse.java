package in.sportscafe.nostragamus.module.allchallenges.join.dto;

import com.google.gson.annotations.SerializedName;

import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto.JoinedChallengeInfo;

/**
 * Created by sandip on 16/06/17.
 */

public class JoinChallengeResponse {

    @SerializedName("joined_challenge_info")
    private JoinedChallengeInfo joinedChallengeInfo;

    public JoinedChallengeInfo getJoinedChallengeInfo() {
        return joinedChallengeInfo;
    }

    public void setJoinedChallengeInfo(JoinedChallengeInfo joinedChallengeInfo) {
        this.joinedChallengeInfo = joinedChallengeInfo;
    }
}
