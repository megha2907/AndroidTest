package in.sportscafe.nostragamus.module.popups;

import java.util.List;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
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

    private void callPopUpsApi(String screenName) {
        MyWebService.getInstance().getPopUpsRequest(screenName).enqueue(
                new NostragamusCallBack<PopUpResponse>() {
                    @Override
                    public void onResponse(Call<PopUpResponse> call, Response<PopUpResponse> response) {
                        if (response.isSuccessful()) {
                            super.onResponse(call, response);

                            List<PopUp> popUps = response.body().getPopUps();
                            if (null != popUps) {
                                mPopUpsModelListener.onSuccessGetUpdatedPopUps(popUps);
                            }
                        } else {
                            mPopUpsModelListener.onFailedGetUpdatePopUps(response.message());
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
