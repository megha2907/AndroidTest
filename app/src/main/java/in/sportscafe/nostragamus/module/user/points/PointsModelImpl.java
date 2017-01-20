package in.sportscafe.nostragamus.module.user.points;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.jeeva.android.Log;

import java.util.Collections;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.user.lblanding.LBLanding;
import in.sportscafe.nostragamus.module.user.leaderboard.LeaderBoardFragment;
import in.sportscafe.nostragamus.module.user.leaderboard.LeaderBoardResponse;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.LeaderBoard;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoard;
import in.sportscafe.nostragamus.module.user.myprofile.myposition.dto.BaseSummary;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;
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

    private Integer mSelectedSportId=0;

    private Integer mSelectedGroupId=0;

    private Integer mSelectedChallengeId=0;

    private Integer mTourId;

    private Integer mLandingType;

    private LBLanding mLBLanding;

    private OnPointsModelListener mPointsModelListener;

    private ViewPagerAdapter mViewPagerAdapter;

    private int mSelectedPosition = 0;

    private List<LeaderBoard> mleaderBoardList;


    private PointsModelImpl(OnPointsModelListener listener, FragmentManager fm) {
        this.mPointsModelListener = listener;
        this.mViewPagerAdapter = new ViewPagerAdapter(fm);
    }

    public static PointsModel newInstance(OnPointsModelListener listener, FragmentManager fm) {
        return new PointsModelImpl(listener, fm);
    }

    @Override
    public void init(Bundle bundle) {

//        mSelectedGroupId = bundle.getLong(BundleKeys.GROUP_ID);
//        mSelectedSportId = bundle.getInt(BundleKeys.SPORT_ID);
//        mSelectedChallengeId = bundle.getInt(BundleKeys.CHALLENGE_ID);
//        mBaseSummary = (BaseSummary) bundle.getSerializable(BundleKeys.TOURNAMENT_SUMMARY);

        mLBLanding = (LBLanding)bundle.getSerializable(BundleKeys.LBLANDING);

        if (bundle.containsKey(BundleKeys.TOURNAMENT_ID)){
            mTourId = bundle.getInt(BundleKeys.TOURNAMENT_ID);
        }else {
            mTourId = null;
        }

        mLandingType = bundle.getInt(BundleKeys.SPORT_ID);

        switch (mLandingType){
            case Constants.LBLandingType.SPORT_TYPE:
                mSelectedSportId = mLBLanding.getId();
                break;
            case Constants.LBLandingType.GROUP_TYPE:
                mSelectedGroupId = mLBLanding.getId();
                break;
            case Constants.LBLandingType.CHALLENGE_TYPE:
                mSelectedChallengeId = mLBLanding.getId();
                break;
        }



    }

    @Override
    public String getName() {
        return mLBLanding.getName();
    }

    @Override
    public String getIcon() {return mLBLanding.getImgUrl();
    }

    @Override
    public ViewPagerAdapter getAdapter() {
        return mViewPagerAdapter;
    }

    @Override
    public void refreshLeaderBoard() {
        callLbDetailApi(mSelectedSportId,mSelectedGroupId,mSelectedChallengeId);
    }

    @Override
    public int getSelectedPosition() {
        return mSelectedPosition;
    }


    private void callLbDetailApi(final Integer sportId, Integer groupId, Integer challengeId) {
        MyWebService.getInstance().getLeaderBoardDetailRequest(
                sportId, groupId, challengeId
        ).enqueue(new NostragamusCallBack<LeaderBoardResponse>() {
            @Override
            public void onResponse(Call<LeaderBoardResponse> call, Response<LeaderBoardResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    List<LeaderBoard> leaderBoardList = response.body().getLeaderBoardList();
                    if (null == leaderBoardList || leaderBoardList.isEmpty()) {
                        mPointsModelListener.onEmpty();
                        return;
                    }

                    mleaderBoardList =leaderBoardList;

                    refreshAdapter(leaderBoardList,"");

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

        for (int i = 0; i < leaderBoardList.size(); i++) {
            leaderBoard = leaderBoardList.get(i);

            mViewPagerAdapter.addFragment(LeaderBoardFragment.newInstance(leaderBoard), leaderBoard.getTournamentName());

           //if match not played change tab to overall
           if (null==mTourId){
                 mSelectedPosition = 0;
               }
           //for challenges change tab to overall
           else if (mSelectedChallengeId!=0){
               mSelectedPosition = 0;
           }
           //for match played change tab to tournament
            else if(mTourId.equals(leaderBoard.getTournamentId())) {
                mSelectedPosition = i;
            }

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


    public interface OnPointsModelListener {


        void onFailureLeaderBoard(String message);

        void onSuccessLeaderBoard();

        void onEmpty();

        void onNoInternet();
    }
}