package in.sportscafe.nostragamus.module.onboard;

import android.animation.Animator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.utils.ViewUtils;
import in.sportscafe.nostragamus.webservice.MyWebService;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Jeeva on 28/3/16.
 */
public class OnBoardingFragment extends NostragamusFragment implements ViewPager.OnPageChangeListener {

    private Button mBtnNext;

    private ImageView mIvOnBoardIn;

    private ImageView mIvOnBoardOut;

    private ViewPager mViewPager;

    private List<OnBoardingDto> mOnBoardingList = new ArrayList<>();

    private AlphaAnimation mAnimIn;

    private AlphaAnimation mAnimOut;

    public static OnBoardingFragment newInstance() {
        return new OnBoardingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_onboard, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mIvOnBoardIn = (ImageView) findViewById(R.id.onboard_iv_image_in);
        mIvOnBoardOut = (ImageView) findViewById(R.id.onboard_iv_image_out);
        mViewPager = (ViewPager) findViewById(R.id.onboard_vp);

        mOnBoardingList = getOnBoardingList();
        initOnBoard();
    }

    private void initOnBoard() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        for (OnBoardingDto onBoardingDto : mOnBoardingList) {
            viewPagerAdapter.addFragment(OnBoardingTextFragment.newInstance(onBoardingDto), "");
        }

        mViewPager.addOnPageChangeListener(this);
        mViewPager.setAdapter(viewPagerAdapter);

        CircleIndicator cpi = (CircleIndicator) findViewById(R.id.onboard_cpi_indicator);
        cpi.setViewPager(mViewPager);

        onPageSelected(0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mIvOnBoardOut.setAlpha(1);
        mIvOnBoardOut.animate().alpha(0).setDuration(1000).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIvOnBoardOut.setImageResource(getImageRes(mViewPager.getCurrentItem()));
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        mIvOnBoardIn.setImageResource(getImageRes(position));
        mIvOnBoardIn.setAlpha(0.1f);
        mIvOnBoardIn.animate().alpha(1).setDuration(1000);

        startTimer();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private List<OnBoardingDto> getOnBoardingList() {
        String json = null;
        try {
            InputStream is = getContext().getAssets().open("json/onboarding.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (null != json) {
            return MyWebService.getInstance().getObjectFromJson(
                    json,
                    new TypeReference<List<OnBoardingDto>>() {
                    }
            );
        }
        return null;
    }

    private int getImageRes(int position) {
        return ViewUtils.getDrawableIdFromResName(
                getContext(),
                mOnBoardingList.get(position).getImageResName()
        );
    }

    private Handler mTimerHandler = new Handler();

    private Runnable mTimerRunnable;

    private void startTimer() {
        if(null != mTimerRunnable) {
            mTimerHandler.removeCallbacks(mTimerRunnable);
        }

        mTimerHandler.postDelayed(mTimerRunnable = new Runnable() {
            @Override
            public void run() {
                swipePageToNext();
            }
        }, 3000);
    }

    private void swipePageToNext() {
        int currentPosition = mViewPager.getCurrentItem();
        if (currentPosition < mOnBoardingList.size() - 1) {
            mViewPager.setCurrentItem(currentPosition + 1);
        }
    }
}