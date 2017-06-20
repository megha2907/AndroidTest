package in.sportscafe.nostragamus.module.navigation.wallet.walletHistory;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.Log;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.user.login.dto.UserPaymentInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class WalletHistoryFragment extends NostragamusFragment implements WalletHistoryModel, View.OnClickListener {

    private static final String TAG = WalletHistoryFragment.class.getSimpleName();

    private WalletHistoryFragmentListener mFragmentListener;
    private RecyclerView mWalletHistoryRecyclerView;
    private TextView mAmountWonTextView;
    private UserPaymentInfo mUserPaymentInfo;

    public WalletHistoryFragment() {
    }

    public void setUserPaymentInfo(UserPaymentInfo userPaymentInfo) {
        mUserPaymentInfo = userPaymentInfo;
    }

    public static WalletHistoryFragment newInstance() {
        WalletHistoryFragment fragment = new WalletHistoryFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WalletHistoryFragmentListener) {
            mFragmentListener = (WalletHistoryFragmentListener) context;
        } else {
            throw new RuntimeException("Activity must implement " +
                    WalletHistoryFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_history, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        showWalletBalance();
        loadTransactionDetails();

        /*if (NostragamusDataHandler.getInstance().getUserInfo() != null) {
            mUserPaymentInfo = NostragamusDataHandler.getInstance().getUserInfo().getUserPaymentInfo();
        }

        if (mUserPaymentInfo == null ||
                (mUserPaymentInfo.getBank() == null && mUserPaymentInfo.getPaytm() == null)) {

            Log.d(TAG, "It seems that paymentInfo not available");
            showAddPaymentInfoButton();
        } else {

        }*/
    }

    private void showWalletBalance() {
        if (getView() != null) {
            double amount = WalletHelper.getDepositAmount();
            if (amount > 0) {
                TextView balanceTextView = (TextView) getView().findViewById(R.id.wallet_history_bal_amount_textView);
                balanceTextView.setText(WalletHelper.getFormattedStringOfAmount(amount));
            }

            // Withdrawals in progress
            int withdrawProgress = WalletHelper.getWithdrawalsInProgress();
            if (withdrawProgress > 0) {
                TextView withdrawProgressTextView = (TextView) getView().findViewById(R.id.wallet_history_withdraw_progress_textView);
                withdrawProgressTextView.setText(withdrawProgress + " withdrawals in progress");
            }
        }
    }

    private void showAddPaymentInfoButton() {
        /*if (getActivity() != null && getView() != null) {
            LinearLayout addPaymentDetailsLayout = (LinearLayout) getView().findViewById(R.id.wallet_add_payment_details_layout);
            addPaymentDetailsLayout.setVisibility(View.VISIBLE);

            LinearLayout historyLayout = (LinearLayout) getView().findViewById(R.id.wallet_history_layout);
            historyLayout.setVisibility(View.GONE);
        }*/
    }

    private void initViews(View view) {
        view.findViewById(R.id.back_button).setOnClickListener(this);

        mWalletHistoryRecyclerView = (RecyclerView) view.findViewById(R.id.walletRecyclerView);
        mWalletHistoryRecyclerView.setHasFixedSize(true);
        mWalletHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    private void loadTransactionDetails() {
        showProgressbar();
        WalletHistoryApiModelImpl apiModel = WalletHistoryApiModelImpl.getInstance(getWalletTransactionApiListener());
        apiModel.fetchWalletTransactionsFromServer();
    }

    @NonNull
    private WalletHistoryApiModelImpl.WalletApiModelListener getWalletTransactionApiListener() {
        return new WalletHistoryApiModelImpl.WalletApiModelListener() {
            @Override
            public void noInternet() {
                dismissProgressbar();
                showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
            }

            @Override
            public void onSuccess(List<WalletHistoryTransaction> transactionList) {
                dismissProgressbar();
                onTransactionListFetchedSuccessful(transactionList);
            }

            @Override
            public void onFailure() {
                dismissProgressbar();
                showMessage(Constants.Alerts.API_FAIL);
            }
        };
    }

    private void onTransactionListFetchedSuccessful(List<WalletHistoryTransaction> transactionList) {
        if (getActivity() != null && getView() != null && mWalletHistoryRecyclerView != null) {
            if (transactionList == null || transactionList.isEmpty()) {
                mWalletHistoryRecyclerView.setVisibility(View.GONE);
                LinearLayout noHistoryLayout = (LinearLayout) getView().findViewById(R.id.wallet_no_transaction_history_layout);
                noHistoryLayout.setVisibility(View.VISIBLE);

                return;
            } else {
                mWalletHistoryRecyclerView.setVisibility(View.VISIBLE);
                mWalletHistoryRecyclerView.setAdapter(new WalletHistoryAdapter(getContext(), transactionList));
            }
        }
    }

    private void setAmountWon(List<WalletHistoryTransaction> transactionList) {
        double amountWon = 0;
        if (transactionList != null) {
            for (WalletHistoryTransaction transaction : transactionList) {
                if (transaction.getMoneyFlow().equals(Constants.MoneyFlow.OUT)) {   // MoneyFlow == OUT means, user got amount (Credit for user)
                    amountWon += transaction.getAmount();
                }
            }

            if (amountWon > 0) {
                mAmountWonTextView.setText("₹ " + String.valueOf(amountWon));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                if (mFragmentListener != null) {
                    mFragmentListener.onBackClicked();
                }
                break;
        }
    }

    private void launchPaymentDetails() {
        if (getActivity() != null && getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).launchPaymentDetailsActivity();
        }
    }
}
