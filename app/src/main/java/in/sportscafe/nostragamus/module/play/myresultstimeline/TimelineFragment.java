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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.common.OnHomeActionListener;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;
import in.sportscafe.nostragamus.utils.ViewUtils;
import in.sportscafe.nostragamus.utils.loadingAnim.LoadingIndicatorView;

/**
 * Created by Jeeva on 15/6/16.
 */
public class TimelineFragment extends NostragamusFragment implements TimelineView, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private static final float MAX_ROTATION = 15f;

    private float mVisibleHeight;

    private float mHalfVisibleHeight;

    private float mDifference;

    private RecyclerView mRcvFeed;

    private RelativeLayout mRlChallengeFilter;

    private TimelinePresenter myResultsTimelinePresenter;

    private LinearLayoutManager mLinearLayoutManager;

    private OnHomeActionListener mOnHomeActionListener;

    public static TimelineFragment newInstance(int roomId) {
        TimelineFragment fragment = new TimelineFragment();
        fragment.setArguments(getBundle(null, roomId));
        return fragment;
    }

    public static TimelineFragment newInstance(PlayerInfo playerInfo, Integer challengeId) {
        TimelineFragment fragment = new TimelineFragment();
        fragment.setArguments(getBundle(playerInfo, challengeId));
        return fragment;
    }

    private static Bundle getBundle(PlayerInfo playerInfo, int roomId) {
        Bundle args = new Bundle();

        if(null != playerInfo) {
            args.putInt(BundleKeys.PLAYER_USER_ID, playerInfo.getId());
            args.putString(BundleKeys.PLAYER_PHOTO, playerInfo.getPhoto());
            args.putString(BundleKeys.PLAYER_NAME, playerInfo.getUserNickName());
        }
            args.putInt(BundleKeys.ROOM_ID, roomId);

        return args;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        if ((null != bundle && bundle.containsKey(BundleKeys.PLAYER_USER_ID)) || null == savedInstanceState) {

            if(getActivity() instanceof OnHomeActionListener) {
                mOnHomeActionListener = (OnHomeActionListener) getActivity();
            }

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
                    if (null != getContext()) {
                        mVisibleHeight = mRcvFeed.getMeasuredHeight();
                        mHalfVisibleHeight = getResources().getDimensionPixelSize(R.dimen.dp_220);
                        mDifference = mVisibleHeight - mHalfVisibleHeight;
                    }
                }
            });

            mRcvFeed.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                    if (mAppbarExpanded) {
                        View child = null;
                        int[] location = new int[2];
                        int yAxis;
                        int childCount = parent.getChildCount();
                        for (int i = 0; i < childCount; i++) {
                            child = parent.getChildAt(i).findViewById(R.id.schedule_row_ll);

                            if (child.getVisibility() == View.VISIBLE) {
                                child.setPivotY(child.getMeasuredHeight());
                                child.getLocationOnScreen(location);
                                child.setRotationX(getRotationByY(location[1]));
                            }
                        }
                    }
                    super.onDraw(c, parent, state);
                }
            });

            mRlChallengeFilter = (RelativeLayout) findViewById(R.id.timeline_rl_challenge_filter);
            mRlChallengeFilter.setOnClickListener(this);

            this.myResultsTimelinePresenter = TimelinePresenterImpl.newInstance(this);
            this.myResultsTimelinePresenter.onCreateFeed(bundle);
        }
    }

    @Override
    public void setAdapter(TimelineAdapter myResultsTimelineAdapter) {
        mRcvFeed.setAdapter(ViewUtils.getAnimationAdapter(myResultsTimelineAdapter));
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
//        myResultsTimelinePresenter.onRefresh();
    }

    private float getRotationByY(int yAxis) {
        float rotation = MAX_ROTATION * (yAxis - mHalfVisibleHeight) / mDifference;
        if (rotation < 0) {
            return 0;
        }
        return rotation;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (null != myResultsTimelinePresenter) {
            myResultsTimelinePresenter.onDestroy();
        }
    }

    private boolean mAppbarExpanded = false;

    public void setAppbarExpanded(boolean expanded) {
        mAppbarExpanded = expanded;
    }

    @Override
    public void showTimelineEmpty(Boolean isMyProfile) {
        TextView mTvEmptyResultsTimeline = (TextView) findViewById(R.id.result_timeline_empty_tv);
        ImageView mIvEmptyResultsTimeline = (ImageView) findViewById(R.id.result_timeline_empty_iv);
        Button mBtnPlayChallenge = (Button) findViewById(R.id.leaderboard_play_challenge_btn);

        if (isMyProfile) {
            mTvEmptyResultsTimeline.setVisibility(View.VISIBLE);
            mIvEmptyResultsTimeline.setVisibility(View.VISIBLE);
            mBtnPlayChallenge.setVisibility(View.VISIBLE);
            mBtnPlayChallenge.setOnClickListener(this);
        }else {
            mTvEmptyResultsTimeline.setVisibility(View.VISIBLE);
            mIvEmptyResultsTimeline.setVisibility(View.VISIBLE);
            mTvEmptyResultsTimeline.setText("User has not played any matches yet.");
        }
    }

    @Override
    public void setChallengeFilter(String challengeName) {
        mRlChallengeFilter.setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.timeline_tv_challenge_name)).setText(challengeName);
    }

    @Override
    public void removeChallengeFilter() {
        mRlChallengeFilter.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leaderboard_play_challenge_btn:
                if(null != mOnHomeActionListener) {
                    mOnHomeActionListener.onClickChallenges();
                }
                break;
            case R.id.timeline_rl_challenge_filter:
               // myResultsTimelinePresenter.onClickFilter();
                break;
        }
    }

    @Override
    public void startProgressAnim() {
        LoadingIndicatorView loadingIndicatorView = (LoadingIndicatorView) findViewById(R.id.other_timeline_loading_anim);
        loadingIndicatorView.show();
    }

    @Override
    public void stopProgressAnim() {
        LoadingIndicatorView loadingIndicatorView = (LoadingIndicatorView) findViewById(R.id.other_timeline_loading_anim);
        loadingIndicatorView.hide();
    }
}