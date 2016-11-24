package in.sportscafe.scgame.module.user.group.editgroupinfo;

import android.os.Bundle;

import com.jeeva.android.View;

import in.sportscafe.scgame.module.user.group.newgroup.GrpTournamentSelectionAdapter;

/**
 * Created by Jeeva on 12/6/16.
 */
public interface EditGroupInfoView extends View {

    void setGroupName(String groupName);

    void setAdapter(GrpTournamentSelectionAdapter adapter);

    void navigateToHome();

    void disableEdit();

    void setGroupIcon(String groupIcon);

    void goBackWithSuccessResult();

    void setSuccessResult();

    void setGroupImage(String imageUrl);
}