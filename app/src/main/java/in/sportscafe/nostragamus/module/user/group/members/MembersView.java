package in.sportscafe.nostragamus.module.user.group.members;

import com.jeeva.android.InAppView;

/**
 * Created by Jeeva on 2/7/16.
 */
public interface MembersView extends InAppView {

    void setAdapter(MembersAdapter membersAdapter);

    void navigateToHome();
}