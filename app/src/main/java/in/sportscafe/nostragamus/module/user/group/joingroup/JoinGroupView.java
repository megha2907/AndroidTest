package in.sportscafe.nostragamus.module.user.group.joingroup;

import android.os.Bundle;

import com.jeeva.android.View;

/**
 * Created by Jeeva on 1/7/16.
 */
public interface JoinGroupView extends View {

    void navigateToCreateGroup();

    void showJoinGroupSuccess(Integer groupId);

    void navigateToGroupInfo(Integer groupId);

    void populateGroupCode(String groupCode);

    void setSuccessResult(Bundle bundle);

    void goBack();

    void goToHome();

    void navigateToLogin();
}