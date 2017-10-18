package in.sportscafe.nostragamus.module.prediction.powerupBank.dataProvider;

import android.support.annotation.NonNull;

import in.sportscafe.nostragamus.module.prediction.powerupBank.dto.PowerupBankStatusRequest;
import in.sportscafe.nostragamus.module.prediction.powerupBank.dto.PowerUpBankStatusResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sandip on 21/07/17.
 */

public class PowerupBankStatusApiModelImpl {

    private PowerupBankStatusApiListener mListener;

    private PowerupBankStatusApiModelImpl(PowerupBankStatusApiListener listener) {
        mListener = listener;
    }

    public static PowerupBankStatusApiModelImpl newInstance(@NonNull PowerupBankStatusApiListener listener) {
        return new PowerupBankStatusApiModelImpl(listener);
    }

    public void performApiCall(int challengeId, int roomId) {
        PowerupBankStatusRequest request = new PowerupBankStatusRequest();
        request.setChallengeId(challengeId);
        request.setRoomId(roomId);

        MyWebService.getInstance().getPowerupBankStatus(request).enqueue(
                new NostragamusCallBack<PowerUpBankStatusResponse>() {
                    @Override
                    public void onResponse(Call<PowerUpBankStatusResponse> call, Response<PowerUpBankStatusResponse> response) {
                        super.onResponse(call, response);

                        if (response != null && response.body() != null && response.isSuccessful()) {
                            if (mListener != null) {
                                mListener.onSuccessResponse(response.body());
                            }
                        } else {
                            if (mListener != null) {
                                mListener.onApiFailed();
                            }
                        }
                    }
                }
        );
    }

    public interface PowerupBankStatusApiListener {
        void noInternet();
        void onApiFailed();
        void onSuccessResponse(PowerUpBankStatusResponse response);
    }
}
