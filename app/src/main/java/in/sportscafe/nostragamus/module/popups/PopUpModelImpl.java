package in.sportscafe.nostragamus.module.popups;

import java.util.List;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import in.sportscafe.nostragamus.webservice.PopUpResponse;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 2/2/17.
 */

public class PopUpModelImpl {

    private PopUpModelImpl.OnGetPopUpModelListener mPopUpsModelListener;

    private PopUpModelImpl(PopUpModelImpl.OnGetPopUpModelListener listener) {
        this.mPopUpsModelListener = listener;
    }

    public static PopUpModelImpl newInstance(PopUpModelImpl.OnGetPopUpModelListener listener) {
        return new PopUpModelImpl(listener);
    }

    public void getPopUps(String screenName) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            callPopUpsApi(screenName);
        }
    }

    public void AcknowledgePopups(String popUpName) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            callAcknowledgePopup(popUpName);
        }
    }

    private void callPopUpsApi(String screenName) {
        MyWebService.getInstance().getPopUpsRequest(screenName).enqueue(
                new NostragamusCallBack<PopUpResponse>() {
                    @Override
                    public void onResponse(Call<PopUpResponse> call, Response<PopUpResponse> response) {
                        if (response.isSuccessful()) {
                            super.onResponse(call, response);

                            List<PopUp> popUps = response.body().getPopUps();
                            if (popUps == null || popUps.isEmpty()) {
                                mPopUpsModelListener.onFailedGetUpdatePopUps(response.message());
                            }else {
                                mPopUpsModelListener.onSuccessGetUpdatedPopUps(popUps);
                            }
                        } else {
                            mPopUpsModelListener.onFailedGetUpdatePopUps(response.message());
                        }
                    }
                }
        );
    }

    private void callAcknowledgePopup(String popUpName) {
        MyWebService.getInstance().getAcknowledgePopupRequest(popUpName).enqueue(
                new NostragamusCallBack<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()) {
                            super.onResponse(call, response);

                            String acknowledgePopup = response.body().getMessage();

                        } else {
                        }
                    }
                }
        );
    }

    public interface OnGetPopUpModelListener {

        void onSuccessGetUpdatedPopUps(List<PopUp> popUps);

        void onFailedGetUpdatePopUps(String message);
    }
    
}
