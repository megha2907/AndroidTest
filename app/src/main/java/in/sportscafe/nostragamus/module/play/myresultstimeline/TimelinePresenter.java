package in.sportscafe.nostragamus.module.play.myresultstimeline;

import android.os.Bundle;

/**
 * Created by deepanshi on 10/5/16.
 */

public interface TimelinePresenter {

    void onCreateFeed(Bundle bundle);

    void onRefresh();

    void onTimelineScroll(int firstVisibleItem, int visibleItemCount, int totalItemCount);
}
