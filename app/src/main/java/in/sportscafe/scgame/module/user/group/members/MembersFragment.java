package in.sportscafe.scgame.module.user.group.members;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.ScGameFragment;
import in.sportscafe.scgame.module.common.SpacesItemDecoration;
import in.sportscafe.scgame.module.home.HomeActivity;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupPerson;
import in.sportscafe.scgame.utils.ViewUtils;

/**
 * Created by Jeeva on 2/7/16.
 */
public class MembersFragment extends ScGameFragment implements MembersView {

    private static final String KEY_GROUP_ID = "groupId";

    private RecyclerView mRvMembers;

    private MembersPresenter mMembersPresenter;

    public static MembersFragment newInstance(String groupid) {

        Long groupId = Long.parseLong(groupid);

        Bundle bundle = new Bundle();
        bundle.putLong(KEY_GROUP_ID, groupId);

        MembersFragment fragment = new MembersFragment();
        fragment.setArguments(bundle);
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
        this.mMembersPresenter.onCreateMembers(getArguments().getLong(KEY_GROUP_ID));
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

    @Override
    public void setSuccessResult() {
        getActivity().setResult(Activity.RESULT_OK);
    }

    public void addNewPerson(GroupPerson newPerson) {
        mMembersPresenter.onGetNewPerson(newPerson);
    }
}