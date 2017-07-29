package in.sportscafe.nostragamus.module.user.myprofile.verify;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.Log;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.EnhancedLinkMovementMethod;
import in.sportscafe.nostragamus.utils.FragmentHelper;

/**
 * Created by deepanshi on 7/18/17.
 */

public class VerifyOTPFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = VerifyOTPFragment.class.getSimpleName();

    private VerifyProfileFragmentListener mVerifyProfileFragmentListener;

    private TextView phoneNumberText;

    private EditText mOTP;

    private TextView errorTextView;

    private TextView otpLabel;

    private Button resendOTPBtn;

    Animation phoneNumberFadeIn;
    Animation phoneNumberOut;

    Animation resendBtnFadeIn;
    Animation resendBtnFadeOut;

    private String phoneNumber = "";

    CountDownTimer countDownTimer;

    public VerifyOTPFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof VerifyProfileActivity) {
            mVerifyProfileFragmentListener = (VerifyProfileFragmentListener) context;
        } else {
            throw new RuntimeException("Activity must implement " + TAG);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_verify_otp, container, false);

        initRootView(rootView);

        setInfo();

        return rootView;
    }

    private void initRootView(View rootView) {
        mOTP = (EditText) rootView.findViewById(R.id.verify_et_otp);
        phoneNumberText = (TextView) rootView.findViewById(R.id.verify_profile_otp_phone_textView);
        otpLabel = (TextView) rootView.findViewById(R.id.verify_otp_label);
        errorTextView = (TextView) rootView.findViewById(R.id.verify_profile_otp_error_textView);
        rootView.findViewById(R.id.verify_otp_btn).setOnClickListener(this);
        resendOTPBtn = (Button) rootView.findViewById(R.id.resend_otp_btn);
        resendOTPBtn.setOnClickListener(this);

        /* phone Number Animation */
        phoneNumberFadeIn = new AlphaAnimation(0.0f, 1.0f);
        phoneNumberFadeIn.setDuration(300);
        phoneNumberOut = new AlphaAnimation(1.0f, 0.0f);
        phoneNumberOut.setDuration(300);
        AnimationSet as = new AnimationSet(true);
        as.addAnimation(phoneNumberOut);
        phoneNumberFadeIn.setStartOffset(300);
        as.addAnimation(phoneNumberFadeIn);

        /* Resend Btn Animation */
        resendBtnFadeIn = new AlphaAnimation(0.0f, 1.0f);
        resendBtnFadeIn.setDuration(100);
        resendBtnFadeOut = new AlphaAnimation(1.0f, 0.0f);
        resendBtnFadeOut.setDuration(100);
        AnimationSet asTwo = new AnimationSet(true);
        asTwo.addAnimation(resendBtnFadeOut);
        resendBtnFadeIn.setStartOffset(0);
        asTwo.addAnimation(resendBtnFadeIn);

        inValidOTPEditText();
    }


    private void setInfo() {
        Bundle args = getArguments();
        if (args != null) {

            startOTPCountDownTimer();

            phoneNumber = getArguments().getString(Constants.BundleKeys.PHONE_NUMBER);
            phoneNumberText.setText("We have sent an OTP to " + phoneNumber +
                    ", if you haven't received the code , use the button to resend it.");
        } else {
            phoneNumberText.setText("We have sent an OTP to your Phone Number, if you haven't received the code , use the button to resend it.");
        }
    }

    private void inValidOTPEditText() {

        mOTP.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                otpLabel.setTextColor(ResourcesCompat.getColor(getActivity().getResources(), R.color.white, null));
                return false;
            }

        });

        mOTP.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable ed) {

//                errorTextView.setVisibility(View.INVISIBLE);

                if (ed.length() == 6) {
                    hideSoftKeyboard();
                }
            }
        });
    }

    private void validateOTP() {

        String otp = getTrimmedText(mOTP);

        if (TextUtils.isEmpty(otp) || otp.length() != 6) {
            errorTextView.setVisibility(View.VISIBLE);
            setOTPNotValid();
        } else {
            if (mVerifyProfileFragmentListener != null) {
                mVerifyProfileFragmentListener.onVerifyOTP(otp);
            } else {
                showMessage(Constants.Alerts.SOMETHING_WRONG);
            }
        }
    }

    private void resendOTP() {

        startOTPCountDownTimer();

        if (mVerifyProfileFragmentListener != null) {
            if (!TextUtils.isEmpty(phoneNumber)) {
                mVerifyProfileFragmentListener.onResendOTP(phoneNumber);
            }
        } else {
            showMessage(Constants.Alerts.SOMETHING_WRONG);
        }
    }


    public void setOTPNotValid() {
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(Constants.Alerts.INVALID_OTP);
    }

    public void setErrorMessage() {
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(Constants.Alerts.API_FAIL);
    }

    private void startOTPCountDownTimer() {

        countDownTimer = new CountDownTimer((15 + 1) * 1000, 1000) {

            @Override
            public void onFinish() {

            }

            @Override
            public void onTick(long millisUntilFinished) {

                if (millisUntilFinished < 2005){

                    resendOTPBtn.startAnimation(resendBtnFadeOut);
                    resendBtnFadeOut.setAnimationListener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            resendOTPBtn.setText("Resend OTP");
                            if (getActivity()!=null) {
                                resendOTPBtn.setTextColor(ResourcesCompat.getColor(getActivity().getResources(), R.color.venice_blue, null));
                            }
                            resendOTPBtn.setClickable(true);
                            resendOTPBtn.startAnimation(resendBtnFadeIn);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }else {
                    resendOTPBtn.setText("Resend OTP in 00:" + String.format("%02d", ((millisUntilFinished) / 1000) - 1));
                    if (getActivity()!=null) {
                        resendOTPBtn.setTextColor(ResourcesCompat.getColor(getActivity().getResources(), R.color.white_999999, null));
                    }
                    resendOTPBtn.setClickable(false);
                }

            }

        };

        countDownTimer.start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verify_otp_btn:
                validateOTP();
                break;

            case R.id.resend_otp_btn:
                resendOTP();
                break;
        }
    }

    /**
     * To remove fragment from backstack
     * @param activity
     */
    public void onBackPressed(@NonNull AppCompatActivity activity) {
        FragmentHelper.removeContentFragmentWithAnimation(activity, this);
    }

}
