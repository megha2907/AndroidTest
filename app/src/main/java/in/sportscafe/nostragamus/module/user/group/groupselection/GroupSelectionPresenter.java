package in.sportscafe.nostragamus.module.user.group.groupselection;

import android.content.Context;
import android.os.Bundle;

import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;

/**
 * Created by deepanshi on 1/6/17.
 */

public interface GroupSelectionPresenter {

    void onGetGroupSelectionInfo(long groupId);

    void onDoneUpdateTournaments();

    void onGetMemberResult();

    void onNoInternet();

    void onGroupTournamentUpdateSuccess();

    void onFailed(String message);

    void onGetGroupSummarySuccess(GroupInfo groupInfo);

    Context getContext();

    void onUpdating();

    void onEditFailed(String message);

    void onSuccessTournamentInfo();
}
