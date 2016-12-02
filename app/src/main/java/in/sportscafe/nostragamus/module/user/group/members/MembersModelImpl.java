package in.sportscafe.nostragamus.module.user.group.members;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.module.user.group.LeaveGroupModelImpl;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupPerson;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 2/7/16.
 */
public class MembersModelImpl implements MembersModel, MembersAdapter.OnMembersOptionListener {

    private boolean mAdmin = false;

    private Long mGroupId;

    private MembersAdapter mMembersAdapter;

    private OnMembersModelListener mMembersModelListener;

    private MembersModelImpl(OnMembersModelListener listener) {
        this.mMembersModelListener = listener;
    }

    public static MembersModel newInstance(OnMembersModelListener listener) {
        return new MembersModelImpl(listener);
    }

    @Override
    public MembersAdapter init(Context context, long groupId) {
        this.mGroupId = groupId;
        Map<Long, GroupInfo> grpInfoMap = NostragamusDataHandler.getInstance().getGrpInfoMap();
        if(grpInfoMap.containsKey(mGroupId)) {

            List<GroupPerson> members = grpInfoMap.get(mGroupId).getMembers();
            if(null == members || members.isEmpty()) {
                mMembersModelListener.onEmpty();
            } else {
                if(null != context) {
                    String myId = NostragamusDataHandler.getInstance().getUserId();
                    List<GroupPerson> groupMembers = new ArrayList<>();
                    for (GroupPerson groupPerson : members) {
                        if(groupPerson.isApproved()) {
                            groupMembers.add(groupPerson);
                        }

                        if(myId.compareTo(groupPerson.getId().toString()) == 0
                                && groupPerson.isAdmin()) {
                            mAdmin = true;
                        }
                    }

                    mMembersAdapter = new MembersAdapter(context, this, mAdmin);
                    mMembersAdapter.addAll(groupMembers);
                    return mMembersAdapter;
                }
            }
        } else {
            mMembersModelListener.onEmpty();
        }
        return null;
    }

    @Override
    public void leaveGroup() {
        new LeaveGroupModelImpl(new LeaveGroupModelImpl.OnLeaveGroupModelListener() {
            @Override
            public void onSuccessLeaveGroup() {
                mMembersModelListener.onLeaveGroupSuccess();
            }

            @Override
            public void onFailedLeaveGroup(String message) {
                mMembersModelListener.onFailed(message);
            }

            @Override
            public void onNoInternet() {
                mMembersModelListener.onNoInternet();
            }
        }).leaveGroup(mAdmin, mGroupId);
    }

    @Override
    public void addNewPerson(GroupPerson newPerson) {
        mMembersAdapter.add(newPerson, 0);
    }

    @Override
    public void onClickRemove(int position) {
        if(Nostragamus.getInstance().hasNetworkConnection()) {
            callRemovePersonApi(getAdminRequest(position), position);
        } else {
            mMembersModelListener.onFailed(Constants.Alerts.NO_NETWORK_CONNECTION);
        }
    }

    private AdminRequest getAdminRequest(int position) {
        AdminRequest removePersonRequest = new AdminRequest();
        removePersonRequest.setAdminId(NostragamusDataHandler.getInstance().getUserId());
        removePersonRequest.setUserId(mMembersAdapter.getItem(position).getId() + "");
        removePersonRequest.setGroupId(mGroupId);

        return removePersonRequest;
    }

    private void callRemovePersonApi(AdminRequest request, final int position) {
        MyWebService.getInstance().getRemovePersonRequest(request).enqueue(
                new NostragamusCallBack<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        super.onResponse(call, response);
                        if(response.isSuccessful()) {
                            handleRemoveResponse(position);
                        } else {
                            mMembersModelListener.onFailed(response.message());
                        }
                    }
                }
        );
    }

    private void handleRemoveResponse(int position) {
        GroupPerson groupPerson = mMembersAdapter.getItem(position);

        Map<Long, GroupInfo> grpInfoMap = NostragamusDataHandler.getInstance().getGrpInfoMap();
        List<GroupPerson> members = grpInfoMap.get(mGroupId).getMembers();
        members.remove(groupPerson);
        NostragamusDataHandler.getInstance().setGrpInfoMap(grpInfoMap);

        mMembersAdapter.remove(position);
        mMembersAdapter.notifyDataSetChanged();
        mMembersModelListener.onRemovedPersonSuccess();
    }

    @Override
    public void onMakeAdmin(int position) {
        if(Nostragamus.getInstance().hasNetworkConnection()) {
            callMakeAdminApi(getAdminRequest(position), position);
        } else {
            mMembersModelListener.onFailed(Constants.Alerts.NO_NETWORK_CONNECTION);
        }
    }

    private void callMakeAdminApi(AdminRequest request, final int position) {
        MyWebService.getInstance().getMakeAdminRequest(request).enqueue(
                new NostragamusCallBack<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        super.onResponse(call, response);
                        if(response.isSuccessful()) {
                            handleMakeAdminResponse(position);
                        } else {
                            mMembersModelListener.onFailed(response.message());
                        }
                    }
                }
        );
    }

    private void handleMakeAdminResponse(int position) {
        GroupPerson groupPerson = mMembersAdapter.getItem(position);
        groupPerson.setAdmin(true);

        Map<Long, GroupInfo> grpInfoMap = NostragamusDataHandler.getInstance().getGrpInfoMap();
        List<GroupPerson> members = grpInfoMap.get(mGroupId).getMembers();
        members.get(members.indexOf(groupPerson)).setAdmin(true);
        NostragamusDataHandler.getInstance().setGrpInfoMap(grpInfoMap);

        mMembersAdapter.notifyDataSetChanged();
        mMembersModelListener.onMakeAdminSuccess();
    }

    public interface OnMembersModelListener {

        void onMakeAdminSuccess();

        void onRemovedPersonSuccess();

        void onFailed(String message);

        void onEmpty();

        void onNoInternet();

        void onLeaveGroupSuccess();
    }
}