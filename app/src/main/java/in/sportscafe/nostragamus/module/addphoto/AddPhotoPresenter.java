package in.sportscafe.nostragamus.module.addphoto;

import android.content.Intent;

/**
 * Created by hmspl on 4/2/16.
 */
public interface AddPhotoPresenter {

    void onCreateAddPhoto();

    void onClickGallery();

    void onClickCamera();

    void onGetSuccessResult(int requestCode, Intent data);

    void onGetFailureResult(int requestCode, Intent data);
}