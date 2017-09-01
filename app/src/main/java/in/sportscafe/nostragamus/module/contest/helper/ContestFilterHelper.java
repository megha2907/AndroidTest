package in.sportscafe.nostragamus.module.contest.helper;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.contest.dto.Contest;

/**
 * Created by sandip on 01/09/17.
 */

public class ContestFilterHelper {

    public List<Contest> getFilteredContestByType(int filterTypeId, List<Contest> list) {
        List<Contest> filteredList = null;

        if (list != null && list.size() > 0) {
            filteredList = new ArrayList<>();
            for (Contest contest : list) {
                if (filterTypeId == contest.getContestId()) {
                    filteredList.add(contest);
                }
            }
        }

        return filteredList;
    }
}
