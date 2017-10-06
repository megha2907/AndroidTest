package in.sportscafe.nostragamus.module.contest.helper;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.contest.dto.Contest;

/**
 * Created by sandip on 01/09/17.
 */

public class ContestFilterHelper {

    public static final String JOINED_CONTEST = "Joined Contest";

    public List<Contest> getFilteredContestByType(String filterTypeName, List<Contest> list) {
        List<Contest> filteredList = null;

        if (list != null && list.size() > 0) {
            filteredList = new ArrayList<>();
            for (Contest contest : list) {
                if (filterTypeName.equalsIgnoreCase(contest.getContestType().getCategoryName()) &&
                        !contest.isContestJoined()) {           /* Contest should not be joined and contestId filter */
                    filteredList.add(contest);
                }
            }
        }

        return filteredList;
    }

    public List<Contest> getJoinedContests(List<Contest> list) {
        List<Contest> filteredList = null;

        if (list != null && list.size() > 0) {
            filteredList = new ArrayList<>();
            for (Contest contest : list) {
                if (contest.isContestJoined()) {
                    filteredList.add(contest);
                }
            }
        }

        return filteredList;
    }
}
