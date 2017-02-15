package in.sportscafe.nostragamus.module.user.group.allgroups;

import android.os.Bundle;

/**
 * Created by deepanshi on 9/27/16.
 */

public interface AllGroupsPresenter {

    void onCreateAllGroups(Bundle bundle);

    void onClickGroupItem(Bundle bundle);

    void onGetGroupInfoResult(Bundle bundle);

    void onGetJoinGroupResult(Bundle bundle);
}