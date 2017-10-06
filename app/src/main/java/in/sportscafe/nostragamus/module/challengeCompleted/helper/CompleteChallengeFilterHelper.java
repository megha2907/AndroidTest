package in.sportscafe.nostragamus.module.challengeCompleted.helper;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.challengeCompleted.dto.CompletedResponse;
import in.sportscafe.nostragamus.module.newChallenges.dataProvider.SportsDataProvider;

/**
 * Created by deepanshi on 9/27/17.
 */

public class CompleteChallengeFilterHelper {

    public CompleteChallengeFilterHelper() {}

    public List<CompletedResponse> getCompletedChallengesFilteredOn(int sportFilterId, List<CompletedResponse> completedResponseList) {
        List<CompletedResponse> filteredChallenges = null;

        if (completedResponseList != null && completedResponseList.size() > 0) {
            filteredChallenges = new ArrayList<>();

            for (CompletedResponse completedResponse : completedResponseList) {
                int[] sportsIdArray = completedResponse.getSportIdArray();

                if (sportsIdArray != null) {
                    for (int temp = 0; temp < sportsIdArray.length; temp++) {
                        if (sportsIdArray[temp] == sportFilterId) {
                            filteredChallenges.add(completedResponse);
                        }
                    }
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
                int[] sportsIdArray = completedResponse.getSportIdArray();

                if (sportsIdArray != null) {
                    for (int temp = 0; temp < sportsIdArray.length; temp++) {
                        if (sportsIdArray[temp] == SportsDataProvider.FILTER_MIXED_SPORTS_ID) {
                            filteredChallenges.add(completedResponse);
                        }
                    }
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
