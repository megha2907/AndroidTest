package in.sportscafe.scgame.module.user.group.newgroup;

import android.os.Bundle;

import com.jeeva.android.View;

/**
 * Created by Jeeva on 1/7/16.
 */
public interface NewGroupView extends View {

    void setAdapter(GrpTournamentSelectionAdapter adapter);

    void setSuccessBack(Bundle bundle);
}