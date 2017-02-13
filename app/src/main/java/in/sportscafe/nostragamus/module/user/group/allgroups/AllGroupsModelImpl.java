package in.sportscafe.nostragamus.module.user.group.allgroups;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.user.group.allgroups.dto.AllGroupsResponse;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 9/27/16.
 */
public class AllGroupsModelImpl implements AllGroupsModel {

    private AllGroupsModelImpl.AllGroupsModelListener mAllGroupsModelListener;

    protected AllGroupsModelImpl(AllGroupsModelImpl.AllGroupsModelListener modelListener) {
        this.mAllGroupsModelListener = modelListener;
    }

    public static AllGroupsModel newInstance(AllGroupsModelImpl.AllGroupsModelListener modelListener) {
        return new AllGroupsModelImpl(modelListener);
    }

    @Override
    public void init(Bundle bundle) {
        if (bundle.getBoolean(BundleKeys.IS_ALL_GROUPS)) {
            getAllGroups();
        } else {
            mAllGroupsModelListener.onGetGroupsSuccess(
                    ((PlayerInfo) Parcels.unwrap(bundle.getParcelable(BundleKeys.PLAYERINFO))).getMutualGroups()
            );
        }
    }

    @Override
    public void getAllGroups() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            mAllGroupsModelListener.onApiCallStarted();

            MyWebService.getInstance().getAllGroupsRequest().enqueue(
                    new NostragamusCallBack<AllGroupsResponse>() {
                        @Override
                        public void onResponse(Call<AllGroupsResponse> call, Response<AllGroupsResponse> response) {
                            super.onResponse(call, response);

                            if (!mAllGroupsModelListener.onApiCallStopped()) {
                                return;
                            }

                            if (response.isSuccessful()) {
                                mAllGroupsModelListener.onGetGroupsSuccess(response.body().getAllGroups());
                            } else {
                                mAllGroupsModelListener.onGetGroupsFailed(response.message());
                            }
                        }
                    }
            );
        } else {
            mAllGroupsModelListener.onNoInternet();
        }
    }

    @Override
    public RecyclerView.Adapter getGroupsAdapter(Context context, List<AllGroups> groupsList) {
        return new AllGroupsAdapter(context, groupsList);
    }

    public interface AllGroupsModelListener {

        void onGetGroupsSuccess(List<AllGroups> groupsList);

        void onGetGroupsFailed(String message);

        void onGroupsEmpty();

        void onNoInternet();

        void onApiCallStarted();

        boolean onApiCallStopped();
    }
}