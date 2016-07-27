package in.sportscafe.scgame.module.user.myprofile.edit;

import android.os.Bundle;

/**
 * Created by Jeeva on 12/6/16.
 */
public interface EditProfilePresenter {

    void onCreateEditProfile(Bundle bundle);

    void onClickDone(String name, String about);
}