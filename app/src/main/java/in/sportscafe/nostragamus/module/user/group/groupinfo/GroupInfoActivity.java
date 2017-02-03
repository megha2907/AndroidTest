package in.sportscafe.nostragamus.module.user.group.groupinfo;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jeeva.android.widgets.HmImageView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.CustomViewPager;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.popups.GetScreenNameListener;
import in.sportscafe.nostragamus.module.user.group.admin.adminmembers.AdminMembersActivity;
import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroupsActivity;
import in.sportscafe.nostragamus.module.user.group.editgroupinfo.EditGroupInfoActivity;
import in.sportscafe.nostragamus.module.user.group.groupselection.GroupSelectionFragment;
import in.sportscafe.nostragamus.module.user.group.members.MembersActivity;
import in.sportscafe.nostragamus.module.user.group.members.MembersFragment;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;

/**
 * Created by Jeeva on 12/6/16.
 */
public class GroupInfoActivity extends NostragamusActivity implements GroupInfoView,
        View.OnClickListener,GroupSelectionFragment.OnTournamentUpdatedListener {

    private static final int CODE_GROUP_INFO = 10;

    private static final int CODE_ADMIN_MEMBERS = 3;

    private static final int GROUPS_CODE = 20;

    private EditText mEtGroupName;

    private ImageButton mIBtnEditProfile;

    private TextView mTvGroupName;

    private Button mBtnShareGroupCode;

    private Button mBtnExitGroup;

    private Button mBtnMembersCount;

    private HmImageView mIvGroupIcon;

    private RecyclerView mRvSportSelection;

    private GroupInfoPresenter mGroupInfoPresenter;

    private LinearLayoutManager mlinearLayoutManagerVertical;

    private Bundle mBundle;

    private ViewPagerAdapter mpagerAdapter;

    private ViewPager mViewPager;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        initToolbar();

        findViewById(R.id.group_info_ll_share).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mGroupInfoPresenter.onLongClickShareCode();
                showMessage("Group code is copied to the clipboard");
                return true;
            }
        });

        findViewById(R.id.group_info_ll_share).setOnClickListener(this);

        this.mGroupInfoPresenter = GroupInfoPresenterImpl.newInstance(this);
        this.mGroupInfoPresenter.onCreateGroupInfo(getIntent().getExtras());

        mBundle = getIntent().getExtras();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_group_back_btn:
                onBackPressed();
                break;
            case R.id.group_info_ll_share:
                mGroupInfoPresenter.onClickShareCode();
                break;
        }
    }

    private void navigatetoEditGroupInfoActivity() {

        Intent intent = new Intent(this, EditGroupInfoActivity.class);
        Bundle mBundleNew = new Bundle();
        mBundleNew.putInt(Constants.BundleKeys.GROUP_ID, mBundle.getInt(Constants.BundleKeys.GROUP_ID));
        mBundleNew.putString(Constants.BundleKeys.GROUP_NAME, mBundle.getString(Constants.BundleKeys.GROUP_NAME));
        intent.putExtras(mBundleNew);
        startActivityForResult(intent, CODE_GROUP_INFO);
    }

    @Override
    public void setGroupName(String groupName) {

        mTvGroupName =(TextView)findViewById(R.id.group_name);

        if (null == groupName || groupName.isEmpty()) {
            mTvGroupName.setText("Group Info");
        } else {
            mTvGroupName.setText(groupName);
        }
    }

    @Override
    public void setGroupIcon(String groupPhotoUrl) {
        mIvGroupIcon = (HmImageView) findViewById(R.id.group_iv_user_image);
        mIvGroupIcon.setImageUrl(groupPhotoUrl);


    }

    @Override
    public void setMembersSize(int size) {

//        mBtnMembersCount = (Button) findViewById(R.id.group_info_btn_edit_members);
//
//        mBtnExitGroup= (Button) findViewById(R.id.group_info_exit_group);
//        mBtnExitGroup.setVisibility(View.VISIBLE);
//
//        RelativeLayout rlShareCode = (RelativeLayout)findViewById(R.id.group_info_ll_members);
//        rlShareCode.setVisibility(View.VISIBLE);
//
//
//        TextView tournamentHeading = (TextView) findViewById(R.id.tournaments_tv);
//        tournamentHeading.setVisibility(View.VISIBLE);
//
//
//        if (size == 1) {
//            mBtnMembersCount.setText(String.valueOf(size) + " Member");
//        } else {
//            mBtnMembersCount.setText(String.valueOf(size) + " Members");
//        }
    }

    @Override
    public void setAdapter(GroupTournamentAdapter adapter) {
        this.mRvSportSelection.setAdapter(adapter);
    }

    @Override
    public void setGroupCode(String groupCode) {
        FloatingActionButton groupCodebtn = (FloatingActionButton) findViewById(R.id.group_info_ll_share);
        groupCodebtn.setVisibility(View.VISIBLE);
        groupCodebtn.setTag(groupCode);
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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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

    @Override
    public void navigateToAllGroups() {
        Intent intent = new Intent(this, AllGroupsActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("GroupInfo Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    public void initMyPosition(GroupInfo groupInfo) {

        mViewPager = (CustomViewPager) findViewById(R.id.tab_vp);
        mpagerAdapter = getAdapter(groupInfo);
        mViewPager.setAdapter(mpagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_tl);
        tabLayout.setupWithViewPager(mViewPager);

        LinearLayout linearLayout = (LinearLayout)tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(getContext().getResources().getColor(R.color.profile_tab_line_color));
        drawable.setSize(1, 1);
        linearLayout.setDividerPadding(10);
        linearLayout.setDividerDrawable(drawable);

    }

    @Override
    public void changeToAdminMode() {
        mAdminMode = true;
        invalidateOptionsMenu();
    }

    private ViewPagerAdapter getAdapter(GroupInfo groupInfo) {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        pagerAdapter.addFragment(GroupSelectionFragment.newInstance(groupInfo.getId(),this),
               "\n Tournaments");

        if (groupInfo.getMembers().size()==1){
            pagerAdapter.addFragment(MembersFragment.newInstance(groupInfo.getId()),
                    groupInfo.getMembers().size()+ "\n Member");
        }else {
            pagerAdapter.addFragment(MembersFragment.newInstance(groupInfo.getId()), groupInfo.getMembers().size()+ "\n Members");
        }
        return pagerAdapter;
    }

    @Override
    public void setTournamentsCount(int tournamentsCount) {

        if(tournamentsCount==1){
            mpagerAdapter.updateTitle(0,tournamentsCount+ " \n Tournament");
        }
        else {
            mpagerAdapter.updateTitle(0,tournamentsCount+ " \n Tournaments");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private boolean mAdminMode = false;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(mAdminMode) {
            menu.findItem(R.id.reset_lb_btn).setVisible(true);
            menu.findItem(R.id.edit_group_btn).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
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
            case R.id.reset_lb_btn:
                mGroupInfoPresenter.onClickResetLb();
                break;
        }
        return true;
    }


    @Override
    public String getScreenName() {
        return Constants.ScreenNames.GROUPS_INFO;
    }
}