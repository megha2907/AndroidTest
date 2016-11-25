package in.sportscafe.nostragamus.module.user.group.admin.adminmembers;

import android.app.Activity;
import android.os.Bundle;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.module.common.AbstractTabFragment;
import in.sportscafe.nostragamus.module.user.group.admin.approve.ApproveFragment;
import in.sportscafe.nostragamus.module.user.group.members.MembersFragment;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupPerson;

import static in.sportscafe.nostragamus.Constants.BundleKeys;

/**
 * Created by Jeeva on 2/7/16.
 */
public class AdminMembersFragment extends AbstractTabFragment implements ApproveFragment.OnApproveAcceptedListener {

    private MembersFragment mMembersFragment;

    public static AdminMembersFragment newInstance(String groupid) {

        Long groupId = Long.parseLong(groupid);

        Bundle bundle = new Bundle();
        bundle.putLong(BundleKeys.GROUP_ID, groupId);
        AdminMembersFragment fragment = new AdminMembersFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public ViewPagerAdapter getAdapter() {
        long groupId = getArguments().getLong(BundleKeys.GROUP_ID);
        Log.i("bundleininstance", String.valueOf(groupId));
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        pagerAdapter.addFragment(mMembersFragment = MembersFragment.newInstance( String.valueOf(groupId)), "Group Members");
        pagerAdapter.addFragment(ApproveFragment.newInstance(groupId, this), "Pending Requests");
        return pagerAdapter;
    }

    @Override
    public void onApproveAccepted(GroupPerson newPerson) {
        getActivity().setResult(Activity.RESULT_OK);

        mMembersFragment.addNewPerson(newPerson);
    }
}