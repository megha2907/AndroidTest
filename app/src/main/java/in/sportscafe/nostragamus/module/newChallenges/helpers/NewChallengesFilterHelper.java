package in.sportscafe.nostragamus.module.newChallenges.helpers;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.newChallenges.dataProvider.SportsDataProvider;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengesResponse;

/**
 * Created by sandip on 30/08/17.
 */

public class NewChallengesFilterHelper {

    public NewChallengesFilterHelper() {}

    public List<NewChallengesResponse> getNewChallengesFilteredOn(int sportFilterId, List<NewChallengesResponse> newChallengesResponses) {
        List<NewChallengesResponse> filteredChallenges = null;

        if (newChallengesResponses != null && newChallengesResponses.size() > 0) {
            filteredChallenges = new ArrayList<>();

            for (NewChallengesResponse challenge : newChallengesResponses) {
                int[] sportsIdArray = challenge.getSportsIdArray();

                if (sportsIdArray != null) {
                    for (int temp = 0; temp < sportsIdArray.length; temp++) {
                        if (sportsIdArray[temp] == sportFilterId) {
                            filteredChallenges.add(challenge);
                        }
                    }
                }
            }
        }

        return filteredChallenges;
    }

    public List<NewChallengesResponse> getDailySports(List<NewChallengesResponse> newChallengesResponseData) {
        List<NewChallengesResponse> dailyChallenge = new ArrayList<>();

        if (newChallengesResponseData != null && newChallengesResponseData.size() > 0) {
            for (NewChallengesResponse newChallenge : newChallengesResponseData) {
                if (newChallenge.isDaily()) {
                    dailyChallenge.add(newChallenge);
                }
            }
        }

        return dailyChallenge;
    }

    public List<NewChallengesResponse> getMixSports(List<NewChallengesResponse> newChallengesResponseData) {
        List<NewChallengesResponse> mixChallenge = new ArrayList<>();

        if (newChallengesResponseData != null && newChallengesResponseData.size() > 0) {
            for (NewChallengesResponse newChallenge : newChallengesResponseData) {
                int[] sportsIdArray = newChallenge.getSportsIdArray();

                if (sportsIdArray != null) {
                    for (int temp = 0; temp < sportsIdArray.length; temp++) {
                        if (sportsIdArray.length > 1) {
                            mixChallenge.add(newChallenge);
                        }
                    }
                }
            }
        }

        return mixChallenge;
    }
}
