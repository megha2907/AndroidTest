package in.sportscafe.nostragamus.module.feed;

import android.os.Bundle;

import java.util.List;

import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.play.myresultstimeline.TimelineAdapter;

/**
 * Created by Jeeva on 15/6/16.
 */
public interface FeedModel {

    void init(Bundle bundle);

    TimelineAdapter getAdapter();

    void getFeeds();

    void destroyAll();

    void handleMatches(List<Match> matchList);
}