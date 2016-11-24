package in.sportscafe.scgame.module.user.myprofile;

import in.sportscafe.scgame.module.user.login.dto.UserInfo;

/**
 * Created by Jeeva on 14/6/16.
 */
public interface ProfileModel {

    void getProfileDetails();

    UserInfo getUserInfo();
}