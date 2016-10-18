package in.sportscafe.scgame.module.feed;

import android.os.Bundle;

import in.sportscafe.scgame.module.home.OnHomeActionListener;

/**
 * Created by Jeeva on 15/6/16.
 */
public interface FeedModel {

    void init(Bundle bundle);

    FeedAdapter getAdapter();

    void getFeeds();
}