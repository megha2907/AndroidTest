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

import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.challengeRewards.dto.Rewards;
import in.sportscafe.nostragamus.module.contest.ui.poolContest.PoolRewardsAdapterListener;
import in.sportscafe.nostragamus.module.newChallenges.helpers.DateTimeHelper;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by sc on 5/12/17.
 */

public class PoolRewardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 11;
    private static final int TYPE_ITEM = 12;
    private static final int TYPE_FOOTER = 13;

    private PoolRewardsAdapterListener mAdapterListner;
    private List<Rewards> mRewardsList;
    private String mChallengeEndTime;
    private String mChallengeStartTime;

    public PoolRewardsAdapter(@Nullable List<Rewards> rewardsList,
                              String challengeEndTime,
                              String challengeStartTime,
                              PoolRewardsAdapterListener listener) {
        mRewardsList = rewardsList;
        mChallengeEndTime = challengeEndTime;
        mChallengeStartTime = challengeStartTime;
        mAdapterListner = listener;
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
                View v1 = inflater.inflate(R.layout.pool_prize_estimate_recyler_item, parent, false);
                viewHolder = new EstimatePrizeViewHolder(v1);
                break;

            case TYPE_HEADER:
                break;

            case TYPE_FOOTER:
                View v3 = inflater.inflate(R.layout.pool_estimate_adapter_footer_item, parent, false);
                viewHolder = new EstimatePrizeFooterVH(v3);
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
                    bindFooter((EstimatePrizeFooterVH) holder);
                    break;
            }
        }
    }

    private void bindFooter(EstimatePrizeFooterVH holder) {
        if (DateTimeHelper.isChallengeTimeOver(mChallengeStartTime)) {      // If challenge started

            setFooterForStartedChallenge(holder);

        } else {
            setFooterForEstimation(holder);
        }
    }

    private void setFooterForStartedChallenge(EstimatePrizeFooterVH holder) {
        /* Prize handout msg */
        if (!TextUtils.isEmpty(mChallengeEndTime)) {
            String msg = "The challenge ends on " + getChallengeEndDateForPrizeOutMsg() + ". Prizes will be handed out a few hours after the challenge completion";
            holder.challengeEndMsg.setText(msg);
        }

        /* Rules layout */
        holder.disclaimerLayout.setVisibility(View.VISIBLE);
    }

    private void setFooterForEstimation(EstimatePrizeFooterVH holder) {
        if (!TextUtils.isEmpty(mChallengeEndTime)) {
            String msg = "The challenge will end on " + getChallengeEndDateForPrizeOutMsg() + ". Prizes will be handed out a few hours after the challenge completion";
            holder.challengeEndMsg.setText(msg);
        }
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
            Rewards rewards = getItem(position);
            EstimatePrizeViewHolder estimatePrizeViewHolder = (EstimatePrizeViewHolder) holder;

            if (rewards != null) {
                if (!TextUtils.isEmpty(rewards.getRank())) {
                    estimatePrizeViewHolder.mTvRank.setVisibility(View.VISIBLE);
                    estimatePrizeViewHolder.mTvRank.setText(rewards.getRank());
                } else {
                    estimatePrizeViewHolder.mTvRank.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(rewards.getUserName())) {
                    estimatePrizeViewHolder.mTvWinnersName.setVisibility(View.VISIBLE);
                    estimatePrizeViewHolder.mTvWinnersName.setText(rewards.getUserName());
                } else {
                    estimatePrizeViewHolder.mTvWinnersName.setVisibility(View.GONE);
                }

                if (rewards.getAmount() != null) {
                    estimatePrizeViewHolder.mTvAmount.setVisibility(View.VISIBLE);
                    estimatePrizeViewHolder.mTvAmount.setText(Constants.RUPEE_SYMBOL + String.valueOf(rewards.getAmount()));
                } else {
                    estimatePrizeViewHolder.mTvAmount.setVisibility(View.GONE);
                }

            }

        }

    }

    private Rewards getItem(int position) {
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

            challengeEndMsg = (TextView) itemView.findViewById(R.id.pool_estimate_adapter_footer_msg_textView);
            disclaimerLayout = (LinearLayout) itemView.findViewById(R.id.pool_estimate_challenge_started_footer_layout);
            rulesButtonTextView = (TextView) itemView.findViewById(R.id.rewards_footer_button_textView);

            rulesButtonTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rewards_footer_button_textView:
                    if (mAdapterListner != null) {
                        mAdapterListner.onNostraRulesClicked();
                    }
                    break;
            }
        }
    }

}
