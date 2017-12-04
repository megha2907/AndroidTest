package in.sportscafe.nostragamus.module.challengeRewards.rewardsPool;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
import in.sportscafe.nostragamus.utils.timeutils.TimeAgo;
import in.sportscafe.nostragamus.utils.timeutils.TimeUnit;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by sc on 2/12/17.
 */

public class RewardsPoolContestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String mChallengeEndTime;
    private List<Rewards> mRewardsList;
    private RewardsPoolContestAdapterListener mAdapterListener;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    public RewardsPoolContestAdapter(
                              List<Rewards> rewardsList, String ChallengeEndTime,
                              RewardsPoolContestAdapterListener adapterListener) {
        mChallengeEndTime = ChallengeEndTime;
        mRewardsList = rewardsList;
        mAdapterListener = adapterListener;
    }

    private Rewards getItem(int position) {
        if (mRewardsList != null) {
            return mRewardsList.get(position);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return (mRewardsList != null) ? mRewardsList.size() + 2 /* Header & Footer */ : 0;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
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
                View v1 = inflater.inflate(R.layout.pool_reward_item, parent, false);
                viewHolder = new RewardsPoolVH(v1);
                break;

            case TYPE_HEADER:
                View v2 = inflater.inflate(R.layout.pool_reward_header_item, parent, false);
                viewHolder = new RewardsPoolHeaderVH(v2);
                break;

            case TYPE_FOOTER:
                View v3 = inflater.inflate(R.layout.pool_reward_footer_item, parent, false);
                viewHolder = new RewardsPoolFooterVH(v3);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder != null) {
            //alternate row color
            if (position % 2 == 0) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black4));
            } else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black5));
            }

            if (position == 0){
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black5));
            }

            switch (holder.getItemViewType()) {
                case TYPE_HEADER:
                    bindHeader((RewardsPoolHeaderVH) holder);
                    break;

                case TYPE_ITEM:
                    bindRewardDetails(holder, position - 1);
                    break;

                case TYPE_FOOTER:
                    bindFooter((RewardsPoolFooterVH) holder);
                    break;
            }
        }
    }

    private void bindFooter(final RewardsPoolFooterVH holder) {
        RewardsPoolFooterVH footerVH = (RewardsPoolFooterVH) holder;
        footerVH.clickHereTextViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdapterListener != null) {
                    mAdapterListener.onClickHereButtonClicked();
                }
            }
        });
    }

    private void bindHeader(RewardsPoolHeaderVH holder) {
        RewardsPoolHeaderVH headerVH = (RewardsPoolHeaderVH) holder;
        String prizesHandOutDate = getPrizesHandoutDate();
        if (getChallengeOver(mChallengeEndTime)) {
        /* set when challenge is over */
            headerVH.mTvChallengeEndTime.setText("Prizes were handed out on "+prizesHandOutDate+".");
        }else {
            // Setting end date of the challenge
            headerVH.mTvChallengeEndTime.setText("The challenge will end on " + prizesHandOutDate + ". The recalculated prizes will be handed out within a few hours of challenge completion.");
        }
    }

    @NonNull
    private String getPrizesHandoutDate() {
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

    private void bindRewardDetails(RecyclerView.ViewHolder holder, int position) {
        if (mRewardsList != null && mRewardsList.size() > position) {
            Rewards rewards = getItem(position);
            RewardsPoolVH rewardsVH = (RewardsPoolVH) holder;
            if (rewards != null && rewardsVH != null) {

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

    private class RewardsPoolVH extends RecyclerView.ViewHolder {

        View mMainView;
        TextView mTvRank;
        TextView mTvAmount;
        TextView mTvWinnersName;

        public RewardsPoolVH(View view) {
            super(view);
            mMainView = view;
            mTvRank = (TextView) view.findViewById(R.id.reward_row_tv_rank);
            mTvAmount = (TextView) view.findViewById(R.id.reward_row_tv_amount);
            mTvWinnersName = (TextView) view.findViewById(R.id.reward_row_tv_name);
        }

    }

    private class RewardsPoolHeaderVH extends RecyclerView.ViewHolder {

        TextView mTvChallengeEndTime;

        public RewardsPoolHeaderVH(View itemView) {
            super(itemView);
            mTvChallengeEndTime = (TextView)itemView.findViewById(R.id.rewards_challenge_end_time);
        }
    }

    private class RewardsPoolFooterVH extends RecyclerView.ViewHolder {
        TextView msgTextView;
        TextView clickHereTextViewButton;

        public RewardsPoolFooterVH(View itemView) {
            super(itemView);
            msgTextView = (TextView) itemView.findViewById(R.id.pool_rewards_footer_msg_textView);
            clickHereTextViewButton = (TextView) itemView.findViewById(R.id.pool_rewards_footer_button_textView);
        }
    }

}
