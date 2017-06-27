package in.sportscafe.nostragamus.module.navigation.referfriends.referralcredits;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;

import java.util.List;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.referfriends.ReferFriendApiModelImpl;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.walletHistory.WalletHistoryAdapter;
import in.sportscafe.nostragamus.module.navigation.wallet.walletHistory.WalletHistoryTransaction;
import in.sportscafe.nostragamus.webservice.UserReferralInfo;
import in.sportscafe.nostragamus.webservice.UserReferralResponse;

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

    public ReferralCreditFragment() {
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

       // fetchReferralCreditsInfoFromServer();
        initRootView(rootView);

        return rootView;
    }

//    private void fetchReferralCreditsInfoFromServer() {
//
//        String appFlavor;
//        if (BuildConfig.IS_PAID_VERSION) {
//            appFlavor="PRO";
//        } else {
//            appFlavor=null;
//        }
//        showProgressbar();
//        ReferFriendApiModelImpl.newInstance(new ReferFriendApiModelImpl.ReferFriendApiListener() {
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
        rootView.findViewById(R.id.referral_credits_money_earned_rl).setOnClickListener(this);
        rootView.findViewById(R.id.referral_code_rl).setOnClickListener(this);

        mReferralHistoryRecyclerView = (RecyclerView) rootView.findViewById(R.id.referralHistoryRecyclerView);
        mReferralHistoryRecyclerView.setHasFixedSize(true);
        mReferralHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        
        tvRCFriendsReferred = (TextView) rootView.findViewById(R.id.referral_credit_number_of_friends_added);
        tvRCPowerup2xCount = (TextView) rootView.findViewById(R.id.referral_credits_2x_powerup_count);
        tvRCPowerupNonegCount = (TextView) rootView.findViewById(R.id.referral_credits_noneg_powerup_count);
        tvRCPowerupPlayerPollCount = (TextView) rootView.findViewById(R.id.referral_credits_player_poll_powerup_count);
        tvRCMoneyEarned = (TextView) rootView.findViewById(R.id.referral_credits_money_earned);
    }


    private void onReferralHistoryListFetchedSuccessful(List<ReferralHistory> referralHistoryList) {

            if (mReferralHistoryAdapter != null) {
                mReferralHistoryAdapter.addReferralHistoryIntoList(referralHistoryList);
            }

        /* Empty list view */
        if (getActivity() != null && getView() != null && mReferralHistoryAdapter != null) {
            if (mReferralHistoryAdapter.getReferralHistoryList() == null || mReferralHistoryAdapter.getReferralHistoryList().isEmpty()) {
                mReferralHistoryRecyclerView.setVisibility(View.GONE);
                LinearLayout noHistoryLayout = (LinearLayout) getView().findViewById(R.id.wallet_no_transaction_history_layout);
                noHistoryLayout.setVisibility(View.VISIBLE);
            } else {
                mReferralHistoryRecyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.refer_referral_credit_layout:
                if (mReferralCreditFragmentListener != null) {
                    mReferralCreditFragmentListener.onPowerUpRewardsClicked();
                }
                break;

            case R.id.refer_terms_layout:
                if (mReferralCreditFragmentListener != null) {
                    mReferralCreditFragmentListener.onCashRewardsClicked();
                }
                break;

        }
    }
}
