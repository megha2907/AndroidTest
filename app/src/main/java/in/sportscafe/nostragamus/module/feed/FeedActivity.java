package in.sportscafe.nostragamus.module.feed;

import android.content.Intent;
import android.graphics.Canvas;
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
import in.sportscafe.nostragamus.module.play.myresultstimeline.TimelineAdapter;
import in.sportscafe.nostragamus.utils.ViewUtils;

/**
 * Created by Jeeva on 15/6/16.
 */
public class FeedActivity extends NostragamusActivity implements FeedView, SwipeRefreshLayout.OnRefreshListener {

    private static final float MAX_ROTATION = 30;

    private float mVisibleHeight;

    private float mHalfVisibleHeight;

    private float mDifference;

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

        initRotation();
    }

    private void initRotation() {
        mRcvFeed.post(new Runnable() {
            @Override
            public void run() {
                mVisibleHeight = findViewById(R.id.content).getMeasuredHeight();
                mHalfVisibleHeight = getResources().getDimensionPixelSize(R.dimen.dp_150);
                mDifference = mVisibleHeight - mHalfVisibleHeight;
            }
        });

        mRcvFeed.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                View child = null;
                int[] location = new int[2];
                int yAxis;
                int childCount = parent.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    child = parent.getChildAt(i).findViewById(R.id.schedule_row_ll);

                    if(child.getVisibility() == View.VISIBLE) {
                        child.setPivotY(child.getMeasuredHeight());
                        child.getLocationOnScreen(location);
                        child.setRotationX(getRotationByY(location[1]));

//                        getRotationWidth(child.getMeasuredWidth(), rotation);

                    }
                }
                super.onDraw(c, parent, state);
            }
        });
    }

    private float getRotationByY(int yAxis) {
        float rotation = MAX_ROTATION * (yAxis - mHalfVisibleHeight) / mDifference;
        if(rotation < 0) {
            return 0;
        }
        return rotation;
    }

    @Override
    public void setAdapter(TimelineAdapter feedAdapter) {
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
                        onBackPressed();
                    }
                }

        );
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFeedPresenter.onDestroy();
    }
}