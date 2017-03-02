package in.sportscafe.nostragamus.module.popups.banktransfer;

import java.util.HashMap;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 30/6/16.
 */
public class BankTranferApiModelImpl {

    private BankTransferApiModelListener mBankTransferApiModelListener;

    private BankTranferApiModelImpl(BankTransferApiModelListener listener) {
        this.mBankTransferApiModelListener = listener;
    }

    public static BankTranferApiModelImpl newInstance(BankTransferApiModelListener listener) {
        return new BankTranferApiModelImpl(listener);
    }

    public void transferToChallenge(HashMap<String, Integer> powerUps, int challengeId) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            BankTransferRequest bankTransferRequest = new BankTransferRequest();
            bankTransferRequest.setChallengeId(challengeId);
            bankTransferRequest.setPowerUps(powerUps);

            callBankTransferApi(bankTransferRequest);
        } else {
            mBankTransferApiModelListener.onNoInternet();
        }
    }

    private void callBankTransferApi(BankTransferRequest bankTransferRequest) {
        MyWebService.getInstance().getBankTransferRequest(bankTransferRequest).enqueue(
                new NostragamusCallBack<BankTransferResponse>() {
                    @Override
                    public void onResponse(Call<BankTransferResponse> call, Response<BankTransferResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            mBankTransferApiModelListener.onSuccess(response.body().getBankTransfer().getChallengeUserInfo().getPowerUps());
                        } else {
                            mBankTransferApiModelListener.onFailed(response.message());
                        }
                    }
                }
        );
    }

    public interface BankTransferApiModelListener {

        void onSuccess(HashMap<String, Integer> powerUps);

        void onNoInternet();

        void onFailed(String message);
    }
}