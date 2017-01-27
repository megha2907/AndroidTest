package in.sportscafe.nostragamus.module.play.myresultstimeline;


import android.content.Context;
import android.os.Bundle;

/**
 * Created by deepanshi on 10/5/16.
 */

public interface TimelineModel {

    void init(Bundle bundle);

    TimelineAdapter getAdapter(Context context);

    void getFeeds();

    void checkPagination(int firstVisibleItem, int visibleItemCount, int totalItemCount);

    boolean isAdapterEmpty();

    void destroyAll();
}
