package in.sportscafe.nostragamus.module.appupdate;

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

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.utils.ViewUtils;
import in.sportscafe.nostragamus.webservice.MyWebService;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by deepanshi on 6/2/17.
 */

public class AppUpdateFragment extends NostragamusFragment {

    private Button mBtnNext;

    private ImageView mIvUpdateIn;

    private ImageView mIvUpdateOut;

    private ViewPager mViewPager;

    private List<AppUpdateDto> mAppUpdateList = new ArrayList<>();

    private AlphaAnimation mAnimIn;

    private AlphaAnimation mAnimOut;

    public static AppUpdateFragment newInstance() {
        return new AppUpdateFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_app, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mIvUpdateIn = (ImageView) findViewById(R.id.update_app_iv_image_in);
        mIvUpdateOut = (ImageView) findViewById(R.id.update_app_iv_image_out);
        mViewPager = (ViewPager) findViewById(R.id.update_app_vp);

        mAppUpdateList = getAppUpdateDetailsList();
        initAppUpdateSlides();
    }

    private void initAppUpdateSlides() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        for (AppUpdateDto appUpdateDto : mAppUpdateList) {
            viewPagerAdapter.addFragment(AppUpdateTextFragment.newInstance(appUpdateDto), "");
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                onPositionChanged(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setAdapter(viewPagerAdapter);

        CircleIndicator cpi = (CircleIndicator) findViewById(R.id.update_app_cpi_indicator);
        cpi.setViewPager(mViewPager);

        onPositionChanged(0);
    }

    public void onPositionChanged(int position) {
        mIvUpdateOut.setAlpha(1);
        mIvUpdateOut.animate().alpha(0).setDuration(1000).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIvUpdateOut.setImageResource(getImageRes(mViewPager.getCurrentItem()));
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        mIvUpdateIn.setImageResource(getImageRes(position));
        mIvUpdateIn.setAlpha(0.1f);
        mIvUpdateIn.animate().alpha(1).setDuration(1000);

        startTimer();
    }

    private List<AppUpdateDto> getAppUpdateDetailsList() {
        String json = null;
        try {
            InputStream is = null;
            if (BuildConfig.IS_PAID_VERSION) {
                is = getContext().getAssets().open("json/onboarding.json");
            } else {
                is = getContext().getAssets().open("json/onboarding_ps.json");
            }
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
                    new TypeReference<List<AppUpdateDto>>() {
                    }
            );
        }
        return null;
    }

    private int getImageRes(int position) {
        return ViewUtils.getDrawableIdFromResName(
                getContext(),
                mAppUpdateList.get(position).getImageUrl()
        );
    }

    private Handler mTimerHandler = new Handler();

    private Runnable mTimerRunnable;

    private void startTimer() {
        if (null != mTimerRunnable) {
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
        if (currentPosition < mAppUpdateList.size() - 1) {
            mViewPager.setCurrentItem(currentPosition + 1);
        }
    }
}
