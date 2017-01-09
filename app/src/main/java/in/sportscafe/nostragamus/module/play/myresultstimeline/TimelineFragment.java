package in.sportscafe.nostragamus.module.play.myresultstimeline;

/**
 * Created by deepanshi on 10/5/16.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.home.HomeActivity;

/**
 * Created by Jeeva on 15/6/16.
 */
public class TimelineFragment extends NostragamusFragment implements MyResultsTimelineView, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRcvFeed;

    private MyResultsTimelinePresenter myResultsTimelinePresenter;

//    private SwipeRefreshLayout mSwipeRefreshLayout;

    private LinearLayoutManager mLinearLayoutManager;

    public static TimelineFragment newInstance(String playerUserId) {
        Bundle args = new Bundle();
        args.putString(BundleKeys.PLAYER_USER_ID, playerUserId);

        TimelineFragment fragment = new TimelineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mRcvFeed = (RecyclerView) findViewById(R.id.feed_rv);

        mLinearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        this.mRcvFeed.setLayoutManager(mLinearLayoutManager);

        // It is to find the scrolling stage to do the pagination
        mRcvFeed.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                myResultsTimelinePresenter.onTimelineScroll(mLinearLayoutManager.findFirstVisibleItemPosition(),
                        mLinearLayoutManager.getChildCount(), mLinearLayoutManager.getItemCount());
            }
        });

        this.mRcvFeed.setHasFixedSize(true);

        /*this.mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swifeRefresh);
        this.mSwipeRefreshLayout.setOnRefreshListener(this);*/

        this.myResultsTimelinePresenter = MyResultsTimelinePresenterImpl.newInstance(this);
        this.myResultsTimelinePresenter.onCreateFeed(getArguments());

        /*this.mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });*/
    }

    @Override
    public void setAdapter(MyResultsTimelineAdapter myResultsTimelineAdapter) {
        mRcvFeed.setAdapter(myResultsTimelineAdapter);
    }

    @Override
    public void moveAdapterPosition(int movePosition) {
        mRcvFeed.getLayoutManager().scrollToPosition(0);
    }

    @Override
    public void dismissSwipeRefresh() {
//        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        myResultsTimelinePresenter.onRefresh();
    }

}