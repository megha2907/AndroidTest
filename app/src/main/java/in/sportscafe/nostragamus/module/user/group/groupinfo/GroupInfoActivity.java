package in.sportscafe.nostragamus.module.user.group.groupinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeeva.android.widgets.customfont.CustomButton;
import com.squareup.picasso.Picasso;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.user.group.admin.adminmembers.AdminMembersActivity;
import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroupsActivity;
import in.sportscafe.nostragamus.module.user.group.editgroupinfo.EditGroupInfoActivity;
import in.sportscafe.nostragamus.module.user.group.members.MembersActivity;

/**
 * Created by Jeeva on 12/6/16.
 */
public class GroupInfoActivity extends NostragamusActivity implements GroupInfoView,
        View.OnClickListener {

    private static final int CODE_GROUP_INFO = 10;

    private static final int CODE_ADMIN_MEMBERS = 3;

    private static final int GROUPS_CODE = 20;

    private EditText mEtGroupName;

    private ImageButton mIBtnEditProfile;

    private TextView mTvGroupName;

    private TextView mEtMembersCount;

    private ImageView mIvGroupIcon;

    private RecyclerView mRvSportSelection;

    private GroupInfoPresenter mGroupInfoPresenter;

    private LinearLayoutManager mlinearLayoutManagerVertical;

    private Toolbar mtoolbar;

    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        initToolBar();

        findViewById(R.id.group_info_ll_share).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mGroupInfoPresenter.onLongClickShareCode();
                showMessage("Group code is copied to the clipboard");
                return true;
            }
        });

        this.mRvSportSelection = (RecyclerView) findViewById(R.id.group_info_rcv);
        this.mRvSportSelection.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        this.mRvSportSelection.setHasFixedSize(true);
        this.mGroupInfoPresenter = GroupInfoPresenterImpl.newInstance(this);
        this.mGroupInfoPresenter.onCreateGroupInfo(getIntent().getExtras());

        mBundle = getIntent().getExtras();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

//            case R.id.edit_profile_btn:
//                navigatetoEditGroupInfoActivity();
//                break;

            case R.id.group_info_tv_edit_members:
                mGroupInfoPresenter.onClickMembers();
                break;
            case R.id.group_info_ll_share:
                mGroupInfoPresenter.onClickShareCode();
                break;

        }
    }

    private void navigatetoEditGroupInfoActivity() {

        Intent intent = new Intent(this, EditGroupInfoActivity.class);
        Bundle mBundleNew = new Bundle();
        mBundleNew.putString(Constants.BundleKeys.GROUP_ID, mBundle.getString(Constants.BundleKeys.GROUP_ID));
        mBundleNew.putString(Constants.BundleKeys.GROUP_NAME, mBundle.getString(Constants.BundleKeys.GROUP_NAME));
        intent.putExtras(mBundleNew);
        startActivityForResult(intent, CODE_GROUP_INFO);
    }

    @Override
    public void setGroupName(String groupName) {

        if (null == groupName || groupName.isEmpty()) {
            mTvGroupName.setText("Group Info");
        } else {
            mTvGroupName.setText(groupName);
        }
    }

    @Override
    public void setGroupIcon(String groupPhotoUrl) {
        mIvGroupIcon = (ImageView) findViewById(R.id.group_iv_user_image);

        if (null == groupPhotoUrl) {

            mIvGroupIcon.setBackgroundResource(R.drawable.placeholder_icon);
        } else {
            Picasso.with(this)
                    .load(groupPhotoUrl)
                    .into(mIvGroupIcon);
        }

    }

    @Override
    public void setMembersSize(int size) {

        mEtMembersCount = (TextView) findViewById(R.id.group_info_tv_edit_members);
        mEtMembersCount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_arrow_group_info, 0);
        View greyline = (View)findViewById(R.id.group_info_view);
        greyline.setVisibility(View.VISIBLE);


        if (size == 1) {
            mEtMembersCount.setText(String.valueOf(size) + " Member");
        } else {
            mEtMembersCount.setText(String.valueOf(size) + " Members");
        }
    }

    @Override
    public void setAdapter(GroupTournamentAdapter adapter) {
        this.mRvSportSelection.setAdapter(adapter);
    }

    @Override
    public void setGroupCode(String groupCode) {
        CustomButton groupCodebtn = (CustomButton) findViewById(R.id.group_info_ll_share);
        groupCodebtn.setVisibility(View.VISIBLE);
        groupCodebtn.setTag(groupCode);
    }

    @Override
    public void showDeleteGroup() {
       // findViewById(R.id.group_info_btn_delete_group).setVisibility(View.VISIBLE);
    }

    @Override
    public void disableEdit() {
        mEtGroupName.setEnabled(false);
    }

    @Override
    public void navigateToAdminMembers(Bundle bundle) {
        Intent intent = new Intent(this, AdminMembersActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, CODE_ADMIN_MEMBERS);
    }

    @Override
    public void navigateToGroupMembers(Bundle bundle) {
        Intent intent = new Intent(this, MembersActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void navigateToHome() {
        Intent intent = new Intent(getContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (RESULT_OK == resultCode && CODE_ADMIN_MEMBERS == requestCode) {
            mGroupInfoPresenter.onGetMemberResult();
        } else if (GROUPS_CODE == requestCode) {
            mGroupInfoPresenter.onCreateGroupInfo(mBundle);
        }
    }

    @Override
    public void setSuccessResult() {
        setResult(RESULT_OK);
    }

    @Override
    public void goBackWithSuccessResult() {
        setResult(RESULT_OK);
        onBackPressed();
    }

    public void initToolBar() {
        mtoolbar = (Toolbar) findViewById(R.id.group_info_toolbar);
//        mIBtnEditProfile = (ImageButton) mtoolbar.findViewById(R.id.edit_profile_btn);
        mTvGroupName = (TextView) mtoolbar.findViewById(R.id.group_name);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mtoolbar.setNavigationIcon(R.drawable.back_icon_grey);
        mtoolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navigateToAllGroups();
                    }
                }

        );
    }

    private void navigateToAllGroups() {
        Intent intent = new Intent(this, AllGroupsActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.edit_group_btn:
                navigatetoEditGroupInfoActivity();
                break;
            case R.id.leave_group_btn:
                 mGroupInfoPresenter.onLeaveGroup();
                break;
        }
        return true;
    }
}