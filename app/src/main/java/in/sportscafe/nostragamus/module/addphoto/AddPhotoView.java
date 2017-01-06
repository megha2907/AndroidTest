package in.sportscafe.nostragamus.module.addphoto;

import android.net.Uri;
import android.os.Bundle;

import com.jeeva.android.View;

/**
 * Created by hmspl on 4/2/16.
 */
public interface AddPhotoView {

    void navigateToGallery(int requestCode);

    void navigateToCamera(Uri cameraOutputUri, int requestCode);

    void setResult(Bundle bundle);

    void goBack();

    void showProcessing();

    void dismissProcessing();
}