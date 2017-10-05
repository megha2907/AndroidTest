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

    private int mRoomId=0;

    private RewardsAdapter mConfigAdapter;

    private RecyclerView mRcvRewards;

    private TextView mChallengeEndTime;

    public RewardsFragment() {

    }

    public static RewardsFragment newInstance(int roomId) {

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BundleKeys.ROOM_ID, roomId);

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
            getRewardsData();
        }
    }

    private void initViews(View rootView) {
        this.mRcvRewards = (RecyclerView)rootView. findViewById(R.id.rewards_rcv);
        this.mRcvRewards.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        this.mRcvRewards.setHasFixedSize(true);

        mChallengeEndTime = (TextView)rootView.findViewById(R.id.rewards_challenge_end_time);
    }

    private void getRewardsData() {
        new RewardsApiModelImpl().getRewardsData(mRoomId,this);
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
        mConfigAdapter = createAdapter(rewardsList,challengeEndTime);
        mRcvRewards.setAdapter(mConfigAdapter);


        String endTime = challengeEndTime;
        long endTimeMs = TimeUtils.getMillisecondsFromDateString(
                endTime,
                Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                Constants.DateFormats.GMT
        );

        int dayOfMonthinEndTime = Integer.parseInt(TimeUtils.getDateStringFromMs(endTimeMs, "d"));

        String prizesHandOutDate = dayOfMonthinEndTime + AppSnippet.ordinalOnly(dayOfMonthinEndTime) + " of " +
                TimeUtils.getDateStringFromMs(endTimeMs, "MMM");

        if (getChallengeOver(challengeEndTime)) {
            /* set when challenge is over */
            mChallengeEndTime.setText("Prizes were handed out on "+prizesHandOutDate+".");
        }else {
            // Setting end date of the challenge
            mChallengeEndTime.setText("The challenge will end on " + prizesHandOutDate + ". Prizes will be handed out within a few hours of challenge completion.");
        }

    }

    private boolean getChallengeOver(String challengeEndTime) {

        boolean isChallengeOver = false;

        if (!TextUtils.isEmpty(challengeEndTime)) {
            String startTime = challengeEndTime.replace("+00:00", ".000Z");
            long startTimeMs = TimeUtils.getMillisecondsFromDateString(
                    startTime,
                    Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                    Constants.DateFormats.GMT
            );
            TimeAgo timeAgo = TimeUtils.calcTimeAgo(Nostragamus.getInstance().getServerTime(), startTimeMs);

            isChallengeOver = timeAgo.timeDiff <= 0
                    || timeAgo.timeUnit == TimeUnit.MILLISECOND
                    || timeAgo.timeUnit == TimeUnit.SECOND;
        }

        return isChallengeOver;
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
            getView().findViewById(R.id.newChallengeContentLayout).setVisibility(View.GONE);
            getView().findViewById(R.id.newChallengeProgressBarLayout).setVisibility(View.VISIBLE);
        }
    }

    private void hideLoadingProgressBar() {
        if (getView() != null) {
            getView().findViewById(R.id.newChallengeProgressBarLayout).setVisibility(View.GONE);
            getView().findViewById(R.id.newChallengeContentLayout).setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

}
