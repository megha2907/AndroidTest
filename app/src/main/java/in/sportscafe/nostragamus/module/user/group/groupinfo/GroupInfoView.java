package in.sportscafe.nostragamus.module.user.group.groupinfo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.jeeva.android.InAppView;
import com.jeeva.android.View;

import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;

/**
 * Created by Jeeva on 12/6/16.
 */
public interface GroupInfoView extends InAppView {

    void setGroupName(String groupName);

    void setGroupImage(String groupImageUrl);

    void setAdapter(ViewPagerAdapter adapter);

    void setTourTabTitle(String title);

    void navigateToHome();

    void navigateToEditGroup(Bundle bundle);

    void changeToAdminMode();

    void goBack();

    FragmentManager getSupportFragmentManager();
}