package in.sportscafe.nostragamus.module.user.group.editgroupinfo;

import android.content.Intent;
import android.os.Bundle;

import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;

/**
 * Created by Jeeva on 12/6/16.
 */
public interface EditGroupInfoModel {

    void init(Bundle bundle);

    GroupInfo getGroupInfo();

    void onGetImage(Intent data);

    void updateGroupName(String groupName);

    Bundle getGroupInfoBundle();

    boolean isAnythingChanged();
}