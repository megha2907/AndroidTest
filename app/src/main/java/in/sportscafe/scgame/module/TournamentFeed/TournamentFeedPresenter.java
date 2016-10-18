package in.sportscafe.scgame.module.TournamentFeed;

import in.sportscafe.scgame.module.home.OnHomeActionListener;

/**
 * Created by deepanshi on 9/29/16.
 */

public interface TournamentFeedPresenter {

    void onCreateFeed(OnHomeActionListener listener);

    void onRefresh();
}
