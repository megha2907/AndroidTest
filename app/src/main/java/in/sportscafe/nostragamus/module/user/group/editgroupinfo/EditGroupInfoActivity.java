package in.sportscafe.nostragamus.module.user.group.editgroupinfo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.jeeva.android.widgets.CustomProgressbar;
import com.jeeva.android.widgets.HmImageView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.addphoto.AddPhotoActivity;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsChecker;
import in.sportscafe.nostragamus.module.user.group.groupinfo.GroupInfoActivity;

/**
 * Created by deepanshi on 10/31/16.
 */
public class EditGroupInfoActivity extends NostragamusActivity implements EditGroupInfoView,
        View.OnClickListener {

    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private PermissionsChecker mPermissionChecker;

    private EditText mEtGroupName;

    private EditGroupInfoPresenter mEditGroupInfoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group_info);

        initToolBar();

        mPermissionChecker = new PermissionsChecker(this);

        mEtGroupName = (EditText) findViewById(R.id.group_info_et_name);

        this.mEditGroupInfoPresenter = EditGroupInfoPresenterImpl.newInstance(this);
        this.mEditGroupInfoPresenter.onCreateGroupInfo(getIntent().getExtras());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_btn:
                mEditGroupInfoPresenter.onSaveGroupName(getTrimmedText(mEtGroupName));
                break;
        }
    }

    private void navigatetoGroupInfoActivity() {
        Intent intent = new Intent(this, GroupInfoActivity.class);
        Bundle mBundleNew = new Bundle();
        mBundleNew.putInt(Constants.BundleKeys.GROUP_ID, mBundle.getInt(Constants.BundleKeys.GROUP_ID));
        mBundleNew.putString(Constants.BundleKeys.GROUP_NAME, mBundle.getString(Constants.BundleKeys.GROUP_NAME));
        mBundleNew.putString(Constants.BundleKeys.SCREEN, Constants.ScreenNames.GROUPS_EDIT_GROUP);
        intent.putExtras(mBundleNew);
        startActivityForResult(intent, CODE_GROUP_INFO);
    }

    @Override
    public void setGroupName(String groupName) {
        mEtGroupName.setText(groupName);
    }

    public void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.group_info_toolbar);
        toolbar.setTitle("Edit Group");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_icon_grey);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mEditGroupInfoPresenter.onClickBack();
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        mEditGroupInfoPresenter.onClickBack();
    }

    @Override
    public void setGroupImage(String imageUrl) {
        ((HmImageView) findViewById(R.id.group_iv_user_image)).setImageUrl(imageUrl);
    }

    @Override
    public void navigateToAddPhoto(int addPhotoRequestCode) {
        startActivityForResult(new Intent(this, AddPhotoActivity.class), addPhotoRequestCode);
    }

    @Override
    public void goBack() {
        super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mEditGroupInfoPresenter.onGetResult(requestCode, resultCode, data);
    }

    public void showImagePopup(View view) {
        if (mPermissionChecker.lacksPermissions(PERMISSIONS_READ_STORAGE)) {
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

    @Override
    public void showProgressbar() {
        CustomProgressbar.getProgressbar(this).show();
    }

    @Override
    public boolean dismissProgressbar() {
        return CustomProgressbar.getProgressbar(this).dismissProgress();
    }
}