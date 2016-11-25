package in.sportscafe.nostragamus.module.play.myresultstimeline;

import com.jeeva.android.InAppView;

/**
 * Created by deepanshi on 10/5/16.
 */

public interface MyResultsTimelineView extends InAppView {

    void setAdapter(MyResultsTimelineAdapter myResultsTimelineAdapter);

    void moveAdapterPosition(int movePosition);

    void dismissSwipeRefresh();

}
