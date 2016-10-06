package in.sportscafe.scgame.module.user.group.newgroup;

import android.content.Context;

/**
 * Created by Jeeva on 1/7/16.
 */
public interface NewGroupModel {

    GrpTournamentSelectionAdapter getAdapter(Context context);

    void createGroup(String groupName);

}