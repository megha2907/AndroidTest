package in.sportscafe.nostragamus.module.navigation.edituserprofile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.customfont.CustomButton;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.addphoto.AddPhotoActivity;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.common.WordUtils;
import in.sportscafe.nostragamus.module.nostraHome.NostraHomeActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsChecker;

/**
 * Created by deepanshi on 8/21/17.
 */
public class EditUserProfileActivity extends NostragamusActivity implements EditUserProfileView,
        View.OnClickListener {


    private PermissionsChecker checker;
    private HmImageView mIvProfileImage;
    private EditUserProfilePresenter mEditProfilePresenter;
    private EditText mUserNickNameEditText;
    private TextView userNickNameLabel;
    private CustomButton mBtnUpdateDone;

    private final String blockCharacterSet = "~#^|$%&*!@_-+/:;!?";

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        initView();
    }

    private void initView() {
        checker = new PermissionsChecker(this);
        mUserNickNameEditText = (EditText) findViewById(R.id.edit_et_nickname_new);
        userNickNameLabel = (TextView) findViewById(R.id.edit_tv_nickname_label);
        mIvProfileImage = (HmImageView) findViewById(R.id.edit_iv_user_image);
        mBtnUpdateDone = (CustomButton) findViewById(R.id.edit_btn_done);
        findViewById(R.id.edit_btn_back).setOnClickListener(this);

        this.mEditProfilePresenter = EditUserProfilePresenterImpl.newInstance(this);
        this.mEditProfilePresenter.onCreateEditProfile();

        initListener();
        setInfo();
    }

    private void setInfo() {
        if (!TextUtils.isEmpty(mUserNickNameEditText.getText())) {
            String editName = mUserNickNameEditText.getText().toString();
            mUserNickNameEditText.setSelection(editName.length());
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
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable arg0) {
                mUserNickNameEditText.setSelection(mUserNickNameEditText.getText().length());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_btn_done:
                onUpdateButtonClicked();
                break;
            case R.id.edit_btn_back:
                finish();
                break;

        }
    }

    private void onUpdateButtonClicked() {
        hideSoftKeyboard();
        mEditProfilePresenter.onClickDone(getTrimmedText(mUserNickNameEditText));
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
        Intent intent = new Intent(this, NostraHomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BundleKeys.LOGIN_SCREEN, Constants.BundleKeys.LOGIN_SCREEN);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void setNicknameEmpty() {
        TextView errorTextView = (TextView) findViewById(R.id.edit_profile_username_error_textView);
        errorTextView.setVisibility(View.VISIBLE);
    }


    @Override
    public void setNicknameConflict() {
        TextView errorTextView = (TextView) findViewById(R.id.edit_profile_username_error_textView);
        errorTextView.setText(Constants.Alerts.NICKNAME_CONFLICT);
        errorTextView.setVisibility(View.VISIBLE);
    }


    @Override
    public void setNicknameNotValid() {
        TextView errorTextView = (TextView) findViewById(R.id.edit_profile_username_error_textView);
        errorTextView.setText(Constants.Alerts.NICKNAME_NOT_VALID);
        errorTextView.setVisibility(View.VISIBLE);

    }

    @Override
    public void navigateToAddPhoto(int requestCode) {
        startActivityForResult(new Intent(this, AddPhotoActivity.class), requestCode);
    }


    public void showImagePopup(View view) {
        if (new PermissionsChecker(this).lacksPermissions(Constants.AppPermissions.STORAGE)) {
            PermissionsActivity.startActivityForResult(this, Constants.RequestCodes.STORAGE_PERMISSION, Constants.AppPermissions.STORAGE);
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

        if (Constants.RequestCodes.STORAGE_PERMISSION == requestCode && PermissionsActivity.PERMISSIONS_GRANTED == resultCode) {
            mEditProfilePresenter.onClickImage();
        }
    }


    @Override
    public String getScreenName() {
        return Constants.ScreenNames.EDIT_USER_PROFILE;
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }

}