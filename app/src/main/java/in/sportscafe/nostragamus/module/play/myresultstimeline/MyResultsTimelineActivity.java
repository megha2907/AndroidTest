package in.sportscafe.nostragamus.module.play.myresultstimeline;

/**
 * Created by deepanshi on 10/5/16.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.home.HomeActivity;

/**
 * Created by Jeeva on 15/6/16.
 */
public class MyResultsTimelineActivity extends NostragamusActivity implements MyResultsTimelineView, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRcvFeed;

    private MyResultsTimelinePresenter myResultsTimelinePresenter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Toolbar mtoolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        this.mRcvFeed = (RecyclerView) findViewById(R.id.feed_rv);
        this.mRcvFeed.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        this.mRcvFeed.setHasFixedSize(true);

        this.mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swifeRefresh);
        this.mSwipeRefreshLayout.setOnRefreshListener(this);

        this.myResultsTimelinePresenter = MyResultsTimelinePresenterImpl.newInstance(this);
        this.myResultsTimelinePresenter.onCreateFeed();

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
    public void setAdapter(MyResultsTimelineAdapter myResultsTimelineAdapter) {
        mRcvFeed.setAdapter(myResultsTimelineAdapter);
    }

    @Override
    public void moveAdapterPosition(int movePosition) {
        mRcvFeed.getLayoutManager().scrollToPosition(0);
    }

    @Override
    public void dismissSwipeRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onRefresh() {
        myResultsTimelinePresenter.onRefresh();
    }

    public void initToolBar() {
        mtoolbar = (Toolbar) findViewById(R.id.feed_toolbar);
        mtoolbar.setTitle("My Points");
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationIcon(R.drawable.back_icon_grey);
        mtoolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoHomeActivity();
                    }
                }

        );
    }

    private void gotoHomeActivity() {
        Intent homeintent = new Intent(this, HomeActivity.class);
        homeintent.putExtra("results", "openprofile");
        startActivity(homeintent);
        finish();
    }

}
