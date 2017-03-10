package in.sportscafe.nostragamus.module.onboard;

import android.graphics.Canvas;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
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
public class OnboardFragment extends NostragamusFragment {

    private static final int AVATAR_PER_SCREEN = 3;

    private RecyclerView mAvatarPager;

    private TextView mTvOnboardTitle;

    private TextView mTvOnboardDesc;

    private float mFullScreenWidth;

    private float mHalfScreenWidth;

    private List<Avatar> mAvatarImageList = Arrays.asList(new Avatar[]{
            new Avatar(R.drawable.onboard_war_friends),
            new Avatar(R.drawable.onboard_how_to_play),
            new Avatar(R.drawable.onboard_join_public),
            new Avatar(R.drawable.onboard_predict_match),
            new Avatar(R.drawable.onboard_war_friends),
            new Avatar(R.drawable.onboard_how_to_play)
    });

    private String[] mOnboardTitles;

    private String[] mOnboardDescs;

    private int mCurrentPosition = -1;

    public static OnboardFragment newInstance() {
        return new OnboardFragment();
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

            initOnboard();
        }
    }

    private void initOnboard() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mFullScreenWidth = displayMetrics.widthPixels;
        mHalfScreenWidth = mFullScreenWidth / 2f;

        mAvatarPager = (RecyclerView) findViewById(R.id.onboard_rvp_avatar_pager);
        mAvatarPager.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        /*mAvatarPager.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {
            @Override
            public void OnPageChanged(int fromPosition, int toPosition) {
            }
        });*/
        mAvatarPager.setHasFixedSize(true);
        mAvatarPager.setAdapter(new AvatarAdapter(getContext(), mAvatarImageList, mFullScreenWidth));

        mAvatarPager.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                View child = null;
                int[] location = new int[2];
                float width;
                float left;
                float right;
                float centerX;
                float scaleX;
                int childCount = parent.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    child = parent.getChildAt(i);
                    width = child.getMeasuredWidth();

                    child.getLocationOnScreen(location);
                    left = location[0];
                    right = left + width;
                    centerX = left + (width / 2f);

                    if (right > 0 && left < mFullScreenWidth) {
                        scaleX = (centerX % mHalfScreenWidth) / mHalfScreenWidth;
                        if (centerX >= mHalfScreenWidth && centerX < mFullScreenWidth) {
                            scaleX = Math.abs(1 - scaleX);
                        }
                        child = child.findViewById(R.id.avatar_iv_image);
                        child.setScaleX(scaleX);
                        child.setScaleY(scaleX);
                        child.setAlpha(scaleX);
                        Log.d("Child Pos : " + i + " --> ", left + ", " + right + ", " + centerX + ", " + scaleX);
                    }

                }

                super.onDraw(c, parent, state);
            }
        });

        mTvOnboardTitle = (TextView) findViewById(R.id.onboard_tv_title);
        mTvOnboardDesc = (TextView) findViewById(R.id.onboard_tv_desc);

        ViewPager viewpager = (ViewPager) findViewById(R.id.onboard_vp);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.onboard_cpi_indicator);
        viewpager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                RelativeLayout relativeLayout = new RelativeLayout(getContext());
                relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

//                relativeLayout.setBackgroundColor(Color.parseColor("#9" + String.format("%03d", (position * 23)) + "1C"));
                relativeLayout.setBackgroundColor(Color.parseColor("#00000000"));

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
        });

        changeOnboardText(0);
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
        float dx = (scrollX - oldScrollX) / AVATAR_PER_SCREEN;
        mAvatarPager.scrollBy((int) dx, 0);
    }

    private float mDeltaWidth;

    /*private void constrctAvatar() {
        Rect rect = new Rect();
        findViewById(R.id.onboard_hsv_avatar).getLocalVisibleRect(rect);

        int holderWidth = rect.width();

        float leftMargin = mLlAvatarHolder.getResources().getDimensionPixelSize(R.dimen.dp_0);
        mDeltaWidth = leftMargin * (AVATAR_PER_SCREEN);

        float avatarWidth = (holderWidth - mDeltaWidth) / AVATAR_PER_SCREEN;
        float avatarHeight = mLlAvatarHolder.getResources().getDimensionPixelSize(R.dimen.dp_160);

        int noOfAvatars = AVATAR_PER_SCREEN + mOnboardTitles.length - 1;

        for (int i = 0; i < noOfAvatars; i++) {
            mLlAvatarHolder.addView(getAvatar(avatarWidth, avatarHeight, leftMargin));
        }
    }*/

    private ImageView getAvatar(float width, float height, float leftMargin) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) width, (int) height);
        lp.leftMargin = (int) leftMargin;

        ImageView avatar = new ImageView(getContext());
        avatar.setLayoutParams(lp);
        avatar.setBackgroundResource(R.drawable.onboard_card_bg);
        return avatar;
    }
}