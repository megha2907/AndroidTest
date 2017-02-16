package in.sportscafe.nostragamus.module.user.group.groupinfo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentFeedInfo;
import in.sportscafe.nostragamus.module.user.group.DeleteGroupModelImpl;
import in.sportscafe.nostragamus.module.user.group.LeaveGroupModelImpl;
import in.sportscafe.nostragamus.module.user.group.ResetLeaderboardModelImpl;
import in.sportscafe.nostragamus.module.user.group.members.MembersFragment;
import in.sportscafe.nostragamus.module.user.group.tourselection.TourSelectionFragment;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupPerson;
import in.sportscafe.nostragamus.webservice.GroupSummaryResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

import static com.google.android.gms.internal.zzng.fm;

/**
 * Created by Jeeva on 12/6/16.
 */
public class GroupInfoModelImpl implements GroupInfoModel, TourSelectionFragment.OnTourSelectionListener, MembersFragment.OnMemberRemoveListener {

    private boolean mAmAdmin = false;

    private int mGroupId;

    private String mGroupName = "";

    private boolean mAnythingChanged = false;

    private GroupInfo mGroupInfo;

    private GroupTournamentAdapter mGrpTournamentAdapter;

    private OnGroupInfoModelListener mGroupInfoModelListener;

    private GrpTournamentUpdateModelImpl mGrpTournamentUpdateModel;

    private GrpNameUpdateModelImpl mGrpNameUpdateModel;

    private GroupInfoModelImpl(OnGroupInfoModelListener listener) {
        this.mGroupInfoModelListener = listener;
    }

    public static GroupInfoModel newInstance(OnGroupInfoModelListener listener) {
        return new GroupInfoModelImpl(listener);
    }

    @Override
    public void init(Bundle bundle) {
        mGroupId = bundle.getInt(BundleKeys.GROUP_ID);
        if(bundle.containsKey(BundleKeys.GROUP_NAME)) {
            mGroupName = bundle.getString(BundleKeys.GROUP_NAME);
        }
    }

    @Override
    public String getGroupName() {
        return mGroupName;
    }

    @Override
    public void getGroupDetails() {
        if(Nostragamus.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().getGroupSummaryRequest(mGroupId).enqueue(
                    new NostragamusCallBack<GroupSummaryResponse>() {
                        @Override
                        public void onResponse(Call<GroupSummaryResponse> call, Response<GroupSummaryResponse> response) {
                            super.onResponse(call, response);
                            if (response.isSuccessful()) {
                                handleGroupInfoResponse(response.body().getGroupInfo());
                            } else {
                                mGroupInfoModelListener.onGetGroupInfoFailed(response.message());
                            }
                        }
                    }
            );
        } else {
            mGroupInfoModelListener.onNoInternetForGroupDetails();
        }
    }

    @Override
    public boolean amAdmin() {
        return mAmAdmin;
    }

    @Override
    public GroupInfo getGroupInfo() {
        return mGroupInfo;
    }

    @Override
    public int getApprovedMembersCount() {
        int membersCount = 0;
        for (GroupPerson groupPerson : mGroupInfo.getMembers()) {
            if (groupPerson.isApproved()) {
                membersCount++;
            }
        }
        return membersCount;
    }

    @Override
    public void leaveGroup() {
        new LeaveGroupModelImpl(new LeaveGroupModelImpl.OnLeaveGroupModelListener() {
            @Override
            public void onSuccessLeaveGroup() {
                mGroupInfo = null;
                mAnythingChanged = true;
                mGroupInfoModelListener.onLeaveGroupSuccess();
            }

            @Override
            public void onFailedLeaveGroup(String message) {
                mGroupInfoModelListener.onFailed(message);
            }

            @Override
            public void onNoInternet() {
                mGroupInfoModelListener.onNoInternet();
            }
        }).leaveGroup(mAmAdmin, mGroupInfo.getId());
    }

    @Override
    public void resetLeaderboard() {
        new ResetLeaderboardModelImpl(new ResetLeaderboardModelImpl.OnResetLeaderboardModelListener() {
            @Override
            public void onSuccessResetLeaderboard() {
                mGroupInfoModelListener.onResetLeaderboardSuccess();
            }

            @Override
            public void onFailedResetLeaderboard(String message) {
                mGroupInfoModelListener.onFailed(message);
            }

            @Override
            public void onNoInternet() {
                mGroupInfoModelListener.onNoInternet();
            }
        }).resetLeaderboard(mGroupInfo.getId());
    }

    @Override
    public void deleteGroup() {
        new DeleteGroupModelImpl(new DeleteGroupModelImpl.OnDeleteGroupModelListener() {
            @Override
            public void onSuccessDeleteGroup() {
                mGroupInfo = null;
                mAnythingChanged = true;
                mGroupInfoModelListener.onDeleteGroupSuccess();
            }

            @Override
            public void onFailedDeleteGroup(String message) {
                mGroupInfoModelListener.onFailed(message);
            }

            @Override
            public void onNoInternet() {
                mGroupInfoModelListener.onNoInternet();
            }
        }).deleteGroup(mAmAdmin, mGroupInfo.getId());
    }

    @Override
    public Bundle getGroupInfoBundle() {
        Bundle bundle = new Bundle();
        if(null != mGroupInfo) {
            bundle.putParcelable(BundleKeys.GROUP_INFO, Parcels.wrap(mGroupInfo));
        }
        return bundle;
    }

    @Override
    public boolean isAnythingChanged() {
        return mAnythingChanged;
    }

    @Override
    public ViewPagerAdapter getAdapter(FragmentManager fm) {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(fm);

        pagerAdapter.addFragment(
                TourSelectionFragment.newInstance(mAmAdmin, mGroupId, mGroupInfo.getFollowedTournaments(), this),
                getTourTitle()
        );

        pagerAdapter.addFragment(
                MembersFragment.newInstance(mAmAdmin, mGroupId, mGroupInfo.getMembers(), this),
                getMemberTitle()
        );
        return pagerAdapter;
    }

    @Override
    public void updateEditData(Bundle bundle) {
        mAnythingChanged = true;
        mGroupInfo = Parcels.unwrap(bundle.getParcelable(BundleKeys.GROUP_INFO));
    }

    private String getTourTitle() {
        return AppSnippet.formatIfPlural(mGroupInfo.getFollowedTournaments().size(), "Tournament", "s");
    }

    private String getMemberTitle() {
        return AppSnippet.formatIfPlural(mGroupInfo.getMembers().size(), "Member", "s");
    }

    private void handleGroupInfoResponse(GroupInfo groupInfo) {
        mGroupInfo = groupInfo;
        checkAmAdminOrNot();
        mGroupInfoModelListener.onGetGroupInfoSuccess(mGroupInfo);
    }

    private void checkAmAdminOrNot() {
        String myId = NostragamusDataHandler.getInstance().getUserId();
        List<GroupPerson> groupMembers = mGroupInfo.getMembers();

        for (GroupPerson groupPerson : groupMembers) {
            if (myId.compareTo(groupPerson.getId().toString()) == 0
                    && groupPerson.isAdmin()) {
                mAmAdmin = true;
                break;
            }
        }
    }

    @Override
    public void onTourSelectionChanged(List<TournamentFeedInfo> followedTours) {
        mAnythingChanged = true;
        mGroupInfo.setFollowedTournaments(followedTours);
        mGroupInfoModelListener.onTourTitleChanged(getTourTitle());
    }

    @Override
    public void onMemberRemoved(List<GroupPerson> updatedMemberList) {
        mAnythingChanged = true;
        mGroupInfo.setMembers(updatedMemberList);
        mGroupInfoModelListener.onMemberTitleChange(getMemberTitle());
    }

    public interface OnGroupInfoModelListener {

        void onNoInternet();

        void onGetGroupInfoSuccess(GroupInfo grpInfoList);

        void onGetGroupInfoFailed(String message);

        void onLeaveGroupSuccess();

        void onResetLeaderboardSuccess();

        void onDeleteGroupSuccess();

        void onFailed(String message);

        void onNoInternetForGroupDetails();

        void onTourTitleChanged(String title);

        void onMemberTitleChange(String title);
    }
}