package in.sportscafe.nostragamus.module.navigation.powerupbank.earnmorepowerups;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.Map;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;

/**
 * Created by deepanshi on 7/29/17.
 */

public class EarnMorePowerUpFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = EarnMorePowerUpFragment.class.getSimpleName();

    private EarnMorePowerUpFragmentListener mEarnMorePowerUpFragmentListener;

    private HashMap<String, PowerUp> mPowerUpMaps;

    private TextView tvPowerupBankOne;
    private TextView tvPowerupBankTwo;
    private TextView mTvRunningLow;

    private TextView tvPBPowerup2xCount;
    private TextView tvPBPowerupNonegCount;
    private TextView tvPBPowerupPlayerPollCount;

    private ImageView ivPBPowerup2x;
    private ImageView ivPBPowerupNoneg;
    private ImageView ivPBPowerupPlayerPoll;


    private Bundle mBundle;

    public EarnMorePowerUpFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof EarnMorePowerUpActivity) {
            mEarnMorePowerUpFragmentListener = (EarnMorePowerUpFragmentListener) context;
        } else {
            throw new RuntimeException("Activity must implement " + TAG);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_earn_more_powerup, container, false);

        initRootView(rootView);
        setInfo();
        return rootView;
    }


    private void initRootView(View rootView) {
        rootView.findViewById(R.id.earn_more_powerup_refer_friend_layout).setOnClickListener(this);
        rootView.findViewById(R.id.earn_more_powerup_shop_layout).setOnClickListener(this);
        tvPowerupBankOne = (TextView) rootView.findViewById(R.id.earn_more_powerup_subheading_tv_one);
        mTvRunningLow = (TextView) rootView.findViewById(R.id.earn_more_powerup_low_powerups);

        tvPBPowerup2xCount = (TextView) rootView.findViewById(R.id.earn_more_powerup_2x_powerup_count);
        tvPBPowerupNonegCount = (TextView) rootView.findViewById(R.id.earn_more_powerup_noneg_powerup_count);
        tvPBPowerupPlayerPollCount = (TextView) rootView.findViewById(R.id.earn_more_powerup_audience_poll_powerup_count);

        ivPBPowerup2x = (ImageView) rootView.findViewById(R.id.earn_more_powerup_2x);
        ivPBPowerupNoneg = (ImageView) rootView.findViewById(R.id.earn_more_powerup_noneg);
        ivPBPowerupPlayerPoll = (ImageView) rootView.findViewById(R.id.earn_more_powerup_audience_poll);

        ivPBPowerup2x.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.double_powerup));
        ivPBPowerupNoneg.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.no_negative_powerup));
        ivPBPowerupPlayerPoll.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.audience_poll_powerup));

        mBundle = new Bundle();
    }

    private void setInfo() {

        UserInfo userInfo = Nostragamus.getInstance().getServerDataManager().getUserInfo();
        if (userInfo != null) {
            getPowerUps(userInfo);
        }
        setPowerUpText();
        showOrHideContentBasedOnAppType();
    }

    private void getPowerUps(UserInfo userInfo) {

        if (userInfo.getPowerUps() != null) {

            mPowerUpMaps = getPowerUpMap(userInfo.getPowerUps());
            mBundle.putParcelable(Constants.BundleKeys.POWERUPS, Parcels.wrap(mPowerUpMaps));

            int count;

            PowerUp powerUp = mPowerUpMaps.get(Constants.Powerups.XX);
            if (null == powerUp) {
                count = 0;
            } else {
                count = powerUp.getCount();
            }
            onGet2xPowerUp(count, count < 1);

            powerUp = mPowerUpMaps.get(Constants.Powerups.NO_NEGATIVE);
            if (null == powerUp) {
                count = 0;
            } else {
                count = powerUp.getCount();
            }
            onGetNonegsPowerUp(count, count < 1);

            powerUp = mPowerUpMaps.get(Constants.Powerups.AUDIENCE_POLL);
            if (null == powerUp) {
                count = 0;
            } else {
                count = powerUp.getCount();
            }
            onGetPollPowerUp(count, count < 1);
        } else {
            int count = 0;
            onGet2xPowerUp(count, count < 1);
            onGetNonegsPowerUp(count, count < 1);
            onGetPollPowerUp(count, count < 1);
        }

    }

    private void onGetPollPowerUp(int count, boolean runningLow) {
        setPowerUpCount(tvPBPowerupPlayerPollCount, count, runningLow, Constants.Powerups.AUDIENCE_POLL);
    }

    private void onGetNonegsPowerUp(int count, boolean runningLow) {
        setPowerUpCount(tvPBPowerupNonegCount, count, runningLow, Constants.Powerups.NO_NEGATIVE);
    }

    private void onGet2xPowerUp(int count, boolean runningLow) {
        setPowerUpCount(tvPBPowerup2xCount, count, runningLow, Constants.Powerups.XX);
    }

    private HashMap<String, PowerUp> getPowerUpMap(HashMap<String, Integer> powerUps) {
        HashMap<String, PowerUp> powerUpMaps = new HashMap<>();
        for (Map.Entry<String, Integer> entry : powerUps.entrySet()) {
            powerUpMaps.put(entry.getKey(), new PowerUp(entry.getKey(), entry.getValue()));
        }
        return powerUpMaps;
    }

    private void setPowerUpCount(TextView textView, int count, boolean runningLow, String powerUp) {
        textView.setText(String.valueOf(count));
        if (runningLow) {
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white_999999));

            if (powerUp.equalsIgnoreCase(Constants.Powerups.XX)) {
                ivPBPowerup2x.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.double_grey_powerup));
            } else if (powerUp.equalsIgnoreCase(Constants.Powerups.NO_NEGATIVE)) {
                ivPBPowerupNoneg.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.no_negative_grey_powerup));
            } else if (powerUp.equalsIgnoreCase(Constants.Powerups.AUDIENCE_POLL)) {
                ivPBPowerupPlayerPoll.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.audience_poll_grey_powerup));
            }

            if (mTvRunningLow.getVisibility() == View.GONE) {
                mTvRunningLow.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setPowerUpText() {
        tvPowerupBankOne.setText(Html.fromHtml(
                getResources().getString(R.string.earn_more_powerups_text)), TextView.BufferType.SPANNABLE);

    }


    private void showOrHideContentBasedOnAppType() {
        /* if design changes needed for playstore app */
        if (!BuildConfig.IS_PAID_VERSION) {

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.earn_more_powerup_shop_layout:
                if (mEarnMorePowerUpFragmentListener != null) {
                    mEarnMorePowerUpFragmentListener.onStoreClicked();
                } else {
                    showMessage(Constants.Alerts.SOMETHING_WRONG);
                }
                break;
            case R.id.earn_more_powerup_refer_friend_layout:
                if (mEarnMorePowerUpFragmentListener != null) {
                    mEarnMorePowerUpFragmentListener.onReferAFriendClicked();
                } else {
                    showMessage(Constants.Alerts.SOMETHING_WRONG);
                }
                break;

        }
    }
}
