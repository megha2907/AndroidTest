package in.sportscafe.nostragamus.module.navigation.referfriends.referralcredits;

/**
 * Created by deepanshi on 6/23/17.
 */

public interface ReferralCreditFragmentListener {
    void onPowerUpRewardsClicked();

    void onCashRewardsClicked();

    void onReferAFriendClicked(String referralCode, String walletInit);
}
