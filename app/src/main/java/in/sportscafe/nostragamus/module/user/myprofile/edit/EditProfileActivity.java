package in.sportscafe.nostragamus.module.user.myprofile.edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.customfont.CustomButton;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AppPermissions;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.RequestCodes;
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

    private EditText mEtName;

    private PermissionsChecker checker;

    private Toolbar mtoolbar;

    private TextView mToolbarTitle;

    private NostraEditText mEtNickName;

    private TextView mTilNickName;

    private RoundImage mIvProfileImage;

    private EditProfilePresenter mEditProfilePresenter;

    private TextView mTvUpdateProfile;

    private CustomButton mBtnUpdateDone;

    private ImageButton mBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        checker = new PermissionsChecker(this);

        initToolBar();

        mTilNickName = (TextView) findViewById(R.id.edit_tv_error_txt);
        mTvUpdateProfile = (TextView) findViewById(R.id.edit_tv);
        mEtNickName = (NostraEditText) findViewById(R.id.edit_et_nickname_new);
        mIvProfileImage = (RoundImage) findViewById(R.id.edit_iv_user_image);
        mBtnUpdateDone = (CustomButton) findViewById(R.id.edit_btn_done);
        initListener();
        this.mEditProfilePresenter = EditProfilePresenterImpl.newInstance(this);
        this.mEditProfilePresenter.onCreateEditProfile(getIntent().getExtras());

        if (!TextUtils.isEmpty(mEtNickName.getEditText().getText())) {
            String editName = mEtNickName.getEditText().getText().toString();
            mEtNickName.getEditText().setSelection(editName.length());
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
                mEditProfilePresenter.onClickDone(
                        getTrimmedText(mEtNickName.getEditText())

                );
                break;

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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        if(fromHome) {
            intent.putExtra(BundleKeys.OPEN_PROFILE, "0");
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void setNicknameEmpty() {
        //mTilNickName.setVisibility(View.VISIBLE);
        mEtNickName.setErrorText(Constants.Alerts.NICKNAME_EMPTY);
    }


    @Override
    public void setNicknameConflict() {
       // mTilNickName.setVisibility(View.VISIBLE);
        mEtNickName.setErrorText(Constants.Alerts.NICKNAME_CONFLICT);
    }

    @Override
    public void changeViewforProfile() {
        mToolbarTitle.setText("Profile");
        mBtnUpdateDone.setText("UPDATE");
        mtoolbar.setNavigationIcon(R.drawable.back_icon_grey);
        mtoolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }

    @Override
    public void changeViewforLogin(String username) {
        mToolbarTitle.setText("Complete your profile");
        mBtnUpdateDone.setText("NEXT");
    }

    @Override
    public void setNicknameNotValid() {
       // mTilNickName.setVisibility(View.VISIBLE);
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
}