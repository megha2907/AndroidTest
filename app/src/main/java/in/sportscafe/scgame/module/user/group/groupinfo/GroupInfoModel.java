package in.sportscafe.scgame.module.user.group.groupinfo;

import android.content.Context;
import android.os.Bundle;

import in.sportscafe.scgame.module.user.group.newgroup.GrpSportSelectionAdapter;
import in.sportscafe.scgame.module.user.group.newgroup.GrpTournamentSelectionAdapter;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;

/**
 * Created by Jeeva on 12/6/16.
 */
public interface GroupInfoModel {

    void init(Bundle bundle);

    void updateGroupMembers(GroupInfo groupInfo);

    boolean amAdmin();

    GroupInfo getGroupInfo();

    int getMembersCount();

    Bundle getGroupIdBundle();


    String getShareCodeContent();

    void refreshGroupInfo();

    void leaveGroup();

    GroupTournamentAdapter getAdapter(Context context);
}