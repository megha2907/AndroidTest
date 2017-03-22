package in.sportscafe.nostragamus.module.user.points;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.jeeva.android.Log;

import org.parceler.Parcels;

import java.util.Calendar;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.LBLandingType;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.user.lblanding.LbLanding;
import in.sportscafe.nostragamus.module.user.leaderboard.LeaderBoardFragment;
import in.sportscafe.nostragamus.module.user.leaderboard.LeaderBoardResponse;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.LeaderBoard;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoard;
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

    private int mGroupId = 0;

    private Integer mChallengeId = 0;

    private Integer mTourId;

    private Integer mMatchId;

    private LbLanding mLbLanding;

    private OnPointsModelListener mPointsModelListener;

    private ViewPagerAdapter mViewPagerAdapter;

    private int mSelectedPosition = 0;

    private List<LeaderBoard> mleaderBoardList;

    private boolean isMatchPoints = false;

    private String mGroupName;

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
        mGroupId = mLbLanding.getGroupId();
        mChallengeId = mLbLanding.getChallengeId();

        if (LBLandingType.GROUP.equalsIgnoreCase(mLbLanding.getType())) {
            isChallengeTimer = false;
            mPointsModelListener.setChallengeTimerView(isChallengeTimer);
            if (bundle.containsKey(BundleKeys.LB_LANDING_TITLE)) {
                mPointsModelListener.setGroupHeadings(bundle.getString(BundleKeys.LB_LANDING_TITLE), mLbLanding.getName());
            }
        }

        if (mLbLanding.getEndTime() != null) {
            long startTimeMs = TimeUtils.getMillisecondsFromDateString(
                    mLbLanding.getEndTime(),
                    Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                    Constants.DateFormats.GMT
            );

            TimeAgo timeAgo = TimeUtils.calcTimeAgo(Calendar.getInstance().getTimeInMillis(), startTimeMs);
            long updatedTime = Long.parseLong(String.valueOf(timeAgo.totalDiff));

            if (updatedTime < 0) {
                isChallengeTimer = false;
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

        LeaderBoard leaderBoard;

        //for challenges change tab to overall
        if (mChallengeId == 0) {
            mViewPagerAdapter.addFragment(LeaderBoardFragment.newInstance(leaderBoardList.get(0)), leaderBoardList.get(0).getTournamentName());
            mPointsModelListener.changeTabsView();
        } else {

            for (int i = 0; i < leaderBoardList.size(); i++) {
                leaderBoard = leaderBoardList.get(i);

                Log.i("groupIdleaderboard", String.valueOf(leaderBoard.getGroupId()));

                mViewPagerAdapter.addFragment(LeaderBoardFragment.newInstance(leaderBoard, mChallengeId), leaderBoard.getTournamentName());

                if (i != 0 && mGroupId == leaderBoard.getGroupId()) {
                    mSelectedPosition = i;
                }
            }
        }
        Log.i("mSelectedPosition", String.valueOf(mSelectedPosition));

        mViewPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void sortAdapter(String sortType) {
        if (sortType.equals("rank")) {
            mViewPagerAdapter.getItem(mSelectedPosition).setUserVisibleHint(true);
        }
    }

    @Override
    public void updateUserLeaderBoard(int position) {

        UserLeaderBoard userLeaderBoard;

        Integer userId = Integer.valueOf(NostragamusDataHandler.getInstance().getUserId());

        LeaderBoard mLeaderBoard = mleaderBoardList.get(position);

        for (int i = 0; i < mLeaderBoard.getUserLeaderBoardList().size(); i++) {

            userLeaderBoard = mLeaderBoard.getUserLeaderBoardList().get(i);

            Log.i("userLeaderBoard", String.valueOf(userLeaderBoard));

            if (userId == userLeaderBoard.getUserId()) {
                mPointsModelListener.setUserLeaderBoard(userLeaderBoard);
                break;
            }
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

        mPointsModelListener.setChallengeTimer(String.format("%02d", days) + "d", String.format("%02d", hours) + "h",
                String.format("%02d", mins) + "m", String.format("%02d", secs) + "s");

    }


    public interface OnPointsModelListener {


        void onFailureLeaderBoard(String message);

        void onSuccessLeaderBoard();

        void onEmpty();

        void onNoInternet();

        void setIsMatchPoints(boolean isMatchPoints);

        void setChallengeTimer(String days, String hours, String mins, String secs);

        void setChallengeTimerView(boolean isChallengeTimer);

        void setGroupHeadings(String groupName, String heading);

        void setUserLeaderBoard(UserLeaderBoard userLeaderBoard);

        void changeTabsView();
    }
}