package in.sportscafe.nostragamus.module.prediction.powerupBank.dataProvider;

import java.util.HashMap;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeUserInfo;
import in.sportscafe.nostragamus.module.prediction.powerupBank.dto.TransferPowerUpFromBankRequest;
import in.sportscafe.nostragamus.module.prediction.powerupBank.dto.TransferPowerUpFromBankResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

public class BankTransferApiModelImpl {

    private BankTransferApiModelListener mBankTransferApiModelListener;

    private BankTransferApiModelImpl(BankTransferApiModelListener listener) {
        this.mBankTransferApiModelListener = listener;
    }

    public static BankTransferApiModelImpl newInstance(BankTransferApiModelListener listener) {
        return new BankTransferApiModelImpl(listener);
    }

    public void transferToChallenge(HashMap<String, Integer> powerUps, int challengeId, int roomId) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            TransferPowerUpFromBankRequest request = new TransferPowerUpFromBankRequest();
            request.setChallengeId(challengeId);
            request.setRoomId(roomId);
            request.setPowerUps(powerUps);

            callBankTransferApi(request);
        } else {
            mBankTransferApiModelListener.onNoInternet();
        }
    }

    private void callBankTransferApi(TransferPowerUpFromBankRequest bankTransferRequest) {
        MyWebService.getInstance().transferPowerUpFromBank(bankTransferRequest).enqueue(
                new NostragamusCallBack<TransferPowerUpFromBankResponse>() {
                    @Override
                    public void onResponse(Call<TransferPowerUpFromBankResponse> call, Response<TransferPowerUpFromBankResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            mBankTransferApiModelListener.onSuccess(response.body().getBankTransfer().getChallengeUserInfo());
                        } else {
                            mBankTransferApiModelListener.onFailed(response.message());
                        }
                    }
                }
        );
    }

    public interface BankTransferApiModelListener {
        void onSuccess(ChallengeUserInfo challengeUserInfo);
        void onNoInternet();
        void onFailed(String message);
    }
}