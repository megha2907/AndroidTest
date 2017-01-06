package in.sportscafe.nostragamus.module.addphoto;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.Alerts;
import in.sportscafe.nostragamus.R;

/**
 * Created by hmspl on 4/2/16.
 */
public class AddPhotoPresenterImpl implements AddPhotoPresenter, AddPhotoModelImpl.OnAddPhotoModelListener {

    private static final int GALLERY_REQUEST_CODE = 12;

    private static final int CAMERA_REQUEST_CODE = 13;

    private AddPhotoView mAddPhotoView;

    private AddPhotoModel mAddPhotoModel;

    private int mSelectedRequest = -1;

    public AddPhotoPresenterImpl(AddPhotoView addPhotoView) {
        this.mAddPhotoView = addPhotoView;
        this.mAddPhotoModel = AddPhotoModelImpl.newInstance(this);
    }

    public static AddPhotoPresenter newInstance(AddPhotoView addPhotoView) {
        return new AddPhotoPresenterImpl(addPhotoView);
    }

    @Override
    public void onCreateAddPhoto() {
        onClickGallery();
//        mAddPhotoView.setListAdapter(mAddPhotoModel.getAddPhotoAdapter(mAddPhotoView.getContext(), this));
    }

    @Override
    public void onClickGallery() {
        mSelectedRequest = GALLERY_REQUEST_CODE;
        mAddPhotoView.navigateToGallery(GALLERY_REQUEST_CODE);
    }

    @Override
    public void onClickCamera() {
        mSelectedRequest = CAMERA_REQUEST_CODE;
        mAddPhotoView.navigateToCamera(mAddPhotoModel.getCameraOutputUri(), CAMERA_REQUEST_CODE);
    }

    @Override
    public void onGetSuccessResult(int requestCode, Intent data) {
        mAddPhotoView.showProcessing();
        switch (mSelectedRequest) {
            case GALLERY_REQUEST_CODE:
                mAddPhotoModel.processGalleryResult(data);
                break;
            case CAMERA_REQUEST_CODE:
                mAddPhotoModel.processCameraResult(data);
                break;
        }
    }

    @Override
    public void onGetFailureResult(int requestCode, Intent data) {
        mAddPhotoView.goBack();
    }

    @Override
    public void onPhotoAdded(Bundle bundle) {
        mAddPhotoView.dismissProcessing();
        mAddPhotoView.setResult(bundle);
        mAddPhotoView.goBack();
    }

    @Override
    public void onPhotoAddedFailed() {
        mAddPhotoView.dismissProcessing();
//        mAddPhotoView.showMessage(Alerts.ADD_PHOTO_FAILED);
        mAddPhotoView.goBack();
    }
}