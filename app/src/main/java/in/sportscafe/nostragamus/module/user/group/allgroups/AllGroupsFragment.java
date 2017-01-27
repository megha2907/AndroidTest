package in.sportscafe.nostragamus.module.user.group.allgroups;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;
import in.sportscafe.nostragamus.utils.ViewUtils;

import static android.app.Activity.RESULT_OK;

/**
 * Created by deepanshi on 12/7/16.
 */

public class AllGroupsFragment extends NostragamusFragment implements AllGroupsView {

    private RecyclerView mRvAllGroups;

    private AllGroupsPresenter mAllGroupsPresenter;

    private TextView mTvEmptyGroups;

    private static final int CODE_GROUP_INFO = 11;

    private static final int GROUPS_CODE = 20;

    private static Boolean allgroups;

    public static AllGroupsFragment newInstance() {

        allgroups=true;
        AllGroupsFragment fragment = new AllGroupsFragment();
        return fragment;
    }

    public static AllGroupsFragment newMutualGroupInstance(PlayerInfo playerInfo) {

        allgroups=false;

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.BundleKeys.PLAYERINFO, playerInfo);

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
        this.mRvAllGroups.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        this.mRvAllGroups.setHasFixedSize(true);
        this.mAllGroupsPresenter = AllGroupsPresenterImpl.newInstance(this);

        if (allgroups) {
            this.mAllGroupsPresenter.onCreateAllGroups();
        }else {
            this.mAllGroupsPresenter.onCreateMutualGroups(getArguments());
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
        homeintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
