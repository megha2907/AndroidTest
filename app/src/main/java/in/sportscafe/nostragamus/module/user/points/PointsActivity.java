package in.sportscafe.nostragamus.module.user.points;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.HmImageView;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.challenge.ChallengeFragment;
import in.sportscafe.nostragamus.module.common.CustomViewPager;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.common.RoundImage;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.popups.inapppopups.InAppPopupActivity;
import in.sportscafe.nostragamus.module.user.leaderboard.LeaderBoardModelImpl;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoard;
import in.sportscafe.nostragamus.module.user.sportselection.dto.Sport;

import static in.sportscafe.nostragamus.R.id.view;

/**
 * Created by Jeeva on 10/6/16.
 */
public class PointsActivity extends NostragamusActivity implements PointsView, View.OnClickListener {

    private PointsPresenter mPointsPresenter;

    private ViewPager mViewPager;

    private Button mBtnSortByAccuracy;

    private Button mBtnSortByTotalPoints;

    private Button mBtnSortByPowerUps;

    private String mHeading;

    private View mSelectedImage;

    private TextView mTvLeaderBoardHeading;

    private boolean ismMatchPoints = false;

    Button mTvChallengeDaysLeft;
    Button mTvChallengeHoursLeft;
    Button mTvChallengeMinsLeft;
    Button mTvChallengeSecsLeft;
    RelativeLayout mRlChallengeTimer;

    private TabLayout mTabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

        //initSortingSpinner();
        LeaderBoardModelImpl.SORT_TYPE = 0;
        setSelected(findViewById(R.id.sort_by_total_points_btn));


        mBtnSortByAccuracy = (Button) findViewById(R.id.sort_by_accuracy_btn);
        mBtnSortByTotalPoints = (Button) findViewById(R.id.sort_by_total_points_btn);
        mBtnSortByPowerUps = (Button) findViewById(R.id.sort_by_powerups_btn);
        mTabLayout = (TabLayout) findViewById(R.id.points_tab_tl);

        mBtnSortByAccuracy.setOnClickListener(this);
        mBtnSortByPowerUps.setOnClickListener(this);
        mBtnSortByTotalPoints.setOnClickListener(this);

        mRlChallengeTimer = (RelativeLayout) findViewById(R.id.all_challenges_row_rl_timer);
        mTvChallengeHoursLeft = (Button) findViewById(R.id.all_challenges_row_btn_hours_left);
        mTvChallengeDaysLeft = (Button) findViewById(R.id.all_challenges_row_btn_days_left);
        mTvChallengeMinsLeft = (Button) findViewById(R.id.all_challenges_row_btn_mins_left);
        mTvChallengeSecsLeft = (Button) findViewById(R.id.all_challenges_row_btn_secs_left);
        mTvLeaderBoardHeading = (TextView) findViewById(R.id.points_sp_name);

        this.mPointsPresenter = PointsPresenterImpl.newInstance(PointsActivity.this);
        this.mPointsPresenter.onCreatePoints(getIntent().getExtras());


    }

//    private void initSortingSpinner() {
//        List categories = new ArrayList();
//        categories.add("By Rank");
//        categories.add("By Accuracy");
//
//        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, categories) {
//
//            public View getView(int position, View convertView, ViewGroup parent) {
//                return setExternalFont(super.getView(position, convertView, parent));
//            }
//
//            public View getDropDownView(int position, View convertView, ViewGroup parent) {
//                return setExternalFont(super.getDropDownView(position, convertView, parent));
//            }
//        };
//
//        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(R.layout.custom_radio_btn_spinner);
//
//        AppCompatSpinner spinner = (AppCompatSpinner) findViewById(R.id.points_sp_sport);
//        spinner.setOnItemSelectedListener(this);
//        spinner.setAdapter(dataAdapter);
//    }

    private View setExternalFont(View spinnerView) {
        ((TextView) spinnerView).setTypeface(Typeface.createFromAsset(
                getContext().getAssets(),
                "fonts/roboto/RobotoCondensed-Regular.ttf"
        ));
        return spinnerView;
    }

    @Override
    public void setName(String name) {
        mTvLeaderBoardHeading.setText(name);
    }

    @Override
    public void initMyPosition(ViewPagerAdapter adapter, int selectedPosition) {
        mViewPager = (CustomViewPager) findViewById(R.id.points_tab_vp);
        mViewPager.setAdapter(adapter);

        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.setCurrentItem(selectedPosition);
        mPointsPresenter.updateUserLeaderBoard(selectedPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               mPointsPresenter.updateUserLeaderBoard(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void setIcon(String icon) {
        ((HmImageView) findViewById(R.id.points_group_icon)).setImageUrl(icon);
    }

    @Override
    public void setMatchPoints(boolean isMatchPoints) {

        ismMatchPoints = isMatchPoints;

        if (isMatchPoints) {
            mBtnSortByPowerUps.setText("Match Score");
            mBtnSortByPowerUps.setCompoundDrawablesWithIntrinsicBounds(R.drawable.match_points_selection_icon, 0, 0, 0);
        }
    }

    @Override
    public void setChallengeTimerView(boolean isChallengeTimer) {
        if (isChallengeTimer == false) {
            mRlChallengeTimer.setVisibility(View.GONE);
        }
    }

    @Override
    public void setChallengeTimer(String days, String hours, String mins, String secs) {
        mTvChallengeDaysLeft.setText(days);
        mTvChallengeHoursLeft.setText(hours);
        mTvChallengeMinsLeft.setText(mins);
        mTvChallengeSecsLeft.setText(secs);
    }

    @Override
    public void setGroupHeadings(String groupName, String heading) {
        TextView mGroupHeading = (TextView) findViewById(R.id.points_group_heading);
        TextView mGroupSubHeading = (TextView) findViewById(R.id.points_group_sub_heading);
        mTvLeaderBoardHeading.setVisibility(View.GONE);
        mGroupHeading.setVisibility(View.VISIBLE);
        mGroupSubHeading.setVisibility(View.VISIBLE);
        mGroupHeading.setText(groupName);
        mGroupSubHeading.setText(heading);
    }

    @Override
    public void setUserLeaderBoardView(UserLeaderBoard userLeaderBoard) {

        RelativeLayout userPoints = (RelativeLayout)findViewById(R.id.points_user_rl);
        View gradientView = (View)findViewById(R.id.gradient_view);
        ImageView mIvStatus = (ImageView) findViewById(R.id.leaderboard_iv_status);
        TextView  mTvRank = (TextView) findViewById(R.id.leaderboard_tv_rank);
        RoundImage mIvUser = (RoundImage) findViewById(R.id.leaderboard_iv_user_img);
        TextView mTvName = (TextView) findViewById(R.id.leaderboard_tv_user_name);
        TextView mTvPoints = (TextView) findViewById(R.id.leaderboard_tv_points);
        TextView mTvPlayed= (TextView) findViewById(R.id.leaderboard_tv_played);
        TextView mTvAccuracy = (TextView)findViewById(R.id.leaderboard_tv_accuracy);
        TextView mTvMatchPoints = (TextView)findViewById(R.id.leaderboard_tv_match_points);

        userPoints.setVisibility(View.VISIBLE);
        gradientView.setVisibility(View.VISIBLE);


        if(null == userLeaderBoard.getRank()) {
            mTvRank.setText("-");
        } else {
            mTvRank.setText(userLeaderBoard.getRank().toString());
        }

        //set PowerUps if Match Points is null
        if (null == userLeaderBoard.getMatchPoints()) {
            mTvMatchPoints.setText(userLeaderBoard.getUserPowerUps().toString());
        } else {
            mTvMatchPoints.setText(String.valueOf(userLeaderBoard.getMatchPoints()));
            mTvMatchPoints.setCompoundDrawablesWithIntrinsicBounds(R.drawable.match_points_white_icon, 0, 0, 0);
        }


        mTvName.setText(userLeaderBoard.getUserName());
        mTvPoints.setText(String.valueOf(userLeaderBoard.getPoints()));

        if(null!=userLeaderBoard.getRankChange()) {
            if (userLeaderBoard.getRankChange() < 0) {
                mIvStatus.setImageResource(R.drawable.status_arrow_down);
            } else {
                mIvStatus.setImageResource(R.drawable.status_arrow_up);
            }
        }

        mIvUser.setImageUrl(
                userLeaderBoard.getUserPhoto()
        );

        if (userLeaderBoard.getCountPlayed()==1 || userLeaderBoard.getCountPlayed()==0) {
            mTvPlayed.setText(String.valueOf(userLeaderBoard.getCountPlayed())+" Match");
        }else {
            mTvPlayed.setText(String.valueOf(userLeaderBoard.getCountPlayed())+" Matches");
        }

        if (userLeaderBoard.getAccuracy()!=null) {
            mTvAccuracy.setText(userLeaderBoard.getAccuracy()+"%");
        }

    }

    @Override
    public void setTabsView() {
        mTabLayout.setSelectedTabIndicatorColor(Color.TRANSPARENT);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public void onClick(View view) {
        //view.setEnabled(false);

        switch (view.getId()) {
            case R.id.points_back_icon:
                LeaderBoardModelImpl.SORT_TYPE = 0;
                onBackPressed();
                break;

            case R.id.sort_by_total_points_btn:

                if (mViewPager != null) {
                    LeaderBoardModelImpl.SORT_TYPE = 0;
                    ((ViewPagerAdapter) mViewPager.getAdapter()).getItem(mViewPager.getCurrentItem()).setUserVisibleHint(true);
                }
                setSelected(findViewById(R.id.sort_by_total_points_btn));
                break;

            case R.id.sort_by_accuracy_btn:

                if (mViewPager != null) {
                    LeaderBoardModelImpl.SORT_TYPE = 1;
                    ((ViewPagerAdapter) mViewPager.getAdapter()).getItem(mViewPager.getCurrentItem()).setUserVisibleHint(true);
                }
                setSelected(findViewById(R.id.sort_by_accuracy_btn));
                break;

            case R.id.sort_by_powerups_btn:
                if (mViewPager != null) {

                    if (ismMatchPoints) {
                        LeaderBoardModelImpl.SORT_TYPE = 3;
                    } else {
                        LeaderBoardModelImpl.SORT_TYPE = 2;
                    }
                    ((ViewPagerAdapter) mViewPager.getAdapter()).getItem(mViewPager.getCurrentItem()).setUserVisibleHint(true);

                }
                setSelected(findViewById(R.id.sort_by_powerups_btn));
                break;
        }
    }

    private void setSelected(View selImg) {
        if (null != mSelectedImage) {
            mSelectedImage.setSelected(false);
        }

        mSelectedImage = selImg;
        mSelectedImage.setSelected(true);
    }

    @Override
    public void showOtherProfilePopUp() {
        openPopup();
    }

    private void openPopup() {
        Intent intent = new Intent(this, InAppPopupActivity.class);
        intent.putExtra(Constants.InAppPopups.IN_APP_POPUP_TYPE, Constants.InAppPopups.NOT_VISITED_OTHER_PROFILE);
        startActivity(intent);
    }


//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        String item = parent.getItemAtPosition(position).toString();
//
//        ((TextView) findViewById(R.id.sorting_tv)).setText(item);
//
//        if (mViewPager != null) {
//            LeaderBoardModelImpl.SORT_TYPE = position;
//            ((ViewPagerAdapter) mViewPager.getAdapter()).getItem(mViewPager.getCurrentItem()).setUserVisibleHint(true);
//        }
//
//    }


//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.LEADERBOARD_DETAIL;
    }
}