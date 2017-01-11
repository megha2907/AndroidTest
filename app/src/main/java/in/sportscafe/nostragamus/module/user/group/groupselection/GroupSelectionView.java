package in.sportscafe.nostragamus.module.user.group.groupselection;

import com.jeeva.android.View;

import in.sportscafe.nostragamus.module.user.group.newgroup.GrpTournamentSelectedAdapter;
import in.sportscafe.nostragamus.module.user.group.newgroup.GrpTournamentSelectionAdapter;

/**
 * Created by deepanshi on 1/6/17.
 */

public interface GroupSelectionView extends View {

    void setSelectedAdapter(GrpTournamentSelectionAdapter adapter);

    void setSuccessResult();
}