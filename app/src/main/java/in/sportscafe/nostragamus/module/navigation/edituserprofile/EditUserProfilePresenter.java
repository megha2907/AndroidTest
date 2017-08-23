package in.sportscafe.nostragamus.module.navigation.edituserprofile;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by deepanshi on 8/22/17.
 */

public interface EditUserProfilePresenter {

    void onCreateEditProfile();

    void onClickDone(String about);

    void onClickImage();

    void onGetResult(int requestCode, int resultCode, Intent data);

}
