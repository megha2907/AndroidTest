package in.sportscafe.nostragamus.module.user.group.mutualgroups;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import java.io.Serializable;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.animator.AnimationAdapter;
import in.sportscafe.nostragamus.animator.SlideInUpAnimationAdapter;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroupsPresenter;
import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroupsPresenterImpl;
import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroupsView;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;

import static android.app.Activity.RESULT_OK;

/**
 * Created by deepanshi on 1/4/17.
 */

public class MutualGroupsFragment extends NostragamusFragment implements MutualGroupsView {

    private RecyclerView mRvAllGroups;

    private MutualGroupsPresenter mAllGroupsPresenter;

    private TextView mTvEmptyGroups;

    private static final int CODE_GROUP_INFO = 11;

    private static final int GROUPS_CODE = 20;

    public static MutualGroupsFragment newInstance(PlayerInfo playerInfo) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.BundleKeys.PLAYERINFO, playerInfo);

        MutualGroupsFragment fragment = new MutualGroupsFragment();
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
        this.mAllGroupsPresenter = MutualGroupsPresenterImpl.newInstance(this);
        this.mAllGroupsPresenter.onCreateMutualGroups(getArguments());

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
        this.mRvAllGroups.setAdapter(getAnimationAdapter(adapter));
    }

    private AnimationAdapter getAnimationAdapter(RecyclerView.Adapter adapter) {
        SlideInUpAnimationAdapter animationAdapter = new SlideInUpAnimationAdapter(adapter);
        animationAdapter.setFirstOnly(true);
        animationAdapter.setDuration(750);
        animationAdapter.setInterpolator(new DecelerateInterpolator(1f));
        return animationAdapter;
    }

    @Override
    public void showGroupsEmpty() {

        mTvEmptyGroups = (TextView) findViewById(R.id.all_groups_empty_tv);
        mTvEmptyGroups.setText("No Mutual Groups.");
        mTvEmptyGroups.setVisibility(View.VISIBLE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            if (CODE_GROUP_INFO == requestCode) {
                mAllGroupsPresenter.onCreateMutualGroups(getArguments());
            }
        }
    }
}