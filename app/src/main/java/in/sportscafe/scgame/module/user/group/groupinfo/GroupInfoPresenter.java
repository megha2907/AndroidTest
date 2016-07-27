package in.sportscafe.scgame.module.user.group.groupinfo;

import android.os.Bundle;

/**
 * Created by Jeeva on 12/6/16.
 */
public interface GroupInfoPresenter {

    void onCreateGroupInfo(Bundle bundle);

    void onClickDeleteGroup();

    void onClickLeaveGroup();

    void onClickMembers();

    void onDoneGroupName(String groupName);

    void onClickShareCode();

    void onLongClickShareCode();

    void onGetMemberResult();
}
