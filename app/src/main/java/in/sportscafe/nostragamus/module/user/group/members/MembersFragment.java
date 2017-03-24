package in.sportscafe.nostragamus.module.user.group.members;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.common.SpacesItemDecoration;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupPerson;
import in.sportscafe.nostragamus.utils.ViewUtils;

/**
 * Created by Jeeva on 2/7/16.
 */
public class MembersFragment extends NostragamusFragment implements MembersView {

    private OnMemberRemoveListener mMemberRemoveListener;

    private RecyclerView mRvMembers;

    private MembersPresenter mMembersPresenter;

    public static MembersFragment newInstance(
            boolean amAdmin,
            int groupId,
            List<GroupPerson> grpMembers,
            OnMemberRemoveListener listener
    ) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(BundleKeys.IS_ADMIN, amAdmin);
        bundle.putInt(BundleKeys.GROUP_ID, groupId);
        bundle.putParcelable(BundleKeys.GROUP_MEMBERS, Parcels.wrap(grpMembers));

        MembersFragment fragment = new MembersFragment();
        fragment.setArguments(bundle);
        fragment.mMemberRemoveListener = listener;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_members, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mRvMembers = (RecyclerView) findViewById(R.id.members_rcv);
        this.mRvMembers.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_10)));
        this.mRvMembers.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        this.mRvMembers.setHasFixedSize(true);

        findViewById(R.id.members_btn_leave_group).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViewUtils.getAlertDialog(getContext(), "Are you to leave group?",
                                "Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mMembersPresenter.onClickLeaveGroup();
                                    }
                                },
                                "No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                    }
                }
        );

        this.mMembersPresenter = MembersPresenterImpl.newInstance(this);
        this.mMembersPresenter.onCreateMembers(getArguments(), mMemberRemoveListener);
    }

    @Override
    public void setAdapter(MembersAdapter membersAdapter) {
        this.mRvMembers.setAdapter(membersAdapter);
    }

    @Override
    public void navigateToHome() {
        Intent intent = new Intent(getContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public interface OnMemberRemoveListener {

        void onMemberRemoved(List<GroupPerson> updatedMemberList);
    }
}