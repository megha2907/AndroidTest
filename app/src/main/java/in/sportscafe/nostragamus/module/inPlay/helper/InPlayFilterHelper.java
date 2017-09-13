package in.sportscafe.nostragamus.module.inPlay.helper;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.inPlay.dto.InPlayResponse;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengesResponse;

/**
 * Created by sandip on 30/08/17.
 */

public class InPlayFilterHelper {

    public static final int FILTER_ALL_SPORTS_ID = -11;
    public static final int FILTER_DAILY_SPORTS_ID = -12;
    public static final int FILTER_MIX_SPORTS_ID = -13;

    public InPlayFilterHelper() {}

    public List<InPlayResponse> getInPlayChallengesFilteredOn(int sportFilterId, List<InPlayResponse> inPlayResponseList) {
        List<InPlayResponse> filteredChallenges = null;

        if (inPlayResponseList != null && inPlayResponseList.size() > 0) {
            filteredChallenges = new ArrayList<>();

            for (InPlayResponse inPlayResponse : inPlayResponseList) {
                if (inPlayResponse.getSportsId() == sportFilterId) {
                    filteredChallenges.add(inPlayResponse);
                }
            }
        }

        return filteredChallenges;
    }

    public List<InPlayResponse> getDailyInplayChallenges(List<InPlayResponse> inPlayResponseList) {
        List<InPlayResponse> filteredChallenges = null;

        if (inPlayResponseList != null && inPlayResponseList.size() > 0) {
            filteredChallenges = new ArrayList<>();

            for (InPlayResponse inPlayResponse : inPlayResponseList) {
                // TODO: daily param
            }
        }

        return filteredChallenges;
    }


}
