package in.sportscafe.nostragamus.module.contest.contestDetailsBeforeJoining;

import android.os.Bundle;

/**
 * Created by deepanshi on 9/12/17.
 */

public interface ContestDetailsFragmentListener {
    void onJoinContestClicked(Bundle args);
    void onBackBtnClicked();
    void onWalletClicked();
}
