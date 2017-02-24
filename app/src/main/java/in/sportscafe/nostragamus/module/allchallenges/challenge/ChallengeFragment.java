package in.sportscafe.nostragamus.module.allchallenges.challenge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.recyclerviewpager.RecyclerViewPager;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.play.myresultstimeline.TimelineFragment;

import static com.google.android.gms.analytics.internal.zzy.m;

/**
 * Created by Jeeva on 17/02/17.
 */
public class ChallengeFragment extends NostragamusFragment implements ChallengeView, View.OnClickListener {

    private ChallengePresenter mChallengePresenter;

    private RecyclerViewPager mRcvHorizontal;

    private RecyclerView mRcvVertical;

    private RelativeLayout mRlChallengeCount;

    private TextView tvChallengeNumber;

    private TextView tvChallengeTotalCount;

    private RelativeLayout mRlSwitch;

    private ImageButton mSwipeView;

    private ImageButton mListView;

    private FrameLayout mFlMatchHolder;

    private ChallengeTimelineFragment mTimelineFragment;

    public static ChallengeFragment newInstance(List<Challenge> challenges) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleKeys.CHALLENGE_LIST, Parcels.wrap(challenges));

        ChallengeFragment fragment = new ChallengeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_challenges, container, false);
    }

    private LinearLayoutManager mVerticalManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getChildFragmentManager().beginTransaction().replace(R.id.challenges_fl_match_holder,
                mTimelineFragment = ChallengeTimelineFragment.newInstance()).commit();

        mRcvHorizontal = (RecyclerViewPager) findViewById(R.id.challenges_rcv_horizontal);
        mRcvHorizontal.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRcvHorizontal.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {
            @Override
            public void OnPageChanged(int fromPosition, int toPosition) {
                mCurrentPosition = toPosition;
                updateChallengeCount(toPosition);
                updateMatchesToCurrentPosition();
            }
        });
        mRcvHorizontal.setHasFixedSize(true);

        mRcvVertical = (RecyclerView) findViewById(R.id.challenges_rcv_vertical);
        mRcvVertical.setLayoutManager(mVerticalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRcvVertical.setHasFixedSize(true);

        mRlChallengeCount = (RelativeLayout) findViewById(R.id.challenges_count_rl);
        tvChallengeNumber = (TextView) findViewById(R.id.challenges_count_tv);
        tvChallengeTotalCount = (TextView) findViewById(R.id.challenges_total_count_tv);

        mRlSwitch = (RelativeLayout) findViewById(R.id.challenges_switch_view_rl);
        mRlSwitch.setOnClickListener(this);
        mSwipeView = (ImageButton) findViewById(R.id.challenges_swipe_view);
        mListView = (ImageButton) findViewById(R.id.challenges_list_view);
        mSwipeView.setSelected(true);
        mListView.setSelected(false);

        mFlMatchHolder = (FrameLayout) findViewById(R.id.challenges_fl_match_holder);

        this.mChallengePresenter = ChallengePresenterImpl.newInstance(this);
        this.mChallengePresenter.onCreateChallenge(getArguments());
    }

    @Override
    public void setSwipeAdapter(RecyclerView.Adapter adapter) {
        mRcvHorizontal.setAdapter(adapter, true);

        tvChallengeTotalCount.setText("/" + String.valueOf(adapter.getItemCount()));

        mTimelineFragment.addInitialMatches(getCurrentChallengeMatches());
    }

    private void updateMatchesToCurrentPosition() {
        mTimelineFragment.refreshMatches(getCurrentChallengeMatches());
    }

    private List<Match> getCurrentChallengeMatches() {
        return ((ChallengeAdapter) mRcvHorizontal.getAdapter()).getItem(mCurrentPosition).getMatches();
    }

    @Override
    public void setListAdapter(RecyclerView.Adapter adapter) {
        mRcvVertical.setAdapter(adapter);
    }

    private void updateChallengeCount(int currentPosition) {
        tvChallengeNumber.setText("Challenge " + String.valueOf(currentPosition + 1));
    }

    private int mFirstItemY = 0;

    private int mCurrentPosition = 0;

    @Override
    public void onClick(View view) {
        if (mFirstItemY == 0) {
            int coords[] = new int[2];
            mRcvVertical.getChildAt(0).getLocationInWindow(coords);
            mFirstItemY = coords[1];
        }

        switch (view.getId()) {
            case R.id.challenges_switch_view_rl:
                if (mListView.isSelected()) {
                    switchToSwipeView();
                    updateMatchesToCurrentPosition();
                } else {
                    switchToListView();
                }
                break;
        }
    }

    private void switchToSwipeView() {
        mCurrentPosition = 0;

        View child;
        int coords[] = new int[2];
        int diffY = 0;

        for (int i = 0; i < mRcvVertical.getChildCount(); i++) {
            child = mRcvVertical.getChildAt(i);
            child.getLocationInWindow(coords);

            diffY = coords[1] - mFirstItemY;
            if (diffY >= 0) {
                mCurrentPosition = mVerticalManager.getPosition(child);
                break;
            }
        }

        mRcvHorizontal.getLayoutManager().scrollToPosition(mCurrentPosition);

        if (diffY != 0) {
            mRcvVertical.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                        mRcvVertical.clearOnScrollListeners();
                        verticalSlideBottomItems(false);
                    }
                }
            });
            mRcvVertical.smoothScrollBy(0, diffY);
        } else {
            verticalSlideBottomItems(false);
        }
    }

    private void switchToListView() {
        mCurrentPosition = mRcvHorizontal.getCurrentPosition();
        mRcvVertical.scrollBy(0, mCurrentPosition * mRcvVertical.getChildAt(0).getMeasuredHeight());
        horizontalSlideLeftRightItems(false);
//        hideMatches();
    }

    private void verticalSlideBottomItems(final boolean in) {
        int childCount = mRcvVertical.getChildCount();
        if (childCount > 1 && mCurrentPosition < mRcvVertical.getAdapter().getItemCount() - 1) {
            Animation slideAnim = getSlideAnim(in, true, false);
            slideAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (!in) {
                        showSwipeView();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            mRcvVertical.getChildAt(childCount - 1).startAnimation(slideAnim);
        } else {
            if (!in) {
                showSwipeView();
            }
        }

        if (in) {
            hideMatches();
        } else {
            showMatches();
        }
    }

    private void horizontalSlideLeftRightItems(final boolean in) {
        int currentPosition = mRcvHorizontal.getCurrentPosition();
        int childCount = mRcvHorizontal.getChildCount();
        if (childCount > 1) {
            Animation slideAnim = null;
            if (currentPosition > 0) {
                slideAnim = getSlideAnim(in, false, true);
                mRcvHorizontal.getChildAt(0).startAnimation(slideAnim);
            }

            if (currentPosition < mRcvHorizontal.getAdapter().getItemCount() - 1) {
                slideAnim = getSlideAnim(in, false, false);
                mRcvHorizontal.getChildAt(childCount - 1).startAnimation(slideAnim);
            }

            if (null != slideAnim) {
                slideAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (!in) {
                            showListView();
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        } else {
            if (!in) {
                showListView();
            }
        }
    }

    private void showSwipeView() {
        mRlChallengeCount.setVisibility(View.VISIBLE);

        mRcvHorizontal.setVisibility(View.VISIBLE);
        mRcvVertical.setVisibility(View.INVISIBLE);

        mSwipeView.setSelected(true);
        mListView.setSelected(false);
        mRcvVertical.scrollToPosition(0);
        horizontalSlideLeftRightItems(true);
        //show matches
    }

    private void showListView() {
        mRlChallengeCount.setVisibility(View.GONE);

        mRcvHorizontal.setVisibility(View.INVISIBLE);
        mRcvVertical.setVisibility(View.VISIBLE);

        mListView.setSelected(true);
        mSwipeView.setSelected(false);
        verticalSlideBottomItems(true);
    }

    private void showMatches() {
        AlphaAnimation animation = new AlphaAnimation(0f, 1f);
        animation.setFillAfter(true);
        animation.setDuration(500);
//        animation.setStartOffset(250);
        mFlMatchHolder.startAnimation(animation);
    }

    private void hideMatches() {
        AlphaAnimation animation = new AlphaAnimation(1f, 0f);
        animation.setFillAfter(true);
        animation.setDuration(500);
        mFlMatchHolder.startAnimation(animation);
    }

    private Animation getSlideAnim(boolean in, boolean vertical, boolean negative) {
        int x = 0;
        int y = 0;

        if (vertical) {
            y = mRcvVertical.getChildAt(0).getMeasuredHeight();
        } else {
            x = mRcvHorizontal.getPaddingLeft();
        }

        if (negative) {
            x = -x;
            y = -y;
        }

        if (in) {
            return getSlideAnim(x, 0, y, 0);
        }
        return getSlideAnim(0, x, 0, y);
    }

    private Animation getSlideAnim(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta) {
        TranslateAnimation animation = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
        animation.setDuration(500);
//        animation.setFillAfter(true);
        return animation;
    }
}