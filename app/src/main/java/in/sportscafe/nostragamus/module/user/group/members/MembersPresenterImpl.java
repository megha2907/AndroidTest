package in.sportscafe.nostragamus.module.user.group.members;

import android.os.Bundle;

import in.sportscafe.nostragamus.Constants.Alerts;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupPerson;

/**
 * Created by Jeeva on 2/7/16.
 */
public class MembersPresenterImpl implements MembersPresenter, MembersModelImpl.OnMembersModelListener {

    private MembersView mMembersView;

    private MembersModel mMembersModel;

    public MembersPresenterImpl(MembersView membersView) {
        this.mMembersView = membersView;
        this.mMembersModel = MembersModelImpl.newInstance(this);
    }

    public static MembersPresenter newInstance(MembersView membersView) {
        return new MembersPresenterImpl(membersView);
    }

    @Override
    public void onCreateMembers(Bundle bundle) {
        MembersAdapter membersAdapter = mMembersModel.init(mMembersView.getContext(), bundle);
        if(null != membersAdapter) {
            mMembersView.setAdapter(membersAdapter);
        }
    }

    @Override
    public void onClickLeaveGroup() {
        mMembersView.showProgressbar();
        mMembersModel.leaveGroup();
    }

    @Override
    public void onMakeAdminSuccess() {
        mMembersView.dismissProgressbar();
        mMembersView.showMessage(Alerts.MAKE_ADMIN);
    }

    @Override
    public void onRemovedPersonSuccess() {
        mMembersView.dismissProgressbar();
        mMembersView.showMessage(Alerts.REMOVE_PERSON);
    }

    @Override
    public void onFailed(String message) {
        mMembersView.dismissProgressbar();
        mMembersView.showMessage(message);
    }

    @Override
    public void onEmpty() {
        mMembersView.dismissProgressbar();
        mMembersView.showMessage(Alerts.MEMBERS_EMPTY);
    }

    @Override
    public void onNoInternet() {
        mMembersView.dismissProgressbar();
        mMembersView.showMessage(Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onLeaveGroupSuccess() {
        mMembersView.dismissProgressbar();
        mMembersView.showMessage(Alerts.LEAVE_GROUP_SUCCESS);
        mMembersView.navigateToHome();
    }
}