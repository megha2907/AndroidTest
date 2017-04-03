package in.sportscafe.nostragamus.module.allchallenges.info;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.allchallenges.dto.Pool;
import in.sportscafe.nostragamus.module.allchallenges.dto.PoolListResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 14/6/16.
 */
public class PoolListApiModelImpl {

    private OnPoolListApiModelListener mApiModelListener;

    public PoolListApiModelImpl(OnPoolListApiModelListener listener) {
        this.mApiModelListener = listener;
    }

    public static PoolListApiModelImpl newInstance(OnPoolListApiModelListener listener) {
        return new PoolListApiModelImpl(listener);
    }

    public void getPoolList(int challengeId) {
        /*if (Nostragamus.getInstance().hasNetworkConnection()) {
            callPoolListApi(challengeId);
        } else {
            mApiModelListener.onNoInternet();
        }*/

        mApiModelListener.onSuccessPoolListApi(getDummyPoolList());
    }

    private void callPoolListApi(int challengeId) {
        mApiModelListener.onApiCallStarted();

        MyWebService.getInstance().getPoolListRequest(challengeId).enqueue(
                new NostragamusCallBack<PoolListResponse>() {
                    @Override
                    public void onResponse(Call<PoolListResponse> call, Response<PoolListResponse> response) {
                        super.onResponse(call, response);

                        if (!mApiModelListener.onApiCallStopped()) {
                            return;
                        }

                        if (response.isSuccessful()) {
                            handlePoolListResponse(response.body().getPoolList());
                        } else {
                            mApiModelListener.onFailedPoolListApi();
                        }
                    }
                }
        );
    }

    private void handlePoolListResponse(List<Pool> poolList) {
        if (null == poolList || poolList.isEmpty()) {
            mApiModelListener.onEmpty();
            return;
        }

        mApiModelListener.onSuccessPoolListApi(poolList);
    }

    private List<Pool> getDummyPoolList() {
        String json = null;
        try {
            InputStream is = Nostragamus.getInstance().getAssets().open("json/pool_list.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (null != json) {
            return MyWebService.getInstance().getObjectFromJson(
                    json,
                    PoolListResponse.class
            ).getPoolList();
        }
        return null;
    }

    public interface OnPoolListApiModelListener {

        void onSuccessPoolListApi(List<Pool> poolList);

        void onEmpty();

        void onFailedPoolListApi();

        void onNoInternet();

        void onApiCallStarted();

        boolean onApiCallStopped();
    }
}