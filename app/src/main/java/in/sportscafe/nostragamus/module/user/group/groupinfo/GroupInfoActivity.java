package in.sportscafe.nostragamus.module.user.group.groupinfo;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.CustomProgressbar;
import com.jeeva.android.widgets.HmImageView;

import in.sportscafe.nostragamus.Constants.ScreenNames;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.CustomViewPager;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.user.group.editgroupinfo.EditGroupInfoActivity;

/**
 * Created by Jeeva on 12/6/16.
 */
public class GroupInfoActivity extends NostragamusActivity implements GroupInfoView, View.OnClickListener {

    private static final int EDIT_CODE = 19;

    private ViewPager mViewPager;

    private ViewPagerAdapter mPagerAdapter;

    private GroupInfoPresenter mGroupInfoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        initToolbar();

        mViewPager = (CustomViewPager) findViewById(R.id.tab_vp);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        findViewById(R.id.group_info_ll_share).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mGroupInfoPresenter.onLongClickShareCode();
                showMessage("Group code is copied to the clipboard");
                return true;
            }
        });

        this.mGroupInfoPresenter = GroupInfoPresenterImpl.newInstance(this);
        this.mGroupInfoPresenter.onCreateGroupInfo(getIntent().getExtras());
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_group_back_btn:
                mGroupInfoPresenter.onClickBack();
                break;
            case R.id.group_info_ll_share:
                mGroupInfoPresenter.onClickShareCode();
                break;
        }
    }

    @Override
    public void setGroupName(String groupName) {
        ((TextView) findViewById(R.id.group_name)).setText(groupName);
    }

    @Override
    public void setGroupImage(String groupImageUrl) {
        ((HmImageView) findViewById(R.id.group_iv_user_image)).setImageUrl(groupImageUrl);
    }

    @Override
    public void setAdapter(ViewPagerAdapter adapter) {
        mViewPager.setAdapter(mPagerAdapter = adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_tl);
        tabLayout.setupWithViewPager(mViewPager);

        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(getContext().getResources().getColor(R.color.profile_tab_line_color));
        drawable.setSize(1, 1);
        linearLayout.setDividerPadding(10);
        linearLayout.setDividerDrawable(drawable);
    }

    @Override
    public void navigateToEditGroup(Bundle bundle) {
        Intent intent = new Intent(this, EditGroupInfoActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, EDIT_CODE);
    }

    @Override
    public void changeToAdminMode() {
        mAdminMode = true;
        invalidateOptionsMenu();
    }

    @Override
    public void setSuccessData(Bundle bundle) {
        setResult(RESULT_OK, new Intent().putExtras(bundle));
    }

    @Override
    public void goBack() {
        super.onBackPressed();
    }

    @Override
    public void setTourTabTitle(String title) {
        mPagerAdapter.updateTitle(0, title);
    }

    @Override
    public void setMemberTabTitle(String title) {
        mPagerAdapter.updateTitle(1, title);
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
        if (mAdminMode) {
            menu.findItem(R.id.reset_lb_btn).setVisible(true);
            menu.findItem(R.id.edit_group_btn).setVisible(true);
            menu.findItem(R.id.delete_group_btn).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_group_btn:
                mGroupInfoPresenter.onClickEditGroup();
                break;
            case R.id.leave_group_btn:
                mGroupInfoPresenter.onClickLeaveGroup();
                break;
            case R.id.reset_lb_btn:
                mGroupInfoPresenter.onClickResetLb();
                break;
            case R.id.delete_group_btn:
                mGroupInfoPresenter.onClickDeleteGroup();
                break;
        }
        return true;
    }

    @Override
    public void showProgressbar() {
        CustomProgressbar.getProgressbar(this).show();
    }

    @Override
    public boolean dismissProgressbar() {
        return CustomProgressbar.getProgressbar(this).dismissProgress();
    }

    @Override
    public String getScreenName() {
        return ScreenNames.GROUPS_INFO;
    }

    @Override
    public void onBackPressed() {
        mGroupInfoPresenter.onClickBack();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(RESULT_OK == resultCode && EDIT_CODE == requestCode) {
            mGroupInfoPresenter.onGetEditResult(data.getExtras());
        }
    }
}