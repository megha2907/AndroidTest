package in.sportscafe.nostragamus.module.challengeCompleted.helper;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.challengeCompleted.dto.CompletedResponse;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayResponse;

/**
 * Created by deepanshi on 9/27/17.
 */

public class CompleteChallengeFilterHelper {

    public static final int FILTER_ALL_SPORTS_ID = -11;
    public static final int FILTER_DAILY_SPORTS_ID = -12;
    public static final int FILTER_MIX_SPORTS_ID = 9;   // MIX-SPORTS sport id

    public CompleteChallengeFilterHelper() {}

    public List<CompletedResponse> getCompletedChallengesFilteredOn(int sportFilterId, List<CompletedResponse> completedResponseList) {
        List<CompletedResponse> filteredChallenges = null;

        if (completedResponseList != null && completedResponseList.size() > 0) {
            filteredChallenges = new ArrayList<>();

            for (CompletedResponse completedResponse : completedResponseList) {
                if (completedResponse.getSportsId() == sportFilterId) {
                    filteredChallenges.add(completedResponse);
                }
            }
        }

        return filteredChallenges;
    }

    public List<CompletedResponse> getCompletedMixedSportsChallengesFilteredOn(List<CompletedResponse> completedResponseList) {
        List<CompletedResponse> filteredChallenges = null;

        if (completedResponseList != null && completedResponseList.size() > 0) {
            filteredChallenges = new ArrayList<>();

            for (CompletedResponse completedResponse : completedResponseList) {
                if (completedResponse.getSportsId() == FILTER_MIX_SPORTS_ID) {
                    filteredChallenges.add(completedResponse);
                }
            }
        }

        return filteredChallenges;
    }

    public List<CompletedResponse> getDailyCompletedChallenges(List<CompletedResponse> completedResponseList) {
        List<CompletedResponse> filteredChallenges = null;

        if (completedResponseList != null && completedResponseList.size() > 0) {
            filteredChallenges = new ArrayList<>();
            for (CompletedResponse completedResponse : completedResponseList) {
                if (completedResponse.isDailyChallenge()) {
                    filteredChallenges.add(completedResponse);
                }
            }
        }

        return filteredChallenges;
    }

}
