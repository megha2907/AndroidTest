package in.sportscafe.nostragamus.module.user.group.joingroup;

import android.os.Bundle;

import in.sportscafe.nostragamus.Constants;

/**
 * Created by Jeeva on 1/7/16.
 */
public class JoinGroupPresenterImpl implements JoinGroupPresenter, JoinGroupModelImpl.OnJoinGroupModelListener {

    private JoinGroupView mJoinGroupView;

    private JoinGroupModel mJoinGroupModel;

    private JoinGroupPresenterImpl(JoinGroupView joinGroupView) {
        this.mJoinGroupView = joinGroupView;
        this.mJoinGroupModel = JoinGroupModelImpl.newInstance(this);
    }

    public static JoinGroupPresenter newInstance(JoinGroupView joinGroupView) {
        return new JoinGroupPresenterImpl(joinGroupView);
    }

    @Override
    public void onCreateJoinGroup(Bundle bundle) {
        mJoinGroupModel.init(bundle);
    }

    @Override
    public void onClickJoin(String groupCode) {
        mJoinGroupView.showProgressbar();
        mJoinGroupModel.joinGroup(groupCode, false);
    }

    @Override
    public void onClickCreateGroup() {
        mJoinGroupView.navigateToCreateGroup();
    }

    @Override
    public void onBack() {
        if(mJoinGroupModel.hadGroupCode()) {
            mJoinGroupView.goToHome();
        } else {
            mJoinGroupView.goBack();
        }
    }

    @Override
    public void onSuccess(Integer groupId) {
        mJoinGroupView.dismissProgressbar();
       // mJoinGroupView.showMessage(Constants.Alerts.JOIN_GROUP_SUCCESS);
        mJoinGroupView.showJoinGroupSuccess(groupId);
    }

    @Override
    public void onInvalidGroupCode() {
        mJoinGroupView.dismissProgressbar();
        mJoinGroupView.showMessage(Constants.Alerts.INVALID_GROUP_CODE);
    }

    @Override
    public void onNoInternet() {
        mJoinGroupView.dismissProgressbar();
        mJoinGroupView.showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onFailed(String message) {
        mJoinGroupView.dismissProgressbar();
        mJoinGroupView.showMessage(message);
    }

    @Override
    public void onGetGroupCode(String groupCode) {
        mJoinGroupView.populateGroupCode(groupCode);
    }
}