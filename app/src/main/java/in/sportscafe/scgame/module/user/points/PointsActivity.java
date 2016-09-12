package in.sportscafe.scgame.module.user.points;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.jeeva.android.Log;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.CustomViewPager;
import in.sportscafe.scgame.module.common.ScGameActivity;
import in.sportscafe.scgame.module.common.ViewPagerAdapter;
import in.sportscafe.scgame.module.user.group.admin.approve.ApproveFragment;
import in.sportscafe.scgame.module.user.group.members.MembersFragment;
import in.sportscafe.scgame.module.user.leaderboard.LeaderBoardFragment;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.scgame.module.user.myprofile.myposition.MyGlobalFragment;
import in.sportscafe.scgame.module.user.myprofile.myposition.MyLeaguesFragment;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.LbSummary;
import in.sportscafe.scgame.module.user.sportselection.dto.Sport;

/**
 * Created by Jeeva on 10/6/16.
 */
public class PointsActivity extends ScGameActivity implements PointsView, View.OnClickListener {

    private Spinner mSpGroup;

    private Spinner mSpSport;

    private TextView mTvWeek;

    private TextView mTvMonth;

    private Button mBtnGroupIcon;

    private ImageButton mBtnBack;

    private TextView mTvAllTime;

    private PointsPresenter mPointsPresenter;

    private CollapsingToolbarLayout mtoolbar;

    private View mSelectedTab;

    private ViewPagerAdapter pagerAdapter;

    private static final float ENABLE_TAB_ALPHA = 0.3f;

    private static final float DISABLE_TAB_ALPHA = 1f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

//        initToolBar();

        mtoolbar = (CollapsingToolbarLayout) findViewById(R.id.htab_collapse_toolbar);
        mBtnGroupIcon = (Button)mtoolbar.findViewById(R.id.points_group_icon);
        mBtnBack= (ImageButton) mtoolbar.findViewById(R.id.points_back_icon);

        mSpGroup = (Spinner) findViewById(R.id.points_sp_group);
        mSpGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPointsPresenter.onGroupItemSelected(position);
                TextView selectedText = (TextView) view;
                mBtnGroupIcon.setText(selectedText.getText().toString().substring(0,1));
                if (selectedText != null) {
                    selectedText.setTextColor(Color.WHITE);
                    selectedText.setTextSize(16);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        mSpSport = (Spinner) findViewById(R.id.points_sp_sport);
        mSpSport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                mPointsPresenter.onSportItemSelected(position);

                TextView selectedText = (TextView) view;
                if (selectedText != null) {
                    selectedText.setTextColor(Color.WHITE);
                    selectedText.setTextSize(15);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

//        mTvWeek = (TextView) findViewById(R.id.points_tv_week);
//        mTvMonth = (TextView) findViewById(R.id.points_tv_month);
//        mTvAllTime = (TextView) findViewById(R.id.points_tv_all);

//        LeaderBoardFragment leaderBoardFragment = new LeaderBoardFragment();
//
//        final OnLeaderBoardUpdateListener listener = leaderBoardFragment;
//
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.points_tab_vp, leaderBoardFragment).commit();

        initMyPosition();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PointsActivity.this.mPointsPresenter = PointsPresenterImpl.newInstance(PointsActivity.this);
                PointsActivity.this.mPointsPresenter.onCreatePoints( getIntent().getExtras());
            }
        }, 300);
    }

    @Override
    public void initMyPosition() {
        CustomViewPager mMostViewedPager = (CustomViewPager) findViewById(R.id.points_tab_vp);
        mMostViewedPager.setOffscreenPageLimit(3);
        setupViewPager(mMostViewedPager);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.points_tab_tl);
        tabLayout.setupWithViewPager(mMostViewedPager);

    }

    @Override
    public void refreshLeaderBoard(Bundle bundle) {
        if(null != pagerAdapter) {
            for (int i = 0; i < pagerAdapter.getCount(); i++) {
                ((LeaderBoardFragment) pagerAdapter.getItem(i)).refreshLeaderBoard(bundle);
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPager.setAdapter(getAdapter());
    }

    private ViewPagerAdapter getAdapter() {
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(LeaderBoardFragment.newInstance(Constants.LeaderBoardPeriods.WEEK), "Week");
        pagerAdapter.addFragment(LeaderBoardFragment.newInstance(Constants.LeaderBoardPeriods.MONTH), "Month");
        pagerAdapter.addFragment(LeaderBoardFragment.newInstance(Constants.LeaderBoardPeriods.ALL_TIME), "All Time");
        return pagerAdapter;
    }

    @Override
    public void onClick(View view) {
        view.setEnabled(false);

        switch (view.getId()) {
            case R.id.points_back_icon:
                onBackPressed();
        }

    }

    @Override
    public void setGroupAdapter(ArrayAdapter<GroupInfo> groupAdapter, int initialGroupPosition) {
        if(null == groupAdapter) {
            mSpGroup.setVisibility(View.GONE);
        } else {
            mSpGroup.setAdapter(groupAdapter);
            mSpGroup.setSelection(initialGroupPosition);
        }
    }

    @Override
    public void setSportAdapter(ArrayAdapter<Sport> sportAdapter, int initialSportPosition) {
        mSpSport.setAdapter(sportAdapter);
        mSpSport.setSelection(initialSportPosition);
        //Log.i("initialSportPosition",String.valueOf(mSpSport.getSelectedItem()));
    }

    public void onBack(View view) {
        onBackPressed();
    }

//
//    public void initToolBar() {
//        mtoolbar = (CollapsingToolbarLayout) findViewById(R.id.htab_collapse_toolbar);
//        mBtnGroupIcon = (Button) mtoolbar.findViewById(R.id.points_group_icon);
//        mBtnBack= (ImageButton) mtoolbar.findViewById(R.id.points_back_icon);
//        setSupportActionBar(mtoolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//    }
}