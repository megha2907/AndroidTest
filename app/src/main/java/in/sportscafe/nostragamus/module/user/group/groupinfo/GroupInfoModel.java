package in.sportscafe.nostragamus.module.user.group.groupinfo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;

/**
 * Created by Jeeva on 12/6/16.
 */
public interface GroupInfoModel {

    void init(Bundle bundle);

    String getGroupName();

    void getGroupDetails();

    boolean amAdmin();

    GroupInfo getGroupInfo();

    int getApprovedMembersCount();

    void leaveGroup();

    void resetLeaderboard();

    void deleteGroup();

    Bundle getGroupInfoBundle();

    boolean isAnythingChanged();

    ViewPagerAdapter getAdapter(FragmentManager fm);

    void updateEditData(Bundle bundle);
}