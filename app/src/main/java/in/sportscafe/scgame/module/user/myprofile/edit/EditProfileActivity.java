package in.sportscafe.scgame.module.user.myprofile.edit;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeeva.android.widgets.customfont.CustomButton;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.Constants.AnalyticsActions;
import in.sportscafe.scgame.Constants.AnalyticsLabels;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.analytics.ScGameAnalytics;
import in.sportscafe.scgame.module.common.ScGameActivity;
import in.sportscafe.scgame.module.home.HomeActivity;
import in.sportscafe.scgame.module.permission.PermissionsActivity;
import in.sportscafe.scgame.module.permission.PermissionsChecker;
import in.sportscafe.scgame.module.user.sportselection.SportSelectionActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

//import com.squareup.picasso.Picasso;

/**
 * Created by Jeeva on 12/6/16.
 */
public class EditProfileActivity extends ScGameActivity implements EditProfileView,
        View.OnClickListener {


    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private EditText mEtName;
    PermissionsChecker checker;
    private String imagePath;
    private EditText mEtNickName;
    private TextInputLayout mTilNickName;
    private ImageView mIvProfileImage;
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
        mIvProfileImage = (ImageView) findViewById(R.id.edit_iv_user_image);
        mBtnUpdateDone = (CustomButton) findViewById(R.id.edit_btn_done);

        this.mEditProfilePresenter = EditProfilePresenterImpl.newInstance(this);
        this.mEditProfilePresenter.onCreateEditProfile(getIntent().getExtras());
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

        Picasso.with(this)
                .load(imageUrl)
                .into(mIvProfileImage);
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
        mTvUpdateProfile.setText("Welcome "+username+"\n"+"Let’s update your profile.");
        mBtnUpdateDone.setText("NEXT");
    }

    public void showImagePopup(View view) {
        if (checker.lacksPermissions(PERMISSIONS_READ_STORAGE)) {
            startPermissionsActivity(PERMISSIONS_READ_STORAGE);
        } else {
            // File System.
            final Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_PICK);

            // Chooser of file system options.
            final Intent chooserIntent = Intent.createChooser(galleryIntent, Constants.Alerts.IMAGE_UPLOAD_TEXT);
            startActivityForResult(chooserIntent, 1010);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK ) {

                if (requestCode == 1010){

                if (data == null) {
                    Toast.makeText(getActivity(), Constants.Alerts.IMAGE_UNABLE_TO_PICK,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                Uri selectedImageUri = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);

                if (cursor != null) {
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imagePath = cursor.getString(columnIndex);

                    Picasso.with(getApplicationContext()).load(new File(imagePath))
                            .into(mIvProfileImage);
                    cursor.close();

                    if (!TextUtils.isEmpty(imagePath)) {
                        uploadImage();
                    }

                    mIvProfileImage.setVisibility(View.VISIBLE);

                    ScGameAnalytics.getInstance().trackEditProfile(AnalyticsActions.PHOTO, AnalyticsLabels.GALLERY);
                } else {

                    mIvProfileImage.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), Constants.Alerts.IMAGE_UNABLE_TO_LOAD,
                            Toast.LENGTH_SHORT).show();
                }
            }
            }

        } catch (Exception e) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT)
                        .show();
            }
    }


    private void startPermissionsActivity(String[] permission) {
        PermissionsActivity.startActivityForResult(this, 0, permission);
    }


    private void uploadImage() {

        //File creating from selected URL
        File file = new File(imagePath);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        // add another part within the multipart request
        String Serverfilepath = "game/profileimage/";
        RequestBody filepath =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), Serverfilepath);

        String ServerfileName = file.getName();
        RequestBody filename =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), ServerfileName);


        mEditProfilePresenter.onProfilePhotoDone(body,filepath,filename);

    }




}