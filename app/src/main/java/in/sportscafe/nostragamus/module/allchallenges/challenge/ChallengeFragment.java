package in.sportscafe.nostragamus.module.allchallenges.challenge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
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

import com.jeeva.android.Log;
import com.jeeva.android.widgets.recyclerviewpager.RecyclerViewPager;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.CustomLayoutManagerWithSmoothScroll;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;

/**
 * Created by Jeeva on 17/02/17.
 */
public class ChallengeFragment extends NostragamusFragment implements ChallengeView, View.OnClickListener {

    private static final String TAG = ChallengeFragment.class.getSimpleName();

    private ChallengePresenter mChallengePresenter;

    private RecyclerViewPager mRcvHorizontal;

    private RecyclerView mRcvVertical;

    private RelativeLayout mRlChallengeCount;

    private TextView tvChallengeNumber;

    private TextView tvChallengeTotalCount;

    private FrameLayout mFlMatchHolder;

    private ChallengeTimelineFragment mTimelineFragment;

    private boolean mIsViewLayoutSwipe = false;

    private RelativeLayout mRlSwitch;

    private View mVSwitchSeek;

    /**
     * To identify that this instance is serving which tab */
    private String mThisTabItemName = "";

    public static ChallengeFragment newInstance(List<Challenge> challenges,
                                                int tagId,
                                                String thisTabItemName) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleKeys.CHALLENGE_LIST, Parcels.wrap(challenges));
        bundle.putInt(BundleKeys.CHALLENGE_TAG_ID, tagId);
        bundle.putString(BundleKeys.TAB_ITEM_NAME, thisTabItemName);

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
        initMembers();

        mRlSwitch = (RelativeLayout) findViewById(R.id.challenges_switch_view_rl);
        mRlSwitch.setOnClickListener(this);
        mVSwitchSeek = findViewById(R.id.challenges_v_switch_seek);

        getChildFragmentManager().beginTransaction().replace(R.id.challenges_fl_match_holder,
                mTimelineFragment = ChallengeTimelineFragment.newInstance()).commit();

        mTimelineFragment.setThisInstantCategory(mThisTabItemName);

        mRcvHorizontal = (RecyclerViewPager) findViewById(R.id.challenges_rcv_horizontal);
        mRcvHorizontal.setLayoutManager(new CustomLayoutManagerWithSmoothScroll(getContext(), LinearLayoutManager.HORIZONTAL, false));
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
        mRcvVertical.setLayoutManager(mVerticalManager = new CustomLayoutManagerWithSmoothScroll(getContext(), LinearLayoutManager.VERTICAL, false));
        mRcvVertical.setHasFixedSize(true);

        mRlChallengeCount = (RelativeLayout) findViewById(R.id.challenges_count_rl);
        tvChallengeNumber = (TextView) findViewById(R.id.challenges_count_tv);
        tvChallengeTotalCount = (TextView) findViewById(R.id.challenges_total_count_tv);
        mFlMatchHolder = (FrameLayout) findViewById(R.id.challenges_fl_match_holder);

        this.mChallengePresenter = ChallengePresenterImpl.newInstance(this);
        this.mChallengePresenter.onCreateChallenge(getArguments(), mThisTabItemName);
    }

    private void initMembers() {
        Bundle args = getArguments();
        if (args != null && args.containsKey(BundleKeys.TAB_ITEM_NAME)) {
            mThisTabItemName = args.getString(BundleKeys.TAB_ITEM_NAME, "");
        }
    }

    private void performScrolling(int newChallengeId) {
        if (newChallengeId >= 0 && mRcvHorizontal != null && mRcvHorizontal.getAdapter() != null) {
            int pos = getChallengePositionFromId(newChallengeId);
            Log.d("Temp", "Item position in challenge list : " + pos);

            if (pos >= 0) {
                mCurrentPosition = pos;
                if (mThisTabItemName.equalsIgnoreCase(Constants.ChallengeTabs.IN_PLAY)) {
                    mRcvHorizontal.smoothScrollToPosition(mCurrentPosition);

                } else if (mThisTabItemName.equalsIgnoreCase(Constants.ChallengeTabs.NEW)) {
                    mRcvVertical.smoothScrollToPosition(mCurrentPosition);
                }
            }
        }
    }

    private int getChallengePositionFromId(int newChallengeId) {
        int pos = -1;
        if (newChallengeId >= 0) {
            Bundle args = getArguments();
            if (args != null && args.containsKey(BundleKeys.CHALLENGE_LIST)) {
                List<Challenge> challengeList = Parcels.unwrap(args.getParcelable(BundleKeys.CHALLENGE_LIST));

                if (challengeList != null && !challengeList.isEmpty()) {
                    for (int temp = 0; temp < challengeList.size(); temp++) {
                        if (challengeList.get(temp).getChallengeId() == newChallengeId) {
                            pos = temp;
                            break;
                        }
                    }
                }
            }
        }
        return pos;
    }

    @Override
    public void setSwipeAdapter(RecyclerView.Adapter adapter) {
        mRcvHorizontal.setAdapter(adapter, true);

        tvChallengeTotalCount.setText("/ " + String.valueOf(adapter.getItemCount()));

        mTimelineFragment.addInitialMatches(getCurrentChallenge());
        changeChallengesView();
    }

    private void updateMatchesToCurrentPosition() {
        mTimelineFragment.refreshChallengeMatches(getCurrentChallenge());
    }

    private Challenge getCurrentChallenge() {
        return ((ChallengeAdapter) mRcvHorizontal.getAdapter()).getItem(mCurrentPosition);
    }

    @Override
    public void setListAdapter(RecyclerView.Adapter adapter) {
        mRcvVertical.setAdapter(adapter);
        changeChallengesView();
    }

    private void changeChallengesView() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mThisTabItemName.equalsIgnoreCase(Constants.ChallengeTabs.COMPLETED)) {
                    mIsViewLayoutSwipe = false;
                    switchToListView();

                } else if (mThisTabItemName.equalsIgnoreCase(Constants.ChallengeTabs.IN_PLAY)) {
                    mIsViewLayoutSwipe = true;

                } else if (mThisTabItemName.equalsIgnoreCase(Constants.ChallengeTabs.NEW)) {
                    mIsViewLayoutSwipe = false;
                    switchToListView();
                }
            }
        }, 100);
    }

    private void updateChallengeCount() {
        tvChallengeNumber.setText("NewChallengesResponse " + String.valueOf(mCurrentPosition + 1));
    }

    private int mFirstItemY = 0;

    private int mYDelta = 0;

    private int mCurrentPosition = 0;

    private int mVerticalChildHeight = 0;

    private int mHorizontalChildHeight = 0;

    private void checkGotEssentialValues() {
        try {
            if (mFirstItemY == 0) {
                int coords[] = new int[2];
                if (mRcvVertical.getChildAt(0) != null) {
                    mRcvVertical.getChildAt(0).getLocationInWindow(coords);
                    mFirstItemY = coords[1];
                    mYDelta = getResources().getDimensionPixelSize(R.dimen.dp_25);
                    mVerticalChildHeight = mRcvVertical.getChildAt(0).getMeasuredHeight();
                    mHorizontalChildHeight = mRcvHorizontal.getChildAt(0).getMeasuredHeight() + mYDelta;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private int findYDiffByPosition(int position) {
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

    public void switchToSwipeView(int position) {
        checkGotEssentialValues();

        int diffY = findYDiffByPosition(position);

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
        updateMatchesToCurrentPosition();
        moveSeekToSwipe();
    }

    public void switchToListView() {
        checkGotEssentialValues();

        mCurrentPosition = mRcvHorizontal.getCurrentPosition();
        mRcvVertical.scrollBy(0, mCurrentPosition * mVerticalChildHeight);
        horizontalSlideLeftRightItems(false);

        hideChallengeCount();
        moveSeekToList();
    }

    private void verticalSlideBottomItems(final boolean in) {
        int childCount = mRcvVertical.getChildCount();

        int i = 1;
        if (mCurrentPosition == 0) {
            i = 0;
        }

        if (childCount > i + 1) {
            AnimationSet animationSet = new AnimationSet(false);
            TranslateAnimation slideAnim;
            if (in) {
                slideAnim = new TranslateAnimation(0, 0, mVerticalChildHeight, 0);
                slideAnim.setDuration(300);
                slideAnim.setStartOffset(300);
            } else {
                slideAnim = new TranslateAnimation(0, 0, 0, mVerticalChildHeight);
                slideAnim.setDuration(500);
                animationSet.addAnimation(slideAnim);
                slideAnim = new TranslateAnimation(0, 0, 0, mVerticalChildHeight);
                slideAnim.setDuration(500);
                slideAnim.setStartOffset(500);
            }
            animationSet.addAnimation(slideAnim);
            if (mRcvVertical.getChildAt(i + 1) != null) {
                mRcvVertical.getChildAt(i + 1).startAnimation(animationSet);
            }
            if (mRcvVertical.getChildAt(i + 2) != null) {
                mRcvVertical.getChildAt(i + 2).startAnimation(animationSet);
            }
        }

        if (mRcvVertical.getChildAt(i) != null) {
            View showGamesBg = mRcvVertical.getChildAt(i).findViewById(R.id.all_challenges_sl_anim_bg);
            if (in) {
                showShowGamesBg(showGamesBg);
            } else {
                hideShowGamesBg(showGamesBg);
            }
        }

        if (in) {
            hideMatches();
        } else {
            showMatches();
        }
    }

    private void hideShowGamesBg(View view) {
        Animation slideAnimation = new TranslateAnimation(0, 0, 0, mHorizontalChildHeight);
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
        Animation slideAnimation = new TranslateAnimation(0, 0, mHorizontalChildHeight, 0);
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
                slideAnim = getSlideAnimX(in, true);
                mRcvHorizontal.getChildAt(0).startAnimation(slideAnim);
            }

            if (currentPosition < mRcvHorizontal.getAdapter().getItemCount() - 1) {
                slideAnim = getSlideAnimX(in, false);
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

    private TranslateAnimation getSlideAnimX(boolean in, boolean left) {
        int x = mRcvHorizontal.getPaddingLeft();

        if (left) {
            x = -x;
        }

        TranslateAnimation animation;
        if (in) {
            animation = new TranslateAnimation(x, 0, 0, 0);
        } else {
            animation = new TranslateAnimation(0, x, 0, 0);
        }
        animation.setDuration(500);
        return animation;
    }

    private void switchView() {
        mIsViewLayoutSwipe = !mIsViewLayoutSwipe;

        if (mIsViewLayoutSwipe) {
            switchToSwipeView(-1);
            NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.CHALLENGES,Constants.AnalyticsClickLabels.SWIPE_VIEW);
        } else {
            switchToListView();
            NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.CHALLENGES,Constants.AnalyticsClickLabels.LIST_VIEW);
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.challenges_switch_view_rl:
                switchView();
                break;
        }
    }

    private void moveSeekToSwipe() {
        try {
            mVSwitchSeek.animate().translationXBy(-getResources().getDimensionPixelSize(R.dimen.dp_26)).setDuration(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void moveSeekToList() {
        try {
            mVSwitchSeek.animate().translationXBy(getResources().getDimensionPixelSize(R.dimen.dp_26)).setDuration(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(mChallengeItemClickReceiver, new IntentFilter(Constants.IntentActions.ACTION_CHALLENGE_CLICK));
        localBroadcastManager.registerReceiver(mScrollChallengeReceiver, new IntentFilter(Constants.IntentActions.ACTION_SCROLL_CHALLENGE));
//        localBroadcastManager.registerReceiver(mNewChallengeFromNotificationReceiver, new IntentFilter(Constants.IntentActions.ACTION_NEW_CHALLENGE_ID));
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mChallengeItemClickReceiver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mScrollChallengeReceiver);
//        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mNewChallengeFromNotificationReceiver);
        super.onStop();
    }

    BroadcastReceiver mChallengeItemClickReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int tagId = intent.getIntExtra(BundleKeys.CHALLENGE_TAG_ID, -1);
            int clickPosition = intent.getIntExtra(BundleKeys.CLICK_POSITION, -1);
            boolean isSwipeView = intent.getBooleanExtra(BundleKeys.CHALLENGE_SWITCH_POS,false);
            String tabName = intent.getStringExtra(BundleKeys.TAB_ITEM_NAME);

            if (mThisTabItemName.equalsIgnoreCase(tabName)) {
                if (isSwipeView) {
                    mIsViewLayoutSwipe = true;
                    mCurrentPosition = clickPosition;
                    switchToSwipeView(mCurrentPosition);

                } else {
                    switchView();
                }
            }
        }
    };

    BroadcastReceiver mNewChallengeFromNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            /* Only for NEW tab in challenge-screen */
            if (intent != null && mThisTabItemName.equalsIgnoreCase(Constants.ChallengeTabs.NEW)) {
                final int newChallengeId = intent.getIntExtra(BundleKeys.CHALLENGE_ID, -1);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        performScrolling(newChallengeId);
                    }
                }, 200);
            }
        }
    };

    BroadcastReceiver mScrollChallengeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (mThisTabItemName.equalsIgnoreCase(Constants.ChallengeTabs.IN_PLAY)) {
                    final int newChallengeId = intent.getIntExtra(BundleKeys.CHALLENGE_ID, -1);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            performScrolling(newChallengeId);
                        }
                    }, 100);
                }
            }
        }
    };

}