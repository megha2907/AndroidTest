package in.sportscafe.nostragamus.module.TournamentFeed;

import com.jeeva.android.InAppView;


/**
 * Created by deepanshi on 9/29/16.
 */

public interface TournamentFeedView extends InAppView {

    void setAdapter(TournamentFeedAdapter tournamentFeedAdapter);

    void moveAdapterPosition(int movePosition);

//    void dismissSwipeRefresh();
}
