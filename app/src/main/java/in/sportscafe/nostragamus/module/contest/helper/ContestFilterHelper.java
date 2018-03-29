package in.sportscafe.nostragamus.module.contest.helper;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.contest.adapter.ContestAdapterItemType;
import in.sportscafe.nostragamus.module.contest.dto.Contest;

/**
 * Created by sandip on 01/09/17.
 */

public class ContestFilterHelper {

    public static final String JOINED_CONTEST = "Joined Contest";
    public static final String PRIVATE_CONTEST_STR = "Private Contest";

    public List<Contest> getFilteredContestByType(String filterTypeName, List<Contest> list) {
        List<Contest> filteredList = null;

        if (list != null && list.size() > 0) {
            filteredList = new ArrayList<>();
            for (Contest contest : list) {
                if (contest.getContestType()!=null && !TextUtils.isEmpty(contest.getContestType().getCategoryName())) {
                    if ((filterTypeName.equalsIgnoreCase(contest.getContestType().getCategoryName())
                            && !contest.isContestJoined())) {  /* Contest should not be joined and contestId filter */
                        filteredList.add(contest);
                    }
                } else if (contest.getContestItemType()== ContestAdapterItemType.REFER_FRIEND_AD){
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

    public List<Contest> getPrivateContests(List<Contest> list) {
        List<Contest> filteredList = null;

        if (list != null && list.size() > 0) {
            filteredList = new ArrayList<>();
            for (Contest contest : list) {
                if (contest.getContestType() != null && !TextUtils.isEmpty(contest.getContestType().getCategoryName())) {
                    if ((PRIVATE_CONTEST_STR.equalsIgnoreCase(contest.getContestType().getCategoryName())
                            && !contest.isContestJoined())) {  /* Contest should not be joined and contestId filter */
                        filteredList.add(contest);
                    }
                }
            }
        }

        return filteredList;
    }
}
