package in.sportscafe.nostragamus.module.challengeRewards;

import android.content.Context;
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
import in.sportscafe.nostragamus.utils.CodeSnippet;
import in.sportscafe.nostragamus.utils.timeutils.TimeAgo;
import in.sportscafe.nostragamus.utils.timeutils.TimeUnit;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by deepanshi on 9/6/17.
 */

public class RewardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RewardsAdapterListener mAdapterListener;
    private String mChallengeEndTime;
    private List<Rewards> mRewardsList;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    public RewardsAdapter(Context context, List<Rewards> rewardsList,
                          String ChallengeEndTime, RewardsAdapterListener listener) {

        mChallengeEndTime = ChallengeEndTime;
        mRewardsList = rewardsList;
        mAdapterListener = listener;
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder != null) {
            //alternate row color
            if (position % 2 == 0) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black4));
            } else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black5));
            }

            if (position==0){
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black5));
            }

            switch (holder.getItemViewType()) {
                case TYPE_HEADER:
                    bindHeader((RewardsHeaderVH) holder);
                    break;

                case TYPE_ITEM:
                    setRewardsItem(holder, position - 1);
                    break;

                case TYPE_FOOTER:
                    bindFooter((RewardsFooterVH) holder);
                    break;
            }
        }
    }

    private void bindHeader(RewardsHeaderVH holder) {
        String endTime = mChallengeEndTime;
        long endTimeMs = TimeUtils.getMillisecondsFromDateString(
                endTime,
                Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                Constants.DateFormats.GMT
        );

        int dayOfMonthinEndTime = Integer.parseInt(TimeUtils.getDateStringFromMs(endTimeMs, "d"));

        String prizesHandOutDate = dayOfMonthinEndTime + AppSnippet.ordinalOnly(dayOfMonthinEndTime) + " of " +
                TimeUtils.getDateStringFromMs(endTimeMs, "MMM");

        if (getChallengeOver(mChallengeEndTime)) {
            /* set when challenge is over */
            holder.mTvChallengeEndTime.setText("Prizes were handed out on "+prizesHandOutDate+".");
        }else {
            // Setting end date of the challenge
            holder.mTvChallengeEndTime.setText("The challenge will end on " + prizesHandOutDate + ". Prizes will be handed out within a few hours of challenge completion.");
        }
    }

    private void bindFooter(RewardsFooterVH holder) {
        RewardsFooterVH rewardsFooterVH = holder;
        rewardsFooterVH.nostraRulesButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdapterListener != null) {
                    mAdapterListener.onNostraRulesClicked();
                }
            }
        });
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
                    rewardsVH.mTvAmount.setText(Constants.RUPEE_SYMBOL + CodeSnippet.getFormattedAmount(rewards.getAmount()));
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

        TextView mTvChallengeEndTime;

        public RewardsHeaderVH(View itemView) {
            super(itemView);
            mTvChallengeEndTime = (TextView)itemView.findViewById(R.id.rewards_challenge_end_time);
        }

    }

    private class RewardsFooterVH extends RecyclerView.ViewHolder {

        TextView mTvDisclaimer;
        TextView nostraRulesButtonTextView;

        public RewardsFooterVH(View itemView) {
            super(itemView);
            mTvDisclaimer = (TextView) itemView.findViewById(R.id.pool_row_tv_disclaimer_txt);
            nostraRulesButtonTextView = (TextView) itemView.findViewById(R.id.rewards_footer_button_textView);
        }

    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }


}
