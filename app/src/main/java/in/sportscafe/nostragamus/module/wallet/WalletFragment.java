package in.sportscafe.nostragamus.module.wallet;


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

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.home.HomeActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class WalletFragment extends NostragamusFragment implements WalletModel, View.OnClickListener {

    private RecyclerView mWalletHistoryRecyclerView;
    private TextView mAmountWonTextView;

    public WalletFragment() {
    }

    public static WalletFragment newInstance() {
        return new WalletFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadTransactionDetails();
    }

    private void initViews(View view) {
        view.findViewById(R.id.wallet_update_payment_btn).setOnClickListener(this);
        view.findViewById(R.id.wallet_update_payment_btn1).setOnClickListener(this);

        mAmountWonTextView = (TextView) view.findViewById(R.id.wallet_amount_won_textview);

        mWalletHistoryRecyclerView = (RecyclerView) view.findViewById(R.id.walletRecyclerView);
        mWalletHistoryRecyclerView.setHasFixedSize(true);
        mWalletHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    private void loadTransactionDetails() {
        showProgressbar();
        WalletApiModelImpl apiModel = WalletApiModelImpl.getInstance(getWalletTransactionApiListener());
        apiModel.fetchWalletTransactionsFromServer();
    }

    @NonNull
    private WalletApiModelImpl.WalletApiModelListener getWalletTransactionApiListener() {
        return new WalletApiModelImpl.WalletApiModelListener() {
            @Override
            public void noInternet() {
                dismissProgressbar();
                showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
            }

            @Override
            public void onSuccess(List<WalletTransaction> transactionList) {
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

    private void onTransactionListFetchedSuccessful(List<WalletTransaction> transactionList) {
        if (transactionList == null || transactionList.isEmpty()) {
            LinearLayout noHistoryLayout = (LinearLayout) findViewById(R.id.wallet_no_history_layout);
            noHistoryLayout.setVisibility(View.VISIBLE);

            LinearLayout historyLayout = (LinearLayout) findViewById(R.id.wallet_history_layout);
            historyLayout.setVisibility(View.GONE);

            return;
        }

        setAmountWon(transactionList);
        mWalletHistoryRecyclerView.setAdapter(new WalletHistoryAdapter(getContext(), transactionList));
    }

    private void setAmountWon(List<WalletTransaction> transactionList) {
        double amountWon = 0;
        if (transactionList != null) {
            for (WalletTransaction transaction : transactionList) {
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
            case R.id.wallet_update_payment_btn:
            case R.id.wallet_update_payment_btn1:
                launchPaymentDetails();
                break;

        }
    }

    private void launchPaymentDetails() {
        if (getActivity() != null && getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).launchPaymentDetailsActivity();
        }
    }
}
