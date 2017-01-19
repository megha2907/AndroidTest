package in.sportscafe.nostragamus.module.user.leaderboard;

import android.content.Context;
import android.os.Bundle;

import com.jeeva.android.Log;

import java.util.Collections;
import java.util.Comparator;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.LeaderBoard;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoard;

/**
 * Created by Jeeva on 10/6/16.
 */
public class LeaderBoardModelImpl implements LeaderBoardModel {

    private LeaderBoardAdapter mLeaderBoardAdapter;

    private OnLeaderBoardModelListener onLeaderBoardModelListener;

    private LeaderBoard mleaderBoard;

    public static int SORT_TYPE = 0;

    private int mUserPosition = 0;

    Integer mName;

    private LeaderBoardModelImpl(OnLeaderBoardModelListener listener) {
        onLeaderBoardModelListener=listener;
    }

    public static LeaderBoardModel newInstance(OnLeaderBoardModelListener listener) {
        return new LeaderBoardModelImpl(listener);
    }

    @Override
    public void init(Bundle bundle) {

        mleaderBoard = (LeaderBoard) bundle.getSerializable(Constants.BundleKeys.LEADERBOARD_LIST);
    }

    private void checkEmpty() {
        if(mLeaderBoardAdapter.getItemCount() == 0) {
            onLeaderBoardModelListener.onEmpty();
        }
    }

    @Override
    public LeaderBoardAdapter getAdapter(Context context) {
        mLeaderBoardAdapter = new LeaderBoardAdapter(context);
        return mLeaderBoardAdapter;
    }

    @Override
    public int getUserPosition() {
        return mUserPosition;
    }

    private void refreshUserPosition() {

        Integer userId = Integer.valueOf(NostragamusDataHandler.getInstance().getUserId());

        UserLeaderBoard userLeaderBoard;

        for (int i = 0; i < mleaderBoard.getUserLeaderBoardList().size(); i++) {

            userLeaderBoard = mleaderBoard.getUserLeaderBoardList().get(i);

                if(userId.equals(userLeaderBoard.getUserId())) {
                    mUserPosition = i;
                    Log.i("userpos",String.valueOf(mUserPosition));
                    onLeaderBoardModelListener.setUserLeaderBoard(userLeaderBoard);


                }
            mLeaderBoardAdapter.add(userLeaderBoard);
        }

        mLeaderBoardAdapter.notifyDataSetChanged();
    }

    @Override
    public void sortAndRefreshLeaderBoard() {

            if (SORT_TYPE == 0) {
                Collections.sort(mleaderBoard.getUserLeaderBoardList(), UserLeaderBoard.UserRankComparator);
            } else {
                Collections.sort(mleaderBoard.getUserLeaderBoardList(), UserLeaderBoard.UserAccuracyComparator);
            }

            mLeaderBoardAdapter.clear();
            mLeaderBoardAdapter.notifyDataSetChanged();
            refreshUserPosition();
            checkEmpty();

    }


    public interface OnLeaderBoardModelListener {

        void onEmpty();

        void setUserLeaderBoard(UserLeaderBoard newuserLeaderBoard);
    }
}