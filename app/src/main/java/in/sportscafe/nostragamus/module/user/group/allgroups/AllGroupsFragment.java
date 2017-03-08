package in.sportscafe.nostragamus.module.user.group.allgroups;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.user.group.groupinfo.GroupInfoActivity;
import in.sportscafe.nostragamus.module.user.group.joingroup.JoinGroupActivity;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;

import static android.app.Activity.RESULT_OK;
import static com.google.android.gms.analytics.internal.zzy.v;

/**
 * Created by deepanshi on 12/7/16.
 */

public class AllGroupsFragment extends NostragamusFragment implements AllGroupsView, View.OnClickListener {

    private static final int GROUP_INFO = 11;

    private static final int JOIN_GROUP = 12;

    private RecyclerView mRvAllGroups;

    private AllGroupsPresenter mAllGroupsPresenter;

    private TextView mTvEmptyGroups;

    public static AllGroupsFragment newInstance() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(BundleKeys.IS_ALL_GROUPS, true);

        AllGroupsFragment fragment = new AllGroupsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static AllGroupsFragment newMutualGroupInstance(PlayerInfo playerInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleKeys.PLAYERINFO, Parcels.wrap(playerInfo));
        bundle.putBoolean(BundleKeys.IS_ALL_GROUPS, false);

        AllGroupsFragment fragment = new AllGroupsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_groups, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mRvAllGroups = (RecyclerView) findViewById(R.id.all_groups_rcv);
        this.mRvAllGroups.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        this.mRvAllGroups.setHasFixedSize(true);
        this.mAllGroupsPresenter = AllGroupsPresenterImpl.newInstance(this);
        this.mAllGroupsPresenter.onCreateAllGroups(getArguments());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            if (GROUP_INFO == requestCode) {
                mAllGroupsPresenter.onGetGroupInfoResult(data.getExtras());
            } else if (JOIN_GROUP == requestCode) {
                mAllGroupsPresenter.onGetJoinGroupResult(data.getExtras());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.join_grp_btn:
                navigateToJoinGroup();
                break;
        }
    }


    @Override
    public void showTitleBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.all_groups_toolbar);
        toolbar.setVisibility(View.VISIBLE);
        Button createGroupbtn = (Button) findViewById(R.id.join_grp_btn);
        createGroupbtn.setVisibility(View.VISIBLE);
        createGroupbtn.setOnClickListener(this);
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        this.mRvAllGroups.setAdapter(adapter);
    }

    @Override
    public void showGroupsEmpty() {
        mTvEmptyGroups = (TextView) findViewById(R.id.all_groups_empty_tv);
        mTvEmptyGroups.setVisibility(View.VISIBLE);
        ImageView mIvEmptyGroups = (ImageView) findViewById(R.id.all_groups_empty_iv);
        mIvEmptyGroups.setVisibility(View.VISIBLE);
    }

    @Override
    public void navigateToHomeActivity() {
        Intent homeintent = new Intent(getContext(), HomeActivity.class);
        homeintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        homeintent.putExtra("group", "openprofile");
        startActivity(homeintent);
        getActivity().finish();
    }

    @Override
    public void navigateToJoinGroup() {
        startActivityForResult(new Intent(getContext(), JoinGroupActivity.class), JOIN_GROUP);
    }

    @Override
    public void navigateToGroupInfo(Bundle bundle) {
        Intent intent = new Intent(getContext(), GroupInfoActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, GROUP_INFO);
    }

    @Override
    public void goBackWithSuccessResult() {
        getActivity().setResult(RESULT_OK);
        getActivity().onBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mGroupItemClickReceiver,
                new IntentFilter(Constants.IntentActions.ACTION_GROUP_CLICK));
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mGroupItemClickReceiver);
    }

    BroadcastReceiver mGroupItemClickReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mAllGroupsPresenter.onClickGroupItem(intent.getExtras());
        }
    };

}