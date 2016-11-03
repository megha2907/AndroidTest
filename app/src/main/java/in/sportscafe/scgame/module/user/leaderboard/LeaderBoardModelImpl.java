package in.sportscafe.scgame.module.user.leaderboard;

import android.content.Context;
import android.os.Bundle;

import com.jeeva.android.Log;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.user.leaderboard.dto.LeaderBoard;
import in.sportscafe.scgame.module.user.leaderboard.dto.UserLeaderBoard;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.SportSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.TourSummary;
import in.sportscafe.scgame.webservice.MyWebService;
import in.sportscafe.scgame.webservice.ScGameCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 10/6/16.
 */
public class LeaderBoardModelImpl implements LeaderBoardModel {

    private LeaderBoardAdapter mLeaderBoardAdapter;

    private OnLeaderBoardModelListener onLeaderBoardModelListener;

    private int mUserPosition = 0;

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

        Integer userId = Integer.valueOf(ScGameDataHandler.getInstance().getUserId());

        LeaderBoard leaderBoard = (LeaderBoard) bundle.getSerializable(Constants.BundleKeys.LEADERBOARD_LIST);
        for (UserLeaderBoard userLeaderBoard : leaderBoard.getUserLeaderBoardList()) {
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
    }
}