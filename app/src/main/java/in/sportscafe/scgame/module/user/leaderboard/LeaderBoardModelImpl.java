package in.sportscafe.scgame.module.user.leaderboard;

import android.content.Context;
import android.os.Bundle;

import java.util.List;

import in.sportscafe.scgame.module.user.leaderboard.dto.LeaderBoard;
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

    private String mRankPeriod;

    private LeaderBoardModelImpl(OnLeaderBoardModelListener listener) {
        onLeaderBoardModelListener=listener;
    }

    public static LeaderBoardModel newInstance(OnLeaderBoardModelListener listener) {
        return new LeaderBoardModelImpl(listener);
    }

    @Override
    public void init(Bundle bundle) {
        mRankPeriod=bundle.getString("Time");
    }

    @Override
    public LeaderBoardAdapter getAdapter(Context context) {
        mLeaderBoardAdapter = new LeaderBoardAdapter(context);
        return mLeaderBoardAdapter;
    }

    @Override
    public void refreshLeaderBoard(Bundle bundle) {
        mLeaderBoardAdapter.clear();
        mLeaderBoardAdapter.notifyDataSetChanged();
        callLbDetailApi(bundle.getLong("GroupId"),bundle.getInt("SportId"));
    }

    private void callLbDetailApi(Long groupId, Integer sportId) {
        MyWebService.getInstance().getLeaderBoardDetailRequest(
                groupId, sportId, mRankPeriod
        ).enqueue(new ScGameCallBack<LeaderBoardResponse>() {
            @Override
            public void onResponse(Call<LeaderBoardResponse> call, Response<LeaderBoardResponse> response) {
                if(response.isSuccessful()) {
                    List<LeaderBoard> leaderBoardList = response.body().getLeaderBoardList();
                    if(null == leaderBoardList || leaderBoardList.isEmpty()) {
                        onLeaderBoardModelListener.onEmpty();
                        return;
                    }
                    mLeaderBoardAdapter.addAll(leaderBoardList);
                    mLeaderBoardAdapter.notifyDataSetChanged();
                    onLeaderBoardModelListener.onSuccessLeaderBoard();


                } else {
                    onLeaderBoardModelListener.onFailureLeaderBoard(response.message());
                }
            }
        });
    }

    public interface OnLeaderBoardModelListener {


        void onFailureLeaderBoard(String message);

        void onSuccessLeaderBoard();

        void onEmpty();

        void onNoInternet();
    }
}