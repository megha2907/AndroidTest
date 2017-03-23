package in.sportscafe.nostragamus.module.user.group.allgroups;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.user.group.JoinGroupApiModelImpl;
import in.sportscafe.nostragamus.module.user.group.allgroups.dto.AllGroupsResponse;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 9/27/16.
 */
public class AllGroupsModelImpl implements AllGroupsModel {

    private boolean mAllGroups = false;

    private AllGroupsAdapter mAllGroupsAdapter;

    private int mSelectedGroupItem = -1;

    private AllGroupsModelImpl.AllGroupsModelListener mAllGroupsModelListener;

    protected AllGroupsModelImpl(AllGroupsModelImpl.AllGroupsModelListener modelListener) {
        this.mAllGroupsModelListener = modelListener;
    }

    public static AllGroupsModel newInstance(AllGroupsModelImpl.AllGroupsModelListener modelListener) {
        return new AllGroupsModelImpl(modelListener);
    }

    @Override
    public void init(Bundle bundle) {
        mAllGroups = bundle.getBoolean(BundleKeys.IS_ALL_GROUPS);

        if (!mAllGroups) {
            mAllGroupsModelListener.onGetGroupsSuccess(
                    ((PlayerInfo) Parcels.unwrap(bundle.getParcelable(BundleKeys.PLAYERINFO))).getMutualGroups()
            );
        }

        checkGroupCode();
    }

    private void checkGroupCode() {
        String groupCode = NostragamusDataHandler.getInstance().getInstallGroupCode();
        if (null == groupCode) {
            getAllGroups();
        } else {
            joinGroup(groupCode);
        }
    }

    private void joinGroup(String groupCode) {
        JoinGroupApiModelImpl.newInstance(new JoinGroupApiModelImpl.OnJoinGroupApiModelListener() {
            @Override
            public void onSuccessJoinGroupApi(GroupInfo groupInfo) {
                getAllGroups();
            }

            @Override
            public void onFailedJoinGroupApi(String message) {
                getAllGroups();
            }

            @Override
            public void onNoInternet() {
                mAllGroupsModelListener.onNoInternet();
            }

            @Override
            public void onInvalidGroupCode() {
                getAllGroups();
            }
        }).joinGroup(groupCode, true);
    }

    @Override
    public boolean isAllGroups() {
        return mAllGroups;
    }

    @Override
    public void getAllGroups() {
        if (mAllGroups) {
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
    }

    @Override
    public RecyclerView.Adapter getGroupsAdapter(Context context, List<AllGroups> groupsList) {
        mAllGroupsAdapter = new AllGroupsAdapter(context, groupsList);
        checkAdapterEmpty();
        return mAllGroupsAdapter;
    }

    @Override
    public void saveSelectedItem(Bundle bundle) {
        mSelectedGroupItem = bundle.getInt(BundleKeys.CLICK_POSITION);
        mAllGroupsModelListener.onItemSaved(getSelectedGroupItemBundle());
    }

    private Bundle getSelectedGroupItemBundle() {
        if (-1 != mSelectedGroupItem) {
            AllGroups allGroups = mAllGroupsAdapter.getItem(mSelectedGroupItem);

            Bundle bundle = new Bundle();
            bundle.putInt(BundleKeys.GROUP_ID, allGroups.getGroupId());
            bundle.putString(BundleKeys.GROUP_NAME, allGroups.getGroupName());
            return bundle;
        }
        return null;
    }

    @Override
    public void updateGroupInfo(Bundle bundle) {
        if (-1 != mSelectedGroupItem) {
            if (bundle.containsKey(BundleKeys.GROUP_INFO)) {
                updateAllGroupsDetails(
                        mAllGroupsAdapter.getItem(mSelectedGroupItem),
                        (GroupInfo) Parcels.unwrap(bundle.getParcelable(BundleKeys.GROUP_INFO))
                );
            } else {
                mAllGroupsAdapter.remove(mSelectedGroupItem);
            }
            mAllGroupsAdapter.notifyDataSetChanged();
            mSelectedGroupItem = -1;
            checkAdapterEmpty();
        }
    }

    @Override
    public void addNewGroup(Bundle bundle) {
        if (bundle.containsKey(BundleKeys.GROUP_INFO)) {
            mSelectedGroupItem = 0;
            mAllGroupsAdapter.add(migrateGroupInfoToAllGroups(
                    (GroupInfo) Parcels.unwrap(bundle.getParcelable(BundleKeys.GROUP_INFO))),
                    mSelectedGroupItem
            );
            mAllGroupsModelListener.onItemSaved(getSelectedGroupItemBundle());
            checkAdapterEmpty();
        }
    }

    private void checkAdapterEmpty() {
        mAllGroupsModelListener.onApiCallStopped();
        if (mAllGroupsAdapter.getItemCount() == 0) {
            mAllGroupsModelListener.onGroupsEmpty();
        }
    }

    private AllGroups migrateGroupInfoToAllGroups(GroupInfo groupInfo) {
        AllGroups allGroups = new AllGroups();
        updateAllGroupsDetails(allGroups, groupInfo);
        return allGroups;
    }

    private void updateAllGroupsDetails(AllGroups allGroups, GroupInfo groupInfo) {
        allGroups.setGroupId(groupInfo.getId());
        allGroups.setGroupName(groupInfo.getName());
        allGroups.setCountGroupMembers(groupInfo.getMembers().size());
        allGroups.setGroupPhoto(groupInfo.getPhoto());
        allGroups.setTournamentsCount(groupInfo.getFollowedTournaments().size());
    }

    public interface AllGroupsModelListener {

        void onGetGroupsSuccess(List<AllGroups> groupsList);

        void onGetGroupsFailed(String message);

        void onGroupsEmpty();

        void onNoInternet();

        void onApiCallStarted();

        boolean onApiCallStopped();

        void onItemSaved(Bundle bundle);
    }
}