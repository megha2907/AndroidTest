package in.sportscafe.nostragamus.module.user.points.pointsFragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.Log;
import com.jeeva.android.widgets.HmImageView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.CustomViewPager;
import in.sportscafe.nostragamus.module.common.OnDismissListener;
import in.sportscafe.nostragamus.module.common.RoundImage;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.contest.dto.Contest;
import in.sportscafe.nostragamus.module.nostraHome.NostraHomeActivity;
import in.sportscafe.nostragamus.module.user.lblanding.LbLanding;
import in.sportscafe.nostragamus.module.user.leaderboard.LeaderBoardModelImpl;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoard;
import in.sportscafe.nostragamus.module.user.myprofile.UserProfileActivity;
import in.sportscafe.nostragamus.module.user.points.PointsPresenter;
import in.sportscafe.nostragamus.module.user.points.pointsFragment.dto.UserLeaderBoardInfo;

/**
 * Created by deepanshi on 9/13/17.
 */

public class PointsFragment extends BaseFragment implements PointsFragmentView, OnDismissListener {

    private PointsPresenter mPointsPresenter;

    private ViewPager mViewPager;

    private Button mBtnSortByAccuracy;

    private Button mBtnSortByTotalPoints;

    private Button mBtnSortByPowerUps;

    private View mSelectedImage;

    private boolean ismMatchPoints = false;

    private final static int POPUP_DIALOG_REQUEST_CODE = 50;

    public static PointsFragment newInstance(Contest contest) {

//        Bundle bundle = new Bundle();
//        bundle.putParcelable(Constants.BundleKeys.CONTEST, Parcels.wrap(contest));

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.BundleKeys.LB_LANDING_DATA, Parcels.wrap(new LbLanding(
                376, 0, "", "", Constants.LBLandingType.CHALLENGE
        )));

        PointsFragment fragment = new PointsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_points, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LeaderBoardModelImpl.SORT_TYPE = 0;
        setSelected(findViewById(R.id.sort_by_total_points_btn));

        mBtnSortByAccuracy = (Button) findViewById(R.id.sort_by_accuracy_btn);
        mBtnSortByTotalPoints = (Button) findViewById(R.id.sort_by_total_points_btn);
        mBtnSortByPowerUps = (Button) findViewById(R.id.sort_by_powerups_btn);

//        mBtnSortByAccuracy.setOnClickListener(this);
//        mBtnSortByPowerUps.setOnClickListener(this);
//        mBtnSortByTotalPoints.setOnClickListener(this);

        this.mPointsPresenter = PointsFragmentPresenterImpl.newInstance(this);
        this.mPointsPresenter.onCreatePoints(getArguments());

    }

    @Override
    public void updateUserLeaderBoard(int selectedPosition) {
        mPointsPresenter.updateUserLeaderBoard(selectedPosition);
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
    public void onSuccessLeaderBoardInfo(UserLeaderBoardInfo userLeaderBoardInfo) {

    }


    @Override
    public void setUserLeaderBoardView(UserLeaderBoard userLeaderBoard) {
        RelativeLayout userPoints = (RelativeLayout) findViewById(R.id.points_user_rl);
        View gradientView = findViewById(R.id.gradient_view);

        if (null == userLeaderBoard) {
            Log.i("insideuserleaderboard","null");
            userPoints.setVisibility(View.GONE);
            gradientView.setVisibility(View.GONE);
            return;
        }

        userPoints.setVisibility(View.VISIBLE);
        gradientView.setVisibility(View.VISIBLE);

        ImageView mIvStatus = (ImageView) findViewById(R.id.leaderboard_iv_status);
        TextView mTvRank = (TextView) findViewById(R.id.leaderboard_tv_rank);
        HmImageView mIvUser = (HmImageView) findViewById(R.id.leaderboard_iv_user_img);
        TextView mTvName = (TextView) findViewById(R.id.leaderboard_tv_user_name);
        TextView mTvPoints = (TextView) findViewById(R.id.leaderboard_tv_points);
        TextView mTvPlayed = (TextView) findViewById(R.id.leaderboard_tv_played);
        TextView mTvAccuracy = (TextView) findViewById(R.id.leaderboard_tv_accuracy);
        TextView mTvMatchPoints = (TextView) findViewById(R.id.leaderboard_tv_match_points);

        if (null == userLeaderBoard.getRank()) {
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

        if (null != userLeaderBoard.getRankChange()) {
            if (userLeaderBoard.getRankChange() < 0) {
                mIvStatus.setImageResource(R.drawable.status_arrow_down);
            } else {
                mIvStatus.setImageResource(R.drawable.status_arrow_up);
            }
        }

        mIvUser.setImageUrl(
                userLeaderBoard.getUserPhoto()
        );

        if (userLeaderBoard.getCountPlayed() == 1 || userLeaderBoard.getCountPlayed() == 0) {
            mTvPlayed.setText(String.valueOf(userLeaderBoard.getCountPlayed()) + " Match");
        } else {
            mTvPlayed.setText(String.valueOf(userLeaderBoard.getCountPlayed()) + " Matches");
        }

        if (userLeaderBoard.getAccuracy() != null) {
            mTvAccuracy.setText(userLeaderBoard.getAccuracy() + "%");
        }

    }

//    @Override
//    public void onClick(View view) {
//        //view.setEnabled(false);
//
//        switch (view.getId()) {
//
//            case R.id.sort_by_total_points_btn:
//
//                if (mViewPager != null) {
//                    LeaderBoardModelImpl.SORT_TYPE = 0;
//                    ((ViewPagerAdapter) mViewPager.getAdapter()).getItem(mViewPager.getCurrentItem()).setUserVisibleHint(true);
//                }
//                setSelected(findViewById(R.id.sort_by_total_points_btn));
//                break;
//
//            case R.id.sort_by_accuracy_btn:
//
//                if (mViewPager != null) {
//                    LeaderBoardModelImpl.SORT_TYPE = 1;
//                    ((ViewPagerAdapter) mViewPager.getAdapter()).getItem(mViewPager.getCurrentItem()).setUserVisibleHint(true);
//                }
//                setSelected(findViewById(R.id.sort_by_accuracy_btn));
//                break;
//
//            case R.id.sort_by_powerups_btn:
//                if (mViewPager != null) {
//
//                    if (ismMatchPoints) {
//                        LeaderBoardModelImpl.SORT_TYPE = 3;
//                    } else {
//                        LeaderBoardModelImpl.SORT_TYPE = 2;
//                    }
//                    ((ViewPagerAdapter) mViewPager.getAdapter()).getItem(mViewPager.getCurrentItem()).setUserVisibleHint(true);
//
//                }
//                setSelected(findViewById(R.id.sort_by_powerups_btn));
//                break;
//            case R.id.points_user_rl:
//                mPointsPresenter.onClickUserPoints();
//                break;
//        }
//    }

    private void setSelected(View selImg) {
        if (null != mSelectedImage) {
            mSelectedImage.setSelected(false);
        }

        mSelectedImage = selImg;
        mSelectedImage.setSelected(true);
    }


    @Override
    public void navigateToUserProfile(Bundle bundle) {
        Intent intent = new Intent(getContext(), UserProfileActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void navigateToHome() {
        Intent intent = new Intent(getContext(), NostraHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }


    @Override
    public void onDismiss(int requestCode, Bundle bundle) {
    }

    @Override
    public void showInApp() {

    }

    @Override
    public void hideInApp() {

    }

    @Override
    public void showInAppMessage(String message) {

    }

    @Override
    public void showMessage(String message, String positiveButton, View.OnClickListener positiveClickListener) {

    }

    @Override
    public void showMessage(String message, String positiveButton, View.OnClickListener positiveClickListener, String negativeButton, View.OnClickListener negativeClickListener) {

    }
}
