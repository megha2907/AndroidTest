package in.sportscafe.nostragamus.module.feed;

import android.os.Bundle;

/**
 * Created by Jeeva on 15/6/16.
 */
public interface FeedModel {

    void init(Bundle bundle);

    FeedAdapter getAdapter();

    void getFeeds();
}