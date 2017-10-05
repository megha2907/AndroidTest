package in.sportscafe.nostragamus.module.newChallenges.ui.matches;

import android.os.Bundle;

public interface NewChallengeMatchFragmentListener {
    void onBackClicked();
    void launchContestActivity(int launchedFrom, Bundle args);
}
