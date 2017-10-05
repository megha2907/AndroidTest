package in.sportscafe.nostragamus.module.challengeRewards;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.challengeRewards.dto.Rewards;

/**
 * Created by deepanshi on 9/6/17.
 */

public class RewardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String mChallengeEndTime;

    private List<Rewards> mRewardsList;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    public RewardsAdapter(Context context, List<Rewards> rewardsList, String ChallengeEndTime) {

        mChallengeEndTime = ChallengeEndTime;
        mRewardsList = rewardsList;
    }


    @Override
    public int getItemViewType(int position) {

        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        } else if (position == mRewardsList.size() + 1) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case TYPE_ITEM:
                View v1 = inflater.inflate(R.layout.inflater_new_reward_row, parent, false);
                viewHolder = new RewardsAdapter.RewardsVH(v1);
                break;

            case TYPE_HEADER:
                View v2 = inflater.inflate(R.layout.inflater_rewards_heading_row, parent, false);
                viewHolder = new RewardsAdapter.RewardsHeaderVH(v2);
                break;

            case TYPE_FOOTER:
                View v3 = inflater.inflate(R.layout.inflater_rewards_footer_row, parent, false);
                viewHolder = new RewardsAdapter.RewardsFooterVH(v3);
                break;

        }

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder != null) {

            //alternate row color
            if (position % 2 == 0) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black4));
            } else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black5));
            }

            if (holder instanceof RewardsVH) {
                setRewardsItem(holder, position - 1);
            } else if (holder instanceof RewardsFooterVH) {
                ((RewardsFooterVH) holder).mTvDisclaimer.setText(Html.fromHtml(((RewardsFooterVH) holder).mTvDisclaimer.getResources().getString(R.string.prize_money_disclaimer)));
                ((RewardsFooterVH) holder).mTvDisclaimer.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }

    }


    private void setRewardsItem(RecyclerView.ViewHolder holder, int position) {

        if (mRewardsList != null && mRewardsList.size() > position) {

            Rewards rewards = getItem(position);

            RewardsVH rewardsVH = (RewardsVH) holder;

            if (rewards != null) {

                if (!TextUtils.isEmpty(rewards.getRank())) {
                    rewardsVH.mTvRank.setVisibility(View.VISIBLE);
                    rewardsVH.mTvRank.setText(rewards.getRank());
                } else {
                    rewardsVH.mTvRank.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(rewards.getUserName())) {
                    rewardsVH.mTvWinnersName.setVisibility(View.VISIBLE);
                    rewardsVH.mTvWinnersName.setText(rewards.getUserName());
                } else {
                    rewardsVH.mTvWinnersName.setVisibility(View.GONE);
                }

                if (rewards.getAmount() != null) {
                    rewardsVH.mTvAmount.setVisibility(View.VISIBLE);
                    rewardsVH.mTvAmount.setText(Constants.RUPEE_SYMBOL + String.valueOf(rewards.getAmount()));
                } else {
                    rewardsVH.mTvAmount.setVisibility(View.GONE);
                }

            }

        }

    }

    private Rewards getItem(int position) {
        return mRewardsList.get(position);
    }

    @Override
    public int getItemCount() {
        return mRewardsList.size() + 2;
    }


    private class RewardsVH extends RecyclerView.ViewHolder {

        View mMainView;

        TextView mTvRank;
        TextView mTvAmount;
        TextView mTvWinnersName;

        public RewardsVH(View view) {
            super(view);

            mMainView = view;
            mTvRank = (TextView) view.findViewById(R.id.reward_row_tv_rank);
            mTvAmount = (TextView) view.findViewById(R.id.reward_row_tv_amount);
            mTvWinnersName = (TextView) view.findViewById(R.id.reward_row_tv_name);

        }

    }

    private class RewardsHeaderVH extends RecyclerView.ViewHolder {

        public RewardsHeaderVH(View itemView) {
            super(itemView);
        }

    }

    private class RewardsFooterVH extends RecyclerView.ViewHolder {

        TextView mTvDisclaimer;

        public RewardsFooterVH(View itemView) {
            super(itemView);
            mTvDisclaimer = (TextView) itemView.findViewById(R.id.pool_row_tv_disclaimer_txt);
        }

    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }


}
