package in.sportscafe.nostragamus.module.user.myprofile.verify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;


import java.util.Calendar;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.navigation.referfriends.SuccessfulReferralActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

/**
 * Created by deepanshi on 7/18/17.
 */

public class VerifyProfileActivity extends NostragamusActivity implements VerifyProfileFragmentListener, View.OnClickListener {

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.VERIFY_PROFILE;
    }

    private HmImageView userPhoto;
    private TextView userName;
    private ImageView backBtn;

    private VerifyOTPFragment verifyOTPFragment;
    private VerifyPhoneNumberFragment verifyPhoneNumberFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_profile);

        initialize();
        loadVerifyPhoneNumberFragment();
    }

    private void initialize() {
        backBtn = (ImageView) findViewById(R.id.verify_profile_iv_back);
        userPhoto = (HmImageView) findViewById(R.id.verify_profile_iv_user_image);
        userName = (TextView) findViewById(R.id.verify_profile_tv_user_name);
        backBtn.setOnClickListener(this);
        setInfo();
    }

    private void setInfo() {
        userPhoto.setImageUrl(NostragamusDataHandler.getInstance().getUserInfo().getPhoto());
        userName.setText(NostragamusDataHandler.getInstance().getUserInfo().getUserName());
    }

    private void loadVerifyPhoneNumberFragment() {
        backBtn.setVisibility(View.INVISIBLE);
        verifyPhoneNumberFragment = new VerifyPhoneNumberFragment();
        FragmentHelper.replaceFragment(this, R.id.fragment_container, verifyPhoneNumberFragment);
    }

    private void loadVerifyOTPFragment(String phoneNumber) {
        Bundle args = new Bundle();
        args.putString(Constants.BundleKeys.PHONE_NUMBER, phoneNumber);
        backBtn.setVisibility(View.VISIBLE);
        verifyOTPFragment = new VerifyOTPFragment();
        verifyOTPFragment.setArguments(args);
        FragmentHelper.addContentFragmentWithAnimation(this, R.id.fragment_container, verifyOTPFragment);
    }

    @Override
    public void onVerifyPhoneNumber(String phoneNumber) {
        getOTPRequest(phoneNumber);
    }

    @Override
    public void onVerifyOTP(String otp) {
        verifyOTPRequest(otp);
    }

    @Override
    public void onResendOTP(String phoneNumber) {
        getOTPRequest(phoneNumber);
    }

    private void getOTPRequest(String phoneNumber) {

        showProgressbar();
        GetOTPApiModelImpl.newInstance(new GetOTPApiModelImpl.GetOTPApiListener() {
            @Override
            public void noInternet() {
                dismissProgressbar();
                showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
            }

            @Override
            public void onApiFailed() {
                dismissProgressbar();
                showMessage(Constants.Alerts.API_FAIL);
            }

            @Override
            public void onSuccessResponse(VerifyOTPInfo verifyOTPInfo, String phoneNumber) {
                dismissProgressbar();
                verifyPhoneNumberValid(verifyOTPInfo, phoneNumber);
            }
        }).performApiCall(phoneNumber);
    }

    private void verifyPhoneNumberValid(VerifyOTPInfo verifyOTPInfo, String phoneNumber) {

        if (verifyOTPInfo!=null) {
            Integer verifyPhoneNumber = verifyOTPInfo.getValidOTPCode();
            if (verifyPhoneNumber == null || verifyPhoneNumber == 0) {
                if (getActivity() != null) {
                    if (verifyPhoneNumberFragment != null) {
                        verifyPhoneNumberFragment.setPhoneNumberAlreadyExist();
                    }
                }
            } else {
                loadVerifyOTPFragment(phoneNumber);
            }
        }else {
            if (getActivity() != null) {
                if (verifyPhoneNumberFragment != null) {
                    verifyPhoneNumberFragment.setErrorMessage();
                }
            }
        }
    }

    private void verifyOTPRequest(String OTP) {

        showProgressbar();
        VerifyOTPApiModelImpl.newInstance(new VerifyOTPApiModelImpl.VerifyOTPApiListener() {
            @Override
            public void noInternet() {
                dismissProgressbar();
                showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
            }

            @Override
            public void onApiFailed() {
                dismissProgressbar();
                showMessage(Constants.Alerts.API_FAIL);
            }

            @Override
            public void onSuccessResponse(VerifyOTPInfo verifyOTPInfo) {
                dismissProgressbar();
                verifyOTPValid(verifyOTPInfo);
            }
        }).performApiCall(OTP);
    }

    private void verifyOTPValid(VerifyOTPInfo verifyOTPInfo) {

        if (verifyOTPInfo!=null) {
            Integer verifyOTPCode = verifyOTPInfo.getValidOTPCode();
            if (verifyOTPCode == null || verifyOTPCode == 0) {
                if (getActivity() != null) {
                    if (verifyOTPFragment != null) {
                        verifyOTPFragment.setOTPNotValid();
                    }
                }
            } else {
                checkForSuccessfulReferral();
            }
        }else {
            if (getActivity() != null) {
                if (verifyOTPFragment != null) {
                    verifyOTPFragment.setErrorMessage();
                }
            }
        }

    }

    private void checkForSuccessfulReferral() {

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey(Constants.BundleKeys.SUCCESSFUL_REFERRAL)) {
                boolean successfulReferral = extras.getBoolean(Constants.BundleKeys.SUCCESSFUL_REFERRAL, false);

                if (successfulReferral) {
                    navigateToSuccessfulReferral();
                } else {
                    navigateToHome();
                }
            }
        } else {
            navigateToHome();
        }

        getAndSendTimeForOnBoarding();

    }

    private void getAndSendTimeForOnBoarding() {

        long currentTimeMs = Calendar.getInstance().getTimeInMillis();
        long editProfileOpenTime = 0;

        if (NostragamusDataHandler.getInstance().getEditProfileShownTime() != 0 ||
                NostragamusDataHandler.getInstance().getEditProfileShownTime() != -1) {

            editProfileOpenTime = NostragamusDataHandler.getInstance().getEditProfileShownTime();

            long getTimeDiff = currentTimeMs - editProfileOpenTime;

            int secs = (int) (getTimeDiff / 1000);
            int mins = secs / 60;
            int hours = mins / 60;
            int days = hours / 24;

            NostragamusAnalytics.getInstance().trackOnBoarding
                    (Constants.AnalyticsActions.ONBOARDING_TIME,
                            String.format("%02d", days) + "d " + String.format("%02d", hours)
                                    + "h " + String.format("%02d", mins) + "m " + String.format("%02d", secs) + "s");
        } else {

            NostragamusAnalytics.getInstance().trackOnBoarding
                    (Constants.AnalyticsActions.ONBOARDING_TIME, "No Data for OnBoarding Time");
        }

    }

    private void navigateToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BundleKeys.LOGIN_SCREEN, Constants.BundleKeys.LOGIN_SCREEN);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void navigateToSuccessfulReferral() {
        Intent intent = new Intent(this, SuccessfulReferralActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verify_profile_iv_back:
                onBackClicked();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBackClicked();
    }

    private void onBackClicked() {
        if (getActivity() != null) {
            if (verifyOTPFragment != null) {
                if (verifyOTPFragment instanceof VerifyOTPFragment) {
                    try {
                        backBtn.setVisibility(View.INVISIBLE);
                        FragmentHelper.removeContentFragmentWithAnimation(this, verifyOTPFragment);
                    } catch (Exception e) {
                        e.printStackTrace();
                        this.finish();
                    }
                } else {
                    this.finish();
                }
            } else {
                this.finish();
            }
        }
    }

}
