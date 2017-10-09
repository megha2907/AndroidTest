package in.sportscafe.nostragamus.module.challengeRewards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.challengeRewards.dto.Rewards;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.utils.AlertsHelper;
import in.sportscafe.nostragamus.utils.timeutils.TimeAgo;
import in.sportscafe.nostragamus.utils.timeutils.TimeUnit;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by deepanshi on 9/6/17.
 */

public class RewardsFragment extends NostraBaseFragment implements RewardsApiModelImpl.RewardsDataListener
        , View.OnClickListener {

    private static final String TAG = RewardsFragment.class.getSimpleName();

    private int mRoomId = -1;

    private int mConfigId = -1;

    private RewardsAdapter mConfigAdapter;

    private RecyclerView mRcvRewards;

    public RewardsFragment() {

    }

    public static RewardsFragment newInstance(int roomId,int configId) {

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BundleKeys.ROOM_ID, roomId);
        bundle.putInt(Constants.BundleKeys.CONFIG_ID, configId);

        RewardsFragment fragment = new RewardsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rewards, container, false);
        initViews(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        openBundle(getArguments());
    }

    private void openBundle(Bundle bundle) {
        if (bundle != null) {
            mRoomId = bundle.getInt(Constants.BundleKeys.ROOM_ID);
            mConfigId = bundle.getInt(Constants.BundleKeys.CONFIG_ID);
            getRewardsData();
        }
    }

    private void initViews(View rootView) {
        this.mRcvRewards = (RecyclerView)rootView. findViewById(R.id.rewards_rcv);
        this.mRcvRewards.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        this.mRcvRewards.setHasFixedSize(true);
    }

    private void getRewardsData() {
        showLoadingProgressBar();
        new RewardsApiModelImpl().getRewardsData(mRoomId,mConfigId,this);
    }

    private RewardsAdapter createAdapter(List<Rewards> rewardsList,String challengeEndTime) {
        return new RewardsAdapter(getContext(),
                rewardsList, challengeEndTime);
    }


    @Override
    public void onEmpty() {
       // showMessage(Constants.Alerts.POLL_LIST_EMPTY);
    }

    @Override
    public void onFailedConfigsApi() {
       // showMessage(Constants.Alerts.API_FAIL);
    }

    @Override
    public void onData(@Nullable List<Rewards> rewardsList,String challengeEndTime) {
        hideLoadingProgressBar();
        mConfigAdapter = createAdapter(rewardsList,challengeEndTime);
        mRcvRewards.setAdapter(mConfigAdapter);
        mRcvRewards.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError(int status) {

    }

    @Override
    public void onNoInternet() {
        AlertsHelper.showAlert(getContext(),"Internet Problem", Constants.Alerts.NO_NETWORK_CONNECTION, null);
    }

    private void showLoadingProgressBar() {
        if (getView() != null) {
            getView().findViewById(R.id.rewards_rcv).setVisibility(View.GONE);
            getView().findViewById(R.id.rewards_progress_bar_layout).setVisibility(View.VISIBLE);
        }
    }

    private void hideLoadingProgressBar() {
        if (getView() != null) {
            getView().findViewById(R.id.rewards_progress_bar_layout).setVisibility(View.GONE);
            getView().findViewById(R.id.rewards_rcv).setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

}
