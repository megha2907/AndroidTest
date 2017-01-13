package in.sportscafe.nostragamus.module.user.group.allgroups;

import android.os.Bundle;

/**
 * Created by deepanshi on 9/27/16.
 */

public interface AllGroupsPresenter {

    void onCreateAllGroups();

    void onCreateAllGroupsAdapter();

    void onClickNext();

    void onCreateMutualGroups(Bundle bundle);
}
