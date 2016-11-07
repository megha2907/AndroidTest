package in.sportscafe.scgame.module.user.points;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.ArrayAdapter;

import com.jeeva.android.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.TournamentFeed.dto.TournamentInfo;
import in.sportscafe.scgame.module.common.ViewPagerAdapter;
import in.sportscafe.scgame.module.user.leaderboard.LeaderBoardFragment;
import in.sportscafe.scgame.module.user.leaderboard.LeaderBoardResponse;
import in.sportscafe.scgame.module.user.leaderboard.dto.LeaderBoard;
import in.sportscafe.scgame.module.user.leaderboard.dto.UserLeaderBoard;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.BaseSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.ChallengesTourSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.GroupsTourSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.TourSummary;
import in.sportscafe.scgame.module.user.sportselection.dto.Sport;
import in.sportscafe.scgame.webservice.MyWebService;
import in.sportscafe.scgame.webservice.ScGameCallBack;
import retrofit2.Call;
import retrofit2.Response;

import static in.sportscafe.scgame.Constants.BundleKeys;
import static in.sportscafe.scgame.Constants.LeaderBoardPeriods;

/**
 * Created by Jeeva on 10/6/16.
 */
public class PointsModelImpl implements PointsModel {

    private boolean mUserInput = false;

    private Integer mSelectedSportId;

    private Long mSelectedGroupId;

    private Integer mSelectedChallengeId;

    private BaseSummary mBaseSummary;

    private OnPointsModelListener mPointsModelListener;

    private ViewPagerAdapter mViewPagerAdapter;

    private int mSelectedPosition = 0;

    private PointsModelImpl(OnPointsModelListener listener, FragmentManager fm) {
        this.mPointsModelListener = listener;
        this.mViewPagerAdapter = new ViewPagerAdapter(fm);
    }

    public static PointsModel newInstance(OnPointsModelListener listener, FragmentManager fm) {
        return new PointsModelImpl(listener, fm);
    }

    @Override
    public void init(Bundle bundle) {
        mSelectedGroupId = bundle.getLong(BundleKeys.GROUP_ID);
        mSelectedSportId = bundle.getInt(BundleKeys.SPORT_ID);
        Log.i("SPORTID",mSelectedSportId+"");
        mSelectedChallengeId = bundle.getInt(BundleKeys.CHALLENGE_ID);
        mBaseSummary = (BaseSummary) bundle.getSerializable(BundleKeys.TOURNAMENT_SUMMARY);
    }

    @Override
    public String getName() {
        return mBaseSummary.getName();
    }

    @Override
    public String getIcon() {
        return mBaseSummary.getPhoto();
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

    private void callLbDetailApi(final Integer sportId, Long groupId, Integer challengeId) {
        MyWebService.getInstance().getLeaderBoardDetailRequest(
                sportId, groupId.intValue(), challengeId
        ).enqueue(new ScGameCallBack<LeaderBoardResponse>() {
            @Override
            public void onResponse(Call<LeaderBoardResponse> call, Response<LeaderBoardResponse> response) {
                if (response.isSuccessful()) {
                    List<LeaderBoard> leaderBoardList = response.body().getLeaderBoardList();
                    if (null == leaderBoardList || leaderBoardList.isEmpty()) {
                        mPointsModelListener.onEmpty();
                        return;
                    }

                    refreshAdapter(leaderBoardList);

                    mPointsModelListener.onSuccessLeaderBoard();
                } else {
                    mPointsModelListener.onFailureLeaderBoard(response.message());
                }
            }
        });
    }

    private void refreshAdapter(List<LeaderBoard> leaderBoardList) {
        Integer tourId = mBaseSummary.getTournamentId();

        Log.d("PointsModelImpl", "Selected Summary --> " + tourId);
        LeaderBoard leaderBoard;
        for (int i = 0; i < leaderBoardList.size(); i++) {
            leaderBoard = leaderBoardList.get(i);
            mViewPagerAdapter.addFragment(LeaderBoardFragment.newInstance(leaderBoard), leaderBoard.getTournamentName());

           if (null==tourId){
               mSelectedPosition = 0;
               }
            else if(tourId.equals(leaderBoard.getTournamentId())) {
                mSelectedPosition = i;
            }

            Log.d("PointsModelImpl", "LeaderBoard --> " + leaderBoard.getTournamentId());
        }

        mViewPagerAdapter.notifyDataSetChanged();
    }

    public interface OnPointsModelListener {


        void onFailureLeaderBoard(String message);

        void onSuccessLeaderBoard();

        void onEmpty();

        void onNoInternet();
    }
}