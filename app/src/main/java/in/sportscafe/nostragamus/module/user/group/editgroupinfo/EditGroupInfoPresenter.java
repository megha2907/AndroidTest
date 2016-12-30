package in.sportscafe.nostragamus.module.user.group.editgroupinfo;

import android.os.Bundle;

import java.io.File;

import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Jeeva on 12/6/16.
 */
public interface EditGroupInfoPresenter {

    void onCreateGroupInfo(Bundle bundle);

    void onDoneGroupName(String groupName,String groupPhoto);

    void onDoneUpdateTournaments();

    void onGetMemberResult();

    void onGroupNameEmpty();

    void onNoInternet();

    void onGroupTournamentUpdateSuccess();

    void onLeaveGroupSuccess();

    void onFailed(String message);

    void onGetGroupSummarySuccess(GroupInfo groupInfo);

    void onGroupPhotoDone(File body, String filepath, String filename);
}
