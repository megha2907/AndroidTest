package in.sportscafe.nostragamus.module.play.myresults.flipPowerup;

import com.jeeva.android.InAppView;
import com.jeeva.android.View;

import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.play.myresults.MyResultsAdapter;

/**
 * Created by deepanshi on 12/20/16.
 */

public interface FlipView extends InAppView {

    void setAdapter(FlipAdapter mflipAdapter);

    void decreasePowerupCount();
}
