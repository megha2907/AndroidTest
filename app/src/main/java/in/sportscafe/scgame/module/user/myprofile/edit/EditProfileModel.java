package in.sportscafe.scgame.module.user.myprofile.edit;

import in.sportscafe.scgame.module.user.login.dto.UserInfo;

/**
 * Created by Jeeva on 12/6/16.
 */
public interface EditProfileModel {

    void updateProfile(String name, String about);

    UserInfo getUserInfo();
}