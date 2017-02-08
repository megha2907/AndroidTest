package in.sportscafe.nostragamus.module.tournamentFeed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.home.OnHomeActionListener;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentInfo;
import in.sportscafe.nostragamus.utils.ViewUtils;

/**
 * Created by deepanshi on 9/29/16.
 */

public class TournamentFeedFragment extends NostragamusFragment implements TournamentFeedView {

    private RecyclerView mRcvTournamentFeed;

    private TournamentFeedPresenter mtournamentFeedPresenter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static TournamentFeedFragment newInstance(TournamentInfo tournamentInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleKeys.TOURNAMENT_LIST, Parcels.wrap(tournamentInfo));

        TournamentFeedFragment fragment = new TournamentFeedFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tournament_feed, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mRcvTournamentFeed = (RecyclerView) findViewById(R.id.tournament_feed_rv);
        this.mRcvTournamentFeed.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        this.mRcvTournamentFeed.setHasFixedSize(true);

        this.mtournamentFeedPresenter = TournamentFeedPresenterImpl.newInstance(this);
        this.mtournamentFeedPresenter.onCreateFeed((OnHomeActionListener) getActivity(), getArguments());

//        this.mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swifeRefresh);
//        this.mSwipeRefreshLayout.setOnRefreshListener(this);

//        this.mSwipeRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                mSwipeRefreshLayout.setRefreshing(false);
//                onRefresh();
//            }
//        });

    }

    @Override
    public void setAdapter(TournamentFeedAdapter tournamentFeedAdapter) {
        mRcvTournamentFeed.setAdapter(tournamentFeedAdapter);
    }

    @Override
    public void moveAdapterPosition(int movePosition) {
        mRcvTournamentFeed.getLayoutManager().scrollToPosition(movePosition);
    }

//    @Override
//    public void dismissSwipeRefresh() {
//        mSwipeRefreshLayout.setRefreshing(false);
//    }

//    @Override
//    public void onRefresh() {
//        mtournamentFeedPresenter.onRefresh();
//    }


}