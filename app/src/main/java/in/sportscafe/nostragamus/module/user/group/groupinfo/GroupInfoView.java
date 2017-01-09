package in.sportscafe.nostragamus.module.user.group.groupinfo;

import android.os.Bundle;

import com.jeeva.android.View;

import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;

/**
 * Created by Jeeva on 12/6/16.
 */
public interface GroupInfoView extends View {

    void setGroupName(String groupName);

    void setGroupIcon(String groupIcon);

    void setMembersSize(int size);

    void setAdapter(GroupTournamentAdapter adapter);

    void setGroupCode(String groupCode);

    void showDeleteGroup();

    void disableEdit();

    void navigateToAdminMembers(Bundle bundle);

    void navigateToGroupMembers(Bundle bundle);

    void navigateToHome();

    void setSuccessResult();

    void goBackWithSuccessResult();

    void navigateToAllGroups();

    void navigateToHomeActivity();

    void initMyPosition(GroupInfo groupInfo);
}