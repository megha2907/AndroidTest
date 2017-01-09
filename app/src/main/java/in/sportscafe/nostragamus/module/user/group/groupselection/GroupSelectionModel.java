package in.sportscafe.nostragamus.module.user.group.groupselection;

import android.content.Context;
import android.os.Bundle;

import in.sportscafe.nostragamus.module.user.group.newgroup.GrpTournamentSelectionAdapter;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;

/**
 * Created by deepanshi on 1/6/17.
 */

public interface GroupSelectionModel {

    void init(long groupId);

    boolean amAdmin();

    void updateTournaments();

    GroupInfo getGroupInfo();

    GrpTournamentSelectionAdapter getAdapter(Context context);

    void refreshGroupInfo();
}
