package in.sportscafe.scgame.module.user.group.newgroup;

import android.content.Context;
import android.os.Bundle;

import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.PayloadBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.ScGame;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.scgame.webservice.MyWebService;
import in.sportscafe.scgame.webservice.ScGameCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 1/7/16.
 */
public class NewGroupModelImpl implements NewGroupModel {

    private GrpSportSelectionAdapter mSportSelectionAdapter;

    private OnNewGroupModelListener mNewGroupModelListener;

    private NewGroupModelImpl(OnNewGroupModelListener listener) {
        this.mNewGroupModelListener = listener;
    }

    public static NewGroupModel newInstance(OnNewGroupModelListener listener) {
        return new NewGroupModelImpl(listener);
    }

    @Override
    public GrpSportSelectionAdapter getAdapter(Context context) {
        this.mSportSelectionAdapter = new GrpSportSelectionAdapter(context, new ArrayList<Integer>());
        this.mSportSelectionAdapter.addAll(ScGameDataHandler.getInstance().getAllSports());
        return mSportSelectionAdapter;
    }

    @Override
    public void createGroup(String groupName) {
        if(groupName.isEmpty()) {
            mNewGroupModelListener.onEmptyGroupName();
            return;
        }

        List<Integer> selectedSports = mSportSelectionAdapter.getSelectedSportList();
        if(null == selectedSports || selectedSports.isEmpty()) {
            mNewGroupModelListener.onNoSportSelected();
            return;
        } else {
            if(ScGame.getInstance().hasNetworkConnection()) {
                NewGroupRequest newGroupRequest = new NewGroupRequest();
                newGroupRequest.setGroupCreatedBy(ScGameDataHandler.getInstance().getUserId());
                newGroupRequest.setGroupName(groupName);
                newGroupRequest.setFollowedSports(selectedSports);
                callNewGroupApi(newGroupRequest);
            } else {
                mNewGroupModelListener.onNoInternet();
            }
        }
    }

    private void callNewGroupApi(NewGroupRequest newGroupRequest) {
        MyWebService.getInstance().getNewGroupRequest(newGroupRequest).enqueue(
                new ScGameCallBack<NewGroupResponse>() {
                    @Override
                    public void onResponse(Call<NewGroupResponse> call, Response<NewGroupResponse> response) {
                        if(response.isSuccessful()) {
                            ScGameDataHandler scGameDataHandler = ScGameDataHandler.getInstance();

                            GroupInfo groupInfo = response.body().getGroupInfo();

                            Map<Long, GroupInfo> grpInfoMap = scGameDataHandler.getGrpInfoMap();
                            grpInfoMap.put(groupInfo.getId(), groupInfo);

                            scGameDataHandler.setGrpInfoMap(grpInfoMap);

                            Bundle bundle = new Bundle();
                            bundle.putLong(Constants.BundleKeys.GROUP_ID, groupInfo.getId());
                            mNewGroupModelListener.onSuccess(bundle);
                        } else {
                            mNewGroupModelListener.onFailed(response.message());
                        }
                    }
                }
        );
    }

    public interface OnNewGroupModelListener {

        void onSuccess(Bundle bundle);

        void onEmptyGroupName();

        void onNoSportSelected();

        void onNoInternet();

        void onFailed(String message);
    }
}