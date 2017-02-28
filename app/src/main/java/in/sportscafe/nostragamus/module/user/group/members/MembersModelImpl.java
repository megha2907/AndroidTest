package in.sportscafe.nostragamus.module.user.group.members;

import android.content.Context;
import android.os.Bundle;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.module.user.group.LeaveGroupModelImpl;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupPerson;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 2/7/16.
 */
public class MembersModelImpl implements MembersModel, MembersAdapter.OnMembersOptionListener {

    private boolean mAmAdmin = false;

    private Integer mGroupId;

    private List<GroupPerson> mMemberList;

    private MembersAdapter mMembersAdapter;

    private OnMembersModelListener mMembersModelListener;

    private MembersModelImpl(OnMembersModelListener listener) {
        this.mMembersModelListener = listener;
    }

    public static MembersModel newInstance(OnMembersModelListener listener) {
        return new MembersModelImpl(listener);
    }

    @Override
    public MembersAdapter init(Context context, Bundle bundle) {
        this.mAmAdmin = bundle.getBoolean(BundleKeys.IS_ADMIN);
        this.mGroupId = bundle.getInt(BundleKeys.GROUP_ID);
        this.mMemberList = Parcels.unwrap(bundle.getParcelable(BundleKeys.GROUP_MEMBERS));

        mMembersAdapter = new MembersAdapter(context, this, mAmAdmin, mMemberList);
        return mMembersAdapter;
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
        }).leaveGroup(mAmAdmin, mGroupId);
    }

    @Override
    public void onClickRemove(int position) {
        if(Nostragamus.getInstance().hasNetworkConnection()) {
            callRemovePersonApi(getMemberRequest(position), position);
        } else {
            mMembersModelListener.onFailed(Constants.Alerts.NO_NETWORK_CONNECTION);
        }
    }

    private MembersRequest getMemberRequest(int position) {
        MembersRequest removePersonRequest = new MembersRequest();
        removePersonRequest.setPlayerId(mMembersAdapter.getItem(position).getId());
        removePersonRequest.setGroupId(mGroupId);

        return removePersonRequest;
    }

    private void callRemovePersonApi(MembersRequest request, final int position) {
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
        mMembersAdapter.remove(position);
        mMemberList.remove(position);
        mMembersAdapter.notifyDataSetChanged();
        mMembersModelListener.onRemovedPersonSuccess(mMemberList);
    }

    @Override
    public void onMakeAdmin(int position) {
        if(Nostragamus.getInstance().hasNetworkConnection()) {
            callMakeAdminApi(getMemberRequest(position), position);
        } else {
            mMembersModelListener.onFailed(Constants.Alerts.NO_NETWORK_CONNECTION);
        }
    }

    private void callMakeAdminApi(MembersRequest request, final int position) {
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
        mMembersAdapter.getItem(position).setAdmin(true);

        mMembersAdapter.notifyDataSetChanged();
        mMembersModelListener.onMakeAdminSuccess();
    }

    public interface OnMembersModelListener {

        void onMakeAdminSuccess();

        void onRemovedPersonSuccess(List<GroupPerson> memberList);

        void onFailed(String message);

        void onEmpty();

        void onNoInternet();

        void onLeaveGroupSuccess();
    }
}