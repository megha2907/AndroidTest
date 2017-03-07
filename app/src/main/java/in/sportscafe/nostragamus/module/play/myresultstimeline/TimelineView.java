package in.sportscafe.nostragamus.module.play.myresultstimeline;

import com.jeeva.android.InAppView;

/**
 * Created by deepanshi on 10/5/16.
 */

public interface TimelineView extends InAppView {

    void setAdapter(TimelineAdapter myResultsTimelineAdapter);

    void moveAdapterPosition(int movePosition);

    void dismissSwipeRefresh();

    void showTimelineEmpty();
}
