package in.sportscafe.scgame.module.user.group.editgroupinfo;

import android.content.Context;
import android.os.Bundle;

import com.jeeva.android.Log;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Deepanshi on 12/6/16.
 */
public class EditGroupInfoPresenterImpl implements EditGroupInfoPresenter, EditGroupInfoModelImpl.OnEditGroupInfoModelListener {

    private EditGroupInfoView mGroupInfoView;

    private EditGroupInfoModel mGroupInfoModel;

    private EditGroupInfoPresenterImpl(EditGroupInfoView groupInfoView) {
        this.mGroupInfoView = groupInfoView;
        this.mGroupInfoModel = EditGroupInfoModelImpl.newInstance(this);
    }

    public static EditGroupInfoPresenter newInstance(EditGroupInfoView groupInfoView) {
        return new EditGroupInfoPresenterImpl(groupInfoView);
    }

    @Override
    public void onCreateGroupInfo(Bundle bundle) {

        mGroupInfoView.setGroupName(bundle.getString(Constants.BundleKeys.GROUP_NAME));
        mGroupInfoView.showProgressbar();
        mGroupInfoModel.init(bundle);
    }

    private void onUpdateGroupInfo(GroupInfo groupInfo){
        mGroupInfoView.setAdapter(mGroupInfoModel.getAdapter(mGroupInfoView.getContext()));

    }



    @Override
    public void onDoneGroupName(String groupName,String groupPhoto) {
        mGroupInfoView.showProgressbar();
        mGroupInfoModel.updateGroupName(groupName,groupPhoto);
        mGroupInfoView.setGroupIcon(groupName.substring(0,1));
    }

    @Override
    public void onDoneUpdateTournaments() {
        mGroupInfoModel.updateTournaments();

    }



    @Override
    public void onGetMemberResult() {
        mGroupInfoModel.refreshGroupInfo();
    }

    @Override
    public void onGroupNameEmpty() {
        mGroupInfoView.dismissProgressbar();
        mGroupInfoView.showMessage(Constants.Alerts.EMPTY_GROUP_NAME);
    }

    @Override
    public void onNoInternet() {
        mGroupInfoView.dismissProgressbar();
        mGroupInfoView.showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onGroupTournamentUpdateSuccess() {
        mGroupInfoView.setSuccessResult();
    }

    @Override
    public void onLeaveGroupSuccess() {
        mGroupInfoView.dismissProgressbar();
        mGroupInfoView.showMessage(Constants.Alerts.LEAVE_GROUP_SUCCESS);
        mGroupInfoView.navigateToHome();
    }

    @Override
    public void onFailed(String message) {
        mGroupInfoView.dismissProgressbar();
        showAlert(message);

    }

    private void showAlert(String message) {

        }


    @Override
    public void onGetGroupSummarySuccess(GroupInfo groupInfo) {
        Log.i("inside","onGetGroupSummarySuccess");
        mGroupInfoView.dismissProgressbar();
        mGroupInfoModel.updateGroupMembers();
        mGroupInfoModel.updateTournaments();
        mGroupInfoView.setGroupImage(groupInfo.getPhoto());
        onUpdateGroupInfo(groupInfo);


    }

    @Override
    public void onGroupPhotoDone(MultipartBody.Part file, RequestBody filepath, RequestBody filename) {
        mGroupInfoModel.updateGroupPhoto(file, filepath,filename);
    }

    @Override
    public void onGetGroupSummaryFailed(String message) {

    }

    @Override
    public Context getContext() {
        return mGroupInfoView.getContext();
    }

    @Override
    public void onGroupImagePathNull() {
        mGroupInfoView.showMessage(Constants.Alerts.IMAGE_FILEPATH_EMPTY);
    }

    @Override
    public void onUpdating() {
        mGroupInfoView.showProgressbar();
    }

    @Override
    public void onPhotoUpdate(String photo) {
        mGroupInfoView.dismissProgressbar();
        mGroupInfoView.setGroupImage(photo);
    }

    @Override
    public void onEditFailed(String message) {
        mGroupInfoView.dismissProgressbar();
        mGroupInfoView.showMessage(message);
    }

    @Override
    public void onGroupNameUpdateSuccess() {
        mGroupInfoView.dismissProgressbar();
        mGroupInfoView.disableEdit();
        mGroupInfoView.setSuccessResult();
    }
}