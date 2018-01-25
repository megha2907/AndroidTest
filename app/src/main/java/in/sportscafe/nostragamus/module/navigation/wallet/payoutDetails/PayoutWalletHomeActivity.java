package in.sportscafe.nostragamus.module.navigation.wallet.payoutDetails;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.freshchat.consumer.sdk.ConversationOptions;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.AddBankDetailsActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.AddPaytmDetailsActivity;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.login.dto.UserPaymentInfo;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class PayoutWalletHomeActivity extends NostragamusActivity implements PayoutWalletHomeFragmentListener {

    private static final String TAG = PayoutWalletHomeActivity.class.getSimpleName();

    private interface ActivityResultRequestCode {
        int ADD_PAYTM = 11;
        int ADD_BANK = 12;
        int EDIT_PAYTM = 13;
        int EDIT_BANK = 14;
    }

    @Override
    public String getScreenName() {
        return Constants.Notifications.SCREEN_WALLET_PAYOUT_HOME;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout_wallet);

        initToolbar();
        loadPayOutFragment();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.payout_toolbar);
        TextView tvToolbar = (TextView) findViewById(R.id.payout_toolbar_tv);
        tvToolbar.setText("Withdrawal Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.back_icon_grey);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }

    private void loadPayOutFragment() {
        PayoutWalletHomeFragment fragment = new PayoutWalletHomeFragment();
        FragmentHelper.replaceFragment(this, R.id.fragment_container, fragment);
    }

    @Override
    public void onAddPaytmClicked() {
        Intent intent = new Intent(this, AddPaytmDetailsActivity.class);
        startActivityForResult(intent, ActivityResultRequestCode.ADD_PAYTM);
    }

    @Override
    public void onAddBankClicked() {
        Intent intent = new Intent(this, AddBankDetailsActivity.class);
        startActivityForResult(intent, ActivityResultRequestCode.ADD_BANK);
    }

    @Override
    public void onEditPaytmClicked() {
        Intent intent = new Intent(this, AddPaytmDetailsActivity.class);
        intent.putExtras(getExtras());
        startActivityForResult(intent, ActivityResultRequestCode.EDIT_PAYTM);
    }

    @Override
    public void onEditBankClicked() {
        Intent intent = new Intent(this, AddBankDetailsActivity.class);
        intent.putExtras(getExtras());
        startActivityForResult(intent, ActivityResultRequestCode.EDIT_BANK);
    }

    private Bundle getExtras() {
        Bundle args = new Bundle();
        UserPaymentInfo userPaymentInfo = WalletHelper.getUserPaymentInfo();
        if (userPaymentInfo != null) {
            args.putParcelable(Constants.BundleKeys.USER_PAYMENT_INFO_PARCEL, Parcels.wrap(userPaymentInfo));
        }
        return  args;
    }

    @Override
    public void onFreshChatOptionChosen() {

        UserInfo userInfo = Nostragamus.getInstance().getServerDataManager().getUserInfo();
        FreshchatUser user = Freshchat.getInstance(getApplicationContext()).getUser();
        if (userInfo != null && user != null) {

            if (!TextUtils.isEmpty(userInfo.getOtpMobileNumber())) {
                user.setFirstName(userInfo.getUserName())
                        .setEmail(userInfo.getEmail())
                        .setPhone("+91 ", userInfo.getOtpMobileNumber());
            } else {
                user.setFirstName(userInfo.getUserName())
                        .setEmail(userInfo.getEmail());
            }

            Freshchat.getInstance(getApplicationContext()).setUser(user);

            /* Set any custom metadata to give agents more context,
            and for segmentation for marketing or pro-active messaging */
            Map<String, String> userMeta = new HashMap<String, String>();
            userMeta.put("UserId", String.valueOf(userInfo.getId()));
            userMeta.put("Transaction Type", ""); //setting it empty to refresh freshChat user properties in chat
            userMeta.put("Transaction Order Id", "");
            userMeta.put("Challenge Id", "");
            userMeta.put("MatchId", "");
            userMeta.put("RoomId", "");

            //Call setUserProperties to sync the user properties with Freshchat's servers
            Freshchat.getInstance(getApplicationContext()).setUserProperties(userMeta);

        }

         /* Open a Withdrawal Queries Chat Channel */
        List<String> tags = new ArrayList<>();
        tags.add("Withdraw");
        ConversationOptions convOptions = new ConversationOptions()
                .filterByTags(tags, "Withdraw");
        Freshchat.showConversations(getApplicationContext(), convOptions);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK &&
                (requestCode == ActivityResultRequestCode.EDIT_BANK ||
                        requestCode == ActivityResultRequestCode.EDIT_PAYTM ||
                        requestCode == ActivityResultRequestCode.ADD_BANK ||
                        requestCode == ActivityResultRequestCode.ADD_PAYTM) ) {

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment != null && fragment instanceof PayoutWalletHomeFragment) {
                ((PayoutWalletHomeFragment) fragment).updateScreenDetails();
            }

            /* Finish this is launched from Withdrawal (As there is no acc was added) */
            if (getIntent() != null && getIntent().getExtras() != null) {
                Bundle args = getIntent().getExtras();
                int launchMode = args.getInt(Constants.BundleKeys.LAUNCH_MODE, -1);
                switch (launchMode) {
                    case PayoutLaunchMode.FROM_WITHDRAWAL_AS_NO_PAY_OUT_ACC:

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setResult(RESULT_OK);
                                finish();
                            }
                        }, 500);
                        break;
                }
            }
        }
    }
}
