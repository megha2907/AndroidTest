package in.sportscafe.nostragamus.module.prediction.powerupBank.dataProvider;

import java.util.HashMap;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PowerUp;
import in.sportscafe.nostragamus.module.prediction.powerupBank.dto.TransferPowerUpFromBankRequest;
import in.sportscafe.nostragamus.module.prediction.powerupBank.dto.TransferPowerUpFromBankResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

public class TransferPowerUpFromBankApiImpl {

    private BankTransferApiModelListener mBankTransferApiModelListener;

    private TransferPowerUpFromBankApiImpl(BankTransferApiModelListener listener) {
        this.mBankTransferApiModelListener = listener;
    }

    public static TransferPowerUpFromBankApiImpl newInstance(BankTransferApiModelListener listener) {
        return new TransferPowerUpFromBankApiImpl(listener);
    }

    public void transferToChallenge(PowerUp powerUp, int challengeId, int roomId) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            TransferPowerUpFromBankRequest request = new TransferPowerUpFromBankRequest();
            request.setChallengeId(challengeId);
            request.setRoomId(roomId);
            request.setPowerUps(powerUp);

            callBankTransferApi(request);
        } else {
            mBankTransferApiModelListener.onNoInternet();
        }
    }

    private void callBankTransferApi(TransferPowerUpFromBankRequest bankTransferRequest) {
        MyWebService.getInstance().transferPowerUpFromBank(bankTransferRequest).enqueue(
                new NostragamusCallBack<TransferPowerUpFromBankResponse>() {
                    @Override
                    public void onResponse(Call<TransferPowerUpFromBankResponse> call,
                                           Response<TransferPowerUpFromBankResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            mBankTransferApiModelListener.onSuccess(response.body());
                        } else {
                            mBankTransferApiModelListener.onFailed(response.message());
                        }
                    }
                }
        );
    }

    public interface BankTransferApiModelListener {
        void onSuccess(TransferPowerUpFromBankResponse transferPowerUpFromBankResponse);
        void onNoInternet();
        void onFailed(String message);
    }
}