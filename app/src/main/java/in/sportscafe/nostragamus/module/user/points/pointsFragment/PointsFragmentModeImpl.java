package in.sportscafe.nostragamus.module.user.points.pointsFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.jeeva.android.Log;

import org.parceler.Parcels;

import java.util.Calendar;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.challengeRewards.RewardsApiModelImpl;
import in.sportscafe.nostragamus.module.challengeRewards.dto.RewardsRequest;
import in.sportscafe.nostragamus.module.challengeRewards.dto.RewardsResponse;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.user.lblanding.LbLanding;
import in.sportscafe.nostragamus.module.user.leaderboard.LeaderBoardFragment;
import in.sportscafe.nostragamus.module.user.leaderboard.LeaderBoardResponse;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.LeaderBoard;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoard;
import in.sportscafe.nostragamus.module.user.points.PointsModel;
import in.sportscafe.nostragamus.module.user.points.pointsFragment.dto.UserLeaderBoardInfo;
import in.sportscafe.nostragamus.module.user.points.pointsFragment.dto.UserLeaderBoardRequest;
import in.sportscafe.nostragamus.module.user.points.pointsFragment.dto.UserLeaderBoardResponse;
import in.sportscafe.nostragamus.utils.timeutils.TimeAgo;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import in.sportscafe.nostragamus.webservice.UserReferralInfo;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 9/13/17.
 */

public class PointsFragmentModeImpl implements PointsModel {

    private boolean mUserInput = false;

    private Integer mSportId = 0;

    private int mGroupId = 0;

    private Integer mChallengeId = 0;

    private Integer mTourId;

    private Integer mMatchId;

    private LbLanding mLbLanding;

    private PointsFragmentModeImpl.OnPointsModelListener mPointsModelListener;

    private ViewPagerAdapter mViewPagerAdapter;

    private int mSelectedPosition = 0;

    private List<LeaderBoard> mleaderBoardList;

    private boolean isMatchPoints = false;

    private String mGroupName;

    private boolean isChallengeTimer = true;


    private PointsFragmentModeImpl(PointsFragmentModeImpl.OnPointsModelListener listener, FragmentManager fm) {
        this.mPointsModelListener = listener;
        this.mViewPagerAdapter = new ViewPagerAdapter(fm);
    }

    public static PointsModel newInstance(PointsFragmentModeImpl.OnPointsModelListener listener, FragmentManager fm) {
        return new PointsFragmentModeImpl(listener, fm);
    }

    @Override
    public void init(Bundle bundle) {
        if (bundle.containsKey(Constants.BundleKeys.TOURNAMENT_ID)) {
            mTourId = bundle.getInt(Constants.BundleKeys.TOURNAMENT_ID);
        }

        if (bundle.containsKey(Constants.BundleKeys.MATCH_ID)) {
            mMatchId = bundle.getInt(Constants.BundleKeys.MATCH_ID);
        } else {
            mMatchId = null;
        }

        mLbLanding = Parcels.unwrap(bundle.getParcelable(Constants.BundleKeys.LB_LANDING_DATA));
        mGroupId = mLbLanding.getGroupId();
        mChallengeId = mLbLanding.getChallengeId();

        if (mLbLanding.getEndTime() != null) {
            long startTimeMs = TimeUtils.getMillisecondsFromDateString(
                    mLbLanding.getEndTime(),
                    Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                    Constants.DateFormats.GMT
            );

            TimeAgo timeAgo = TimeUtils.calcTimeAgo(Calendar.getInstance().getTimeInMillis(), startTimeMs);
            long updatedTime = Long.parseLong(String.valueOf(timeAgo.totalDiff));


        }


    }

    @Override
    public String getName() {
        return mLbLanding.getName();
    }


    @Override
    public String getIcon() {
        return mLbLanding.getImgUrl();
    }

    @Override
    public ViewPagerAdapter getAdapter() {
        return null;
    }


    @Override
    public void refreshLeaderBoard() {

    }

    @Override
    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    @Override
    public List<LeaderBoard> getLeaderBoardList() {
        return null;
    }

    @Override
    public void refreshAdapter(List<LeaderBoard> leaderBoardList, String SortType) {

    }



    @Override
    public void sortAdapter(String sortType) {
        if (sortType.equals("rank")) {
            mViewPagerAdapter.getItem(mSelectedPosition).setUserVisibleHint(true);
        }
    }

    @Override
    public void updateUserLeaderBoard(int position) {
        String userId = NostragamusDataHandler.getInstance().getUserId();
        for (UserLeaderBoard userLeaderBoard : mleaderBoardList.get(position).getUserLeaderBoardList()) {
            if (userId.equalsIgnoreCase(userLeaderBoard.getUserId() + "")) {
                mPointsModelListener.setUserLeaderBoard(userLeaderBoard);
                return;
            }
        }

        mPointsModelListener.setUserLeaderBoard(null);
    }

    @Override
    public Bundle getUserProfileBundle() {
        Bundle bundle = new Bundle();
        if(null != mChallengeId) {
            bundle.putInt(Constants.BundleKeys.CHALLENGE_ID, mChallengeId);
        }
        return bundle;
    }


    public interface OnPointsModelListener {

        void onFailureLeaderBoard(String message);

        void onSuccessLeaderBoard(UserLeaderBoardInfo userLeaderBoardInfo);

        void onEmpty();

        void onNoInternet();

        void setIsMatchPoints(boolean isMatchPoints);

        void setUserLeaderBoard(UserLeaderBoard userLeaderBoard);
    }
}
