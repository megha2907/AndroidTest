package in.sportscafe.scgame.module.user.leaderboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.ScGameFragment;
import in.sportscafe.scgame.module.user.leaderboard.dto.LeaderBoard;
import in.sportscafe.scgame.module.user.points.OnLeaderBoardUpdateListener;

/**
 * Created by Jeeva on 10/6/16.
 */
public class LeaderBoardFragment extends ScGameFragment implements LeaderBoardView {

    private RecyclerView mRvLeaderBoard;

    private LeaderBoardPresenter mLeaderBoardPresenter;


    public static LeaderBoardFragment newInstance(LeaderBoard leaderBoard) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.BundleKeys.LEADERBOARD_LIST, leaderBoard);

        LeaderBoardFragment fragment = new LeaderBoardFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_leaderboard, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mRvLeaderBoard = (RecyclerView) findViewById(R.id.leaderboard_rcv);
        this.mRvLeaderBoard.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        this.mRvLeaderBoard.setHasFixedSize(true);

        this.mLeaderBoardPresenter = LeaderBoardPresenterImpl.newInstance(this);
        this.mLeaderBoardPresenter.onCreateLeaderBoard(getArguments());
    }

    @Override
    public void setLeaderBoardAdapter(LeaderBoardAdapter leaderBoardAdapter) {
        mRvLeaderBoard.setAdapter(leaderBoardAdapter);
    }

    @Override
    public void moveAdapterPosition(int movePosition) {
        Log.i("moveadapter",String.valueOf(movePosition));
        mRvLeaderBoard.getLayoutManager().scrollToPosition(movePosition);
    }

    public void refreshLeaderBoard(Bundle bundle) {
        mLeaderBoardPresenter.update(bundle);
    }
}