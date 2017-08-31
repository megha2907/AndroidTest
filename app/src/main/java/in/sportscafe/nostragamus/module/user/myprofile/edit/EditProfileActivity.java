package in.sportscafe.nostragamus.module.user.myprofile.edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.ExceptionTracker;
import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.customfont.CustomButton;

import java.util.Calendar;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AppPermissions;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.RequestCodes;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.addphoto.AddPhotoActivity;
import in.sportscafe.nostragamus.module.newChallenges.ui.NewChallengesActivity;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.common.WordUtils;
import in.sportscafe.nostragamus.module.navigation.referfriends.SuccessfulReferralActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsChecker;
import in.sportscafe.nostragamus.module.user.myprofile.verify.VerifyProfileActivity;

/**
 * Created by Jeeva on 12/6/16.
 */
public class EditProfileActivity extends NostragamusActivity implements EditProfileView,
        View.OnClickListener {

    public interface ILaunchedFrom {
        int HOME_ACTIVITY = 1;
        int GET_STARTED_ACTIVITY = 2;
        int LOG_IN_ACTIVITY = 3;
        int PROFILE_FRAGMENT = 4;
        int SETTINGS_ACTIVITY = 5;
        int NAVIGATION_SCREEN = 6;
    }

    private PermissionsChecker checker;
    private Toolbar mtoolbar;
    private TextView mToolbarTitle;
    private HmImageView mIvProfileImage;
    private CheckBox mCbProfileDisclaimer;
    private EditProfilePresenter mEditProfilePresenter;
    private CustomButton mBtnUpdateDone;
    private TextView mReferralCodeText;
    private EditText mReferralEditText;
    private EditText mUserNickNameEditText;
    private LinearLayout mReferralLayout;
    private TextView userNickNameLabel;
    private TextView mReferralLabel;

    private final String blockCharacterSet = "~#^|$%&*!@_-+/:;!?";

    private String mReferralCode = "";

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };


    /**
     * If only On-board (HomeActivity) && paid version, then only true
     */
    private boolean mIsOnBoardFlow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        checker = new PermissionsChecker(this);

        initToolBar();

        mUserNickNameEditText = (EditText) findViewById(R.id.edit_et_nickname_new);
        userNickNameLabel = (TextView) findViewById(R.id.edit_tv_nickname_label);
        mIvProfileImage = (HmImageView) findViewById(R.id.edit_iv_user_image);
        mBtnUpdateDone = (CustomButton) findViewById(R.id.edit_btn_done);
        mCbProfileDisclaimer = (CheckBox) findViewById(R.id.edit_disclaimer_checkbox);
        initListener();
        this.mEditProfilePresenter = EditProfilePresenterImpl.newInstance(this);
        this.mEditProfilePresenter.onCreateEditProfile(getIntent().getExtras());

        initOnBoardFlow();
        initReferralCode();
        getAndStoreCurrentTime();

        if (!TextUtils.isEmpty(mUserNickNameEditText.getText())) {
            String editName = mUserNickNameEditText.getText().toString();
            mUserNickNameEditText.setSelection(editName.length());
        }
    }

    private void getAndStoreCurrentTime() {

        long currentTimeMs = Calendar.getInstance().getTimeInMillis();
        if (currentTimeMs!=0){
            NostragamusDataHandler.getInstance().setEditProfileShownTime(currentTimeMs);
        }
    }

    private void initReferralCode() {

//        mReferralCodeText = (TextView) findViewById(R.id.edit_profile_tv_referral_code_txt);
        mReferralLayout = (LinearLayout) findViewById(R.id.edit_profile_referral_layout);
        mReferralLabel = (TextView) findViewById(R.id.edit_profile_tv_referral_label);
        mReferralEditText = (EditText) findViewById(R.id.edit_profile_tv_referral_code_editText);

        if (BuildConfig.IS_PAID_VERSION) {
            if (!NostragamusDataHandler.getInstance().isMarketingCampaign()) {

//                mReferralCodeText.setVisibility(View.VISIBLE);
                mReferralLayout.setVisibility(View.VISIBLE);
                mReferralEditText.setVisibility(View.VISIBLE);

                if (!TextUtils.isEmpty(NostragamusDataHandler.getInstance().getUserReferralCode())) {
                    populateUserReferralCode(NostragamusDataHandler.getInstance().getUserReferralCode());
                    mReferralCode = NostragamusDataHandler.getInstance().getUserReferralCode();
                    NoEditingReferralCode();
                } else {
                    enterReferralCodeManually();

                }
            } else {
//                mReferralCodeText.setVisibility(View.GONE);
                mReferralLayout.setVisibility(View.GONE);
                mReferralEditText.setVisibility(View.GONE);
            }
        } else {
//            mReferralCodeText.setVisibility(View.GONE);
            mReferralLayout.setVisibility(View.GONE);
            mReferralEditText.setVisibility(View.GONE);
        }
    }

    private void NoEditingReferralCode() {
        mReferralEditText.setEnabled(false);
    }


    /**
     * If only on-board flow, and only paid version, then show age-disclaimer; else not
     */
    private void initOnBoardFlow() {
        if (BuildConfig.IS_PAID_VERSION) {
            mIsOnBoardFlow = true;
            mCbProfileDisclaimer.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(NostragamusDataHandler.getInstance().getDisclaimerText())) {
                mCbProfileDisclaimer.setText(NostragamusDataHandler.getInstance().getDisclaimerText());
            } else {
                mCbProfileDisclaimer.setText(getResources().getString(R.string.profile_disclaimer));
            }
        }
    }

    private void initListener() {

        mUserNickNameEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                userNickNameLabel.setTextColor(ResourcesCompat.getColor(getActivity().getResources(), R.color.white, null));
                return false;
            }

        });

        mUserNickNameEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        mUserNickNameEditText.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //size as per your requirement
                if (mUserNickNameEditText.length() < 3 || mUserNickNameEditText.length() > 15) {
                    setNicknameNotValid();
                } else {
//                    mUserNickNameEditText.setError("");
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable arg0) {
                String s = arg0.toString();
//                if (!s.equals(s.toLowerCase())) {
//                    s = s.toLowerCase();
//                    mUserNickNameEditText.setText(s);
                    mUserNickNameEditText.setSelection(mUserNickNameEditText.getText().length());
                //}
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_btn_done:
                onUpdateButtonClicked();
                break;

        }
    }

    private void onUpdateButtonClicked() {
        hideSoftKeyboard();

        if (mIsOnBoardFlow) {
            if (!mCbProfileDisclaimer.isChecked()) {
                showMessage(Constants.Alerts.EDIT_PROFILE_DISCLAIMER_CHECK_REQUIRED);
            } else {
                mEditProfilePresenter.onClickDone(getTrimmedText(mUserNickNameEditText), mReferralCode, true);
            }
        } else {
            mEditProfilePresenter.onClickDone(getTrimmedText(mUserNickNameEditText), mReferralCode, false);
        }
    }

    @Override
    public void setProfileImage(String imageUrl) {
        mIvProfileImage.setImageUrl(imageUrl);
    }


    @Override
    public void setNickName(String nickname) {
        mUserNickNameEditText.setText(WordUtils.capitalize(nickname));
    }

    @Override
    public void close() {
        onBackPressed();
    }

    @Override
    public void setSuccessResult() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void navigateToHome(boolean fromHome) {
        Intent intent = new Intent(this, NewChallengesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.LOGIN_SCREEN, Constants.BundleKeys.LOGIN_SCREEN);
        intent.putExtras(bundle);
//        if(fromHome) {
//            intent.putExtra(BundleKeys.OPEN_PROFILE, "0");
//        }
        startActivity(intent);
        finish();
    }

    @Override
    public void setNicknameEmpty() {
//        mEtNickName.setErrorText(Constants.Alerts.NICKNAME_EMPTY);
        TextView errorTextView = (TextView) findViewById(R.id.edit_profile_username_error_textView);
        errorTextView.setVisibility(View.VISIBLE);
    }


    @Override
    public void setNicknameConflict() {
//        mEtNickName.setErrorText(Constants.Alerts.NICKNAME_CONFLICT);
        TextView errorTextView = (TextView) findViewById(R.id.edit_profile_username_error_textView);
        errorTextView.setText(Constants.Alerts.NICKNAME_CONFLICT);
        errorTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void changeViewforProfile() {
        mToolbarTitle.setText("Update Your Profile");
        mBtnUpdateDone.setText("Update Details");

        /*mtoolbar.setNavigationIcon(R.drawable.back_icon_grey);
        mtoolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );*/
    }

    @Override
    public void changeViewforLogin(String username) {
        mToolbarTitle.setText("Complete your profile");
        mBtnUpdateDone.setText("Update Details");
    }

    @Override
    public void setNicknameNotValid() {
//        mEtNickName.setErrorText(Constants.Alerts.NICKNAME_NOT_VALID);
        TextView errorTextView = (TextView) findViewById(R.id.edit_profile_username_error_textView);
        errorTextView.setText(Constants.Alerts.NICKNAME_NOT_VALID);
        errorTextView.setVisibility(View.VISIBLE);

    }

    @Override
    public void navigateToAddPhoto(int requestCode) {
        startActivityForResult(new Intent(this, AddPhotoActivity.class), requestCode);
    }

    @Override
    public void onIncorrectReferralCode() {
        TextView incorrectReferralCode = (TextView) findViewById(R.id.edit_profile_referral_error_textView);
        incorrectReferralCode.setVisibility(View.VISIBLE);
        incorrectReferralCode.setText("The referral code is invalid.");
        mReferralCode = "";
    }

    @Override
    public void onCorrectReferralCode() {
        TextView correctReferralCode = (TextView) findViewById(R.id.edit_profile_referral_error_textView);
        correctReferralCode.setVisibility(View.VISIBLE);
        correctReferralCode.setText("Referral Code applied successfully.");
    }

    @Override
    public void navigateToSuccessfulReferral() {
        Intent intent = new Intent(this, SuccessfulReferralActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void navigateToOTPVerification(boolean successfulReferral) {
        Intent intent = new Intent(this, VerifyProfileActivity.class);
        intent.putExtra(Constants.BundleKeys.SUCCESSFUL_REFERRAL,successfulReferral);
        startActivity(intent);
        this.finish();
    }

    public void showImagePopup(View view) {
        if (new PermissionsChecker(this).lacksPermissions(AppPermissions.STORAGE)) {
            PermissionsActivity.startActivityForResult(this, RequestCodes.STORAGE_PERMISSION, AppPermissions.STORAGE);
        } else {
            mEditProfilePresenter.onClickImage();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            mEditProfilePresenter.onGetResult(requestCode, resultCode, data);
        }

        if (RequestCodes.STORAGE_PERMISSION == requestCode && PermissionsActivity.PERMISSIONS_GRANTED == resultCode) {
            mEditProfilePresenter.onClickImage();
        }
    }

    public void initToolBar() {
        mtoolbar = (Toolbar) findViewById(R.id.edit_profile_toolbar);
        mToolbarTitle = (TextView) findViewById(R.id.edit_tv);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.PROFILE_EDIT;
    }

    @Override
    public void onBackPressed() {
        if (mIsOnBoardFlow) {
            showMessage(Constants.Alerts.FORCE_UPDATE_PROFILE_MSG_FOR_PAID_VERSION);
        } else {
            super.onBackPressed();
        }
    }

    public void populateUserReferralCode(String referralCode) {
        try {
            mReferralEditText.setText(referralCode);
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
    }

    private void enterReferralCodeManually() {

        mReferralEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mReferralLabel.setTextColor(ResourcesCompat.getColor(getActivity().getResources(), R.color.white, null));
                return false;
            }

        });

        mReferralEditText.setFilters(new InputFilter[]{filter, new InputFilter.AllCaps()});

        mReferralEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 7) {
                    hideSoftKeyboard();

                    mReferralCode = s.toString();
                    mEditProfilePresenter.verifyReferralCode(mReferralCode);
                }
            }
        });

    }


}