package in.sportscafe.nostragamus.module.tournamentFeed;

import android.os.Bundle;

import in.sportscafe.nostragamus.module.home.OnHomeActionListener;

/**
 * Created by deepanshi on 9/29/16.
 */

public interface TournamentFeedPresenter {

    void onCreateFeed(OnHomeActionListener listener,Bundle bundle);

    void update(Bundle bundle);

}
