package in.sportscafe.nostragamus.module.navigation.powerupbank.powerupbanktransaction;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.referfriends.referralcredits.ReferralCreditFragment;
import in.sportscafe.nostragamus.module.navigation.referfriends.referralcredits.ReferralHistory;
import in.sportscafe.nostragamus.module.navigation.referfriends.referralcredits.ReferralHistoryAdapter;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.powerups.PowerUp;
import in.sportscafe.nostragamus.webservice.UserReferralInfo;

/**
 * Created by deepanshi on 7/13/17.
 */

public class PBTransactionFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = ReferralCreditFragment.class.getSimpleName();

    private PBTransactionFragmentListener mPBTransactionFragmentListener;

    private TextView tvPBPowerup2xCount;
    private TextView tvPBPowerupNonegCount;
    private TextView tvPBPowerupPlayerPollCount;

    private TextView mTvRunningLow;

    private RecyclerView mPBTransactionHistoryRecyclerView;
    private ReferralHistoryAdapter mPBTransactionHistoryAdapter;

    public PBTransactionFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof PBTransactionActivity) {
            mPBTransactionFragmentListener = (PBTransactionFragmentListener) context;
        } else {
            throw new RuntimeException("Activity must implement " + TAG);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pb_transaction, container, false);

        fetchPBTransactionHistoryFromServer();
        initRootView(rootView);
        initAdapter();

        return rootView;
    }

    private void initAdapter() {
        mPBTransactionHistoryAdapter = new ReferralHistoryAdapter(getContext());

        if (mPBTransactionHistoryRecyclerView != null) {
            mPBTransactionHistoryRecyclerView.setAdapter(mPBTransactionHistoryAdapter);
        }

    }


    private void fetchPBTransactionHistoryFromServer() {

        String appFlavor;
        if (BuildConfig.IS_PAID_VERSION) {
            appFlavor = "PRO";
        } else {
            appFlavor = "PS";
        }
        showProgressbar();
        PBTransactionApiModelImpl.newInstance(new PBTransactionApiModelImpl.PBTransactionHistoryApiListener() {
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
            public void onSuccessResponse(List<ReferralHistory> referralHistoryList) {
                dismissProgressbar();
                onReferralHistoryListFetchedSuccessful(referralHistoryList);
            }
        }).performApiCall(appFlavor);
    }

    private void initRootView(View rootView) {

        mPBTransactionHistoryRecyclerView = (RecyclerView) rootView.findViewById(R.id.pbTransactionRecyclerView);
        mPBTransactionHistoryRecyclerView.setHasFixedSize(true);
        mPBTransactionHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        tvPBPowerup2xCount = (TextView) rootView.findViewById(R.id.powerup_bank_2x_powerup_count);
        tvPBPowerupNonegCount = (TextView) rootView.findViewById(R.id.powerup_bank_noneg_powerup_count);
        tvPBPowerupPlayerPollCount = (TextView) rootView.findViewById(R.id.powerup_bank_audience_poll_powerup_count);
        mTvRunningLow = (TextView) rootView.findViewById(R.id.powerup_bank_low_powerups);
        
        setPowerups(getPowerUpMap());
    }

    private void setPowerups(HashMap<String, PowerUp> powerUpMaps) {

        if (powerUpMaps != null) {
            int count;

            PowerUp powerUp = powerUpMaps.get(Constants.Powerups.XX);
            if (null == powerUp) {
                count = 0;
            } else {
                count = powerUp.getCount();
            }
            onGet2xPowerUp(count, count < 3);

            powerUp = powerUpMaps.get(Constants.Powerups.NO_NEGATIVE);
            if (null == powerUp) {
                count = 0;
            } else {
                count = powerUp.getCount();
            }
            onGetNonegsPowerUp(count, count < 3);

            powerUp = powerUpMaps.get(Constants.Powerups.AUDIENCE_POLL);
            if (null == powerUp) {
                count = 0;
            } else {
                count = powerUp.getCount();
            }
            onGetPollPowerUp(count, count < 3);
        } else {
            int count = 0;
            onGet2xPowerUp(count, count < 3);
            onGetNonegsPowerUp(count, count < 3);
            onGetPollPowerUp(count, count < 3);
        }

    }

    private void onGetPollPowerUp(int count, boolean runningLow) {
        setPowerUpCount(tvPBPowerupPlayerPollCount, count, runningLow);
    }

    private void onGetNonegsPowerUp(int count, boolean runningLow) {
        setPowerUpCount(tvPBPowerupNonegCount, count, runningLow);
    }

    private void onGet2xPowerUp(int count, boolean runningLow) {
        setPowerUpCount(tvPBPowerup2xCount, count, runningLow);
    }


    private void setPowerUpCount(TextView textView, int count, boolean runningLow) {
        textView.setText(String.valueOf(count));
        if (runningLow) {
            //textView.setBackground(getResources().getDrawable(R.drawable.bank_powerup_count_bg));

            if (mTvRunningLow.getVisibility() == View.GONE) {
                mTvRunningLow.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showOrHideContentBasedOnAppType() {
        if (!BuildConfig.IS_PAID_VERSION) {

        }
    }


    private void onReferralHistoryListFetchedSuccessful(List<ReferralHistory> referralHistoryList) {

        if (mPBTransactionHistoryAdapter != null) {
            mPBTransactionHistoryAdapter.addReferralHistoryIntoList(referralHistoryList);
        }

        RelativeLayout recyclerViewLayout = (RelativeLayout) getView().findViewById(R.id.pb_transaction_friends_act_rl);

        /* Empty list view */
        if (getActivity() != null && getView() != null && mPBTransactionHistoryAdapter != null) {
            if (mPBTransactionHistoryAdapter.getReferralHistoryList() == null || mPBTransactionHistoryAdapter.getReferralHistoryList().isEmpty()) {
                mPBTransactionHistoryRecyclerView.setVisibility(View.GONE);
                recyclerViewLayout.setVisibility(View.GONE);
                LinearLayout noHistoryLayout = (LinearLayout) getView().findViewById(R.id.pb_transaction_no_history_layout);
                noHistoryLayout.setVisibility(View.VISIBLE);
            } else {
                recyclerViewLayout.setVisibility(View.VISIBLE);
                mPBTransactionHistoryRecyclerView.setVisibility(View.VISIBLE);
            }
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.refer_friend_btn:
                if (mPBTransactionFragmentListener != null) {
                   // mPBTransactionFragmentListener.onReferAFriendClicked(mReferralCode, mWalletInit);
                }
                break;

        }
    }

    public HashMap<String,PowerUp> getPowerUpMap() {
        HashMap<String, PowerUp> powerUpMaps = null;
        Bundle args = getArguments();
        if (args != null) {
            powerUpMaps = Parcels.unwrap(getArguments().getParcelable(Constants.BundleKeys.POWERUPS));
        }
        return powerUpMaps;
    }
}
