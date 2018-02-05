package in.sportscafe.nostragamus.module.contest.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.Log;

import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.challengeRewards.dto.Rewards;
import in.sportscafe.nostragamus.module.contest.dto.bumper.BumperRewards;
import in.sportscafe.nostragamus.module.contest.ui.bumperContest.BumperRewardsAdapterListener;
import in.sportscafe.nostragamus.module.newChallenges.helpers.DateTimeHelper;
import in.sportscafe.nostragamus.utils.CodeSnippet;
import in.sportscafe.nostragamus.utils.timeutils.TimeAgo;
import in.sportscafe.nostragamus.utils.timeutils.TimeUnit;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by deepanshi on 2/2/18.
 */

public class BumperRewardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 11;
    private static final int TYPE_ITEM = 12;
    private static final int TYPE_FOOTER = 13;

    private BumperRewardsAdapterListener mAdapterListener;
    private List<BumperRewards> mRewardsList;
    private String mChallengeEndTime;
    private String mChallengeStartTime;
    private double mTotalPrizeMoney;

    public BumperRewardsAdapter(@Nullable List<BumperRewards> bumperRewardsList,
                                double totalPrizeMoney, String challengeEndTime,
                                String challengeStartTime,
                                BumperRewardsAdapterListener listener) {
        mRewardsList = bumperRewardsList;
        mChallengeEndTime = challengeEndTime;
        mChallengeStartTime = challengeStartTime;
        mAdapterListener = listener;
        mTotalPrizeMoney = totalPrizeMoney;
    }

    @Override
    public int getItemViewType(int position) {
        if (mRewardsList != null) {
            if (position == mRewardsList.size()) {
                return TYPE_FOOTER;
            }
            return TYPE_ITEM;
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case TYPE_ITEM:
                View v1 = inflater.inflate(R.layout.bumper_prize_estimate_recyler_item, parent, false);
                viewHolder = new BumperRewardsAdapter.EstimatePrizeViewHolder(v1);
                break;

            case TYPE_HEADER:
                break;

            case TYPE_FOOTER:
                View v3 = inflater.inflate(R.layout.bumper_estimate_adapter_footer_item, parent, false);
                viewHolder = new BumperRewardsAdapter.EstimatePrizeFooterVH(v3);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder != null) {
            if (position % 2 == 0) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black4));
            } else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black5));
            }

            switch (holder.getItemViewType()) {
                case TYPE_HEADER:
                    break;

                case TYPE_ITEM:
                    setRewardsItem(holder, position);
                    break;

                case TYPE_FOOTER:
                    bindFooter((BumperRewardsAdapter.EstimatePrizeFooterVH) holder);
                    break;
            }
        }
    }

    private void bindFooter(BumperRewardsAdapter.EstimatePrizeFooterVH holder) {
        if (DateTimeHelper.isChallengeTimeOver(mChallengeStartTime)) {      // If challenge started

            setFooterForStartedChallenge(holder);

        } else {
            setFooterForEstimation(holder);
        }
    }

    private void setFooterForStartedChallenge(BumperRewardsAdapter.EstimatePrizeFooterVH holder) {
        /* Prize handout msg */
        if (!TextUtils.isEmpty(mChallengeEndTime)) {
            String msg = "The challenge ends on " + getChallengeEndDateForPrizeOutMsg() + ". Prizes will be handed out a few hours after the challenge completion";
            holder.challengeEndMsg.setText(msg);

        }

        /* Rules layout */
        holder.disclaimerLayout.setVisibility(View.VISIBLE);

        if (holder.getAdapterPosition() % 2 == 0) {
            holder.disclaimerLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black5));
        } else {
            holder.disclaimerLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black4));
        }

    }

    private void setFooterForEstimation(BumperRewardsAdapter.EstimatePrizeFooterVH holder) {
        if (!TextUtils.isEmpty(mChallengeEndTime)) {

            if (getChallengeOver(mChallengeEndTime)) {
               /* Invisible when challenge is over */
                holder.challengeEndMsg.setVisibility(View.GONE);
                holder.disclaimerLayout.setVisibility(View.VISIBLE);
            } else {
                // Setting end date of the challenge
                String msg = "The challenge will end on " + getChallengeEndDateForPrizeOutMsg() + ". Prizes will be handed out a few hours after the challenge completion";
                holder.challengeEndMsg.setText(msg);
            }
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

    @NonNull
    private String getChallengeEndDateForPrizeOutMsg() {
        String endTime = mChallengeEndTime;
        long endTimeMs = TimeUtils.getMillisecondsFromDateString(
                endTime,
                Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                Constants.DateFormats.GMT
        );

        int dayOfMonthinEndTime = Integer.parseInt(TimeUtils.getDateStringFromMs(endTimeMs, "d"));

        return dayOfMonthinEndTime + AppSnippet.ordinalOnly(dayOfMonthinEndTime) + " of " +
                TimeUtils.getDateStringFromMs(endTimeMs, "MMM");
    }

    private void setRewardsItem(RecyclerView.ViewHolder holder, int position) {
        if (mRewardsList != null && mRewardsList.size() > position) {
            BumperRewards rewards = getItem(position);
            BumperRewardsAdapter.EstimatePrizeViewHolder estimatePrizeViewHolder =
                    (BumperRewardsAdapter.EstimatePrizeViewHolder) holder;

            if (rewards != null) {
                if (!TextUtils.isEmpty(rewards.getRank())) {
                    estimatePrizeViewHolder.mTvRank.setVisibility(View.VISIBLE);
                    estimatePrizeViewHolder.mTvRank.setText(rewards.getRank());
                } else {
                    estimatePrizeViewHolder.mTvRank.setVisibility(View.GONE);
                }

                if (rewards.getPercentage() != 0 && mTotalPrizeMoney != 0) {
                    double amount = rewards.getPercentage() * mTotalPrizeMoney;
                    Log.i("amount-->", String.valueOf(amount));
                    Log.i("percentage-->", String.valueOf(rewards.getPercentage()));
                    Log.i("totalPrizeMoney-->", String.valueOf(mTotalPrizeMoney));

                    estimatePrizeViewHolder.mTvAmount.setVisibility(View.VISIBLE);
                    estimatePrizeViewHolder.mTvAmount.setText(Constants.RUPEE_SYMBOL +
                            CodeSnippet.getFormattedAmount(amount));
                } else {
                    estimatePrizeViewHolder.mTvAmount.setVisibility(View.GONE);
                }

            }

        }

    }

    private BumperRewards getItem(int position) {
        return (mRewardsList != null && position < mRewardsList.size()) ? mRewardsList.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return (mRewardsList != null) ? mRewardsList.size() + 1 /* Footer */ : 0;
    }

    /* --------- View Holder -------- */

    private class EstimatePrizeViewHolder extends RecyclerView.ViewHolder {

        View mMainView;
        TextView mTvRank;
        TextView mTvAmount;
        TextView mTvWinnersName;

        public EstimatePrizeViewHolder(View view) {
            super(view);
            mMainView = view;
            mTvRank = (TextView) view.findViewById(R.id.reward_row_tv_rank);
            mTvAmount = (TextView) view.findViewById(R.id.reward_row_tv_amount);
            mTvWinnersName = (TextView) view.findViewById(R.id.reward_row_tv_name);
        }
    }

    private class EstimatePrizeFooterVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView challengeEndMsg;

        LinearLayout disclaimerLayout;
        TextView rulesButtonTextView;

        public EstimatePrizeFooterVH(View itemView) {
            super(itemView);

            challengeEndMsg = (TextView) itemView.findViewById(R.id.bumper_estimate_adapter_footer_msg_textView);
            disclaimerLayout = (LinearLayout) itemView.findViewById(R.id.bumper_estimate_challenge_started_footer_layout);
            rulesButtonTextView = (TextView) itemView.findViewById(R.id.rewards_footer_button_textView);

            rulesButtonTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rewards_footer_button_textView:
                    if (mAdapterListener != null) {
                        mAdapterListener.onNostraRulesClicked();
                    }
                    break;
            }
        }
    }
}
