package in.sportscafe.scgame.module.feed;

import in.sportscafe.scgame.module.home.OnHomeActionListener;

/**
 * Created by Jeeva on 15/6/16.
 */
public interface FeedModel {

    FeedAdapter getAdapter(OnHomeActionListener listener);

    void getFeeds();
}