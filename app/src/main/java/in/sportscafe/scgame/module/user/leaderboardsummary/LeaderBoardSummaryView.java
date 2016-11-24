package in.sportscafe.scgame.module.user.leaderboardsummary;

import android.view.View;

import com.jeeva.android.InAppView;

import in.sportscafe.scgame.module.user.myprofile.myposition.dto.LbSummary;

/**
 * Created by deepanshi on 11/7/16.
 */

public interface LeaderBoardSummaryView extends InAppView {

    void initMyPosition(LbSummary lbSummary);

}
