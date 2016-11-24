package in.sportscafe.scgame.module.user.group.newgroup;

import android.os.Bundle;

import in.sportscafe.scgame.Constants;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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
    public void onGroupPhotoDone(MultipartBody.Part file, RequestBody filepath, RequestBody filename) {
        mNewGroupModel.updateGroupPhoto(file, filepath,filename);
    }

    @Override
    public void onSuccess(Bundle bundle) {
        mNewGroupView.dismissProgressbar();
        mNewGroupView.setSuccessBack(bundle);
    }

    @Override
    public void onSuccessTournamentInfo() {
        mNewGroupView.dismissProgressbar();
    }

    @Override
    public void onEmptyGroupName() {
        mNewGroupView.dismissProgressbar();
        mNewGroupView.showMessage(Constants.Alerts.EMPTY_GROUP_NAME);
    }

    @Override
    public void onNoSportSelected() {
        mNewGroupView.dismissProgressbar();
        mNewGroupView.showMessage(Constants.Alerts.EMPTY_TOURNAMENT_SELECTION);
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

    @Override
    public void onGroupImagePathNull() {
        mNewGroupView.showMessage(Constants.Alerts.IMAGE_FILEPATH_EMPTY);
    }

    @Override
    public void onUpdating() {
        mNewGroupView.showProgressbar();
    }

    @Override
    public void onEditFailed(String message) {
        mNewGroupView.dismissProgressbar();
        mNewGroupView.showMessage(message);

    }

    @Override
    public void onPhotoUpdate(String groupPhoto) {

        mNewGroupView.dismissProgressbar();
        mNewGroupView.setGroupImage(groupPhoto);

    }
}