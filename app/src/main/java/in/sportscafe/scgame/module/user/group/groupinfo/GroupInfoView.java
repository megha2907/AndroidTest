package in.sportscafe.scgame.module.user.group.groupinfo;

import android.os.Bundle;

import com.jeeva.android.View;

import in.sportscafe.scgame.module.user.group.newgroup.GrpSportSelectionAdapter;

/**
 * Created by Jeeva on 12/6/16.
 */
public interface GroupInfoView extends View {

    void setGroupName(String groupName);

    void setMembersSize(int size);

    void setAdapter(GrpSportSelectionAdapter adapter);

    void setGroupCode(String groupCode);

    void showDeleteGroup();

    void disableEdit();

    void navigateToAdminMembers(Bundle bundle);

    void navigateToGroupMembers(Bundle bundle);

    void navigateToHome();

    void setSuccessResult();
}