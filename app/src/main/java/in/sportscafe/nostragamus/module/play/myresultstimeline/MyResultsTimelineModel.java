package in.sportscafe.nostragamus.module.play.myresultstimeline;


import android.content.Context;
import android.os.Bundle;

/**
 * Created by deepanshi on 10/5/16.
 */

public interface MyResultsTimelineModel {

    void init(Bundle bundle);

    MyResultsTimelineAdapter getAdapter(Context context);

    void getFeeds();

    void checkPagination(int firstVisibleItem, int visibleItemCount, int totalItemCount);

    boolean isAdapterEmpty();
}
