package in.sportscafe.scgame.module.user.group.groupinfo;

import android.os.Bundle;

import com.jeeva.android.Log;

import java.util.List;

import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;

/**
 * Created by Jeeva on 12/6/16.
 */
public interface GroupInfoPresenter {

    void onCreateGroupInfo(Bundle bundle);

    void onClickMembers();

    void onClickShareCode();

    void onLongClickShareCode();

    void onGetMemberResult();
}
