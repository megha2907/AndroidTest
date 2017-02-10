package in.sportscafe.nostragamus.module.feed;

import android.content.Context;
import android.os.Bundle;

import java.util.List;

import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.play.myresultstimeline.TimelineAdapter;

/**
 * Created by Jeeva on 15/6/16.
 */
public interface FeedModel {

    void init(Bundle bundle);

    TimelineAdapter getAdapter(Context context, List<Match> matchList);

    void getFeeds();

    void destroyAll();

    String getTournamentName();

    void refreshFeed();

    boolean isFeedRefreshed();
}