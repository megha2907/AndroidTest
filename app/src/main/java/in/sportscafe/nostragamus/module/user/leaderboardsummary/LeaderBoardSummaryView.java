package in.sportscafe.nostragamus.module.user.leaderboardsummary;

import com.jeeva.android.InAppView;

import in.sportscafe.nostragamus.module.user.myprofile.myposition.dto.LbSummary;

/**
 * Created by deepanshi on 11/7/16.
 */

public interface LeaderBoardSummaryView extends InAppView {

    void initMyPosition(LbSummary lbSummary);

}
