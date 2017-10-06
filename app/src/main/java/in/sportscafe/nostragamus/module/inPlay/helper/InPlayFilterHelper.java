package in.sportscafe.nostragamus.module.inPlay.helper;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.inPlay.dto.InPlayResponse;

/**
 * Created by sandip on 30/08/17.
 */

public class InPlayFilterHelper {

    public static final int FILTER_ALL_SPORTS_ID = -11;
    public static final int FILTER_DAILY_SPORTS_ID = -12;
    public static final int FILTER_MIX_SPORTS_ID = 9;   // MIX-SPORTS sport id

    public InPlayFilterHelper() {}

    public List<InPlayResponse> getInPlayChallengesFilteredOn(int sportFilterId, List<InPlayResponse> inPlayResponseList) {
        List<InPlayResponse> filteredChallenges = null;

        if (inPlayResponseList != null && inPlayResponseList.size() > 0) {
            filteredChallenges = new ArrayList<>();

            for (InPlayResponse inPlayResponse : inPlayResponseList) {
                int[] sportsIdArray = inPlayResponse.getSportsIdArray();

                if (sportsIdArray != null) {
                    for (int temp = 0; temp < sportsIdArray.length; temp++) {
                        if (sportsIdArray[temp] == FILTER_MIX_SPORTS_ID) {
                            filteredChallenges.add(inPlayResponse);
                        }
                    }
                }
            }
        }

        return filteredChallenges;
    }

    public List<InPlayResponse> getInPlayMixedSportsChallengesFilteredOn(List<InPlayResponse> inPlayResponseList) {
        List<InPlayResponse> filteredChallenges = null;

        if (inPlayResponseList != null && inPlayResponseList.size() > 0) {
            filteredChallenges = new ArrayList<>();

            for (InPlayResponse inPlayResponse : inPlayResponseList) {
                int[] sportsIdArray = inPlayResponse.getSportsIdArray();

                if (sportsIdArray != null) {
                    for (int temp = 0; temp < sportsIdArray.length; temp++) {
                        if (sportsIdArray[temp] == FILTER_MIX_SPORTS_ID) {
                            filteredChallenges.add(inPlayResponse);
                        }
                    }
                }
            }
        }

        return filteredChallenges;
    }

    public List<InPlayResponse> getDailyInPlayChallenges(List<InPlayResponse> inPlayResponseList) {
        List<InPlayResponse> filteredChallenges = null;

        if (inPlayResponseList != null && inPlayResponseList.size() > 0) {
            filteredChallenges = new ArrayList<>();
            for (InPlayResponse inPlayResponse : inPlayResponseList) {
                if (inPlayResponse.isDailyChallenge()) {
                    filteredChallenges.add(inPlayResponse);
                }
            }
        }

        return filteredChallenges;
    }


}
