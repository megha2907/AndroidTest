package in.sportscafe.nostragamus.module.onboard;

import android.animation.Animator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jeeva.android.Log;
import com.jeeva.android.widgets.HmImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.utils.ViewUtils;
import in.sportscafe.nostragamus.webservice.MyWebService;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Jeeva on 28/3/16.
 */
public class OnBoardingFragment extends NostragamusFragment {

    private Button mBtnNext;

    private HmImageView mIvOnBoardIn;

    private HmImageView mIvOnBoardOut;

    private ViewPager mViewPager;

    private RelativeLayout mOnBoardImageLayout;

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

//        mIvOnBoardIn = (HmImageView) findViewById(R.id.onboard_iv_image_in);
//        mIvOnBoardOut = (HmImageView) findViewById(R.id.onboard_iv_image_out);
        mViewPager = (ViewPager) findViewById(R.id.onboard_vp);
//        mOnBoardImageLayout =(RelativeLayout)findViewById(R.id.onboarding_image_rl);

        mOnBoardingList = getOnBoardingList();
        initOnBoard();
    }

    private void initOnBoard() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        for (OnBoardingDto onBoardingDto : mOnBoardingList) {
            viewPagerAdapter.addFragment(OnBoardingTextFragment.newInstance(onBoardingDto), "");
        }


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // onPositionChanged(position);
                String newPos;
                if (mOnBoardingList.get(0).getReferralCode() != null) {
                    newPos = String.valueOf(position);
                }else {
                    newPos = String.valueOf(position+1);
                }
                NostragamusAnalytics.getInstance().trackScreenShown(Constants.AnalyticsCategory.ONBOARDING,"Slide "+newPos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setAdapter(viewPagerAdapter);

        CircleIndicator cpi = (CircleIndicator) findViewById(R.id.onboard_cpi_indicator);
        cpi.setViewPager(mViewPager);

        RelativeLayout.LayoutParams cpiLayoutParams = (RelativeLayout.LayoutParams) cpi.getLayoutParams();
        cpiLayoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.onboarding_cpi_bottom_gap);
        cpi.setLayoutParams(cpiLayoutParams);


        //onPositionChanged(0);
    }

    public void onPositionChanged(final int position) {
        mIvOnBoardOut.setAlpha(1);
        mIvOnBoardOut.animate().alpha(0).setDuration(1000).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIvOnBoardOut.setImageUrl(getImage(mViewPager.getCurrentItem()));
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        mIvOnBoardIn.setImageUrl(getImage(position));
        mIvOnBoardIn.setAlpha(0.1f);
        mIvOnBoardIn.animate().alpha(1).setDuration(1000);

        if (mOnBoardingList.get(position).getReferralCode() != null) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mOnBoardImageLayout.getLayoutParams();
            params.height = 190;
            params.width = 190;
            mOnBoardImageLayout.setLayoutParams(params);
        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mOnBoardImageLayout.getLayoutParams();
            params.height = 320;
            params.width = 320;
            mOnBoardImageLayout.setLayoutParams(params);
        }

//        getResources().getDimensionPixelSize(R.dimen.width);

        //startTimer();
    }

    private List<OnBoardingDto> getOnBoardingList() {

        List<OnBoardingDto> onBoardingList = new ArrayList();
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

//        NostragamusDataHandler.getInstance().setUserReferralCode("DEE1135");
//        NostragamusDataHandler.getInstance().setUserReferralName("Deepanshi Bajaj");
//        NostragamusDataHandler.getInstance().setWalletInitialAmount(20);
//        NostragamusDataHandler.getInstance().setUserReferralPhoto("https://scontent.xx.fbcdn.net/v/t1.0-1/p480x480/17523409_1443962065645034_1348270082615908797_n.jpg?oh=894d6be15bb712660f8cd11f98e47f22&oe=59CF4AA9");

        OnBoardingDto onBoardingDto = new OnBoardingDto();
        if (!TextUtils.isEmpty(NostragamusDataHandler.getInstance().getUserReferralCode())) {

            if (!TextUtils.isEmpty(NostragamusDataHandler.getInstance().getUserReferralName())) {
                onBoardingDto.setTitle("Sign Up with " + NostragamusDataHandler.getInstance().getUserReferralName() + "'s " + "Referral Code");
            }

            if (NostragamusDataHandler.getInstance().getWalletInitialAmount() != null) {
                onBoardingDto.setDesc("Join " + NostragamusDataHandler.getInstance().getUserReferralName()
                        + " in predicting live sports matches! \n Your first " +
                        "₹" + NostragamusDataHandler.getInstance().getWalletInitialAmount()
                        + " is on us!");
            }
            onBoardingDto.setImageUrl(NostragamusDataHandler.getInstance().getUserReferralPhoto());
            onBoardingDto.setReferralCode(NostragamusDataHandler.getInstance().getUserReferralCode());
            onBoardingDto.setReferral(true);
            onBoardingList.add(onBoardingDto);
        }

        if (null != json) {
            List<OnBoardingDto> onBoardingListTwo = new ArrayList();
            onBoardingListTwo = MyWebService.getInstance().getObjectFromJson(
                    json,
                    new TypeReference<List<OnBoardingDto>>() {
                    }
            );
            for (int i = 0; i < onBoardingListTwo.size(); i++) {
                onBoardingList.add(onBoardingListTwo.get(i));
            }
        }

        if (null != json) {
            return onBoardingList;
        }
        return null;
    }

    private int getImageRes(int position) {
        if (mOnBoardingList.get(position).getImageResName() == null) {
            return 0;
        }
        return ViewUtils.getDrawableIdFromResName(
                getContext(),
                mOnBoardingList.get(position).getImageResName()
        );
    }

    private String getImage(int position) {
        return mOnBoardingList.get(position).getImageUrl();
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
        if (currentPosition < mOnBoardingList.size() - 1) {
            mViewPager.setCurrentItem(currentPosition + 1);
        }
    }

}