package in.sportscafe.nostragamus.module.user.group.joingroup;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 1/7/16.
 */
public class JoinGroupModelImpl implements JoinGroupModel {

    private String mGroupCode;

    private OnJoinGroupModelListener mJoinGroupModelListener;

    private JoinGroupModelImpl(OnJoinGroupModelListener listener) {
        this.mJoinGroupModelListener = listener;
    }

    public static JoinGroupModel newInstance(OnJoinGroupModelListener listener) {
        return new JoinGroupModelImpl(listener);
    }

    @Override
    public void init(Bundle bundle) {
        if(null != bundle && bundle.containsKey(BundleKeys.GROUP_CODE)) {
            mGroupCode = bundle.getString(BundleKeys.GROUP_CODE);
            if(isValidGroupCode(mGroupCode)) {
                mJoinGroupModelListener.onGetGroupCode(mGroupCode);
            }
        }
    }

    @Override
    public void joinGroup(String groupCode) {
        if(isValidGroupCode(groupCode)) {
            if (Nostragamus.getInstance().hasNetworkConnection()) {
                callJoinGroupApi(groupCode);
            } else {
                mJoinGroupModelListener.onNoInternet();
            }
        } else {
            mJoinGroupModelListener.onInvalidGroupCode();
        }
    }

    @Override
    public boolean hadGroupCode() {
        return null != mGroupCode;
    }

    private boolean isValidGroupCode(String groupCode) {
        return groupCode.length() == 5;
    }

    private void callJoinGroupApi(String groupCode) {
        MyWebService.getInstance().getJoinGroupRequest(groupCode).enqueue(
                new NostragamusCallBack<ApiResponse>() {
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
        values.put("UserID", NostragamusDataHandler.getInstance().getUserId());

        NostragamusAnalytics.getInstance().trackOtherEvents("JOIN GROUP-ONCLICK", values);
    }

    public interface OnJoinGroupModelListener {

        void onSuccess();

        void onInvalidGroupCode();

        void onNoInternet();

        void onFailed(String message);

        void onGetGroupCode(String groupCode);
    }
}