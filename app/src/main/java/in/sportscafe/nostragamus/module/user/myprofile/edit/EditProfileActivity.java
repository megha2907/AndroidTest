package in.sportscafe.nostragamus.module.user.myprofile.edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.customfont.CustomButton;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AppPermissions;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.RequestCodes;
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

        if (!TextUtils.isEmpty(mEtNickName.getEditText().getText())) {
            String editName = mEtNickName.getEditText().getText().toString();
            mEtNickName.getEditText().setSelection(editName.length());
        }

        initOnBoardFlow();

    }

    /**
     * If only on-board flow, and only paid version, then show age-disclaimer; else not
     */
    private void initOnBoardFlow() {
        Intent thisIntent = getIntent();
        if (thisIntent != null && thisIntent.hasExtra(Constants.BundleKeys.EDIT_PROFILE_LAUNCHED_FROM)) {
            int thisLaunchedFrom = thisIntent.getIntExtra(Constants.BundleKeys.EDIT_PROFILE_LAUNCHED_FROM, -1);
            if (thisLaunchedFrom == ILaunchedFrom.HOME_ACTIVITY) {

                if (BuildConfig.IS_PAID_VERSION) {
                    mIsOnBoardFlow = true;
                    mCbProfileDisclaimer.setVisibility(View.VISIBLE);
                    mCbProfileDisclaimer.setText(String.valueOf(NostragamusDataHandler.getInstance().getDisclaimerText()));
                }
            }
        }
    }

    private void initListener() {

        mEtNickName.getEditText().addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //size as per your requirement
                if (mEtNickName.getEditText().length() < 3 || mEtNickName.getEditText().length() > 15){
                    setNicknameNotValid();
                }else {
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
                mEditProfilePresenter.onClickDone(getTrimmedText(mEtNickName.getEditText()),true);
            }
        } else {
            mEditProfilePresenter.onClickDone(getTrimmedText(mEtNickName.getEditText()),false);
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
        if(RESULT_OK == resultCode) {
            mEditProfilePresenter.onGetResult(requestCode, resultCode, data);
        }

        if(RequestCodes.STORAGE_PERMISSION == requestCode && PermissionsActivity.PERMISSIONS_GRANTED == resultCode) {
            mEditProfilePresenter.onClickImage();
        }
    }

    public void initToolBar() {
        mtoolbar = (Toolbar) findViewById(R.id.edit_profile_toolbar);
        mToolbarTitle = (TextView)findViewById(R.id.edit_tv);
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
}