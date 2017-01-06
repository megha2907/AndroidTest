package in.sportscafe.nostragamus.module.addphoto;

import android.content.Intent;
import android.net.Uri;

/**
 * Created by hmspl on 4/2/16.
 */
public interface AddPhotoModel {

    Uri getCameraOutputUri();

    void processGalleryResult(Intent data);

    void processCameraResult(Intent data);
}