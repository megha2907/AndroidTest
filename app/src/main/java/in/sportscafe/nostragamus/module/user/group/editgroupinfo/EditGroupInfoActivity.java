package in.sportscafe.nostragamus.module.user.group.editgroupinfo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeeva.android.widgets.HmImageView;
import com.squareup.picasso.Picasso;

import java.io.File;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsChecker;
import in.sportscafe.nostragamus.module.user.group.groupinfo.GroupInfoActivity;
import in.sportscafe.nostragamus.module.user.group.newgroup.GrpTournamentSelectionAdapter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by deepanshi on 10/31/16.
 */


public class EditGroupInfoActivity extends NostragamusActivity implements EditGroupInfoView,
        View.OnClickListener {

    private static final int CODE_GROUP_INFO = 10;

    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    PermissionsChecker checker;

    private String imagePath;

    private HmImageView mIvGroupImage;

    private static final int CODE_ADMIN_MEMBERS = 3;

    private EditText mEtGroupName;

    private ImageButton mIBtnEditName;

    private TextView mEtMembersCount;

    private Button mBtnGroupIcon;

    private RecyclerView mRvSportSelection;

    private EditGroupInfoPresenter mEditGroupInfoPresenter;

    private LinearLayoutManager mlinearLayoutManagerVertical;

    private Toolbar mtoolbar;

    private Boolean editGroupName = false;

    private Bundle mBundle;

    private String mPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group_info);

        initToolBar();
        mBundle=getIntent().getExtras();
        mBtnGroupIcon = (Button) findViewById(R.id.group_info_iv_user_image);
        mIvGroupImage = (HmImageView) findViewById(R.id.group_iv_user_image);

        checker = new PermissionsChecker(this);

        mEtGroupName = (EditText) findViewById(R.id.group_info_et_name);

        mIBtnEditName = (ImageButton) findViewById(R.id.group_info_btn_edit_name);

        this.mRvSportSelection = (RecyclerView) findViewById(R.id.group_info_selected_tournaments_rcv);
        this.mRvSportSelection.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        this.mRvSportSelection.setHasFixedSize(true);
        this.mEditGroupInfoPresenter = EditGroupInfoPresenterImpl.newInstance(this);
        this.mEditGroupInfoPresenter.onCreateGroupInfo(getIntent().getExtras());

        mEtGroupName.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                editGroupName = true;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.save_btn:

                if (editGroupName)
                {
                    mEditGroupInfoPresenter.onDoneGroupName(getTrimmedText(mEtGroupName),mPhoto);
                    setResult(RESULT_OK);
                }

                break;

            case R.id.group_info_btn_edit_name:
                mEtGroupName.setEnabled(true);
                mIBtnEditName.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void setGroupName(String groupName) {
        mEtGroupName.setText(groupName);
        mBtnGroupIcon.setText(groupName.substring(0,1));
    }



    @Override
    public void setAdapter(GrpTournamentSelectionAdapter adapter) {
        this.mRvSportSelection.setAdapter(adapter);
    }

    private void navigatetoGroupInfoActivity() {

        Intent intent =  new Intent(this, GroupInfoActivity.class);
        Bundle mBundleNew = new Bundle();
        mBundleNew.putString(Constants.BundleKeys.GROUP_ID, mBundle.getString(Constants.BundleKeys.GROUP_ID));
        mBundleNew.putString(Constants.BundleKeys.GROUP_NAME, mBundle.getString(Constants.BundleKeys.GROUP_NAME));
        intent.putExtras(mBundleNew);
        startActivityForResult(intent,CODE_GROUP_INFO);
    }



    @Override
    public void navigateToHome() {
        Intent intent = new Intent(getContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void disableEdit() {

    }

    @Override
    public void setGroupIcon(String groupIcon) {

        mBtnGroupIcon.setText(groupIcon);

    }

    @Override
    public void goBackWithSuccessResult() {
        setResult(RESULT_OK);
        onBackPressed();
        finish();
    }


    @Override
    public void setSuccessResult() {
        setResult(RESULT_OK);
    }

    public void initToolBar() {
        mtoolbar = (Toolbar) findViewById(R.id.group_info_toolbar);
        mtoolbar.setTitle("Edit Group");
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationIcon(R.drawable.back_icon_grey);
        mtoolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goBackWithSuccessResult();
                    }
                }

        );
    }



    @Override
    public void setGroupImage(String imageUrl) {

        mPhoto = imageUrl;

        if (null==imageUrl) {

            mIvGroupImage.setBackgroundResource(R.drawable.placeholder_icon);
        }
        else
        {
            mIvGroupImage.setImageUrl(imageUrl);

        }

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

    private void startPermissionsActivity(String[] permission) {
        PermissionsActivity.startActivityForResult(this, 0, permission);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(RESULT_OK == resultCode && CODE_ADMIN_MEMBERS == requestCode) {
                mEditGroupInfoPresenter.onGetMemberResult();
            }

           else if (resultCode == Activity.RESULT_OK ) {

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

                        File file = new File(imagePath);
                        long length = file.length() / 10240;

                        if(length < 10240){
                            if (!TextUtils.isEmpty(imagePath)) {
                                uploadImage();
                            }

                            Picasso.with(getApplicationContext()).load(new File(imagePath))
                                    .into(mIvGroupImage);

                            cursor.close();

                            mIvGroupImage.setVisibility(View.VISIBLE);

                        }
                        else
                        {
                            Toast toast =Toast.makeText(getContext(), "Image size is too large, please select an image with size <10MB", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }

                    } else {

                        mIvGroupImage.setVisibility(View.GONE);
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

    private void uploadImage() {

        //File creating from selected URL
        File file = new File(imagePath);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        // add another part within the multipart request
        String Serverfilepath = "game/groupimages/";
        RequestBody filepath =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), Serverfilepath);

        String ServerfileName = file.getName();
        RequestBody filename =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), ServerfileName);


        mEditGroupInfoPresenter.onGroupPhotoDone(file, Serverfilepath, ServerfileName);

    }


}