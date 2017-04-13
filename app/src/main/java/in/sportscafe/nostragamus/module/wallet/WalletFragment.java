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

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class WalletFragment extends NostragamusFragment implements WalletModel {

    private RecyclerView mWalletHistoryRecyclerView;

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

        mWalletHistoryRecyclerView.setAdapter(new WalletHistoryAdapter(getContext(), transactionList));
    }
}
