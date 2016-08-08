package in.sportscafe.scgame.module.user.points;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.jeeva.android.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import in.sportscafe.scgame.ScGame;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.user.leaderboard.LeaderBoardResponse;
import in.sportscafe.scgame.module.user.leaderboard.dto.LeaderBoard;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;
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

    private boolean mInitialSetDone = false;

    private boolean mUserInput = false;

    private Integer mSelectedSportId;

    private Long mSelectedGroupId;

    private String mSelectedPeriod = LeaderBoardPeriods.ALL_TIME;

    private List<GroupInfo> mGroupsList = new ArrayList<>();

    private Map<Long, List<Sport>> mSportsListMap = new HashMap<>();

    private ArrayAdapter<Sport> mSportAdapter;

    private OnPointsModelListener mPointsModelListener;

    private PointsModelImpl(OnPointsModelListener listener) {
        this.mPointsModelListener = listener;
    }

    public static PointsModel newInstance(OnPointsModelListener listener) {
        return new PointsModelImpl(listener);
    }

    @Override
    public void init(Bundle bundle) {
        mSelectedGroupId = bundle.getLong(BundleKeys.GROUP_ID);
        mSelectedSportId = bundle.getInt(BundleKeys.SPORT_ID);

        if(mSelectedGroupId == 0) {
            mInitialSetDone = true;
            mSportsListMap.put(mSelectedGroupId, ScGameDataHandler.getInstance().getGlbFollowedSports());
        } else {
            Map<Long, GroupInfo> grpInfoMap = ScGameDataHandler.getInstance().getGrpInfoMap();

            mGroupsList = new ArrayList<>(grpInfoMap.values());

            Set<Long> grpInfoIterator = grpInfoMap.keySet();
            for (Long groupId : grpInfoIterator) {
                mSportsListMap.put(groupId, grpInfoMap.get(groupId).getFollowedSports());
            }
        }
    }

    @Override
    public ArrayAdapter<GroupInfo> getGroupAdapter(Context context) {
        if(mGroupsList.size() == 0) {
            return null;
        }

        return new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, mGroupsList);
    }

    @Override
    public ArrayAdapter<Sport> getSportsAdapter(Context context) {
        mSportAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
        updateSportsAdapter();
        return mSportAdapter;
    }

    @Override
    public int getInitialGroupPosition() {
        return mGroupsList.indexOf(new GroupInfo(mSelectedGroupId));
    }

    @Override
    public int getInitialSportPosition() {
        return mSportsListMap.get(mSelectedGroupId).indexOf(new Sport(mSelectedSportId));

    }

    @Override
    public void setInitialSetDone() {
//        this.mInitialSetDone = true;
    }

    private void updateSportsAdapter() {
        mSportAdapter.clear();
        for (Sport sport : mSportsListMap.get(mSelectedGroupId)) {
            mSportAdapter.add(sport);
        }
        mUserInput = false;
        mSportAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGroupSelected(int position) {
        if(mInitialSetDone) {
            mSelectedGroupId = mGroupsList.get(position).getId();
            mSelectedSportId = mSportsListMap.get(mSelectedGroupId).get(0).getId();

            updateSportsAdapter();

            refreshLeaderBoard();
        }
        mInitialSetDone = true;
    }

    @Override
    public void onSportSelected(int position) {
        if(mInitialSetDone) {
            if (mUserInput) {
                mSelectedSportId =mSportsListMap.get(mSelectedGroupId).get(position).getId();
                refreshLeaderBoard();
            }
            mUserInput = true;
        }
    }

    @Override
    public void onWeekSelected() {
        mSelectedPeriod = LeaderBoardPeriods.WEEK;
        refreshLeaderBoard();
    }

    @Override
    public void onMonthSelected() {
        mSelectedPeriod = LeaderBoardPeriods.MONTH;
        refreshLeaderBoard();
    }

    @Override
    public void onAllTimeSelected() {
        mSelectedPeriod = LeaderBoardPeriods.ALL_TIME;
        refreshLeaderBoard();
    }

    @Override
    public void refreshLeaderBoard() {
        mPointsModelListener.onGetLeaderBoardData(new ArrayList<LeaderBoard>());
        if(ScGame.getInstance().hasNetworkConnection()) {
            mPointsModelListener.onGettingLeaderBoard();
            callLbDetailApi(mSelectedGroupId, mSelectedSportId, mSelectedPeriod);
        } else {
            mPointsModelListener.onNoInternet();
        }
    }

    private void callLbDetailApi(Long groupId, Integer sportId, String rankPeriod) {
        MyWebService.getInstance().getLeaderBoardDetailRequest(
                groupId, sportId, rankPeriod
        ).enqueue(new ScGameCallBack<LeaderBoardResponse>() {
            @Override
            public void onResponse(Call<LeaderBoardResponse> call, Response<LeaderBoardResponse> response) {
                if(response.isSuccessful()) {
                    List<LeaderBoard> leaderBoardList = response.body().getLeaderBoardList();
                    if(null == leaderBoardList || leaderBoardList.isEmpty()) {
                        mPointsModelListener.onEmpty();
                        return;
                    }

                    mPointsModelListener.onGetLeaderBoardData(leaderBoardList);
                } else {
                    mPointsModelListener.onFailureLeaderBoard(response.message());
                }
            }
        });
    }

    public interface OnPointsModelListener {

        void onGettingLeaderBoard();

        void onFailureLeaderBoard(String message);

        void onGetLeaderBoardData(List<LeaderBoard> leaderBoardList);

        void onEmpty();

        void onNoInternet();
    }
}