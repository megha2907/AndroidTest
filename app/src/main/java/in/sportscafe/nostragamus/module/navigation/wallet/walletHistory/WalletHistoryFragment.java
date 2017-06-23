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

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class WalletHistoryFragment extends NostragamusFragment implements WalletHistoryModel, View.OnClickListener {

    private static final String TAG = WalletHistoryFragment.class.getSimpleName();

    private WalletHistoryFragmentListener mFragmentListener;
    private RecyclerView mWalletHistoryRecyclerView;
    private WalletHistoryAdapter mWalletHistoryAdapter;

    /**
     * Used for paginating list items */
    private int mPageNumber = 0;

    /**
     * Used to identify last api call / stop calling more...
     * Default always should allow to fetch more
     */
    private boolean mShouldFetchMore = true;

    public WalletHistoryFragment() {
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
        initMember();
    }

    private void initMember() {
        mWalletHistoryAdapter = new WalletHistoryAdapter(getContext()) {

            @Override
            public void loadMoreHistory() {
                if (mShouldFetchMore) {
                    loadTransactionDetails(++mPageNumber);
                }
            }
        };

        if (mWalletHistoryRecyclerView != null) {
            mWalletHistoryRecyclerView.setAdapter(mWalletHistoryAdapter);
        }

        /* Load first time */
        loadTransactionDetails(++mPageNumber);
    }

    private void showWalletBalance() {
        if (getView() != null) {
            double amount = WalletHelper.getTotalBalance();
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

    private void initViews(View view) {
        view.findViewById(R.id.back_button).setOnClickListener(this);

        mWalletHistoryRecyclerView = (RecyclerView) view.findViewById(R.id.walletRecyclerView);
        mWalletHistoryRecyclerView.setHasFixedSize(true);
        mWalletHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    private void loadTransactionDetails(int pageNumber) {
        showProgressbar();
        WalletHistoryApiModelImpl apiModel = WalletHistoryApiModelImpl.getInstance(getWalletTransactionApiListener());
        apiModel.fetchWalletTransactionsFromServer(pageNumber);
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
        if (transactionList == null || transactionList.isEmpty()) {
            mShouldFetchMore = false;
        } else {
            if (mWalletHistoryAdapter != null) {
                mWalletHistoryAdapter.addWalletHistoryIntoList(transactionList);
            }
        }

        /* Empty list view */
        if (getActivity() != null && getView() != null && mWalletHistoryAdapter != null) {
            if (mWalletHistoryAdapter.getWalletHistoryList() == null || mWalletHistoryAdapter.getWalletHistoryList().isEmpty()) {
                mWalletHistoryRecyclerView.setVisibility(View.GONE);
                LinearLayout noHistoryLayout = (LinearLayout) getView().findViewById(R.id.wallet_no_transaction_history_layout);
                noHistoryLayout.setVisibility(View.VISIBLE);
            } else {
                mWalletHistoryRecyclerView.setVisibility(View.VISIBLE);
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
}
