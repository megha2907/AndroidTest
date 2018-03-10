package in.sportscafe.nostragamus.module.nostraHome.ui;

import android.os.Bundle;

/**
 * Created by sandip on 09/10/17.
 */

public interface NostraHomeActivityListener {
    void showNewChallenges(Bundle args);
    void showInPlayChallenges(Bundle args);
    void showHistoryChallenges(Bundle args);
    void updateInplayCounter();
}
