package in.sportscafe.nostragamus.module.tournamentFeed;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by deepanshi on 9/29/16.
 */

public interface TournamentFeedModel {

    void init(Bundle bundle);

    TournamentFeedAdapter getAdapter(Context context);
}