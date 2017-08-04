package in.sportscafe.nostragamus.module.navigation.powerupbank;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
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
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.powerups.PowerUp;
import in.sportscafe.nostragamus.webservice.UserReferralResponse;

/**
 * Created by deepanshi on 7/12/17.
 */

public class PowerUpBankFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = PowerUpBankFragment.class.getSimpleName();

    private PowerUpBankFragmentListener mPowerUpBankFragmentListener;

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

    public PowerUpBankFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof PowerUpBankActivity) {
            mPowerUpBankFragmentListener = (PowerUpBankFragmentListener) context;
        } else {
            throw new RuntimeException("Activity must implement " + TAG);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_powerup_bank, container, false);

        initRootView(rootView);
        setInfo();
        return rootView;
    }

    /* private void fetchPowerUpBankInfoFromServer() {

        String appFlavor;
        if (BuildConfig.IS_PAID_VERSION) {
            appFlavor = "PRO";
        } else {
            appFlavor = "PS";
        }

        showProgressbar();
        PowerUpBankApiModelImpl.newInstance(new PowerUpBankApiModelImpl.PowerUpBankApiListener() {
            @Override
            public void noInternet() {
                dismissProgressbar();
                showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
            }

            @Override
            public void onApiFailed() {
                dismissProgressbar();
                showMessage(Constants.Alerts.API_FAIL);
            }

            @Override
            public void onSuccessResponse(UserReferralResponse response) {
                dismissProgressbar();
                setInfo();
            }
        }).performApiCall(appFlavor);
    } */

    private void initRootView(View rootView) {
        rootView.findViewById(R.id.powerup_bank_earn_more_layout).setOnClickListener(this);
        rootView.findViewById(R.id.powerup_bank_terms_layout).setOnClickListener(this);
        rootView.findViewById(R.id.powerup_bank_txn_history_layout).setOnClickListener(this);
        rootView.findViewById(R.id.powerup_bank_store_layout).setOnClickListener(this);
        tvPowerupBankOne = (TextView) rootView.findViewById(R.id.powerup_bank_subheading_tv_one);
        tvPowerupBankTwo = (TextView) rootView.findViewById(R.id.powerup_bank_subheading_tv_two);
        mTvRunningLow = (TextView) rootView.findViewById(R.id.powerup_bank_low_powerups);

        tvPBPowerup2xCount = (TextView) rootView.findViewById(R.id.powerup_bank_2x_powerup_count);
        tvPBPowerupNonegCount = (TextView) rootView.findViewById(R.id.powerup_bank_noneg_powerup_count);
        tvPBPowerupPlayerPollCount = (TextView) rootView.findViewById(R.id.powerup_bank_audience_poll_powerup_count);

        ivPBPowerup2x = (ImageView) rootView.findViewById(R.id.powerup_bank_2x);
        ivPBPowerupNoneg = (ImageView) rootView.findViewById(R.id.powerup_bank_noneg);
        ivPBPowerupPlayerPoll = (ImageView) rootView.findViewById(R.id.powerup_bank_audience_poll);

        ivPBPowerup2x.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.double_powerup));
        ivPBPowerupNoneg.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.no_negative_powerup));
        ivPBPowerupPlayerPoll.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.audience_poll_powerup));

        showOrHideContentBasedOnAppType(rootView);

        mBundle = new Bundle();
    }

    private void setInfo() {

        UserInfo userInfo = Nostragamus.getInstance().getServerDataManager().getUserInfo();
        if (userInfo != null) {
            getPowerUps(userInfo);
        }
        setPowerUpText();
    }

    private void getPowerUps(UserInfo userInfo) {

        if (userInfo.getPowerUps() != null) {

            mPowerUpMaps = getPowerUpMap(userInfo.getPowerUps());

            if (mPowerUpMaps != null) {

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
            }else {
                int count = 0;
                onGet2xPowerUp(count, count < 1);
                onGetNonegsPowerUp(count, count < 1);
                onGetPollPowerUp(count, count < 1);
            }
        }else {
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
        if (powerUps != null && !powerUps.isEmpty()) {
            for (Map.Entry<String, Integer> entry : powerUps.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    powerUpMaps.put(entry.getKey(), new PowerUp(entry.getKey(), entry.getValue()));
                }
            }
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
        tvPowerupBankOne.setText(Html.fromHtml(getResources().getString(R.string.pb_text_one)), TextView.BufferType.SPANNABLE);

        /* Set the icon for "powerupbank" */
        String src = String.valueOf(Html.fromHtml(getResources().getString(R.string.pb_text_two)));
        SpannableString str = new SpannableString(src);
        Drawable d = getResources().getDrawable(R.drawable.powerbank_profile_icon);
        d.setBounds(0, 0, getResources().getDimensionPixelSize(R.dimen.dim_16), getResources().getDimensionPixelSize(R.dimen.dim_16));
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
        str.setSpan(span, str.toString().indexOf("@"), str.toString().indexOf("@") + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.WHITE);
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);

        /* Set the text color for "powerupbank" text to white */
        str.setSpan(fcs, str.toString().indexOf("@"), str.toString().indexOf("@") + 15, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        /* make them also bold */
        str.setSpan(bss, str.toString().indexOf("@"), str.toString().indexOf("@") + 15, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        tvPowerupBankTwo.setText(str, TextView.BufferType.SPANNABLE);
    }


    private void showOrHideContentBasedOnAppType(View rootView) {
        /* if design changes needed for playstore app */
        if (!BuildConfig.IS_PAID_VERSION) {
            rootView.findViewById(R.id.powerup_bank_earn_more_layout).setVisibility(View.GONE);
            rootView.findViewById(R.id.powerup_bank_store_layout).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.powerup_bank_earn_more_layout:
                if (mPowerUpBankFragmentListener != null) {
                    mPowerUpBankFragmentListener.onEarnMorePowerUpsClicked();
                } else {
                    showMessage(Constants.Alerts.SOMETHING_WRONG);
                }
                break;

            case R.id.powerup_bank_terms_layout:
                if (mPowerUpBankFragmentListener != null) {
                    mPowerUpBankFragmentListener.onTermsClicked();
                }
                break;

            case R.id.powerup_bank_txn_history_layout:
                if (mPowerUpBankFragmentListener != null) {
                    mPowerUpBankFragmentListener.onPowerUpTransactionHistoryClicked(mBundle);
                }
                break;

            case R.id.powerup_bank_store_layout:
                if (mPowerUpBankFragmentListener != null) {
                    mPowerUpBankFragmentListener.onStoreClicked();
                }
                break;
        }
    }
}
