package in.sportscafe.nostragamus.module.newChallenges.helpers;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengesResponse;

/**
 * Created by sandip on 30/08/17.
 */

public class NewChallengesFilterHelper {

    public static final int FILTER_ALL_SPORTS_ID = -11;
    public static final int FILTER_DAILY_SPORTS_ID = -12;

    public NewChallengesFilterHelper() {}

    public List<NewChallengesResponse> getNewChallengesFilteredOn(int sportFilterId, List<NewChallengesResponse> newChallengesResponses) {
        List<NewChallengesResponse> filteredChallenges = null;

        if (newChallengesResponses != null && newChallengesResponses.size() > 0) {
            filteredChallenges = new ArrayList<>();

            for (NewChallengesResponse challenge : newChallengesResponses) {
                if (challenge.getSportsId() == sportFilterId) {
                    filteredChallenges.add(challenge);
                }
            }
        }

        return filteredChallenges;
    }
}
