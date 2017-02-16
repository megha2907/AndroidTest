package in.sportscafe.nostragamus.module.user.comparisons.compareLeaderBoards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;

/**
 * Created by deepanshi on 2/14/17.
 */

public class CompareLeaderBoardFragment extends NostragamusFragment implements CompareLeaderBoardView {


    private RecyclerView mRvLeaderBoardComp;

    private CompareLeaderBoardPresenter mcompareLeaderBoardPresenter;

    public static CompareLeaderBoardFragment newInstance(Bundle bundle) {
        CompareLeaderBoardFragment fragment = new CompareLeaderBoardFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_compare_leaderboard, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mRvLeaderBoardComp = (RecyclerView) findViewById(R.id.compare_leaderboard_rcv);
        this.mRvLeaderBoardComp.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        this.mRvLeaderBoardComp.setHasFixedSize(true);
        this.mcompareLeaderBoardPresenter = CompareLeaderBoardPresenterImpl.newInstance(this);
        this.mcompareLeaderBoardPresenter.onCreateCompareLeaderBoard(getArguments());
        
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        this.mRvLeaderBoardComp.setAdapter(adapter);
    }

    @Override
    public void noCommonLeaderBoards() {

    }

}
