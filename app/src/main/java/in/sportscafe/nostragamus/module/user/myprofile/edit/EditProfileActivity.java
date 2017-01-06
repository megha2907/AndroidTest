package in.sportscafe.nostragamus.module.user.myprofile.edit;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.customfont.CustomButton;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AnalyticsActions;
import in.sportscafe.nostragamus.Constants.AnalyticsLabels;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.addphoto.AddPhotoActivity;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.ImageHandler;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsChecker;
import in.sportscafe.nostragamus.module.user.sportselection.SportSelectionActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

//import com.squareup.picasso.Picasso;

/**
 * Created by Jeeva on 12/6/16.
 */
public class EditProfileActivity extends NostragamusActivity implements EditProfileView,
        View.OnClickListener {

    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private EditText mEtName;

    private PermissionsChecker checker;

    private EditText mEtNickName;

    private TextInputLayout mTilNickName;

    private HmImageView mIvProfileImage;

    private EditProfilePresenter mEditProfilePresenter;

    private TextView mTvUpdateProfile;

    private CustomButton mBtnUpdateDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        checker = new PermissionsChecker(this);

        mTilNickName = (TextInputLayout) findViewById(R.id.input_layout_edit_et_nickname);
        mTvUpdateProfile = (TextView) findViewById(R.id.edit_tv);
        mEtNickName = (EditText) findViewById(R.id.edit_et_nickname);
        mIvProfileImage = (HmImageView) findViewById(R.id.edit_iv_user_image);
        mBtnUpdateDone = (CustomButton) findViewById(R.id.edit_btn_done);
        initListener();

        this.mEditProfilePresenter = EditProfilePresenterImpl.newInstance(this);
        this.mEditProfilePresenter.onCreateEditProfile(getIntent().getExtras());
    }


    private void initListener() {
        mEtNickName.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //size as per your requirement
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable arg0) {
                String s = arg0.toString();
                if (!s.equals(s.toLowerCase())) {
                    s = s.toLowerCase();
                    mEtNickName.setText(s);
                    mEtNickName.setSelection(mEtNickName.getText().length());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_btn_done:
                mEditProfilePresenter.onClickDone(
                        getTrimmedText(mEtNickName)

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
        mEtNickName.setText(nickname);
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
    public void navigateToHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void navigateToSportsSelection() {
        startActivity(new Intent(this, SportSelectionActivity.class));
        finish();
    }

    @Override
    public void setNicknameEmpty() {
        mTilNickName.setErrorEnabled(true);
        mTilNickName.setError(Constants.Alerts.NICKNAME_EMPTY);
    }


    @Override
    public void setNicknameConflict() {
        mTilNickName.setErrorEnabled(true);
        mTilNickName.setError(Constants.Alerts.NICKNAME_CONFLICT);
    }

    @Override
    public void changeViewforProfile() {
        mTvUpdateProfile.setText("Update your profile here.");
        mBtnUpdateDone.setText("UPDATE");
    }

    @Override
    public void changeViewforLogin(String username) {
        mTvUpdateProfile.setText("Welcome " + username + "\n" + "Let’s update your profile.");
        mBtnUpdateDone.setText("NEXT");
    }

    @Override
    public void setNicknameNotValid() {
        mTilNickName.setErrorEnabled(true);
        mTilNickName.setError(Constants.Alerts.NICKNAME_NOT_VALID);
    }

    @Override
    public void navigateToAddPhoto(int requestCode) {
        startActivityForResult(new Intent(this, AddPhotoActivity.class), requestCode);
    }

    public void showImagePopup(View view) {
        if (checker.lacksPermissions(PERMISSIONS_READ_STORAGE)) {
            startPermissionsActivity(PERMISSIONS_READ_STORAGE);
        } else {
            mEditProfilePresenter.onClickImage();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mEditProfilePresenter.onGetResult(requestCode, resultCode, data);
    }

    private void startPermissionsActivity(String[] permission) {
        PermissionsActivity.startActivityForResult(this, 0, permission);
    }
}