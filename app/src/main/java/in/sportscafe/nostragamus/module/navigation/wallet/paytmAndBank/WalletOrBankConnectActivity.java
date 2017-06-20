package in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.ScreenNames;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.user.login.UserInfoModelImpl;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.login.dto.UserPaymentInfo;

/**
 * Created by Jeeva on 23/03/17.
 */
public class WalletOrBankConnectActivity extends NostragamusActivity {

    private static final String TAG = WalletOrBankConnectActivity.class.getSimpleName();

    private static final int BANK_REQUEST_CODE = 1001;
    private static final int PAYTM_REQUEST_CODE = 1002;

    private UserPaymentInfo mUserPaymentInfo;

    @Override
    public String getScreenName() {
        return ScreenNames.PAYTM_CONNECT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm_connect);

        loadUserInfo();
    }

    private void loadUserInfo() {
        showProgressbar();
        UserInfoModelImpl.newInstance(getUserInfoCallBackListener()).getUserInfo();
    }

    private UserInfoModelImpl.OnGetUserInfoModelListener getUserInfoCallBackListener() {
        return new UserInfoModelImpl.OnGetUserInfoModelListener() {
            @Override
            public void onSuccessGetUpdatedUserInfo(UserInfo userInfo) {
                dismissProgressbar();
                if (userInfo != null) {
                    mUserPaymentInfo = userInfo.getUserPaymentInfo();
                }
            }

            @Override
            public void onFailedGetUpdateUserInfo(String message) {
                dismissProgressbar();
                // Initial call, Api fails but can not be taken action
                Log.d(TAG, "Get UserInfo API failed.");
                showMessage(Constants.Alerts.API_FAIL);
            }

            @Override
            public void onNoInternet() {
                dismissProgressbar();
                showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
            }
        };
    }

    private Bundle getExtras() {
        Bundle args = new Bundle();
        if (mUserPaymentInfo != null) {
            args.putParcelable(Constants.BundleKeys.USER_PAYMENT_INFO_PARCEL, Parcels.wrap(mUserPaymentInfo));
        }
        return  args;
    }

    /**
     * called from xml layout
     * @param view
     */
    public void onClickConnect(View view) {
        navigateToAddPaytmDetail();
    }

    /**
     * Invoked from xml directly
     * @param view
     */
    public void onClickNoPaytmAccount(View view) {
        navigateToAddBankDetails();
    }

    /**
     * Invoked from xml
     * @param view
     */
    public void onClickSkip(View view) {
        NostragamusAnalytics.getInstance().trackNoCashRewards(Constants.AnalyticsActions.NO_CASH_REWARDS);
        onBackPressed();
    }

    private void navigateToAddBankDetails() {
        Intent intent = new Intent(this, AddBankDetailsActivity.class);
        intent.putExtras(getExtras());
        startActivityForResult(intent, BANK_REQUEST_CODE);
    }

    private void navigateToAddPaytmDetail() {
        Intent intent = new Intent(this, AddPaytmDetailsActivity.class);
        intent.putExtras(getExtras());
        startActivityForResult(intent, PAYTM_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && (requestCode == BANK_REQUEST_CODE || requestCode == PAYTM_REQUEST_CODE)) {
            onBackPressed();
        }
    }

}