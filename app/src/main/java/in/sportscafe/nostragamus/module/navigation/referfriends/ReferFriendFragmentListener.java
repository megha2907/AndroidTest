package in.sportscafe.nostragamus.module.navigation.referfriends;

import android.os.Bundle;

import org.parceler.Parcel;

import in.sportscafe.nostragamus.webservice.UserReferralInfo;

/**
 * Created by deepanshi on 6/21/17.
 */

public interface ReferFriendFragmentListener {

    void onReferralCreditsClicked(Bundle bundle);

    void onTermsClicked();

    void onReferAFriendClicked(String referralCode,String walletInit,String downloadLink);
}
