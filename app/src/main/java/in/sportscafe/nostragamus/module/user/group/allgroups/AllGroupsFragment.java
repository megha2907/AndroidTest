package in.sportscafe.nostragamus.module.user.group.allgroups;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;

import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.user.group.joingroup.JoinGroupActivity;
import in.sportscafe.nostragamus.module.user.group.members.MembersFragment;
import in.sportscafe.nostragamus.module.user.myprofile.myposition.dto.GroupSummary;
import in.sportscafe.nostragamus.module.user.myprofile.myposition.groups.GroupsAdapter;

import static android.app.Activity.RESULT_OK;

/**
 * Created by deepanshi on 12/7/16.
 */

public class AllGroupsFragment extends NostragamusFragment implements AllGroupsView , View.OnClickListener {

    private RecyclerView mRvAllGroups;

    private AllGroupsPresenter mAllGroupsPresenter;

    private Toolbar mtoolbar;

    private Button mbtnEmptyGroups;

    private TextView mTvEmptyGroups;

    private static final int CODE_GROUP_INFO = 11;

    private static final int GROUPS_CODE = 20;

    public static AllGroupsFragment newInstance() {

        AllGroupsFragment fragment = new AllGroupsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_groups, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mRvAllGroups = (RecyclerView) findViewById(R.id.all_groups_rcv);
        this.mRvAllGroups.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        this.mRvAllGroups.setHasFixedSize(true);
        this.mAllGroupsPresenter = AllGroupsPresenterImpl.newInstance(this);
        this.mAllGroupsPresenter.onCreateAllGroups();

    }

    @Override
    public void navigateToJoinGroup() {
        startActivity(new Intent(getContext(), JoinGroupActivity.class));
        getActivity().finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.all_groups_empty_btn:
                navigateToJoinGroup();
                break;

        }
    }

    @Override
    public void goBackWithSuccessResult() {
        getActivity().setResult(RESULT_OK);
        getActivity().onBackPressed();
    }

    @Override
    public void navigateToHomeActivity() {
        Intent homeintent = new Intent(getContext(), HomeActivity.class);
        homeintent.putExtra("group", "openprofile");
        startActivity(homeintent);
        getActivity().finish();
    }


    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        this.mRvAllGroups.setAdapter(adapter);
    }

    @Override
    public void showGroupsEmpty() {
        mbtnEmptyGroups = (Button)findViewById(R.id.all_groups_empty_btn);
        mbtnEmptyGroups.setVisibility(View.VISIBLE);

        mTvEmptyGroups=(TextView)findViewById(R.id.all_groups_empty_tv);
        mTvEmptyGroups.setVisibility(View.VISIBLE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(RESULT_OK == resultCode) {
            if(CODE_GROUP_INFO == requestCode) {
                mAllGroupsPresenter.onCreateAllGroups();
            }
        }
    }

}
