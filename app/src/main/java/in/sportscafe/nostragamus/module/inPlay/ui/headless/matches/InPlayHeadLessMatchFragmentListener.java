package in.sportscafe.nostragamus.module.inPlay.ui.headless.matches;

import android.os.Bundle;

public interface InPlayHeadLessMatchFragmentListener {
    void onBackClicked();
    void launchContestActivity(int launchedFrom, Bundle args);
}
