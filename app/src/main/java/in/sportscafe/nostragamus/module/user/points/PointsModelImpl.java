package in.sportscafe.nostragamus.module.user.points;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.jeeva.android.Log;

import org.parceler.Parcels;

import java.util.Calendar;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.LBLandingType;
import in.sportscafe.nostragamus.module.allchallenges.challenge.ChallengeAdapter;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.user.lblanding.LbLanding;
import in.sportscafe.nostragamus.module.user.leaderboard.LeaderBoardFragment;
import in.sportscafe.nostragamus.module.user.leaderboard.LeaderBoardResponse;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.LeaderBoard;
import in.sportscafe.nostragamus.utils.timeutils.TimeAgo;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

import static in.sportscafe.nostragamus.Constants.BundleKeys;

/**
 * Created by Jeeva on 10/6/16.
 */
public class PointsModelImpl implements PointsModel {

    private boolean mUserInput = false;

    private Integer mSportId = 0;

    private Integer mGroupId = 0;

    private Integer mChallengeId = 0;

    private Integer mTourId;

    private Integer mMatchId;

    private LbLanding mLbLanding;

    private OnPointsModelListener mPointsModelListener;

    private ViewPagerAdapter mViewPagerAdapter;

    private int mSelectedPosition = 0;

    private List<LeaderBoard> mleaderBoardList;

    private boolean isMatchPoints = false;

    private boolean isChallengeTimer = true;


    private PointsModelImpl(OnPointsModelListener listener, FragmentManager fm) {
        this.mPointsModelListener = listener;
        this.mViewPagerAdapter = new ViewPagerAdapter(fm);
    }

    public static PointsModel newInstance(OnPointsModelListener listener, FragmentManager fm) {
        return new PointsModelImpl(listener, fm);
    }

    @Override
    public void init(Bundle bundle) {
        if (bundle.containsKey(BundleKeys.TOURNAMENT_ID)) {
            mTourId = bundle.getInt(BundleKeys.TOURNAMENT_ID);
        }

        if (bundle.containsKey(BundleKeys.MATCH_ID)) {
            mMatchId = bundle.getInt(BundleKeys.MATCH_ID);
        } else {
            mMatchId = null;
        }

        mLbLanding = Parcels.unwrap(bundle.getParcelable(BundleKeys.LB_LANDING_DATA));

        switch (mLbLanding.getType()) {
            case LBLandingType.GROUP:
                isChallengeTimer=false;
                mPointsModelListener.setChallengeTimerView(isChallengeTimer);
                mGroupId = mLbLanding.getGroupId();
                break;
            case LBLandingType.CHALLENGE:
                mChallengeId = mLbLanding.getChallengeId();
                break;
        }

        if (mLbLanding.getEndTime() !=null) {
            long startTimeMs = TimeUtils.getMillisecondsFromDateString(
                    mLbLanding.getEndTime(),
                    Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                    Constants.DateFormats.GMT
            );

            TimeAgo timeAgo = TimeUtils.calcTimeAgo(Calendar.getInstance().getTimeInMillis(), startTimeMs);
            long updatedTime = Long.parseLong(String.valueOf(timeAgo.totalDiff));

            if (updatedTime < 0) {
                isChallengeTimer=false;
                mPointsModelListener.setChallengeTimerView(isChallengeTimer);
            } else {
                updateTimer(updatedTime);
            }

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
        return mViewPagerAdapter;
    }

    @Override
    public void refreshLeaderBoard() {
        callLbDetailApi();
    }

    @Override
    public int getSelectedPosition() {
        return mSelectedPosition;
    }


    private void callLbDetailApi() {
        MyWebService.getInstance().getLeaderBoardDetailRequest(mGroupId, mChallengeId, mMatchId)
                .enqueue(new NostragamusCallBack<LeaderBoardResponse>() {
                    @Override
                    public void onResponse(Call<LeaderBoardResponse> call, Response<LeaderBoardResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {

                            List<LeaderBoard> leaderBoardList = response.body().getLeaderBoardList();
                            if (null == leaderBoardList || leaderBoardList.isEmpty()) {
                                mPointsModelListener.onEmpty();
                                return;
                            }

                            if (leaderBoardList.get(0).getUserLeaderBoardList().get(0).getMatchPoints() != null) {
                                isMatchPoints = true;
                                mPointsModelListener.setIsMatchPoints(isMatchPoints);
                            }

                            mleaderBoardList = leaderBoardList;

                            refreshAdapter(leaderBoardList, "");

                            mPointsModelListener.onSuccessLeaderBoard();
                        } else {
                            mPointsModelListener.onFailureLeaderBoard(response.message());
                        }
                    }
                });
    }

    @Override
    public List<LeaderBoard> getLeaderBoardList() {
        return mleaderBoardList;
    }

    @Override
    public void refreshAdapter(List<LeaderBoard> leaderBoardList, String SortType) {


        Log.d("PointsModelImpl", "Selected Summary --> " + mTourId);
        LeaderBoard leaderBoard;

        mGroupId = mLbLanding.getGroupId();

        for (int i = 0; i < leaderBoardList.size(); i++) {
            leaderBoard = leaderBoardList.get(i);

            Log.i("groupIdleaderboard", String.valueOf(leaderBoard.getGroupId()));

            mViewPagerAdapter.addFragment(LeaderBoardFragment.newInstance(leaderBoard), leaderBoard.getTournamentName());

            //for challenges change tab to overall
            //if challnegedid=0
            if (mChallengeId == 0) {
                mSelectedPosition = 0;
            } else if (mGroupId == 0) {
                mSelectedPosition = 0;
            } else if (mGroupId.equals(leaderBoard.getGroupId())) {
                mSelectedPosition = i;
            }

            Log.i("mChallengeId", mChallengeId.toString());
            Log.i("mgroupid", mGroupId.toString());
            Log.i("mSelectedPosition", String.valueOf(mSelectedPosition));

            Log.d("PointsModelImpl", "LeaderBoard --> " + leaderBoard.getTournamentId());
        }

        mViewPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void sortAdapter(String sortType) {
        if (sortType.equals("rank")) {
            mViewPagerAdapter.getItem(mSelectedPosition).setUserVisibleHint(true);
        }
    }


    private void updateTimer(long updatedTime) {

        int secs = (int) (updatedTime / 1000);
        int mins = secs / 60;
        int hours = mins / 60;
        int days = hours / 24;
        hours = hours % 24;
        mins = mins % 60;
        secs = secs % 60;

        mPointsModelListener.setChallengeTimer(String.format("%02d", days) + "d",String.format("%02d", hours) + "h",
                String.format("%02d", mins) + "m",String.format("%02d", secs) + "s");

    }


    public interface OnPointsModelListener {


        void onFailureLeaderBoard(String message);

        void onSuccessLeaderBoard();

        void onEmpty();

        void onNoInternet();

        void setIsMatchPoints(boolean isMatchPoints);

        void setChallengeTimer(String days, String hours, String mins, String secs);

        void setChallengeTimerView(boolean isChallengeTimer);
    }
}