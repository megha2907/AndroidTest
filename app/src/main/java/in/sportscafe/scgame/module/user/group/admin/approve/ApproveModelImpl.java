package in.sportscafe.scgame.module.user.group.admin.approve;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.sportscafe.scgame.ScGame;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.common.ApiResponse;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupPerson;
import in.sportscafe.scgame.webservice.MyWebService;
import in.sportscafe.scgame.webservice.ScGameCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 2/7/16.
 */
public class ApproveModelImpl implements ApproveModel, ApproveAdapter.OnApproveOptionListener {

    private Long mGroupId;

    private ApproveAdapter mApproveAdapter;

    private boolean mApproveSelected = false;

    private ApproveFragment.OnApproveAcceptedListener mApproveAcceptedListener;

    private OnApproveModelListener mApproveModelListener;

    private ApproveModelImpl(OnApproveModelListener listener) {
        this.mApproveModelListener = listener;
    }

    public static ApproveModel newInstance(OnApproveModelListener listener) {
        return new ApproveModelImpl(listener);
    }

    @Override
    public ApproveAdapter init(Context context, long groupId, ApproveFragment.OnApproveAcceptedListener listener) {
        this.mGroupId = groupId;
        this.mApproveAcceptedListener = listener;
        
        Map<Long, GroupInfo> grpInfoMap = ScGameDataHandler.getInstance().getGrpInfoMap();
        if(grpInfoMap.containsKey(mGroupId)) {

            List<GroupPerson> members = grpInfoMap.get(mGroupId).getMembers();
            if(null == members || members.isEmpty()) {
                mApproveModelListener.onEmpty();
            } else {
                if(null != context) {

                    List<GroupPerson> pendingRequests = new ArrayList<>();
                    for (GroupPerson groupPerson : members) {
                        if(!groupPerson.isApproved()) {
                            pendingRequests.add(groupPerson);
                        }
                    }

                    mApproveAdapter = new ApproveAdapter(context, this);
                    mApproveAdapter.addAll(pendingRequests);
                    return mApproveAdapter;
                }
            }
        } else {
            mApproveModelListener.onEmpty();
        }
        return null;
    }

    @Override
    public void onAccept(int position) {
        approve(true, position);
    }

    @Override
    public void onReject(int position) {
        approve(false, position);
    }

    private void approve(boolean approved, int position) {
        if(ScGame.getInstance().hasNetworkConnection()) {
            mApproveSelected = approved;

            ApproveRequest approveRequest = new ApproveRequest();
            approveRequest.setGroupId(mGroupId);
            approveRequest.setUserId(mApproveAdapter.getItem(position).getId() + "");
            approveRequest.setAdminId(ScGameDataHandler.getInstance().getUserId());
            approveRequest.setApproved(approved);

            callApprovePersonApi(approveRequest, position);
        } else {
            mApproveModelListener.onNoInternet();
        }
    }

    private void callApprovePersonApi(ApproveRequest approveRequest, final int position) {
        MyWebService.getInstance().getApproveMemberRequest(approveRequest).enqueue(
                new ScGameCallBack<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if(response.isSuccessful()) {
                            handleApproveResponse(position, response.body().getMessage());
                        } else {
                            mApproveModelListener.onApprovalFailed(response.message());
                        }
                    }
                }
        );
    }

    private void handleApproveResponse(int position, String message) {
        GroupPerson groupPerson = mApproveAdapter.getItem(position);

        Map<Long, GroupInfo> grpInfoMap = ScGameDataHandler.getInstance().getGrpInfoMap();
        List<GroupPerson> members = grpInfoMap.get(mGroupId).getMembers();
        members.remove(groupPerson);

        if(mApproveSelected) {
            groupPerson.setApproved(mApproveSelected);
            mApproveAcceptedListener.onApproveAccepted(groupPerson);

            members.add(groupPerson);
        }

        ScGameDataHandler.getInstance().setGrpInfoMap(grpInfoMap);

        mApproveAdapter.remove(position);
        mApproveAdapter.notifyDataSetChanged();
        mApproveModelListener.onApprovalSuccess(message);

        if(mApproveAdapter.getItemCount() == 0) {
            mApproveModelListener.onEmpty();
        }
    }

    public interface OnApproveModelListener {

        void onApprovalSuccess(String message);

        void onNoInternet();

        void onEmpty();

        void onApprovalFailed(String message);
    }
}