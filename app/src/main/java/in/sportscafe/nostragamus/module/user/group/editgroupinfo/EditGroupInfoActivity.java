package in.sportscafe.nostragamus.module.user.group.editgroupinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.jeeva.android.widgets.CustomProgressbar;
import com.jeeva.android.widgets.HmImageView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AppPermissions;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.addphoto.AddPhotoActivity;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsChecker;

/**
 * Created by deepanshi on 10/31/16.
 */
public class EditGroupInfoActivity extends NostragamusActivity implements EditGroupInfoView,
        View.OnClickListener {

    private EditText mEtGroupName;

    private EditGroupInfoPresenter mEditGroupInfoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group_info);

        initToolBar();

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
    public void setSuccessData(Bundle bundle) {
        setResult(RESULT_OK, new Intent().putExtras(bundle));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mEditGroupInfoPresenter.onGetResult(requestCode, resultCode, data);
    }

    public void showImagePopup(View view) {
        if(new PermissionsChecker(this).lacksPermissions(AppPermissions.STORAGE)) {
            PermissionsActivity.startActivityForResult(this, 0, AppPermissions.STORAGE);
        } else {
            mEditGroupInfoPresenter.onClickImage();
        }
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