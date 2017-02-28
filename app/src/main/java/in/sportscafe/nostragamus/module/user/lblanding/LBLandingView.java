package in.sportscafe.nostragamus.module.user.lblanding;

import com.jeeva.android.InAppView;

import java.util.List;

/**
 * Created by deepanshi on 11/7/16.
 */

public interface LBLandingView extends InAppView {

    void sortLeaderBoards();

    void initMyPosition(List<LBLandingSummary> lbSummary);
}
