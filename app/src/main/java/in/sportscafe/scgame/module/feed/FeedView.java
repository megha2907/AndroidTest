package in.sportscafe.scgame.module.feed;

import com.jeeva.android.InAppView;

/**
 * Created by Jeeva on 15/6/16.
 */
public interface FeedView extends InAppView {

    void setAdapter(FeedAdapter feedAdapter);

    void moveAdapterPosition(int movePosition);

    void dismissSwipeRefresh();
}