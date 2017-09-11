package in.sportscafe.nostragamus.module.challengeRewards;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeConfig;
import in.sportscafe.nostragamus.module.allchallenges.dto.RewardBreakUp;
import in.sportscafe.nostragamus.module.allchallenges.dto.WinnersRewards;
import in.sportscafe.nostragamus.module.allchallenges.info.ChallengeRewardAdapter;
import in.sportscafe.nostragamus.module.challengeRewards.dto.Rewards;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.feed.FeedWebView;
import in.sportscafe.nostragamus.module.newChallenges.adapter.NewChallengeAdapterItemType;
import in.sportscafe.nostragamus.module.newChallenges.adapter.NewChallengesRecyclerAdapter;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengesResponse;
import in.sportscafe.nostragamus.utils.ViewUtils;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by deepanshi on 9/6/17.
 */

public class RewardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String mChallengeEndTime;

    private List<Rewards> mRewardsList;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public RewardsAdapter(Context context, List<Rewards> rewardsList, String ChallengeEndTime) {

        mChallengeEndTime = ChallengeEndTime;
        mRewardsList = rewardsList;
    }


    @Override
    public int getItemViewType(int position) {

        if (isPositionHeader(position)) {
            return TYPE_HEADER;
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
        }

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder != null) {
            if (holder instanceof RewardsVH) {
                setRewardsItem(holder, position-1);
            }
        }

    }

    private void setRewardsItem(RecyclerView.ViewHolder holder, int position) {

        if (mRewardsList != null && mRewardsList.size() > position) {

            Rewards rewards = getItem(position);

            RewardsVH rewardsVH = (RewardsVH) holder;

            if (rewards != null) {

                //alternate row color
                if (position % 2 == 0) {
                    holder.itemView.setBackgroundColor(ContextCompat.getColor(rewardsVH.mTvRank.getContext(), R.color.black5));
                } else {
                    holder.itemView.setBackgroundColor(ContextCompat.getColor(rewardsVH.mTvRank.getContext(), R.color.black4));
                }

                if (!TextUtils.isEmpty(rewards.getRank())) {
                    rewardsVH.mTvRank.setVisibility(View.VISIBLE);
                    rewardsVH.mTvRank.setText(rewards.getRank());
                } else {
                    rewardsVH.mTvRank.setVisibility(View.GONE);
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
        return mRewardsList.size()+1;
    }


    private class RewardsVH extends RecyclerView.ViewHolder {

        View mMainView;

        TextView mTvRank;
        TextView mTvAmount;

        public RewardsVH(View view) {
            super(view);

            mMainView = view;
            mTvRank = (TextView) view.findViewById(R.id.reward_row_tv_rank);
            mTvAmount = (TextView) view.findViewById(R.id.reward_row_tv_amount);

        }

    }

    private class RewardsHeaderVH extends RecyclerView.ViewHolder {

        public RewardsHeaderVH(View itemView) {
            super(itemView);
        }

    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }


}
