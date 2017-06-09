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

public class WalletApiModelImpl {

    public interface WalletApiModelListener {
        void noInternet();
        void onSuccess(List<WalletTransaction> transactionList);
        void onFailure();
    }

    private WalletApiModelListener modelListener;

    private WalletApiModelImpl(WalletApiModelListener listener) {
        modelListener = listener;
    }

    public static WalletApiModelImpl getInstance(WalletApiModelListener listener) {
        return new WalletApiModelImpl(listener);
    }

    public void fetchWalletTransactionsFromServer() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().getWalletTransactionHistory().enqueue(getUserPaymentCallBack());
        } else {
            modelListener.noInternet();
        }
    }

    @NonNull
    private NostragamusCallBack<List<WalletTransaction>> getUserPaymentCallBack() {
        return new NostragamusCallBack<List<WalletTransaction>>() {
            @Override
            public void onResponse(Call<List<WalletTransaction>> call, Response<List<WalletTransaction>> response) {
                super.onResponse(call, response);
                if (response != null && response.isSuccessful()) {
                    List<WalletTransaction> transactionList = (List<WalletTransaction>) response.body();
                    modelListener.onSuccess(transactionList);
                } else {
                    modelListener.onFailure();
                }
            }
        };
    }

}
