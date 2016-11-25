package in.sportscafe.nostragamus.module.feed;

import android.os.Bundle;

/**
 * Created by Jeeva on 15/6/16.
 */
public interface FeedPresenter {

    void onCreateFeed(Bundle bundle);

    void onRefresh();
}