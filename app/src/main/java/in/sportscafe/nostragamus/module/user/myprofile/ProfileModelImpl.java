package in.sportscafe.nostragamus.module.user.myprofile;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import java.util.List;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.play.myresultstimeline.TimelineFragment;
import in.sportscafe.nostragamus.module.user.badges.Badge;
import in.sportscafe.nostragamus.module.user.badges.BadgeFragment;
import in.sportscafe.nostragamus.module.user.login.UserInfoModelImpl;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;

/**
 * Created by Jeeva on 14/6/16.
 */
public class ProfileModelImpl implements ProfileModel, UserInfoModelImpl.OnGetUserInfoModelListener {

    private Integer mChallengeId;

    private boolean mSeparateScreen = false;

    private OnProfileModelListener mProfileModelListener;

    private ProfileModelImpl(OnProfileModelListener listener) {
        this.mProfileModelListener = listener;
    }

    public static ProfileModel newInstance(OnProfileModelListener listener) {
        return new ProfileModelImpl(listener);
    }

    @Override
    public void init(Bundle bundle) {
        if(null != bundle) {
            if(bundle.containsKey(BundleKeys.CHALLENGE_ID)) {
                mChallengeId = bundle.getInt(BundleKeys.CHALLENGE_ID);
            }

            mSeparateScreen = bundle.getBoolean(BundleKeys.IS_SEPARATE_SCREEN);
        }
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
        pagerAdapter.addFragment(TimelineFragment.newInstance(mChallengeId), "Matches");

        // Wallet should NOT be displayed for free app version
        /*if (BuildConfig.IS_PAID_VERSION) {
            UserPaymentInfo paymentInfo = null;
            if (userInfo != null) {
                paymentInfo = userInfo.getUserPaymentInfo();
            }
            pagerAdapter.addFragment(WalletHistoryFragment.newInstance(paymentInfo), "Wallet");
        }*/

        /*HashMap<String, PowerUp> powerUpMaps = getPowerUpMap(userInfo.getPowerUps());
        pagerAdapter.addFragment(BankFragment.newInstance(powerUpMaps), "Powerup Bank");*/

        List<Badge> badgeList = userInfo.getBadges();
        pagerAdapter.addFragment(BadgeFragment.newInstance(badgeList), "Achievements");

//        pagerAdapter.addFragment(ProfileSportSelectionFragment.newInstance(this), "Sports");

        return pagerAdapter;
    }

    @Override
    public boolean isSeparateScreen() {
        return mSeparateScreen;
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

    public interface OnProfileModelListener {

        void onGetProfileSuccess();

        void onGetProfileFailed(String message);

        void onNoInternet();

        void onSportsTitleChanged(String title);
    }
}