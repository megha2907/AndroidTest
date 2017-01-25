package in.sportscafe.nostragamus.module.user.lblanding;

import android.os.Bundle;

/**
 * Created by deepanshi on 11/7/16.
 */

public interface LBLandingPresenter {

    void onCreateLeaderBoard();

    void onFuzzyLbClick(Bundle bundle);

    void sortLeaderBoardList(Integer sortBy);

}
