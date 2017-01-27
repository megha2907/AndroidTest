package in.sportscafe.nostragamus.module.user.group.admin.approve;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.common.SpacesItemDecoration;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupPerson;

/**
 * Created by Jeeva on 2/7/16.
 */
public class ApproveFragment extends NostragamusFragment implements ApproveView {

    private static final String KEY_GROUP_ID = "groupId";

    private ApprovePresenter mApprovePresenter;

    private RecyclerView mRvMembers;

    private OnApproveAcceptedListener mApproveAcceptedListener;

    public interface OnApproveAcceptedListener {

        void onApproveAccepted(GroupPerson newPerson);
    }

    public static ApproveFragment newInstance(long groupId, OnApproveAcceptedListener listener) {
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_GROUP_ID, groupId);

        ApproveFragment fragment = new ApproveFragment();
        fragment.mApproveAcceptedListener = listener;
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

        this.mApprovePresenter = ApprovePresenterImpl.newInstance(this);
        this.mApprovePresenter.onCreateApprove(getArguments().getInt(KEY_GROUP_ID), mApproveAcceptedListener);
    }

    @Override
    public void setAdapter(ApproveAdapter adapter) {
        mRvMembers.setAdapter(adapter);
    }
}