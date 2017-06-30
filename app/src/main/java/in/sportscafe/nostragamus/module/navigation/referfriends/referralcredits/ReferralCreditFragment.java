package in.sportscafe.nostragamus.module.navigation.referfriends.referralcredits;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.Log;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.List;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.referfriends.ReferFriendFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.webservice.UserReferralInfo;

/**
 * Created by deepanshi on 6/23/17.
 */

public class ReferralCreditFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = ReferralCreditFragment.class.getSimpleName();

    private ReferralCreditFragmentListener mReferralCreditFragmentListener;

    private TextView tvRCFriendsReferred;
    private TextView tvRCPowerup2xCount;
    private TextView tvRCPowerupNonegCount;
    private TextView tvRCPowerupPlayerPollCount;
    private TextView tvRCMoneyEarned;

    private RecyclerView mReferralHistoryRecyclerView;
    private ReferralHistoryAdapter mReferralHistoryAdapter;

    String mReferralCode;
    String mWalletInit;

    public ReferralCreditFragment() {
    }


    public static ReferralCreditFragment newInstance(UserReferralInfo userReferralInfo) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.BundleKeys.USER_REFERRAL_INFO, Parcels.wrap(userReferralInfo));

        ReferralCreditFragment fragment = new ReferralCreditFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ReferralCreditActivity) {
            mReferralCreditFragmentListener = (ReferralCreditFragmentListener) context;
        } else {
            throw new RuntimeException("Activity must implement " + TAG);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_referral_credits, container, false);

        fetchReferralCreditsInfoFromServer();
        initRootView(rootView);
        initAdapter();

        return rootView;
    }

    private void initAdapter() {
        mReferralHistoryAdapter = new ReferralHistoryAdapter(getContext());

        if (mReferralHistoryRecyclerView != null) {
            mReferralHistoryRecyclerView.setAdapter(mReferralHistoryAdapter);
        }

    }


    private void fetchReferralCreditsInfoFromServer() {

        String appFlavor;
        if (BuildConfig.IS_PAID_VERSION) {
            appFlavor = "PRO";
        } else {
            appFlavor = null;
        }
        showProgressbar();
        ReferralCreditApiModelImpl.newInstance(new ReferralCreditApiModelImpl.ReferralCreditApiListener() {
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

        UserReferralInfo userReferralInfo = Parcels.unwrap(getArguments().getParcelable(Constants.BundleKeys.USER_REFERRAL_INFO));

        mReferralHistoryRecyclerView = (RecyclerView) rootView.findViewById(R.id.referralHistoryRecyclerView);
        mReferralHistoryRecyclerView.setHasFixedSize(true);
        mReferralHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        tvRCFriendsReferred = (TextView) rootView.findViewById(R.id.referral_credit_number_of_friends_added);
        tvRCPowerup2xCount = (TextView) rootView.findViewById(R.id.referral_credits_2x_powerup_count);
        tvRCPowerupNonegCount = (TextView) rootView.findViewById(R.id.referral_credits_noneg_powerup_count);
        tvRCPowerupPlayerPollCount = (TextView) rootView.findViewById(R.id.referral_credits_player_poll_powerup_count);
        tvRCMoneyEarned = (TextView) rootView.findViewById(R.id.referral_credits_money_earned);
        rootView.findViewById(R.id.refer_friend_btn).setOnClickListener(this);

        setReferralInfo(userReferralInfo);
    }

    private void setReferralInfo(UserReferralInfo userReferralInfo) {

        if (userReferralInfo != null) {
            tvRCFriendsReferred.setText(userReferralInfo.getFriendsReferred() + " Friends Added");

            if (userReferralInfo.getReferralCredits() != null && userReferralInfo.getReferralCredits() > 0) {
                tvRCMoneyEarned.setText(WalletHelper.getFormattedStringOfAmount(userReferralInfo.getReferralCredits()));
            }

            HashMap<String, Integer> powerUpMap = userReferralInfo.getPowerUps();

            Integer powerUp2xCount = powerUpMap.get(Constants.Powerups.XX);
            Integer powerUpNonNegsCount = powerUpMap.get(Constants.Powerups.NO_NEGATIVE);
            Integer powerUpPlayerPollCount = powerUpMap.get(Constants.Powerups.AUDIENCE_POLL);

            if (null == powerUp2xCount) {
                powerUp2xCount = 0;
            }

            if (null == powerUpNonNegsCount) {
                powerUpNonNegsCount = 0;
            }

            if (null == powerUpPlayerPollCount) {
                powerUpPlayerPollCount = 0;
            }

            tvRCPowerup2xCount.setText(String.valueOf(powerUp2xCount));
            tvRCPowerupNonegCount.setText(String.valueOf(powerUpNonNegsCount));
            tvRCPowerupPlayerPollCount.setText(String.valueOf(powerUpPlayerPollCount));
            mReferralCode = userReferralInfo.getReferralCode();
            mWalletInit = String.valueOf(userReferralInfo.getWalletInitialAmount());

        } else {
            tvRCFriendsReferred.setText("0 Friends Added");
            tvRCPowerup2xCount.setText("0");
            tvRCPowerupNonegCount.setText("0");
            tvRCPowerupPlayerPollCount.setText("0");
        }

    }


    private void onReferralHistoryListFetchedSuccessful(List<ReferralHistory> referralHistoryList) {

        if (mReferralHistoryAdapter != null) {
            mReferralHistoryAdapter.addReferralHistoryIntoList(referralHistoryList);
        }

        /* Empty list view */
        if (getActivity() != null && getView() != null && mReferralHistoryAdapter != null) {
            if (mReferralHistoryAdapter.getReferralHistoryList() == null || mReferralHistoryAdapter.getReferralHistoryList().isEmpty()) {
                mReferralHistoryRecyclerView.setVisibility(View.GONE);
                LinearLayout noHistoryLayout = (LinearLayout) getView().findViewById(R.id.referral_credits_no_history_layout);
                noHistoryLayout.setVisibility(View.VISIBLE);
            } else {
                mReferralHistoryRecyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.refer_friend_btn:
                if (mReferralCreditFragmentListener != null) {
                    mReferralCreditFragmentListener.onReferAFriendClicked(mReferralCode, mWalletInit);
                }
                break;

        }
    }
}
