package in.sportscafe.nostragamus.module.feed;

import android.os.Bundle;

import in.sportscafe.nostragamus.module.play.myresultstimeline.TimelineAdapter;

/**
 * Created by Jeeva on 15/6/16.
 */
public interface FeedModel {

    void init(Bundle bundle);

    TimelineAdapter getAdapter();

    void getFeeds();
}