package in.sportscafe.scgame.module.TournamentFeed;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.ScGameFragment;
import in.sportscafe.scgame.module.common.SpacesItemDecoration;
import in.sportscafe.scgame.module.home.OnHomeActionListener;

/**
 * Created by deepanshi on 9/29/16.
 */

public class TournamentFeedFragment extends ScGameFragment implements TournamentFeedView, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRcvTournamentFeed;

    private TournamentFeedPresenter mtournamentFeedPresenter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Toolbar mtoolbar;

    private TextView mTitle;

    private ImageView mLogo;

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

        this.mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swifeRefresh);
        this.mSwipeRefreshLayout.setOnRefreshListener(this);

        this.mtournamentFeedPresenter = TournamentFeedPresenterImpl.newInstance(this);
        this.mtournamentFeedPresenter.onCreateFeed((OnHomeActionListener) getActivity());

        this.mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });

        initToolBar();
    }

    @Override
    public void setAdapter(TournamentFeedAdapter tournamentFeedAdapter) {
        mRcvTournamentFeed.setAdapter(tournamentFeedAdapter);
    }

    @Override
    public void moveAdapterPosition(int movePosition) {
        mRcvTournamentFeed.getLayoutManager().scrollToPosition(movePosition);
    }

    @Override
    public void dismissSwipeRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mtournamentFeedPresenter.onRefresh();
    }

    public void initToolBar() {
        mtoolbar = (Toolbar) findViewById(R.id.tournament_toolbar);
        mLogo = (ImageView) mtoolbar.findViewById(R.id.toolbar_logo);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

}