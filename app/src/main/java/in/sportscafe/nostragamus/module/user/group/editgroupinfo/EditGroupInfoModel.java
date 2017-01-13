package in.sportscafe.nostragamus.module.user.group.editgroupinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.File;

import in.sportscafe.nostragamus.module.user.group.newgroup.GrpTournamentSelectionAdapter;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Jeeva on 12/6/16.
 */
public interface EditGroupInfoModel {

    void init(Bundle bundle);

    void onGetImage(Intent data);

    boolean amAdmin();

    GroupInfo getGroupInfo();

    int getMembersCount();

    Bundle getGroupIdBundle();

    void updateGroupName(String groupName, String photo);


    void refreshGroupInfo();

    void leaveGroup();

    void updateGroupMembers();

    void updateGroupPhoto(File file, String filepath, String filename);


}