package in.sportscafe.nostragamus.module.allchallenges.challenge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.recyclerviewpager.RecyclerViewPager;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRcvHorizontal = (RecyclerViewPager) findViewById(R.id.challenges_rcv_horizontal);
        mRcvHorizontal.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRcvHorizontal.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {
            @Override
            public void OnPageChanged(int fromPosition, int toPosition) {
                updateChallengeCount(toPosition);
            }
        });
        mRcvHorizontal.setHasFixedSize(true);

        mRcvVertical = (RecyclerView) findViewById(R.id.challenges_rcv_vertical);
        mRcvVertical.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
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

        this.mChallengePresenter = ChallengePresenterImpl.newInstance(this);
        this.mChallengePresenter.onCreateChallenge(getArguments());
    }

    @Override
    public void setSwipeAdapter(RecyclerView.Adapter adapter) {
        mRcvHorizontal.setAdapter(adapter, true);

        tvChallengeTotalCount.setText("/" + String.valueOf(adapter.getItemCount()));
    }

    @Override
    public void setListAdapter(RecyclerView.Adapter adapter) {
        mRcvVertical.setAdapter(adapter);
    }

    private void updateChallengeCount(int currentPosition) {
        tvChallengeNumber.setText("Challenge " + String.valueOf(currentPosition + 1));
    }

    private int mFirstItemY = 0;

    @Override
    public void onClick(View view) {
        int coords[] = new int[2];
        if(mFirstItemY == 0) {
            mRcvVertical.getChildAt(0).getLocationInWindow(coords);
            mFirstItemY = coords[1];
        }
        switch (view.getId()) {
            case R.id.challenges_switch_view_rl:
                int challengeCardHeight = mRcvVertical.getChildAt(0).getMeasuredHeight();
                if (mListView.isSelected()) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) mRcvVertical.getLayoutManager();
                    int visibleItemPosition = 0;
                    if(layoutManager.findFirstCompletelyVisibleItemPosition() != layoutManager.findFirstVisibleItemPosition()) {
                        visibleItemPosition = 1;
                    }
                    mRcvVertical.getChildAt(visibleItemPosition).getLocationInWindow(coords);
                    mRcvVertical.smoothScrollBy(0,  coords[1] - mFirstItemY);

                    /*horizontalSlideLeftRightItems(true);

                    mRcvHorizontal.scrollToPosition(visibleItemPosition);
                    mSwipeView.setSelected(true);
                    mListView.setSelected(false);
                    mChallengePresenter.changeAdapterLayout(true);
                    mRlChallengeCount.setVisibility(View.VISIBLE);

                    mRcvHorizontal.setVisibility(View.VISIBLE);
                    mRcvVertical.setVisibility(View.INVISIBLE);

                    mRcvVertical.scrollToPosition(0);*/
                } else {
                    horizontalSlideLeftRightItems(false);
                    mRcvVertical.scrollBy(0, mRcvHorizontal.getCurrentPosition() * challengeCardHeight);
//                    mRcvVertical.scrollToPosition(mRcvHorizontal.getCurrentPosition());
//                    mRcvVertical.getChildAt(mRcvHorizontal.getCurrentPosition()).scrollTo(0, 0);
                    mListView.setSelected(true);
                    mSwipeView.setSelected(false);
                    mChallengePresenter.changeAdapterLayout(false);
                    mRlChallengeCount.setVisibility(View.GONE);

                    mRcvHorizontal.setVisibility(View.INVISIBLE);
                    mRcvVertical.setVisibility(View.VISIBLE);

                    verticalSlideBottomItems(true);
                }
                break;
        }
    }

    private void verticalSlideBottomItems(boolean in) {
        int currentPosition = mRcvHorizontal.getCurrentPosition();
        int childCount = mRcvVertical.getChildCount();
        if(childCount > 1) {
            if (currentPosition < mRcvVertical.getAdapter().getItemCount() - 1) {
                mRcvVertical.getChildAt(childCount - 1)
                        .startAnimation(getSlideAnim(in, true, false));
            }
        }
    }

    private void horizontalSlideLeftRightItems(boolean in) {
        int currentPosition = mRcvHorizontal.getCurrentPosition();
        int childCount = mRcvHorizontal.getChildCount();
        if(childCount > 1) {
            if (currentPosition > 0) {
                mRcvHorizontal.getChildAt(0).startAnimation(getSlideAnim(in, false, true));
            }

            if (currentPosition < mRcvHorizontal.getAdapter().getItemCount() - 1) {
                mRcvHorizontal.getChildAt(childCount - 1)
                        .startAnimation(getSlideAnim(in, false, false));
            }
        }
    }

    private Animation getSlideAnim(boolean in, boolean vertical, boolean negative) {
        int x = 0;
        int y = 0;

        if(vertical) {
            y = mRcvVertical.getChildAt(0).getMeasuredHeight();
        } else {
            x = mRcvHorizontal.getPaddingLeft();
        }

        if(negative) {
            x = -x;
            y = -y;
        }

        if(in) {
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