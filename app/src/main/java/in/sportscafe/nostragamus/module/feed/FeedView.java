package in.sportscafe.nostragamus.module.feed;

import com.jeeva.android.InAppView;

import in.sportscafe.nostragamus.module.play.myresultstimeline.TimelineAdapter;

/**
 * Created by Jeeva on 15/6/16.
 */
public interface FeedView extends InAppView {

    void setAdapter(TimelineAdapter feedAdapter);

    void moveAdapterPosition(int movePosition);

    void initToolBar(Integer powerUp2x, Integer powerUpNonEgs, Integer powerUpAudiencePoll);
}