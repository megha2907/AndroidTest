package in.sportscafe.nostragamus.module.user.group.editgroupinfo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.HmImageView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.addphoto.AddPhotoActivity;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsChecker;
import in.sportscafe.nostragamus.module.popups.GetScreenNameListener;
import in.sportscafe.nostragamus.module.user.group.groupinfo.GroupInfoActivity;

/**
 * Created by deepanshi on 10/31/16.
 */
public class EditGroupInfoActivity extends NostragamusActivity implements EditGroupInfoView,
        View.OnClickListener{

    private static final int CODE_GROUP_INFO = 10;

    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    PermissionsChecker checker;

    private HmImageView mIvGroupImage;

    private static final int CODE_ADMIN_MEMBERS = 3;

    private EditText mEtGroupName;

    private ImageButton mIBtnEditName;

    private Button mBtnGroupIcon;

    private EditGroupInfoPresenter mEditGroupInfoPresenter;

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

        this.mEditGroupInfoPresenter = EditGroupInfoPresenterImpl.newInstance(this);
        this.mEditGroupInfoPresenter.onCreateGroupInfo(getIntent().getExtras());

        mEtGroupName.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                editGroupName = true;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.save_btn:

                   Log.i("name,photo",getTrimmedText(mEtGroupName)+""+mPhoto);
                    mEditGroupInfoPresenter.onDoneGroupName(getTrimmedText(mEtGroupName),mPhoto);
                    //setResult(RESULT_OK);

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


    private void navigatetoGroupInfoActivity() {

        Intent intent =  new Intent(this, GroupInfoActivity.class);
        Bundle mBundleNew = new Bundle();
        mBundleNew.putInt(Constants.BundleKeys.GROUP_ID, mBundle.getInt(Constants.BundleKeys.GROUP_ID));
        mBundleNew.putString(Constants.BundleKeys.GROUP_NAME, mBundle.getString(Constants.BundleKeys.GROUP_NAME));
        mBundleNew.putString(Constants.BundleKeys.SCREEN,Constants.ScreenNames.GROUPS_EDIT_GROUP);
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
        navigatetoGroupInfoActivity();
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
        mIvGroupImage.setImageUrl(imageUrl);
    }

    @Override
    public void navigateToAddPhoto(int addPhotoRequestCode) {
        startActivityForResult(new Intent(this, AddPhotoActivity.class), addPhotoRequestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mEditGroupInfoPresenter.onGetResult(requestCode, resultCode, data);
    }

    public void showImagePopup(View view) {
        if (checker.lacksPermissions(PERMISSIONS_READ_STORAGE)) {
            startPermissionsActivity(PERMISSIONS_READ_STORAGE);
        } else {
            mEditGroupInfoPresenter.onClickImage();
        }
    }

    private void startPermissionsActivity(String[] permission) {
        PermissionsActivity.startActivityForResult(this, 0, permission);
    }


    @Override
    public String getScreenName() {
        return Constants.ScreenNames.GROUPS_EDIT_GROUP;
    }
}