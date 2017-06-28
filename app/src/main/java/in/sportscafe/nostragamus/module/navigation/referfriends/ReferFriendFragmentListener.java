package in.sportscafe.nostragamus.module.navigation.referfriends;

/**
 * Created by deepanshi on 6/21/17.
 */

public interface ReferFriendFragmentListener {

    void onReferralCreditsClicked();

    void onTermsClicked();

    void onReferAFriendClicked(String referralCode,String walletInit);
}
