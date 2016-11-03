package in.sportscafe.scgame.module.user.group.joingroup;

import java.util.HashMap;
import java.util.Map;

import in.sportscafe.scgame.ScGame;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.analytics.ScGameAnalytics;
import in.sportscafe.scgame.module.common.ApiResponse;
import in.sportscafe.scgame.webservice.MyWebService;
import in.sportscafe.scgame.webservice.ScGameCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 1/7/16.
 */
public class JoinGroupModelImpl implements JoinGroupModel {

    private OnJoinGroupModelListener mJoinGroupModelListener;

    private JoinGroupModelImpl(OnJoinGroupModelListener listener) {
        this.mJoinGroupModelListener = listener;
    }

    public static JoinGroupModel newInstance(OnJoinGroupModelListener listener) {
        return new JoinGroupModelImpl(listener);
    }

    @Override
    public void joinGroup(String groupCode) {
        if(groupCode.isEmpty() || groupCode.length() < 5) {
            mJoinGroupModelListener.onInvalidGroupCode();
            return;
        }

        if(ScGame.getInstance().hasNetworkConnection()) {
            callJoinGroupApi(groupCode);
        } else {
            mJoinGroupModelListener.onNoInternet();
        }
    }

    private void callJoinGroupApi(String groupCode) {
        MyWebService.getInstance().getJoinGroupRequest(groupCode).enqueue(
                new ScGameCallBack<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if(response.isSuccessful()) {
                            mJoinGroupModelListener.onSuccess();
                        } else {
                            mJoinGroupModelListener.onFailed(response.message());
                        }
                    }
                }
        );


        Map<String, String> values = new HashMap<>();
        values.put("GroupCode", groupCode);
        values.put("UserID", ScGameDataHandler.getInstance().getUserId());

        ScGameAnalytics.getInstance().trackOtherEvents("JOIN GROUP-ONCLICK", values);
    }

    public interface OnJoinGroupModelListener {

        void onSuccess();

        void onInvalidGroupCode();

        void onNoInternet();

        void onFailed(String message);
    }
}