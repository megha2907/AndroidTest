package in.sportscafe.nostragamus.module.contest.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.challengeRewards.dto.Rewards;

/**
 * Created by sc on 5/12/17.
 */

public class PoolPrizeEstimationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Rewards> mRewardsList;

    public PoolPrizeEstimationAdapter(List<Rewards> rewardsList) {
        mRewardsList = rewardsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.pool_prize_estimate_recyler_item, parent, false);
        return new EstimatePriceViewHolder(v1);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder != null) {
            if (position % 2 == 0) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black4));
            } else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black5));
            }

            setRewardsItem(holder, position);
        }
    }

    private void setRewardsItem(RecyclerView.ViewHolder holder, int position) {
        if (mRewardsList != null && mRewardsList.size() > position) {
            Rewards rewards = getItem(position);
            EstimatePriceViewHolder estimatePriceViewHolder = (EstimatePriceViewHolder) holder;

            if (rewards != null) {
                if (!TextUtils.isEmpty(rewards.getRank())) {
                    estimatePriceViewHolder.mTvRank.setVisibility(View.VISIBLE);
                    estimatePriceViewHolder.mTvRank.setText(rewards.getRank());
                } else {
                    estimatePriceViewHolder.mTvRank.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(rewards.getUserName())) {
                    estimatePriceViewHolder.mTvWinnersName.setVisibility(View.VISIBLE);
                    estimatePriceViewHolder.mTvWinnersName.setText(rewards.getUserName());
                } else {
                    estimatePriceViewHolder.mTvWinnersName.setVisibility(View.GONE);
                }

                if (rewards.getAmount() != null) {
                    estimatePriceViewHolder.mTvAmount.setVisibility(View.VISIBLE);
                    estimatePriceViewHolder.mTvAmount.setText(Constants.RUPEE_SYMBOL + String.valueOf(rewards.getAmount()));
                } else {
                    estimatePriceViewHolder.mTvAmount.setVisibility(View.GONE);
                }

            }

        }

    }

    private Rewards getItem(int position) {
        return (mRewardsList != null && position < mRewardsList.size()) ? mRewardsList.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return (mRewardsList != null) ? mRewardsList.size() : 0;
    }

    /* --------- View Holder -------- */

    private class EstimatePriceViewHolder extends RecyclerView.ViewHolder {

        View mMainView;
        TextView mTvRank;
        TextView mTvAmount;
        TextView mTvWinnersName;

        public EstimatePriceViewHolder(View view) {
            super(view);
            mMainView = view;
            mTvRank = (TextView) view.findViewById(R.id.reward_row_tv_rank);
            mTvAmount = (TextView) view.findViewById(R.id.reward_row_tv_amount);
            mTvWinnersName = (TextView) view.findViewById(R.id.reward_row_tv_name);
        }
    }

}
