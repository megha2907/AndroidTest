package in.sportscafe.nostragamus.module.recentActivity.adapter;

import android.os.Bundle;

/**
 * Created by deepanshi on 3/23/18.
 */

public interface RecentActivityAdapterListener {
    void handleItemOnClick(Bundle args);
    void showEmptyListView(String activityType);
    void showFilteredList();
}
