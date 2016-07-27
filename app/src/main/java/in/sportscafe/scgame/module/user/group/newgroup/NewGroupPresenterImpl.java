package in.sportscafe.scgame.module.user.group.newgroup;

import android.os.Bundle;

import in.sportscafe.scgame.Constants;

/**
 * Created by Jeeva on 1/7/16.
 */
public class NewGroupPresenterImpl implements NewGroupPresenter, NewGroupModelImpl.OnNewGroupModelListener {

    private NewGroupView mNewGroupView;

    private NewGroupModel mNewGroupModel;

    private NewGroupPresenterImpl(NewGroupView mNewGroupView) {
        this.mNewGroupView = mNewGroupView;
        this.mNewGroupModel = NewGroupModelImpl.newInstance(this);
    }

    public static NewGroupPresenter newInstance(NewGroupView newGroupView) {
        return new NewGroupPresenterImpl(newGroupView);
    }

    @Override
    public void onCreateNewGroup() {
        mNewGroupView.setAdapter(mNewGroupModel.getAdapter(mNewGroupView.getContext()));
    }

    @Override
    public void onClickDone(String groupName) {
        mNewGroupView.showProgressbar();
        mNewGroupModel.createGroup(groupName);
    }

    @Override
    public void onSuccess(Bundle bundle) {
        mNewGroupView.dismissProgressbar();
        mNewGroupView.setSuccessBack(bundle);
    }

    @Override
    public void onEmptyGroupName() {
        mNewGroupView.dismissProgressbar();
        mNewGroupView.showMessage(Constants.Alerts.EMPTY_GROUP_NAME);
    }

    @Override
    public void onNoSportSelected() {
        mNewGroupView.dismissProgressbar();
        mNewGroupView.showMessage(Constants.Alerts.EMPTY_SPORT_SELECTION);
    }

    @Override
    public void onNoInternet() {
        mNewGroupView.dismissProgressbar();
        mNewGroupView.showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onFailed(String message) {
        mNewGroupView.dismissProgressbar();
        mNewGroupView.showMessage(message);
    }
}