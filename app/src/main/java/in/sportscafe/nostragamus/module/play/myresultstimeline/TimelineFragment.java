package in.sportscafe.nostragamus.module.play.myresultstimeline;

/**
 * Created by deepanshi on 10/5/16.
 */

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;

import static com.google.android.gms.analytics.internal.zzy.w;

/**
 * Created by Jeeva on 15/6/16.
 */
public class TimelineFragment extends NostragamusFragment implements TimelineView, SwipeRefreshLayout.OnRefreshListener {

    private static final float MAX_ROTATION = 20;

    private float mVisibleHeight;

    private float mHalfVisibleHeight;

    private float mDifference;

    private RecyclerView mRcvFeed;

    private TimelinePresenter myResultsTimelinePresenter;

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
                float rotation;
                int childCount = parent.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    child = parent.getChildAt(i).findViewById(R.id.schedule_row_ll);

                    if(child.getVisibility() == View.VISIBLE) {
                        child.setPivotY(child.getMeasuredHeight());
                        child.getLocationOnScreen(location);
                        child.setRotationX(rotation = getRotationByY(location[1]));

//                        getRotationWidth(child.getMeasuredWidth(), rotation);

                    }
                }
                super.onDraw(c, parent, state);
            }
        });

        this.myResultsTimelinePresenter = TimelinePresenterImpl.newInstance(this);
        this.myResultsTimelinePresenter.onCreateFeed(getArguments());
    }

    @Override
    public void setAdapter(TimelineAdapter myResultsTimelineAdapter) {
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

    private float getRotationByY(int yAxis) {
        float rotation = MAX_ROTATION * (yAxis - mHalfVisibleHeight) / mDifference;
        if(rotation < 0) {
            return 0;
        }
        return rotation;
    }

}