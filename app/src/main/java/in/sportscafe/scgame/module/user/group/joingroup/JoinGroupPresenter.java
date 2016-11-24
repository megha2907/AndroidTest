package in.sportscafe.scgame.module.user.group.joingroup;

import android.os.Bundle;

/**
 * Created by Jeeva on 1/7/16.
 */
public interface JoinGroupPresenter {

    void onCreateJoinGroup(Bundle bundle);

    void onClickJoin(String groupCode);

    void onClickCreateGroup();

    void onNewGroupSuccess(Bundle bundle);

    void onBack();
}