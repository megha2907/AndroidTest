package in.sportscafe.nostragamus.module.user.group.editgroupinfo;

import android.content.Intent;
import android.os.Bundle;

import in.sportscafe.nostragamus.Constants.Alerts;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Deepanshi on 12/6/16.
 */
public class EditGroupInfoPresenterImpl implements EditGroupInfoPresenter, EditGroupInfoModelImpl.OnEditGroupInfoModelListener {

    private EditGroupInfoView mGroupInfoView;

    private static final int ADD_PHOTO_REQUEST_CODE = 23;

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
        mGroupInfoModel.init(bundle);

        GroupInfo groupInfo = mGroupInfoModel.getGroupInfo();
        mGroupInfoView.setGroupImage(groupInfo.getPhoto());
        mGroupInfoView.setGroupName(groupInfo.getName());
    }

    @Override
    public void onSaveGroupName(String groupName) {
        mGroupInfoView.showProgressbar();
        mGroupInfoModel.updateGroupName(groupName);
    }

    @Override
    public void onGroupNameEmpty() {
        mGroupInfoView.dismissProgressbar();
        mGroupInfoView.showMessage(Alerts.EMPTY_GROUP_NAME);
    }

    @Override
    public void onNoInternet() {
        mGroupInfoView.dismissProgressbar();
        mGroupInfoView.showMessage(Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onClickImage() {
        mGroupInfoView.navigateToAddPhoto(ADD_PHOTO_REQUEST_CODE);
    }

    @Override
    public void onGetResult(int requestCode, int resultCode, Intent data) {
        if (ADD_PHOTO_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            mGroupInfoModel.onGetImage(data);
        }
    }

    @Override
    public void onClickBack() {
        if(mGroupInfoModel.isAnythingChanged()) {
            mGroupInfoView.goBackWithSuccessData(mGroupInfoModel.getGroupInfoBundle());
        } else {
            mGroupInfoView.goBack();
        }
    }

    @Override
    public void onGroupImagePathNull() {
        mGroupInfoView.showMessage(Alerts.IMAGE_FILEPATH_EMPTY);
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
    }
}