package in.sportscafe.nostragamus.module.user.myprofile;

import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.play.myresultstimeline.TimelineFragment;
import in.sportscafe.nostragamus.module.user.badges.Badge;
import in.sportscafe.nostragamus.module.user.badges.BadgeFragment;
import in.sportscafe.nostragamus.module.user.login.UserInfoModelImpl;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.powerups.PowerUp;
import in.sportscafe.nostragamus.module.user.powerups.PowerUpFragment;
import in.sportscafe.nostragamus.module.user.sportselection.profilesportselection.ProfileSportSelectionFragment;

/**
 * Created by Jeeva on 14/6/16.
 */
public class ProfileModelImpl implements ProfileModel, UserInfoModelImpl.OnGetUserInfoModelListener,
        ProfileSportSelectionFragment.OnSportSelectionChangedListener {

    private OnProfileModelListener mProfileModelListener;

    private ProfileModelImpl(OnProfileModelListener listener) {
        this.mProfileModelListener = listener;
    }

    public static ProfileModel newInstance(OnProfileModelListener listener) {
        return new ProfileModelImpl(listener);
    }

    @Override
    public void getProfileDetails() {
        UserInfoModelImpl.newInstance(this).getUserInfo();
    }

    @Override
    public UserInfo getUserInfo() {
        return NostragamusDataHandler.getInstance().getUserInfo();
    }

    @Override
    public ViewPagerAdapter getAdapter(FragmentManager fm) {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(fm);

        UserInfo userInfo = getUserInfo();
        pagerAdapter.addFragment(TimelineFragment.newInstance(), AppSnippet.formatIfPlural(userInfo.getTotalMatchesPlayed(), "Match", "es"));

        List<Badge> badgeList = userInfo.getBadges();
        pagerAdapter.addFragment(BadgeFragment.newInstance(badgeList), AppSnippet.formatIfPlural(badgeList.size(), "Achievement", "s"));

        List<PowerUp> powerUpList = getPowerUpList(userInfo.getPowerUps());
        pagerAdapter.addFragment(PowerUpFragment.newInstance(powerUpList), AppSnippet.formatIfPlural(powerUpList.size(), "Powerup", "s"));

        pagerAdapter.addFragment(ProfileSportSelectionFragment.newInstance(this), getSportsTabTitle());

        return pagerAdapter;
    }

    @Override
    public String getSportsTabTitle() {
        return AppSnippet.formatIfPlural(NostragamusDataHandler.getInstance().getFavoriteSportsIdList().size(), "Sport", "s");
    }

    private List<PowerUp> getPowerUpList(HashMap<String, Integer> powerUps) {
        List<PowerUp> powerUpList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : powerUps.entrySet()) {
            powerUpList.add(new PowerUp(entry.getKey(), entry.getValue()));
        }
        return powerUpList;
    }

    @Override
    public void onSuccessGetUpdatedUserInfo(UserInfo updatedUserInfo) {
        mProfileModelListener.onGetProfileSuccess();
    }

    @Override
    public void onFailedGetUpdateUserInfo(String message) {
        mProfileModelListener.onGetProfileFailed(message);
    }

    @Override
    public void onSportsSelectionChanged() {
        mProfileModelListener.onSportsTitleChanged(getSportsTabTitle());
    }

    public interface OnProfileModelListener {

        void onGetProfileSuccess();

        void onGetProfileFailed(String message);

        void onNoInternet();

        void onSportsTitleChanged(String title);
    }
}