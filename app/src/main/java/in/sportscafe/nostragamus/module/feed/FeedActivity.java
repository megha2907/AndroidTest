package in.sportscafe.nostragamus.module.feed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.utils.ViewUtils;

/**
 * Created by Jeeva on 15/6/16.
 */
public class FeedActivity extends NostragamusActivity implements FeedView, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRcvFeed;

    private FeedPresenter mFeedPresenter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Toolbar mtoolbar;

    Bundle bundle;

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

        this.mFeedPresenter = FeedPresenterImpl.newInstance(this);
        this.mFeedPresenter.onCreateFeed(getIntent().getExtras());

        bundle = getIntent().getExtras();

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
    public void setAdapter(FeedAdapter feedAdapter) {
        mRcvFeed.setAdapter(ViewUtils.getAnimationAdapter(feedAdapter));
    }

    @Override
    public void moveAdapterPosition(int movePosition) {
        mRcvFeed.getLayoutManager().scrollToPosition(movePosition);
    }

    @Override
    public void dismissSwipeRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mFeedPresenter.onRefresh();
    }

    public void initToolBar() {
        mtoolbar = (Toolbar) findViewById(R.id.feed_toolbar);
        mtoolbar.setTitle(bundle.getString(Constants.BundleKeys.TOURNAMENT_NAME));
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationIcon(R.drawable.back_icon_grey);
        mtoolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(i);
                    }
                }

        );
    }

    @Override
    public void onBackPressed(){

        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(i);
        finish();
    }
}