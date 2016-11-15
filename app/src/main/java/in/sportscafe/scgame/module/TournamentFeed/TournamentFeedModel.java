package in.sportscafe.scgame.module.tournamentFeed;

import android.os.Bundle;

import in.sportscafe.scgame.module.home.OnHomeActionListener;

/**
 * Created by deepanshi on 9/29/16.
 */

public interface TournamentFeedModel {

    TournamentFeedAdapter getAdapter(OnHomeActionListener listener);

    void init(Bundle bundle);

    void getTournamentFeed(Bundle bundle);
}
