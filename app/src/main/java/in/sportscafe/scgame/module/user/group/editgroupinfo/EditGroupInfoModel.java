package in.sportscafe.scgame.module.user.group.editgroupinfo;

import android.content.Context;
import android.os.Bundle;

import in.sportscafe.scgame.module.user.group.newgroup.GrpTournamentSelectionAdapter;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Jeeva on 12/6/16.
 */
public interface EditGroupInfoModel {

    void init(Bundle bundle);

    void updateTournaments();

    boolean amAdmin();

    GroupInfo getGroupInfo();

    int getMembersCount();

    GrpTournamentSelectionAdapter getAdapter(Context context);

    Bundle getGroupIdBundle();

    void updateGroupName(String groupName, String photo);

    String getShareCodeContent();

    void refreshGroupInfo();

    void leaveGroup();

    void updateGroupMembers();

    void updateGroupPhoto(MultipartBody.Part file, RequestBody filepath, RequestBody filename);
}