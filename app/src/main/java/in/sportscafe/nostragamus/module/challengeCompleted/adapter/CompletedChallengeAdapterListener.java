package in.sportscafe.nostragamus.module.challengeCompleted.adapter;

import android.os.Bundle;

/**
 * Created by deepanshi on 9/27/17.
 */

public interface CompletedChallengeAdapterListener {

    /* Completed Card Item */
    void onCompletedCardClicked(Bundle args);
    void onCompletedWinningClicked(Bundle args);
}
