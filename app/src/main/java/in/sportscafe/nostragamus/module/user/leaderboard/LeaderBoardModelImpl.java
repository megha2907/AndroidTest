package in.sportscafe.nostragamus.module.user.leaderboard;

import android.content.Context;
import android.os.Bundle;

import com.jeeva.android.Log;

import org.parceler.Parcels;

import java.util.Collections;
import java.util.Comparator;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.challengeRewards.RewardsApiModelImpl;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.LeaderBoard;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoard;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoardInfo;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoardRequest;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoardResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 10/6/16.
 */
public class LeaderBoardModelImpl implements LeaderBoardModel {

    private static final String TAG = LeaderBoardModelImpl.class.getSimpleName();

    private LeaderBoardAdapter mLeaderBoardAdapter;

    private OnLeaderBoardModelListener onLeaderBoardModelListener;

    private Integer mRoomId = 0;

    private UserLeaderBoardInfo mUserLeaderBoardInfo;

    public static int SORT_TYPE = 0;

    private int mUserPosition = 0;

    Integer mName;

    private LeaderBoardModelImpl(OnLeaderBoardModelListener listener) {
        onLeaderBoardModelListener = listener;
    }

    public static LeaderBoardModel newInstance(OnLeaderBoardModelListener listener) {
        return new LeaderBoardModelImpl(listener);
    }

    @Override
    public void init(Bundle bundle) {
        if (bundle.containsKey(BundleKeys.ROOM_ID)) {
            mRoomId = bundle.getInt(BundleKeys.ROOM_ID);
        }
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            callLbDetailApi();
        } else {
            Log.d(TAG, "No Network connection");
            onLeaderBoardModelListener.onNoInternet();
        }
    }

    private void checkEmpty() {
        if (mLeaderBoardAdapter.getItemCount() == 0) {
            onLeaderBoardModelListener.onEmpty();
        }
    }

    @Override
    public LeaderBoardAdapter getAdapter(Context context) {
        mLeaderBoardAdapter = new LeaderBoardAdapter(context, mRoomId);
        return mLeaderBoardAdapter;
    }

    @Override
    public int getUserPosition() {
        return mUserPosition;
    }

    private void callLbDetailApi() {

        MyWebService.getInstance().getUserLeaderBoardDetails(mRoomId)
                .enqueue(new NostragamusCallBack<UserLeaderBoardResponse>() {
                    @Override
                    public void onResponse(Call<UserLeaderBoardResponse> call, Response<UserLeaderBoardResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {

                            if (response.body()!=null) {
                                mUserLeaderBoardInfo = response.body().getUserLeaderBoardInfo();
                                if (null == mUserLeaderBoardInfo) {
                                    onLeaderBoardModelListener.onEmpty();
                                    return;
                                }else {
                                    sortAndRefreshLeaderBoard();
                                    updateUserLeaderBoard();
                                  //  onLeaderBoardModelListener.setSortSelectedPos(0);
                                }
                            }else {
                                onLeaderBoardModelListener.onEmpty();
                            }

                        } else {
                            onLeaderBoardModelListener.onEmpty();
                        }
                    }
                });
    }


    private void refreshUserPosition() {

        Integer userId = Integer.valueOf(NostragamusDataHandler.getInstance().getUserId());

        UserLeaderBoard userLeaderBoard;

        for (int i = 0; i < mUserLeaderBoardInfo.getUserLeaderBoardList().size(); i++) {

            userLeaderBoard = mUserLeaderBoardInfo.getUserLeaderBoardList().get(i);

            if (userId.equals(userLeaderBoard.getUserId())) {
                mUserPosition = i;
                Log.i("userpos", String.valueOf(mUserPosition));
            }
            mLeaderBoardAdapter.add(userLeaderBoard);
        }

        mLeaderBoardAdapter.notifyDataSetChanged();
    }

    public void updateUserLeaderBoard() {
        String userId = NostragamusDataHandler.getInstance().getUserId();
        for (UserLeaderBoard userLeaderBoard : mUserLeaderBoardInfo.getUserLeaderBoardList()) {
            if (userId.equalsIgnoreCase(userLeaderBoard.getUserId() + "")) {
                onLeaderBoardModelListener.setUserLeaderBoard(userLeaderBoard);
                return;
            }
        }

        onLeaderBoardModelListener.setUserLeaderBoard(null);
    }


    @Override
    public void sortAndRefreshLeaderBoard() {

        if (mUserLeaderBoardInfo!=null) {
            if (SORT_TYPE == 0) {
                Collections.sort(mUserLeaderBoardInfo.getUserLeaderBoardList(), UserLeaderBoard.UserRankComparator);
            } else if (SORT_TYPE == 1) {
                Collections.sort(mUserLeaderBoardInfo.getUserLeaderBoardList(), UserLeaderBoard.UserAccuracyComparator);
            } else if (SORT_TYPE == 2) {
                Collections.sort(mUserLeaderBoardInfo.getUserLeaderBoardList(), UserLeaderBoard.UserPowerUpsComparator);
            } else if (SORT_TYPE == 3) {
                Collections.sort(mUserLeaderBoardInfo.getUserLeaderBoardList(), UserLeaderBoard.UserMatchPointsComparator);
            } else {
                Collections.sort(mUserLeaderBoardInfo.getUserLeaderBoardList(), UserLeaderBoard.UserRankComparator);
            }
        }

        mLeaderBoardAdapter.clear();
        mLeaderBoardAdapter.setPositionSelected(SORT_TYPE);
        mLeaderBoardAdapter.notifyDataSetChanged();
        if (SORT_TYPE == 0){
            onLeaderBoardModelListener.setSortSelectedPos(0);
        }
        refreshUserPosition();
        checkEmpty();

    }


    public interface OnLeaderBoardModelListener {

        void onEmpty();

        void onNoInternet();

        void setSortSelectedPos(int sortType);

        void setUserLeaderBoard(UserLeaderBoard userLeaderBoard);
    }
}