package in.sportscafe.nostragamus.module.user.myprofile.edit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeeva.android.ExceptionTracker;
import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.customfont.CustomButton;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AppPermissions;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.RequestCodes;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.addphoto.AddPhotoActivity;
import in.sportscafe.nostragamus.module.common.NostraEditText;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.common.RoundImage;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsChecker;

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

    private EditText mEtName;

    private PermissionsChecker checker;

    private Toolbar mtoolbar;

    private TextView mToolbarTitle;

    private NostraEditText mEtNickName;

    private HmImageView mIvProfileImage;

    private CheckBox mCbProfileDisclaimer;

    private TextView mTvEmail;

    private EditProfilePresenter mEditProfilePresenter;

    private TextView mTvUpdateProfile;

    private CustomButton mBtnUpdateDone;

    private ImageButton mBackBtn;

    private String blockCharacterSet = "~#^|$%&*!@_-+/:;!?";

    private EditText mEtReferralCode1;
    private EditText mEtReferralCode2;
    private EditText mEtReferralCode3;
    private EditText mEtReferralCode4;
    private EditText mEtReferralCode5;
    private EditText mEtReferralCode6;
    private EditText mEtReferralCode7;

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

        mTvUpdateProfile = (TextView) findViewById(R.id.edit_tv);
        mEtNickName = (NostraEditText) findViewById(R.id.edit_et_nickname_new);
        mIvProfileImage = (HmImageView) findViewById(R.id.edit_iv_user_image);
        mBtnUpdateDone = (CustomButton) findViewById(R.id.edit_btn_done);
        mCbProfileDisclaimer = (CheckBox) findViewById(R.id.edit_disclaimer_checkbox);
        initListener();
        this.mEditProfilePresenter = EditProfilePresenterImpl.newInstance(this);
        this.mEditProfilePresenter.onCreateEditProfile(getIntent().getExtras());

        initOnBoardFlow();
        initReferralCode();

        if (!TextUtils.isEmpty(mEtNickName.getEditText().getText())) {
            String editName = mEtNickName.getEditText().getText().toString();
            mEtNickName.getEditText().setSelection(editName.length());
        }

    }

    private void initReferralCode() {
        mEtReferralCode1 = (EditText) findViewById(R.id.edit_profile_et_referral_code_char_one);
        mEtReferralCode2 = (EditText) findViewById(R.id.edit_profile_et_referral_code_char_two);
        mEtReferralCode3 = (EditText) findViewById(R.id.edit_profile_et_referral_code_char_three);
        mEtReferralCode4 = (EditText) findViewById(R.id.edit_profile_et_referral_code_char_four);
        mEtReferralCode5 = (EditText) findViewById(R.id.edit_profile_et_referral_code_char_five);
        mEtReferralCode6 = (EditText) findViewById(R.id.edit_profile_et_referral_code_char_six);
        mEtReferralCode7 = (EditText) findViewById(R.id.edit_profile_et_referral_code_char_seven);

        if (!TextUtils.isEmpty(NostragamusDataHandler.getInstance().getUserReferralCode())) {
            populateUserReferralCode(NostragamusDataHandler.getInstance().getUserReferralCode());
            mReferralCode = NostragamusDataHandler.getInstance().getUserReferralCode();
            NoEditingReferralCode();
        } else {
            enterReferralCodeManually();
        }

    }

    private void NoEditingReferralCode() {
        mEtReferralCode1.setEnabled(false);
        mEtReferralCode2.setEnabled(false);
        mEtReferralCode3.setEnabled(false);
        mEtReferralCode4.setEnabled(false);
        mEtReferralCode5.setEnabled(false);
        mEtReferralCode6.setEnabled(false);
        mEtReferralCode7.setEnabled(false);
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

        mEtNickName.getEditText().addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //size as per your requirement
                if (mEtNickName.getEditText().length() < 3 || mEtNickName.getEditText().length() > 15) {
                    setNicknameNotValid();
                } else {
                    mEtNickName.setErrorText("");
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable arg0) {
                String s = arg0.toString();
                if (!s.equals(s.toLowerCase())) {
                    s = s.toLowerCase();
                    mEtNickName.getEditText().setText(s);
                    mEtNickName.getEditText().setSelection(mEtNickName.getEditText().getText().length());
                }
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
                mEditProfilePresenter.onClickDone(getTrimmedText(mEtNickName.getEditText()), mReferralCode, true);
            }
        } else {
            mEditProfilePresenter.onClickDone(getTrimmedText(mEtNickName.getEditText()), mReferralCode, false);
        }
    }

    @Override
    public void setProfileImage(String imageUrl) {
        mIvProfileImage.setImageUrl(imageUrl);
    }


    @Override
    public void setNickName(String nickname) {
        mEtNickName.getEditText().setText(nickname);
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
        Intent intent = new Intent(this, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.LOGIN_SCREEN, Constants.BundleKeys.LOGIN_SCREEN);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        if(fromHome) {
//            intent.putExtra(BundleKeys.OPEN_PROFILE, "0");
//        }
        startActivity(intent);
        finish();
    }

    @Override
    public void setNicknameEmpty() {
        mEtNickName.setErrorText(Constants.Alerts.NICKNAME_EMPTY);
    }


    @Override
    public void setNicknameConflict() {
        mEtNickName.setErrorText(Constants.Alerts.NICKNAME_CONFLICT);
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
        mBtnUpdateDone.setText("NEXT");
    }

    @Override
    public void setNicknameNotValid() {
        mEtNickName.setErrorText(Constants.Alerts.NICKNAME_NOT_VALID);
    }

    @Override
    public void navigateToAddPhoto(int requestCode) {
        startActivityForResult(new Intent(this, AddPhotoActivity.class), requestCode);
    }

    @Override
    public void onIncorrectReferralCode() {
        TextView incorrectReferralCode = (TextView) findViewById(R.id.edit_profile_referral_code_error_text);
        incorrectReferralCode.setVisibility(View.VISIBLE);
        incorrectReferralCode.setText("The referral code is invalid.");
        mReferralCode = "";
    }

    @Override
    public void onCorrectReferralCode() {
        TextView correctReferralCode = (TextView) findViewById(R.id.edit_profile_referral_code_error_text);
        correctReferralCode.setVisibility(View.VISIBLE);
        correctReferralCode.setText("Referral Code applied successfully.");
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
            String[] codeSplitter = referralCode.split("");
            mEtReferralCode1.setText(codeSplitter[1]);
            mEtReferralCode2.setText(codeSplitter[2]);
            mEtReferralCode3.setText(codeSplitter[3]);
            mEtReferralCode4.setText(codeSplitter[4]);
            mEtReferralCode5.setText(codeSplitter[5]);
            mEtReferralCode6.setText(codeSplitter[6]);
            mEtReferralCode7.setText(codeSplitter[7]);
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
    }

    private void enterReferralCodeManually() {

        mEtReferralCode1.setFilters(new InputFilter[]{filter, new InputFilter.AllCaps()});
        mEtReferralCode2.setFilters(new InputFilter[]{filter, new InputFilter.AllCaps()});
        mEtReferralCode3.setFilters(new InputFilter[]{filter, new InputFilter.AllCaps()});
        mEtReferralCode4.setFilters(new InputFilter[]{filter, new InputFilter.AllCaps()});
        mEtReferralCode5.setFilters(new InputFilter[]{filter, new InputFilter.AllCaps()});
        mEtReferralCode6.setFilters(new InputFilter[]{filter, new InputFilter.AllCaps()});
        mEtReferralCode7.setFilters(new InputFilter[]{filter, new InputFilter.AllCaps()});

        mEtReferralCode1.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.length() == 1) {
                    mEtReferralCode2.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });

        mEtReferralCode2.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    mEtReferralCode3.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });
        mEtReferralCode3.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    mEtReferralCode4.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });

        mEtReferralCode4.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    mEtReferralCode5.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });

        mEtReferralCode5.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    mEtReferralCode6.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });

        mEtReferralCode6.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    mEtReferralCode7.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });

        mEtReferralCode7.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {

                    hideSoftKeyboard();

                    mReferralCode = getTrimmedText(mEtReferralCode1) + getTrimmedText(mEtReferralCode2)
                            + getTrimmedText(mEtReferralCode3) + getTrimmedText(mEtReferralCode4)
                            + getTrimmedText(mEtReferralCode5) + getTrimmedText(mEtReferralCode6)
                            + getTrimmedText(mEtReferralCode7);

                    mEditProfilePresenter.verifyReferralCode(mReferralCode);
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });

    }


}