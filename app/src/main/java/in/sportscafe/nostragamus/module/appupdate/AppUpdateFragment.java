package in.sportscafe.nostragamus.module.appupdate;

import android.animation.Animator;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jeeva.android.ExceptionTracker;
import com.jeeva.android.Log;
import com.jeeva.android.widgets.HmImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.AllChallengesResponse;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengesDataResponse;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.common.OnDismissListener;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.utils.ViewUtils;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 6/2/17.
 */

public class AppUpdateFragment extends NostragamusFragment implements View.OnClickListener {

    private Button mBtnNext;

    private HmImageView mIvUpdateIn;

    private HmImageView mIvUpdateOut;

    private ViewPager mViewPager;

    private AppUpdateResponse mAppUpdateResponse;

    private List<AppUpdateDto> mAppUpdateList = new ArrayList<>();

    private AlphaAnimation mAnimIn;

    private AlphaAnimation mAnimOut;

    private int DISMISS_SCREEN = 58;

    private String mAppLink;

    private OnDismissListener mDismissListener;

    public static AppUpdateFragment newInstance() {
        return new AppUpdateFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDismissListener) {
            mDismissListener = (OnDismissListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_app, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mIvUpdateIn = (HmImageView) findViewById(R.id.update_app_iv_image_in);
        mIvUpdateOut = (HmImageView) findViewById(R.id.update_app_iv_image_out);
        mViewPager = (ViewPager) findViewById(R.id.update_app_vp);
        Button updateAppBtn =  (Button) findViewById(R.id.update_app_btn);
        Button updateAppLater =  (Button) findViewById(R.id.update_app_later);
        updateAppBtn.setOnClickListener(this);
        updateAppLater.setOnClickListener(this);

        getAppUpdateDetailsList();

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
                mIvUpdateOut.setImageUrl(getImage(mViewPager.getCurrentItem()));
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        mIvUpdateIn.setImageUrl(getImage(position));
        mIvUpdateIn.setAlpha(0.1f);
        mIvUpdateIn.animate().alpha(1).setDuration(1000);

        startTimer();
    }

    private void getAppUpdateDetailsList() {

        if (BuildConfig.IS_PAID_VERSION) {
            callAppUpdatesApi("FULL");
        } else {
            callAppUpdatesApi(null);
        }
    }

    private void callAppUpdatesApi(String flavor) {

        showProgressbar();

        MyWebService.getInstance().getAppUpdatesRequest(flavor).enqueue(
                new NostragamusCallBack<AppUpdateResponse>() {
                    @Override
                    public void onResponse(Call<AppUpdateResponse> call, Response<AppUpdateResponse> response) {
                        super.onResponse(call, response);

                        if (response.isSuccessful() && response.body() != null && response.body().getAppUpdateDetails() != null) {

                            mAppUpdateResponse = response.body();
                            mAppUpdateList = mAppUpdateResponse.getAppUpdateDetails().getAppUpdateSlides();
                            mAppLink = mAppUpdateResponse.getAppUpdateDetails().getUpdateUrl();
                            initAppUpdateSlides();

                        } else {
                            NoUpdatesAvailable();
                        }

                        dismissProgressbar();
                    }
                }
        );
    }

    private void NoUpdatesAvailable() {
        showMessage(Constants.Alerts.NO_UPDATES);
    }

    private String getImage(int position) {
        return mAppUpdateList.get(position).getImageUrl();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_app_later:
                mDismissListener.onDismiss(DISMISS_SCREEN, null);
                break;

            case R.id.update_app_btn:
                navigateAppHostedUrl(mAppLink);
                break;

        }
    }

    private void navigateAppHostedUrl(String apkLink) {
        String fixedApkLink = "http://nostragamus.in/pro/";

        if (TextUtils.isEmpty(apkLink)) {
            apkLink = fixedApkLink;
        }

        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(apkLink)));
        } catch (ActivityNotFoundException e) {
            ExceptionTracker.track(e);
        }
    }
}
