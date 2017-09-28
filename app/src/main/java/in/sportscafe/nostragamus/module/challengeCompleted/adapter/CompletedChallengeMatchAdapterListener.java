package in.sportscafe.nostragamus.module.challengeCompleted.adapter;

import android.os.Bundle;

/**
 * Created by deepanshi on 9/27/17.
 */

public interface CompletedChallengeMatchAdapterListener {
    void onMatchClicked(Bundle args);
    void onMatchActionClicked(int action, Bundle args);
}
