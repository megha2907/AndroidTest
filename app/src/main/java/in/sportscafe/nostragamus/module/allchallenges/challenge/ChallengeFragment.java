package in.sportscafe.nostragamus.module.allchallenges.challenge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.recyclerviewpager.RecyclerViewPager;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;

import static android.R.attr.animation;
import static android.R.attr.fromXDelta;
import static android.R.attr.fromYDelta;
import static android.R.attr.toXDelta;
import static android.R.attr.toYDelta;

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

    private View mVSwitchSeek;

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
                updateChallengeCount();
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
        mVSwitchSeek = findViewById(R.id.challenges_v_switch_seek);
        mFlMatchHolder = (FrameLayout) findViewById(R.id.challenges_fl_match_holder);

        this.mChallengePresenter = ChallengePresenterImpl.newInstance(this);
        this.mChallengePresenter.onCreateChallenge(getArguments());
    }

    @Override
    public void setSwipeAdapter(RecyclerView.Adapter adapter) {
        mRcvHorizontal.setAdapter(adapter, true);

        tvChallengeTotalCount.setText("/ " + String.valueOf(adapter.getItemCount()));

        Challenge currentChallenge = getCurrentChallenge();
        mTimelineFragment.addInitialMatches(currentChallenge.getMatches(), currentChallenge.getCountMatchesLeft());
    }

    private void updateMatchesToCurrentPosition() {
        Challenge currentChallenge = getCurrentChallenge();
        mTimelineFragment.refreshMatches(currentChallenge.getMatches(), currentChallenge.getCountMatchesLeft());
    }

    private Challenge getCurrentChallenge() {
        return ((ChallengeAdapter) mRcvHorizontal.getAdapter()).getItem(mCurrentPosition);
    }

    @Override
    public void setListAdapter(RecyclerView.Adapter adapter) {
        mRcvVertical.setAdapter(adapter);
    }

    private void updateChallengeCount() {
        tvChallengeNumber.setText("Challenge " + String.valueOf(mCurrentPosition + 1));
    }

    private int mFirstItemY = 0;

    private int mYDelta = 0;

    private int mCurrentPosition = 0;

    private int mChildHeight = 0;

    private boolean mSwipeViewSelected = true;

    @Override
    public void onClick(View view) {
        if (mFirstItemY == 0) {
            int coords[] = new int[2];
            mRcvVertical.getChildAt(0).getLocationInWindow(coords);
            mFirstItemY = coords[1];
            mYDelta = getResources().getDimensionPixelSize(R.dimen.dp_40);
            if (mRcvVertical.getChildCount() > 1) {
                mChildHeight = mRcvVertical.getChildAt(1).getMeasuredHeight();
            }
        }

        switch (view.getId()) {
            case R.id.challenges_switch_view_rl:
                if (mSwipeViewSelected) {
                    switchToListView();
                } else {
                    switchToSwipeView(findDiff(-1));
                }
                break;
        }
    }

    private int findDiff(int position) {
        mCurrentPosition = 0;

        View child;
        int coords[] = new int[2];
        int diffY = 0;

        for (int i = 0; i < mRcvVertical.getChildCount(); i++) {
            child = mRcvVertical.getChildAt(i);
            mCurrentPosition = mVerticalManager.getPosition(child);

            child.getLocationInWindow(coords);
            diffY = coords[1] - mFirstItemY;

            if (position == -1) {
                if (diffY >= 0) {
                    return diffY;
                }
            } else if (mCurrentPosition == position) {
                return diffY;
            }
        }

        return diffY;
    }

    private void switchToSwipeView(int diffY) {
        if (mCurrentPosition != 0) {
//            diffY += mYDelta;
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
        showChallengeCount();
        moveSeekToSwipe();
        mSwipeViewSelected = true;
        updateMatchesToCurrentPosition();
    }

    private void switchToListView() {
        mCurrentPosition = mRcvHorizontal.getCurrentPosition();
        mRcvVertical.scrollBy(0, mCurrentPosition * mChildHeight);
        horizontalSlideLeftRightItems(false);
//        hideMatches();

        hideChallengeCount();
        moveSeekToList();
        mSwipeViewSelected = false;
    }

    private void verticalSlideBottomItems(final boolean in) {
        int childCount = mRcvVertical.getChildCount();
//        if (mCurrentPosition < mRcvVertical.getAdapter().getItemCount() - 1) {

        int i = 1;
        if (mCurrentPosition == 0) {
            i = 0;
        }

        View showGamesBg = mRcvVertical.getChildAt(i).findViewById(R.id.all_challenges_rl_anim_bg);
        if(in) {
            showShowGamesBg(showGamesBg);
        } else {
            hideShowGamesBg(showGamesBg);
        }

        if (childCount > i + 1) {
            TranslateAnimation slideAnim = getSlideAnim(in, true, false);
            if (in) {
                slideAnim.setDuration(300);
                slideAnim.setStartOffset(300);
            } else {
                slideAnim = new TranslateAnimation(0, 0, 0, 2 * (mRcvHorizontal.getChildAt(0).getMeasuredHeight() + mYDelta));
                slideAnim.setDuration(1000);
            }
            mRcvVertical.getChildAt(i + 1).startAnimation(slideAnim);
        }

        if (in) {
            hideMatches();
        } else {
            showMatches();
        }
    }

    private void hideShowGamesBg(View view) {
        Animation slideAnimation = new TranslateAnimation(0, 0, 0, mRcvHorizontal.getChildAt(0).getMeasuredHeight() + mYDelta);
        slideAnimation.setDuration(500);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 2f, 1f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1f);
        scaleAnimation.setDuration(500);
        scaleAnimation.setStartOffset(500);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(500);
        alphaAnimation.setStartOffset(500);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(slideAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                showSwipeView();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        view.startAnimation(animationSet);
    }

    private void showShowGamesBg(View view) {
        Animation slideAnimation = new TranslateAnimation(0, 0, mRcvHorizontal.getChildAt(0).getMeasuredHeight() + mYDelta, 0);
        slideAnimation.setDuration(300);
        slideAnimation.setStartOffset(300);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1.5f, 1f, 1.5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1f);
        scaleAnimation.setDuration(300);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(300);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(slideAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        view.startAnimation(animationSet);
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
        mRcvHorizontal.setVisibility(View.VISIBLE);
        mRcvVertical.setVisibility(View.INVISIBLE);

        mRcvVertical.scrollToPosition(0);
        horizontalSlideLeftRightItems(true);
        //show matches
    }

    private void showListView() {
        mRcvHorizontal.setVisibility(View.INVISIBLE);
        mRcvVertical.setVisibility(View.VISIBLE);

        verticalSlideBottomItems(true);
    }

    private void showChallengeCount() {
        updateChallengeCount();
        mRlChallengeCount.animate().alpha(1f).setDuration(500);
    }

    private void hideChallengeCount() {
        updateChallengeCount();
        mRlChallengeCount.animate().alpha(0f).setDuration(500);
    }

    private void moveSeekToSwipe() {
        mVSwitchSeek.animate().translationXBy(-getResources().getDimensionPixelSize(R.dimen.dp_35)).setDuration(500);
    }

    private void moveSeekToList() {
        mVSwitchSeek.animate().translationXBy(getResources().getDimensionPixelSize(R.dimen.dp_35)).setDuration(500);
    }

    private void showMatches() {
        Animation alphaAnimation = getAlphaAnimation(true);
        alphaAnimation.setStartOffset(650);
        mFlMatchHolder.startAnimation(alphaAnimation);
    }

    private void hideMatches() {
        mFlMatchHolder.startAnimation(getAlphaAnimation(false));
    }

    private Animation getAlphaAnimation(boolean show) {
        int fromAlpha = 1;
        if (show) {
            fromAlpha = 0;
        }
        AlphaAnimation animation = new AlphaAnimation(fromAlpha, Math.abs(1 - fromAlpha));
        animation.setFillAfter(true);
        animation.setDuration(500);
        return animation;
    }

    private TranslateAnimation getSlideAnim(boolean in, boolean vertical, boolean negative) {
        int x = 0;
        int y = 0;

        if (vertical) {
            y = mRcvVertical.getChildAt(0).getMeasuredHeight() + mYDelta;
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

    private TranslateAnimation getSlideAnim(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta) {
        TranslateAnimation animation = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
        animation.setDuration(500);
//        animation.setFillAfter(true);
        return animation;
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mChallengeItemClickReceiver,
                new IntentFilter(Constants.IntentActions.ACTION_CHALLENGE_CLICK));
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mChallengeItemClickReceiver);
    }

    BroadcastReceiver mChallengeItemClickReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switchToSwipeView(findDiff(intent.getIntExtra(BundleKeys.CLICK_POSITION, -1)));
        }
    };
}