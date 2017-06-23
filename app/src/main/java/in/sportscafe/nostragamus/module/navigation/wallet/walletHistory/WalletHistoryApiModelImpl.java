package in.sportscafe.nostragamus.module.navigation.wallet.walletHistory;

import android.support.annotation.NonNull;

import java.util.List;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sandip on 12/04/17.
 */

public class WalletHistoryApiModelImpl {

    public interface WalletApiModelListener {
        void noInternet();
        void onSuccess(List<WalletHistoryTransaction> transactionList);
        void onFailure();
    }

    private WalletApiModelListener modelListener;

    private WalletHistoryApiModelImpl(WalletApiModelListener listener) {
        modelListener = listener;
    }

    public static WalletHistoryApiModelImpl getInstance(WalletApiModelListener listener) {
        return new WalletHistoryApiModelImpl(listener);
    }

    public void fetchWalletTransactionsFromServer(int pageNumber) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().getWalletTransactionHistory(pageNumber).enqueue(getUserPaymentCallBack());
        } else {
            modelListener.noInternet();
        }
    }

    @NonNull
    private NostragamusCallBack<List<WalletHistoryTransaction>> getUserPaymentCallBack() {
        return new NostragamusCallBack<List<WalletHistoryTransaction>>() {
            @Override
            public void onResponse(Call<List<WalletHistoryTransaction>> call, Response<List<WalletHistoryTransaction>> response) {
                super.onResponse(call, response);
                if (response != null && response.isSuccessful()) {
                    List<WalletHistoryTransaction> transactionList = response.body();
                    modelListener.onSuccess(transactionList);
                } else {
                    modelListener.onFailure();
                }
            }
        };
    }

}
