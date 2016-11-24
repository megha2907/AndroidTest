package in.sportscafe.scgame.module.user.group.members;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupPerson;

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
    public void onCreateMembers(long groupId) {
        MembersAdapter membersAdapter = mMembersModel.init(mMembersView.getContext(), groupId);
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
    public void onGetNewPerson(GroupPerson newPerson) {
        mMembersModel.addNewPerson(newPerson);
    }

    @Override
    public void onMakeAdminSuccess() {
        mMembersView.dismissProgressbar();
        mMembersView.showMessage(Constants.Alerts.MAKE_ADMIN);
    }

    @Override
    public void onRemovedPersonSuccess() {
        mMembersView.dismissProgressbar();
        mMembersView.showMessage(Constants.Alerts.REMOVE_PERSON);

        mMembersView.setSuccessResult();
    }

    @Override
    public void onFailed(String message) {
        mMembersView.dismissProgressbar();
        mMembersView.showMessage(message);
    }

    @Override
    public void onEmpty() {
        mMembersView.dismissProgressbar();
        mMembersView.showMessage(Constants.Alerts.MEMBERS_EMPTY);
    }

    @Override
    public void onNoInternet() {
        mMembersView.dismissProgressbar();
        mMembersView.showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onLeaveGroupSuccess() {
        mMembersView.dismissProgressbar();
        mMembersView.showMessage(Constants.Alerts.LEAVE_GROUP_SUCCESS);
        mMembersView.navigateToHome();
    }
}