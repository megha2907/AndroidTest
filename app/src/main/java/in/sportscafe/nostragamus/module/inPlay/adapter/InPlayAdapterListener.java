package in.sportscafe.nostragamus.module.inPlay.adapter;

import android.os.Bundle;

/**
 * Created by deepanshi on 9/6/17.
 */

public interface InPlayAdapterListener {

    /* Challenge */
    void onJoinAnotherContestClicked(Bundle args);

    /* Joined Card Item */
    void onJoinedContestCardClicked(Bundle args);
    void onJoinedContestMoreContestButtonClicked(Bundle args);
    void onJoinedContestWinningsClicked(Bundle args);

    /* Completed Card Item */
    void onCompletedCardClicked(Bundle args);
    void onCompletedWinningClicked(Bundle args);

    /* Head Less Contest card Item */
    void onHeadLessContestCardClicked(Bundle args);
    void onHeadLessJoinButtonClicked(Bundle args);
}
