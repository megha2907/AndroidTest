package in.sportscafe.nostragamus.module.onboard;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.Log;

import java.util.Arrays;
import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Jeeva on 08/03/17.
 */
public class AvatarFragment extends NostragamusFragment {

    private static final float CARD_WIDTH_PERECENTAGE = 33.33f / 100;

    private static final float CARD_HEIGHT_PERECENTAGE = 50f / 100;

    private static final float CARD_MOVEMENT_PERCENTAGE = 8f / 100;

    private static final float MINIMUM_SCALE_X = 0.2f;

    private static final float MAXIMUM_SCALE_X = 1f;

    private static final float MINIMUM_SCALE_Y = 0.6f;

    private static final float MAXIMUM_SCALE_Y = 1f;

    private static final float MINIMUM_ROTATION_Y = 0f;

    private static final float MAXIMUM_ROTATION_Y = 40f;

    private static final int AVATAR_PER_SCREEN = 3;

    private RecyclerView mAvatarPager;

    private TextView mTvOnboardTitle;

    private TextView mTvOnboardDesc;

    private float mFullScreenWidth;

    private float mHalfScreenWidth;

    private float mCardWidth;

    private float mCardHeight;

    private List<Avatar> mAvatarImageList = Arrays.asList(new Avatar[]{
            new Avatar(R.drawable.onboard_predict_match),
            new Avatar(R.drawable.onboard_war_friends),
            new Avatar(R.drawable.onboard_how_to_play),
            new Avatar(R.drawable.onboard_join_public),
            new Avatar(R.drawable.onboard_predict_match),
            new Avatar(R.drawable.onboard_war_friends),
            new Avatar(R.drawable.onboard_how_to_play),
            new Avatar(R.drawable.onboard_join_public)
    });

    private String[] mOnboardTitles;

    private String[] mOnboardDescs;

    private int mCurrentPosition = -1;

    public static AvatarFragment newInstance() {
        return new AvatarFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_onboard, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (null == savedInstanceState) {
            mOnboardTitles = getResources().getStringArray(R.array.onboard_titles);
            mOnboardDescs = getResources().getStringArray(R.array.onboard_descs);

            initScreenDimension();

            initAvatar();

            initViewPager();
        }
    }

    private void initScreenDimension() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mFullScreenWidth = displayMetrics.widthPixels;
        mHalfScreenWidth = mFullScreenWidth / 2f;

        mCardWidth = mFullScreenWidth * CARD_WIDTH_PERECENTAGE;
        mCardHeight = mFullScreenWidth * CARD_HEIGHT_PERECENTAGE;
    }

    private void initAvatar() {
        mAvatarPager = (RecyclerView) findViewById(R.id.onboard_rvp_avatar_pager);
        mAvatarPager.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mAvatarPager.setHasFixedSize(true);
        mAvatarPager.setAdapter(new AvatarAdapter(getContext(), mAvatarImageList, mCardWidth, mCardHeight));

        mAvatarPager.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                handleAvatarScroll(c, parent, state);
                super.onDraw(c, parent, state);
            }
        });

//        handleAvatarScroll(null, mAvatarPager, null);
    }

    private void initViewPager() {
        mTvOnboardTitle = (TextView) findViewById(R.id.onboard_tv_title);
        mTvOnboardDesc = (TextView) findViewById(R.id.onboard_tv_desc);

        ViewPager viewpager = (ViewPager) findViewById(R.id.onboard_vp);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.onboard_cpi_indicator);
        viewpager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mOnboardTitles.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                RelativeLayout relativeLayout = new RelativeLayout(getContext());
                relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                container.addView(relativeLayout);
                return relativeLayout;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        indicator.setViewPager(viewpager);

        viewpager.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                handleViewPagerScroll(v, scrollX, scrollY, oldScrollX, oldScrollY);
            }
        });

        changeOnboardText(0);
        viewpager.post(new Runnable() {
            @Override
            public void run() {
                handleViewPagerScroll(null, 1, 0, 0, 0);
            }
        });
    }

    private boolean mScrolling = false;

    private void handleAvatarScroll(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mScrolling) {
            View child;
            int[] location = new int[2];
            float width;
            float left;
            float right;
            float centerX;
            float percentage;
            float scaleX;
            float scaleY;
            float rotationY;

            for (int i = 0; i < parent.getChildCount(); i++) {
                child = parent.getChildAt(i);
                width = child.getMeasuredWidth();

                child.getLocationOnScreen(location);
                left = location[0];
                right = left + width;
                centerX = left + (width / 2f);

                if (right > 0 && left < mFullScreenWidth) {
                    rotationY = -1;
                    percentage = (centerX % mHalfScreenWidth) / mHalfScreenWidth;

                    if (centerX >= mHalfScreenWidth && centerX < mFullScreenWidth) {
                        percentage = Math.abs(1 - percentage);
                        rotationY = 1;
                    }

                    scaleX = ((MAXIMUM_SCALE_X - MINIMUM_SCALE_X) * percentage) + MINIMUM_SCALE_X;
                    scaleY = ((MAXIMUM_SCALE_Y - MINIMUM_SCALE_Y) * percentage) + MINIMUM_SCALE_Y;

                    child = child.findViewById(R.id.avatar_iv_image);
                    child.setAlpha(scaleX);
                    child.getLayoutParams().width = (int) (mCardWidth * scaleX);
                    child.getLayoutParams().height = (int) (mCardHeight * scaleY);

                    rotationY = MAXIMUM_ROTATION_Y * (1 - percentage) * rotationY;
                    child.setRotationY(rotationY);
                    child.requestLayout(); // good one
                    Log.d("Child Pos : " + i + " --> ", left + ", " + right + ", " + centerX + ", " + percentage);
                }

            }
        }
        mScrolling = false;
    }

    private void handleViewPagerScroll(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        float xFull = scrollX % mFullScreenWidth;
        float alpha = (xFull % mHalfScreenWidth) / mHalfScreenWidth;
        if (xFull > mHalfScreenWidth) {
            applyAlpha(alpha);
        } else {
            applyAlpha(1 - alpha);
        }

        if (xFull != 0) {
            int position = (int) (((scrollX - mHalfScreenWidth) / mFullScreenWidth) + 1);
            changeOnboardText(position > 0 ? position : 0);
        }

        scrollViews(scrollX, oldScrollX);
    }

    private void applyAlpha(float alpha) {
        mTvOnboardTitle.setAlpha(alpha);
        mTvOnboardDesc.setAlpha(alpha);
    }

    private void changeOnboardText(int position) {
        if (mCurrentPosition != position) {
            mCurrentPosition = position;
            mTvOnboardTitle.setText(mOnboardTitles[position]);
            mTvOnboardDesc.setText(mOnboardDescs[position]);
        }
    }

    private void scrollViews(int scrollX, int oldScrollX) {
        mScrolling = true;

//        float dx = (scrollX - oldScrollX) / AVATAR_PER_SCREEN;
        float dx = (scrollX - oldScrollX) * CARD_MOVEMENT_PERCENTAGE;
        mAvatarPager.scrollBy((int) dx, 0);
    }
}