package in.sportscafe.nostragamus.module.user.leaderboard;

import android.content.Context;
import android.os.Bundle;

import com.jeeva.android.Log;

import org.parceler.Parcels;

import java.util.Collections;
import java.util.Comparator;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.LeaderBoard;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoard;
import in.sportscafe.nostragamus.module.user.points.pointsFragment.dto.UserLeaderBoardInfo;
import in.sportscafe.nostragamus.module.user.points.pointsFragment.dto.UserLeaderBoardRequest;
import in.sportscafe.nostragamus.module.user.points.pointsFragment.dto.UserLeaderBoardResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 10/6/16.
 */
public class LeaderBoardModelImpl implements LeaderBoardModel {

    private LeaderBoardAdapter mLeaderBoardAdapter;

    private OnLeaderBoardModelListener onLeaderBoardModelListener;

    private Integer mRoomId;

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
        callLbDetailApi();
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

        UserLeaderBoardRequest userLeaderBoardRequest = new UserLeaderBoardRequest();
        userLeaderBoardRequest.setRoomId(mRoomId);

        MyWebService.getInstance().getUserLeaderBoardDetails(userLeaderBoardRequest)
                .enqueue(new NostragamusCallBack<UserLeaderBoardResponse>() {
                    @Override
                    public void onResponse(Call<UserLeaderBoardResponse> call, Response<UserLeaderBoardResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {

                            mUserLeaderBoardInfo = response.body().getUserLeaderBoardInfo();
                            if (null ==  mUserLeaderBoardInfo) {
                                onLeaderBoardModelListener.onEmpty();
                                return;
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

    @Override
    public void sortAndRefreshLeaderBoard() {

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

        mLeaderBoardAdapter.clear();
        mLeaderBoardAdapter.setPositionSelected(SORT_TYPE);
        mLeaderBoardAdapter.notifyDataSetChanged();
        refreshUserPosition();
        checkEmpty();

    }


    public interface OnLeaderBoardModelListener {

        void onEmpty();

    }
}