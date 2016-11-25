package in.sportscafe.nostragamus.module.user.myprofile;

import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;

/**
 * Created by Jeeva on 14/6/16.
 */
public interface ProfileModel {

    void getProfileDetails();

    UserInfo getUserInfo();
}