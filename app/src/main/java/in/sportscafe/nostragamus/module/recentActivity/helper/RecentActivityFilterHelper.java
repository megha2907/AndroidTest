package in.sportscafe.nostragamus.module.recentActivity.helper;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.recentActivity.dto.RecentActivity;

/**
 * Created by deepanshi on 3/22/18.
 */

public class RecentActivityFilterHelper {

    public RecentActivityFilterHelper() {
    }

    public List<RecentActivity> getRecentActivityFilteredOn(String activityType, List<RecentActivity> recentActivityList) {
        List<RecentActivity> filteredRecentActivity = null;

        if (recentActivityList != null && recentActivityList.size() > 0 && !TextUtils.isEmpty(activityType)) {
            filteredRecentActivity = new ArrayList<>();
            for (RecentActivity recentActivity : recentActivityList) {
                if (recentActivity.getActivityType() == activityType) {
                    filteredRecentActivity.add(recentActivity);
                }
            }
        }

        return filteredRecentActivity;
    }
}
