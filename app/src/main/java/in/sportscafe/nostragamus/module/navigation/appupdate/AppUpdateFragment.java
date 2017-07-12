package in.sportscafe.nostragamus.module.navigation.appupdate;

import android.animation.Animator;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.ExceptionTracker;
import com.jeeva.android.Log;
import com.jeeva.android.widgets.HmImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.module.common.OnDismissListener;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsChecker;
import in.sportscafe.nostragamus.service.NostraFileDownloadService;
import in.sportscafe.nostragamus.utils.StorageUtility;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 6/2/17.
 */

public class AppUpdateFragment extends BaseFragment implements View.OnClickListener {

    private Button mBtnNext;

    private HmImageView mIvUpdateIn;

    private HmImageView mIvUpdateOut;

    private Button mUpdateAppBtn;

    private Button mUpdateAppLater;

    private TextView mBtnSkip;

    private ViewPager mViewPager;

    private TextView mTvUpdateAppNext;

    private boolean mChangeToSkip = false;

    private AppUpdateResponse mAppUpdateResponse;

    private List<AppUpdateDto> mAppUpdateList = new ArrayList<>();

    private AlphaAnimation mAnimIn;

    private AlphaAnimation mAnimOut;

    private int DISMISS_SCREEN = 58;

    private String mAppLink;

    private OnDismissListener mDismissListener;

    private AppUpdateActionListener mAppUpdateActionListener;

    public interface AppUpdateActionListener {
        void onAppDownload(String screenType);
    }

    public void setFailureListener(AppUpdateFragment.AppUpdateActionListener listener) {
        mAppUpdateActionListener = listener;
    }

    public static AppUpdateFragment newInstance(String screenType, AppUpdateActionListener listener) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BundleKeys.SCREEN, screenType);

        AppUpdateFragment fragment = new AppUpdateFragment();
        fragment.setFailureListener(listener);
        fragment.setArguments(bundle);
        return fragment;
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

        initViews(getArguments());

        /*call App Update Api and get App Update Details */
        getAppUpdateDetailsList();

    }

    private void initViews(Bundle bundle) {
        mIvUpdateIn = (HmImageView) findViewById(R.id.update_app_iv_image_in);
        mIvUpdateOut = (HmImageView) findViewById(R.id.update_app_iv_image_out);
        mViewPager = (ViewPager) findViewById(R.id.update_app_vp);
        mUpdateAppBtn = (Button) findViewById(R.id.update_app_btn);
        mUpdateAppLater = (Button) findViewById(R.id.update_app_later);
        mBtnSkip = (TextView) findViewById(R.id.update_app_btn_skip);
        mTvUpdateAppNext = (TextView) findViewById(R.id.update_app_btn_next);
        TextView screenHeading = (TextView) findViewById(R.id.update_app_tv_heading);
        RelativeLayout rlViewPager = (RelativeLayout) findViewById(R.id.update_app_rl_viewpager);
        ImageView backBtn = (ImageView) findViewById(R.id.update_app_iv_back);
        mUpdateAppBtn.setOnClickListener(this);
        mUpdateAppLater.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        mTvUpdateAppNext.setOnClickListener(this);
        mBtnSkip.setOnClickListener(this);
        mBtnSkip.setVisibility(View.GONE);

        RelativeLayout.LayoutParams updateLaterLayoutParams = (RelativeLayout.LayoutParams) mUpdateAppLater.getLayoutParams();
        updateLaterLayoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.update_later_bottom_gap);
        mUpdateAppLater.setLayoutParams(updateLaterLayoutParams);

        if (bundle.getString(Constants.BundleKeys.SCREEN) != null) {

            /* check if it's a What's NEW Screen or a Force Update Screen or a Normal Update Screen */
            if (bundle.getString(Constants.BundleKeys.SCREEN).equals(Constants.ScreenNames.WHATS_NEW)) {
                mUpdateAppLater.setVisibility(View.INVISIBLE);
                mUpdateAppBtn.setVisibility(View.INVISIBLE);
                screenHeading.setText("What's New !");
//                RelativeLayout.LayoutParams paramsFour = (RelativeLayout.LayoutParams) rlViewPager.getLayoutParams();
//                paramsFour.setMargins(0, 50, 0, 100);
//                rlViewPager.setLayoutParams(paramsFour);

//                if (NostragamusDataHandler.getInstance().getUserInfo().getInfoDetails().getWhatsNewShown() == null) {
//                    backBtn.setVisibility(View.GONE);
//                    mBtnSkip.setVisibility(View.VISIBLE);
//                }

            } else if (bundle.getString(Constants.BundleKeys.SCREEN).equals(Constants.ScreenNames.APP_FORCE_UPDATE)) {
                screenHeading.setText("Update the App !");
                mUpdateAppLater.setVisibility(View.VISIBLE);
                backBtn.setVisibility(View.INVISIBLE);
                ViewGroup.LayoutParams params = mUpdateAppLater.getLayoutParams();
                params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                mUpdateAppLater.setLayoutParams(params);
                mUpdateAppLater.setText("You need to update the app to continue Playing!");
                mUpdateAppLater.setTextSize(13);
                mUpdateAppLater.setOnClickListener(null);
                mUpdateAppBtn.setVisibility(View.VISIBLE);
            } else {
                screenHeading.setText("Update the App !");
                mUpdateAppLater.setVisibility(View.VISIBLE);
                mUpdateAppBtn.setVisibility(View.VISIBLE);
            }
        } else {
            screenHeading.setText("Update the App !");
            mUpdateAppLater.setVisibility(View.VISIBLE);
            mUpdateAppBtn.setVisibility(View.VISIBLE);
        }


    }

    private void callWhatsNewShownApi() {

        MyWebService.getInstance().getWhatsNewShown().enqueue(
                new NostragamusCallBack<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        super.onResponse(call, response);

                        if (response.isSuccessful() && response.body() != null) {
                            NostragamusDataHandler.getInstance().setWhatsNewShown(true);
                        } else {
                            NostragamusDataHandler.getInstance().setWhatsNewShown(true);
                        }
                    }
                }
        );

    }

    /* Set Details of App Update */
    private void initAppUpdateSlides() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        for (AppUpdateDto appUpdateDto : mAppUpdateList) {
            viewPagerAdapter.addFragment(AppUpdateTextFragment.newInstance(appUpdateDto), "");
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position < mAppUpdateList.size() - 1) {
                    mTvUpdateAppNext.setText("Next");
//                    if (NostragamusDataHandler.getInstance().getUserInfo().getInfoDetails().getWhatsNewShown() == null) {
//                        mChangeToSkip = false;
//                        mBtnSkip.setVisibility(View.VISIBLE);
//                    }
                } else {
                    mTvUpdateAppNext.setText("Done");
//                    if (NostragamusDataHandler.getInstance().getUserInfo().getInfoDetails().getWhatsNewShown() == null) {
//                        mChangeToSkip = true;
//                        mBtnSkip.setVisibility(View.GONE);
//                    }
                }

            }

            @Override
            public void onPageSelected(int position) {
               // onPositionChanged(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setAdapter(viewPagerAdapter);

        CircleIndicator cpi = (CircleIndicator) findViewById(R.id.update_app_cpi_indicator);
        cpi.setViewPager(mViewPager);

        //onPositionChanged(0);
    }

    /* Alpha animation on slide changed */
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

    }

    private void getAppUpdateDetailsList() {

        if (BuildConfig.IS_PAID_VERSION) {
            callAppUpdatesApi("FULL");
        } else {
            callAppUpdatesApi(null);
        }
    }

    /* Get details of App Update from Api according to App Flavor */
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

    private int getItem(int i) {
        return mViewPager.getCurrentItem() + i;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_app_later:
                NostragamusAnalytics.getInstance().trackUpdateLater(Constants.AnalyticsActions.CLICKED);
                if (getArguments().getString(Constants.BundleKeys.SCREEN) != null) {
                    navigateToHome();
                } else {
                    mDismissListener.onDismiss(DISMISS_SCREEN, null);
                }
                break;

            case R.id.update_app_btn_skip:
                callWhatsNewShownApi();
                navigateToHome();
                break;

            case R.id.update_app_btn_next:
//                if (mChangeToSkip) {
//                    callWhatsNewShownApi();
//                    navigateToHome();
//                } else {
                    mViewPager.setCurrentItem(getItem(+1), true);
                //}
                break;

            case R.id.update_app_btn:
                if (new PermissionsChecker(getActivity()).lacksPermissions(Constants.AppPermissions.STORAGE)) {
                    PermissionsActivity.startActivityForResult(getActivity(), Constants.RequestCodes.STORAGE_PERMISSION, Constants.AppPermissions.STORAGE);
                } else {
                    if (BuildConfig.IS_PAID_VERSION) {
                        downloadAndInstallApp(mAppLink);
                    } else {
                        if(mAppLink.contains("apk")){
                            downloadAndInstallApp(mAppLink);
                        }else {
                            navigateAppHostedUrl(mAppLink);
                        }
                    }
                }
                NostragamusAnalytics.getInstance().trackUpdateApp(Constants.AnalyticsActions.CLICKED);
                break;

            case R.id.update_app_iv_back:
                mDismissListener.onDismiss(DISMISS_SCREEN, null);
                break;

        }
    }

    /* If it's a Pro App , download and install using NostraFileDownloadService */
    private void downloadAndInstallApp(String apkLink) {

        String fixedApkLink = "http://nostragamus.in/pro/";

        if (TextUtils.isEmpty(apkLink)) {
            apkLink = fixedApkLink;
        }

        Intent intent = new Intent(getContext().getApplicationContext(), NostraFileDownloadService.class);
        intent.putExtra(NostraFileDownloadService.FILE_DOWNLOAD_URL, apkLink);
        intent.putExtra(NostraFileDownloadService.FILE_NAME_WITH_EXTENSION, StorageUtility.getFileNameWithSuffix(apkLink));
        getContext().startService(intent);

        mAppUpdateActionListener.onAppDownload(getArguments().getString(Constants.BundleKeys.SCREEN));
    }


    /* If it's a Playstore ver. ,then navigate to playstore */
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


    private void navigateToHome() {
        Intent intent = new Intent(getContext(), HomeActivity.class);
        intent.putExtra(Constants.BundleKeys.SCREEN, Constants.BundleKeys.LOGIN_SCREEN);
        startActivity(intent);
    }

}
