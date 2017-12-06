package in.sportscafe.nostragamus.module.contest.poolContest;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.challengeRewards.dto.RewardScreenData;
import in.sportscafe.nostragamus.module.challengeRewards.dto.Rewards;
import in.sportscafe.nostragamus.module.contest.adapter.PoolPrizeEstimationAdapter;
import in.sportscafe.nostragamus.module.contest.dto.PoolPrizeEstimationScreenData;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PoolPrizesEstimationFragment extends BaseFragment implements View.OnClickListener {

    private PoolPrizesEstimationFragmentListener mFragmentListener;
    private PoolPrizeEstimationScreenData mScreenData;
    private RecyclerView mRecyclerView;
    private SeekBar mSeekBar;

    public PoolPrizesEstimationFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PoolPrizesEstimationFragmentListener) {
            mFragmentListener = (PoolPrizesEstimationFragmentListener) context;
        } else {
            throw new RuntimeException("Activity should implement " +
                    PoolPrizesEstimationFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_poll_contest_reward_calculation, container, false);
        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {
        mSeekBar = (SeekBar) rootView.findViewById(R.id.estimation_seekbar);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.pool_estimation_recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        mSeekBar.setOnSeekBarChangeListener(getSeekBarChangeListener());
        rootView.findViewById(R.id.back_btn).setOnClickListener(this);
    }

    @NonNull
    private SeekBar.OnSeekBarChangeListener getSeekBarChangeListener() {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                onSeekBarTrackingCompleted(progress);
            }
        };
    }

    private void onSeekBarTrackingCompleted(int progress) {
        setEntries(progress);
        PoolPrizeEstimationAdapter adapter = new PoolPrizeEstimationAdapter(getEstimatedRewardsList(progress));
        mRecyclerView.setAdapter(adapter);
    }

    private void setEntries(int progress) {
        if (getView() != null) {
            TextView entriesTextView = (TextView) getView().findViewById(R.id.estimation_slider_progress_textView);
            entriesTextView.setText(progress + " Entries");
        }
    }

    private List<Rewards> getEstimatedRewardsList(int progress) {
        List<Rewards> rewardsList = new ArrayList<>();

        Rewards rewards = new Rewards();
        rewards.setAmount(100);
        rewards.setRank(String.valueOf(1));
        rewards.setUserName("Sandy");

        rewardsList.add(rewards);
        rewardsList.add(rewards);
        rewardsList.add(rewards);
        rewardsList.add(rewards);
        rewardsList.add(rewards);

        return rewardsList;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initMembers();
        populateInitDetails();
    }

    private void initMembers() {
        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey(Constants.BundleKeys.POOL_PRIZE_ESTIMATION_SCREEN_DATA)) {
                mScreenData = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.POOL_PRIZE_ESTIMATION_SCREEN_DATA));
            }

        }
    }

    private void populateInitDetails() {
        if (mScreenData != null && getView() != null) {
            int poolSize = 100;

             /*Show contest name on heading*/
            String contestName = mScreenData.getContestName();
            if (!TextUtils.isEmpty(contestName)) {
                TextView subHeadingTextView = (TextView) getView().findViewById(R.id.toolbar_heading_two);
                subHeadingTextView.setText(contestName);
            }

            /* Message */
            String msgStr = String.format(getString(R.string.pool_prize_estimation_msg), String.valueOf(poolSize));
            TextView msgTextview = (TextView) getView().findViewById(R.id.estimation_msg_textView);
            msgTextview.setText(msgStr);

            int max = getMaxValue(poolSize);
            /* Min , Max values */
            TextView minEntryTextView = (TextView) getView().findViewById(R.id.estimation_seekbar_min_textView);
            TextView maxEntryTextView = (TextView) getView().findViewById(R.id.estimation_seekbar_max_textView);
            minEntryTextView.setText(String.valueOf(2));
            maxEntryTextView.setText(String.valueOf(max));

            /* Seekbar values */
            mSeekBar.setMax(max);
            mSeekBar.setProgress(10);
        }
    }

    private int getMaxValue(int poolSize) {
        int max = poolSize;

        if (poolSize > 0) {
            max = (int) (poolSize * 0.75);
        }

        return max;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                if (mFragmentListener != null) {
                    mFragmentListener.onBackClicked();
                }
                break;


        }
    }

}
