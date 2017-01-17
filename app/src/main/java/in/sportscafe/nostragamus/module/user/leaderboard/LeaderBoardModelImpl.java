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
        refreshLeaderBoard(bundle);
        checkEmpty();
    }

    private void checkEmpty() {
        if(mLeaderBoardAdapter.getItemCount() == 0) {
            onLeaderBoardModelListener.onEmpty();
        }
    }

    @Override
    public LeaderBoardAdapter getAdapter(Context context) {
        return mLeaderBoardAdapter = new LeaderBoardAdapter(context);
    }

    @Override
    public int getUserPosition() {
        return mUserPosition;
    }

    @Override
    public void refreshLeaderBoard(Bundle bundle) {
        mLeaderBoardAdapter.clear();

        Integer userId = Integer.valueOf(NostragamusDataHandler.getInstance().getUserId());

        LeaderBoard leaderBoard = (LeaderBoard) bundle.getSerializable(Constants.BundleKeys.LEADERBOARD_LIST);

        for (UserLeaderBoard userLeaderBoard : leaderBoard.getUserLeaderBoardList()) {


            if (userLeaderBoard.getUserId().equals(userId)){
                onLeaderBoardModelListener.setUserLeaderBoard(userLeaderBoard);
            }
            
            mLeaderBoardAdapter.add(userLeaderBoard);
            

            UserLeaderBoard newuserLeaderBoard;

            for (int i = 0; i < leaderBoard.getUserLeaderBoardList().size(); i++) {
                newuserLeaderBoard = leaderBoard.getUserLeaderBoardList().get(i);

                if(userId.equals(newuserLeaderBoard.getUserId())) {
                    mUserPosition = i;
                    Log.i("userpos",String.valueOf(mUserPosition));
                    
                }

            }

        }


        mLeaderBoardAdapter.notifyDataSetChanged();
    }


    public interface OnLeaderBoardModelListener {

        void onEmpty();

        void setUserLeaderBoard(UserLeaderBoard newuserLeaderBoard);
    }
}