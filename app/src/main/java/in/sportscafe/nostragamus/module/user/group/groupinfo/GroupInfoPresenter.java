package in.sportscafe.nostragamus.module.user.group.groupinfo;

import android.os.Bundle;

/**
 * Created by Jeeva on 12/6/16.
 */
public interface GroupInfoPresenter {

    void onCreateGroupInfo(Bundle bundle);

    void onClickShareCode();

    void onLongClickShareCode();

    void onClickEditGroup();

    void onClickLeaveGroup();

    void onClickResetLb();

    void onClickDeleteGroup();

    void onClickBack();

    void onGetEditResult(Bundle bundle);
}