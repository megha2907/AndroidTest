package in.sportscafe.nostragamus.module.store;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.powerupbank.PowerUpBankActivity;
import in.sportscafe.nostragamus.module.navigation.powerupbank.PowerUpBankFragmentListener;
import in.sportscafe.nostragamus.module.navigation.powerupbank.powerupbanktransaction.PBTransactionApiModelImpl;
import in.sportscafe.nostragamus.module.navigation.referfriends.referralcredits.ReferralHistory;
import in.sportscafe.nostragamus.module.navigation.referfriends.referralcredits.ReferralHistoryAdapter;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.powerups.PowerUp;

/**
 * Created by deepanshi on 7/25/17.
 */

public class StoreFragment extends BaseFragment {

    private static final String TAG = StoreFragment.class.getSimpleName();

    private PowerUpBankFragmentListener mPowerUpBankFragmentListener;

    private HashMap<String, PowerUp> mPowerUpMaps;

    private TextView mTvRunningLow;

    private TextView tvPBPowerup2xCount;
    private TextView tvPBPowerupNonegCount;
    private TextView tvPBPowerupPlayerPollCount;

    private ImageView ivPBPowerup2x;
    private ImageView ivPBPowerupNoneg;
    private ImageView ivPBPowerupPlayerPoll;

    private RecyclerView mStoreRecyclerView;
    private StoreAdapter storeAdapter;

    public StoreFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_store, container, false);
//
//        fetchStoreDataFromServer();
//        initRootView(rootView);
//        initAdapter();
        setInfo();

        return rootView;
    }

//    private void initAdapter() {
//        storeAdapter = new StoreAdapter(getContext());
//
//        if (mStoreRecyclerView != null) {
//            mStoreRecyclerView.setAdapter(storeAdapter);
//        }
//
//    }


//    private void fetchStoreDataFromServer() {
//
//        String appFlavor;
//        if (BuildConfig.IS_PAID_VERSION) {
//            appFlavor = "PRO";
//        } else {
//            appFlavor = "PS";
//        }
//        showProgressbar();
//        StoreApiModelImpl.newInstance(new StoreApiModelImpl().PBTransactionHistoryApiListener() {
//            @Override
//            public void noInternet() {
//                dismissProgressbar();
//                showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
//            }
//
//            @Override
//            public void onApiFailed() {
//                dismissProgressbar();
//                showMessage(Constants.Alerts.API_FAIL);
//            }
//
//            @Override
//            public void onSuccessResponse(List<ReferralHistory> referralHistoryList) {
//                dismissProgressbar();
//                onReferralHistoryListFetchedSuccessful(referralHistoryList);
//            }
//        }).performApiCall(appFlavor);
//    }

    private void initRootView(View rootView) {

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

    }

    private void setInfo() {

        if (NostragamusDataHandler.getInstance().getUserInfo() != null) {
            UserInfo userInfo = NostragamusDataHandler.getInstance().getUserInfo();
            getPowerUps(userInfo);
        }

    }

    private void getPowerUps(UserInfo userInfo) {

        if (userInfo.getPowerUps() != null) {

            mPowerUpMaps = getPowerUpMap(userInfo.getPowerUps());

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

    private void showOrHideContentBasedOnAppType() {
        /* if design changes needed for playstore app */
        if (!BuildConfig.IS_PAID_VERSION) {

        }
    }

}