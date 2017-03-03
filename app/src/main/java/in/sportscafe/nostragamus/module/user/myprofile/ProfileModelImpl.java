package in.sportscafe.nostragamus.module.user.myprofile;

import android.support.v4.app.FragmentManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.bank.BankFragment;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.play.myresultstimeline.TimelineFragment;
import in.sportscafe.nostragamus.module.user.badges.Badge;
import in.sportscafe.nostragamus.module.user.badges.BadgeFragment;
import in.sportscafe.nostragamus.module.user.login.UserInfoModelImpl;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.powerups.PowerUp;
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
        pagerAdapter.addFragment(TimelineFragment.newInstance(), "Matches");

        HashMap<String, PowerUp> powerUpMaps = getPowerUpMap(userInfo.getPowerUps());
//        pagerAdapter.addFragment(PowerUpFragment.newInstance(powerUpList), AppSnippet.formatIfPlural(getPowerUpTotalCount(powerUpList), "Powerup", "s"));
        pagerAdapter.addFragment(BankFragment.newInstance(powerUpMaps), "Powerup Bank");

        List<Badge> badgeList = userInfo.getBadges();
        pagerAdapter.addFragment(BadgeFragment.newInstance(badgeList), "Achievements");

//        pagerAdapter.addFragment(ProfileSportSelectionFragment.newInstance(this), "Sports");

        return pagerAdapter;
    }

    @Override
    public String getSportsTabTitle() {
        return AppSnippet.formatIfPlural(NostragamusDataHandler.getInstance().getFavoriteSportsIdList().size(), "Sport", "s");
    }

    private HashMap<String, PowerUp> getPowerUpMap(HashMap<String, Integer> powerUps) {
        HashMap<String, PowerUp> powerUpMaps = new HashMap<>();
        for (Map.Entry<String, Integer> entry : powerUps.entrySet()) {
            powerUpMaps.put(entry.getKey(), new PowerUp(entry.getKey(), entry.getValue()));
        }
        return powerUpMaps;
    }

    private int getPowerUpTotalCount(List<PowerUp> powerUpList) {
        int count = 0;
        for (PowerUp powerUp : powerUpList) {
            count += powerUp.getCount();
        }
        return count;
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
    public void onNoInternet() {
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