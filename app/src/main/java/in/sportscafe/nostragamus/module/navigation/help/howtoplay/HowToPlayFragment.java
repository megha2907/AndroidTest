package in.sportscafe.nostragamus.module.navigation.help.howtoplay;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.Log;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.crash.NostragamusUncaughtExceptionHandler;
import in.sportscafe.nostragamus.module.customViews.CustomSnackBar;
import in.sportscafe.nostragamus.module.navigation.help.howtoplay.dto.HowToPlay;
import in.sportscafe.nostragamus.module.navigation.help.howtoplay.dto.HowToPlayDetails;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by deepanshi on 2/21/18.
 */

public class HowToPlayFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = HowToPlayFragment.class.getSimpleName();

    private HowToPlayFragmentListener howToPlayFragmentListener;

    public HowToPlayFragment() {
    }

    private ViewPager mViewPager;
    private TextView nextOption;
    private String slideId; /* To Send Request for different screens with same slide design */
    private CustomSnackBar mSnackBar;
    private String screenTitle = "";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof HowToPlayActivity) {
            howToPlayFragmentListener = (HowToPlayFragmentListener) context;
        } else {
            throw new RuntimeException("Activity must implement " + TAG);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_how_to_play, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        openBundle();
    }

    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.how_to_play_vp);
        ImageView backBtn = (ImageView) findViewById(R.id.how_to_play_iv_back);
        nextOption = (TextView) findViewById(R.id.how_to_play_btn_next);
        backBtn.setOnClickListener(this);
        nextOption.setOnClickListener(this);
    }

    private void openBundle() {
        Bundle args = getArguments();
        if (args != null && args.containsKey(Constants.BundleKeys.SLIDE_ID)) {
            slideId = args.getString(Constants.BundleKeys.SLIDE_ID);
            loadHowToPlayData(slideId);
        } else {
            handleHowToPlayError();
        }
    }


    private void loadHowToPlayData(String slideId) {
        HowToPlayDataProvider dataProvider = new HowToPlayDataProvider();
        dataProvider.getHowToPlayData(getContext().getApplicationContext(), slideId, new HowToPlayDataProvider.HowToPlayDataProviderListener() {
            @Override
            public void onData(int status, @Nullable HowToPlayDetails howToPlayDetails) {
                onHowToPlayDataReceived(status, howToPlayDetails.getTitle(), howToPlayDetails.getHowToPlayList());
            }

            @Override
            public void onError(int status) {
                handleHowToPlayError();
            }
        });
    }

    private void onHowToPlayDataReceived(int status, String title, List<HowToPlay> howToPlayList) {
        switch (status) {
            case Constants.DataStatus.FROM_SERVER_API_SUCCESS:
                setHowToPlaySlides(howToPlayList);
                setScreenTitle(title);
                break;

            case Constants.DataStatus.FROM_DATABASE_AS_NO_INTERNET:
            case Constants.DataStatus.FROM_DATABASE_AS_SERVER_FAILED:
                handleHowToPlayError();
                break;

            default:
                handleHowToPlayError();
                break;

        }
    }

    private void setScreenTitle(String title) {
        if (getActivity() != null && getView() != null) {
            TextView screenTitleTv = (TextView) getView().findViewById(R.id.how_to_play_tv_heading);
            if (!TextUtils.isEmpty(title)) {
                screenTitleTv.setText(title);
                screenTitle = title;
            } else {
                screenTitleTv.setText("Nostragamus");
            }
        }
    }

    private void handleHowToPlayError() {
        if (getView() != null) {
            mSnackBar = CustomSnackBar.make(getView(), Constants.Alerts.COULD_NOT_FETCH_DATA_FROM_SERVER, CustomSnackBar.DURATION_INFINITE);
            mSnackBar.show();
        }
    }


    /* Set How to Play Slides */
    private void setHowToPlaySlides(final List<HowToPlay> howToPlayList) {

        if (mViewPager != null && howToPlayList != null && getView() != null) {
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
            for (HowToPlay howToPlay : howToPlayList) {
                viewPagerAdapter.addFragment(HowToPlaySlidesFragment.newInstance(howToPlay), "");
            }

            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    if (getView() != null) {
                        TextView nextOption = (TextView) getView().findViewById(R.id.how_to_play_btn_next);
                        if (position < howToPlayList.size() - 1) {
                            nextOption.setText("Next");
                        } else {
                            nextOption.setText("Done");
                        }
                    }

                }

                @Override
                public void onPageSelected(int position) {
                    String newPos = String.valueOf(position);
                    if (!TextUtils.isEmpty(screenTitle)) {
                        NostragamusAnalytics.getInstance().trackScreenShown(screenTitle, "Slide " + newPos);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
            mViewPager.setAdapter(viewPagerAdapter);

            CircleIndicator cpi = (CircleIndicator) getView().findViewById(R.id.how_to_play_cpi_indicator);
            cpi.setViewPager(mViewPager);

            final RelativeLayout viewPagerLayout = (RelativeLayout) getView().findViewById(R.id.how_to_play_rl_viewpager);
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);

            if (hasBackKey && hasHomeKey) {
                // no navigation bar, unless it is enabled in the settings
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewPagerLayout.getLayoutParams();
                params.setMargins(0, 0, 0, 96);
                viewPagerLayout.setLayoutParams(params);
            } else {
                // 99% sure there's a navigation bar
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewPagerLayout.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                viewPagerLayout.setLayoutParams(params);
            }

        }
    }


    private int getItem(int i) {
        return mViewPager.getCurrentItem() + i;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.how_to_play_btn_next:
                if (getTrimmedText(nextOption) != null && getTrimmedText(nextOption).equalsIgnoreCase("done")
                        && howToPlayFragmentListener != null) {
                    howToPlayFragmentListener.onBackClicked();
                } else {
                    mViewPager.setCurrentItem(getItem(+1), true);
                }
                break;

            case R.id.how_to_play_iv_back:
                if (howToPlayFragmentListener != null) {
                    howToPlayFragmentListener.onBackClicked();
                }
                break;

        }
    }
}
