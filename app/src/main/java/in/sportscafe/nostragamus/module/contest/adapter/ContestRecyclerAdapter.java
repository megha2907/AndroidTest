package in.sportscafe.nostragamus.module.contest.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.contest.dto.Contest;

/**
 * Created by sandip on 01/09/17.
 */

public class ContestRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Contest> mContestList;
    private ContestAdapterListener mContestAdapterListener;

    public ContestRecyclerAdapter(Context cxt, @NonNull List<Contest> list,
                                  @NonNull ContestAdapterListener listener) {
        mContext = cxt;
        mContestList = list;
        mContestAdapterListener = listener;
    }

    @Override
    public int getItemViewType(int position) {

        int viewType = ContestAdapterItemType.CONTEST;
        if (mContestList != null && !mContestList.isEmpty()) {
            viewType = mContestList.get(position).getContestItemType();
        }
        return viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ContestAdapterItemType.CONTEST:
                View v1 = inflater.inflate(R.layout.contest_card_item_layout, parent, false);
                viewHolder = new ContestViewHolder(v1, mContestAdapterListener);
                break;

            case ContestAdapterItemType.REFER_FRIEND_AD:
                View v2 = inflater.inflate(R.layout.refer_friend_card_layout, parent, false);
                viewHolder = new ReferFriendViewHolder(v2, mContestAdapterListener);
                break;

            case ContestAdapterItemType.JOINED_CONTEST:
                View v3 = inflater.inflate(R.layout.joined_contest_card_item_layout, parent, false);
                viewHolder = new JoinedContestViewHolder(v3);
                break;

            case ContestAdapterItemType.NON_JOINABLE_CONTEST:
                View nonJoinable = inflater.inflate(R.layout.contest_card_non_joinable_item_layout, parent, false);
                viewHolder = new NonJoinablaContestViewHolder(nonJoinable);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder != null) {
            switch (holder.getItemViewType()) {
                case ContestAdapterItemType.CONTEST:
                    bindContestValues(holder, position);
                    break;

                case ContestAdapterItemType.REFER_FRIEND_AD:
                    bindReferFriendValues(holder, position);
                    break;

                case ContestAdapterItemType.JOINED_CONTEST:
                    bindJoinedContestValues(holder, position);
                    break;

                case ContestAdapterItemType.NON_JOINABLE_CONTEST:
                    bindNonJoinableContestValues(holder, position);
                    break;
            }
        }
    }

    private void bindNonJoinableContestValues(RecyclerView.ViewHolder holder, int position) {
        if (mContestList != null && mContestList.size() > position) {
            Contest contest = mContestList.get(position);
            NonJoinablaContestViewHolder viewHolder = (NonJoinablaContestViewHolder) holder;

            if (contest != null) {
                viewHolder.mTvPoolName.setText(contest.getConfigName());

                if (contest.noPrizes()) {
                    viewHolder.mTvPrizes.setText("No Prizes");
                } else {
                    viewHolder.mTvPrizes.setText(Constants.RUPEE_SYMBOL + String.valueOf(contest.getPrizes()));
                }

                if (!TextUtils.isEmpty(contest.getSubtitle())) {
                    viewHolder.mTvNumberOfPrizes.setText("(" + contest.getSubtitle() + ")");
                }

                String contestMode = contest.getContestMode();
                if (!TextUtils.isEmpty(contestMode)) {
                    if (contestMode.equalsIgnoreCase(Constants.ContestType.GUARANTEED)) {
                        viewHolder.mIvContestsType.setImageResource(R.drawable.guaranteed_icon);
                    } else if (contestMode.equalsIgnoreCase(Constants.ContestType.POOL)) {
                        viewHolder.mIvContestsType.setImageResource(R.drawable.pool_icon);
                    } else if (contestMode.equalsIgnoreCase(Constants.ContestType.NON_GUARANTEED)) {
                        viewHolder.mIvContestsType.setImageResource(R.drawable.no_guarantee_icon);
                    }
                }

                if (contest.isFreeEntry()) {
                    viewHolder.mTvEntryFee.setText("Free");
                    viewHolder.mTvEntryFee.setAllCaps(true);

                    if (contest.isUnlimitedEntries()) {
                        viewHolder.mTvMaxEntries.setText("UNLIMITED");
                    } else {
                        viewHolder.mTvMaxEntries.setText(String.valueOf(contest.getRoomSize()));
                    }
                } else {
                    viewHolder.mTvEntryFee.setText(Constants.RUPEE_SYMBOL + String.valueOf(contest.getEntryFee()));
                    viewHolder.mTvMaxEntries.setText(String.valueOf(contest.getRoomSize()));
                }
            }
        }
    }

    private void bindReferFriendValues(RecyclerView.ViewHolder holder, int position) {

    }

    private void bindContestValues(RecyclerView.ViewHolder holder, int position) {
        if (mContestList != null && mContestList.size() > position) {
            Contest contest = mContestList.get(position);
            ContestViewHolder viewHolder = (ContestViewHolder) holder;

            if (contest != null) {
                viewHolder.mTvPoolName.setText(contest.getConfigName());

                if (contest.noPrizes()) {
                    viewHolder.mTvPrizes.setText("No Prizes");
                } else {
                    viewHolder.mTvPrizes.setText(Constants.RUPEE_SYMBOL + String.valueOf(contest.getPrizes()));
                }

                viewHolder.mTvNumberOfPrizes.setText("(" + contest.getSubtitle() + ")");
                viewHolder.mTvFilledContests.setText(String.valueOf(contest.getFilledRooms()));
                viewHolder.mTvContestsAvailable.setText(String.valueOf(contest.getFillingRooms()));

                if (contest.getContestMode().equalsIgnoreCase(Constants.ContestType.GUARANTEED)) {
                    viewHolder.mIvContestsType.setImageResource(R.drawable.guaranteed_icon);
                } else if (contest.getContestMode().equalsIgnoreCase(Constants.ContestType.POOL)) {
                    viewHolder.mIvContestsType.setImageResource(R.drawable.pool_icon);
                } else if (contest.getContestMode().equalsIgnoreCase(Constants.ContestType.NON_GUARANTEED)) {
                    viewHolder.mIvContestsType.setImageResource(R.drawable.no_guarantee_icon);
                }

                if (contest.isFreeEntry()) {
                    viewHolder.mTvEntryFee.setText("Free");
                    viewHolder.mTvEntryFee.setAllCaps(true);

                    if (contest.isUnlimitedEntries()) {
                        viewHolder.mTvMaxEntries.setText("UNLIMITED");
                    } else {
                        viewHolder.mTvMaxEntries.setText(String.valueOf(contest.getRoomSize()));
                    }
                } else {
                    viewHolder.mTvEntryFee.setText(Constants.RUPEE_SYMBOL + String.valueOf(contest.getEntryFee()));
                    viewHolder.mTvMaxEntries.setText(String.valueOf(contest.getRoomSize()));
                }
            }
        }
    }

    private void bindJoinedContestValues(RecyclerView.ViewHolder holder, int position) {
        if (mContestList != null && mContestList.size() > position) {
            Contest contest = mContestList.get(position);
            JoinedContestViewHolder viewHolder = (JoinedContestViewHolder) holder;

            if (contest != null) {
                viewHolder.mTvPoolName.setText(contest.getConfigName());

                if (contest.noPrizes()) {
                    viewHolder.mTvPrizes.setText("No Prizes");
                } else {
                    viewHolder.mTvPrizes.setText(Constants.RUPEE_SYMBOL + String.valueOf(contest.getPrizes()));
                }

                if (!TextUtils.isEmpty(contest.getSubtitle())) {
                    viewHolder.mTvNumberOfPrizes.setText("(" + contest.getSubtitle() + ")");
                }

                String contestMode = contest.getContestMode();
                if (!TextUtils.isEmpty(contestMode)) {
                    if (contestMode.equalsIgnoreCase(Constants.ContestType.GUARANTEED)) {
                        viewHolder.mIvContestsType.setImageResource(R.drawable.guaranteed_icon);
                    } else if (contestMode.equalsIgnoreCase(Constants.ContestType.POOL)) {
                        viewHolder.mIvContestsType.setImageResource(R.drawable.pool_icon);
                    } else if (contestMode.equalsIgnoreCase(Constants.ContestType.NON_GUARANTEED)) {
                        viewHolder.mIvContestsType.setImageResource(R.drawable.no_guarantee_icon);
                    }
                }

                if (contest.isFreeEntry()) {
                    viewHolder.mTvEntryFee.setText("Free");
                    viewHolder.mTvEntryFee.setAllCaps(true);

                    if (contest.isUnlimitedEntries()) {
                        viewHolder.mTvMaxEntries.setText("UNLIMITED");
                    } else {
                        viewHolder.mTvMaxEntries.setText(String.valueOf(contest.getRoomSize()));
                    }
                } else {
                    viewHolder.mTvEntryFee.setText(Constants.RUPEE_SYMBOL + String.valueOf(contest.getEntryFee()));
                    viewHolder.mTvMaxEntries.setText(String.valueOf(contest.getRoomSize()));
                }
            }
        }
    }

    private Contest getItem(int position) {
        return mContestList.get(position);
    }

    @Override
    public int getItemCount() {
        return (mContestList != null) ? mContestList.size() : 0;
    }

    @NonNull
    private Bundle getContestBundle(int adapterPos) {
        Bundle args = new Bundle();
        args.putParcelable(Constants.BundleKeys.CONTEST, Parcels.wrap(mContestList.get(adapterPos)));
        return args;
    }

    /* -------------------- View holders ----------------------- */

    public class ContestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTvPoolName;
        public Button mBtnJoin;
        public TextView mTvEntryFee;
        public TextView mTvMaxEntries;
        public TextView mTvPrizes;
        public TextView mTvNumberOfPrizes;
        public TextView mTvFilledContests;
        public TextView mTvContestsAvailable;
        public RelativeLayout mRlContestLayout;
        public ImageView mIvContestsType;
        LinearLayout mRewardsPrizesLayout;
        LinearLayout mEntriesLayout;
        LinearLayout mEntryFeeLayout;

        private ContestAdapterListener clickListener;

        public ContestViewHolder(View itemView, @NonNull ContestAdapterListener listener) {
            super(itemView);
            this.clickListener = listener;

            mTvPoolName = (TextView) itemView.findViewById(R.id.pool_row_tv_name);
            mBtnJoin = (Button) itemView.findViewById(R.id.pool_row_btn_join);
            mTvEntryFee = (TextView) itemView.findViewById(R.id.pool_row_tv_entry_fee);
            mTvMaxEntries = (TextView) itemView.findViewById(R.id.pool_row_tv_member_count);
            mTvPrizes = (TextView) itemView.findViewById(R.id.pool_row_tv_reward);
            mTvNumberOfPrizes = (TextView) itemView.findViewById(R.id.pool_row_tv_number_of_prizes);
            mTvFilledContests = (TextView) itemView.findViewById(R.id.pool_row_tv_rooms_filled);
            mTvContestsAvailable = (TextView) itemView.findViewById(R.id.pool_row_tv_rooms_available);
            mRlContestLayout = (RelativeLayout) itemView.findViewById(R.id.pool_rl_layout);
            mIvContestsType = (ImageView) itemView.findViewById(R.id.pool_row_iv_contest_type);
            mRewardsPrizesLayout = (LinearLayout) itemView.findViewById(R.id.pool_row_ll_reward_layout);
            mEntriesLayout = (LinearLayout) itemView.findViewById(R.id.pool_row_ll_member_layout);
            mEntryFeeLayout = (LinearLayout) itemView.findViewById(R.id.pool_row_ll_entry_fee_layout);

            itemView.setOnClickListener(this);
            mBtnJoin.setOnClickListener(this);
            mRlContestLayout.setOnClickListener(this);
            mRewardsPrizesLayout.setOnClickListener(this);
            mIvContestsType.setOnClickListener(this);
            mEntriesLayout.setOnClickListener(this);
            mEntryFeeLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pool_row_btn_join:
                    if (clickListener != null) {
                        clickListener.onJoinContestClicked(getContestBundle(getAdapterPosition()));
                    }
                    break;

                case R.id.pool_rl_layout:
                    if (clickListener != null) {
                        int adapterPos = getAdapterPosition();
                        if (clickListener != null && mContestList != null && mContestList.size() > adapterPos) {
                            clickListener.onContestClicked(getContestBundle(adapterPos));
                        }
                    }
                    break;

                case R.id.pool_row_ll_reward_layout:
                    if (clickListener != null) {
                        clickListener.onPrizesClicked(getContestBundle(getAdapterPosition()));
                    }
                    break;

                case R.id.pool_row_iv_contest_type:
                    if (clickListener != null) {
                        Bundle args = getContestBundle(getAdapterPosition());
                        clickListener.onRulesClicked(args);
                    }
                    break;

                case R.id.pool_row_ll_member_layout:
                    if (clickListener != null) {
                        Bundle args = getContestBundle(getAdapterPosition());
                        clickListener.onEntriesClicked(args);
                    }
                    break;

                case R.id.pool_row_ll_entry_fee_layout:
                    if (clickListener != null) {
                        Bundle args = getContestBundle(getAdapterPosition());
                        clickListener.onEntryFeeClicked(args);
                    }
                    break;

            }
        }
    }

    public class JoinedContestViewHolder extends RecyclerView.ViewHolder {

        public TextView mTvPoolName;
        public TextView mTvEntryFee;
        public TextView mTvMaxEntries;
        public TextView mTvPrizes;
        public TextView mTvNumberOfPrizes;
        public ImageView mIvContestsType;

        public JoinedContestViewHolder(View itemView) {
            super(itemView);

            mTvPoolName = (TextView) itemView.findViewById(R.id.pool_row_tv_name);
            mTvEntryFee = (TextView) itemView.findViewById(R.id.pool_row_tv_entry_fee);
            mTvMaxEntries = (TextView) itemView.findViewById(R.id.pool_row_tv_member_count);
            mTvPrizes = (TextView) itemView.findViewById(R.id.pool_row_tv_reward);
            mTvNumberOfPrizes = (TextView) itemView.findViewById(R.id.pool_row_tv_number_of_prizes);
            mIvContestsType = (ImageView) itemView.findViewById(R.id.pool_row_iv_contest_type);
        }
    }

    public class ReferFriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public Button mReferButton;
        private ContestAdapterListener clickListener;

        public ReferFriendViewHolder(View itemView, @NonNull ContestAdapterListener listener) {
            super(itemView);
            this.clickListener = listener;

            mReferButton = (Button) itemView.findViewById(R.id.refer_friend_btn);
            mReferButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.refer_friend_btn:
                    if (clickListener != null) {
                        clickListener.onReferAFriendClicked();
                    }
                    break;
            }
        }
    }

    public class NonJoinablaContestViewHolder extends RecyclerView.ViewHolder {

        public TextView mTvPoolName;
        public Button mBtnJoin;
        public TextView mTvEntryFee;
        public TextView mTvMaxEntries;
        public TextView mTvPrizes;
        public TextView mTvNumberOfPrizes;
        public TextView mTvFilledContests;
        public RelativeLayout mRlContestLayout;
        public ImageView mIvContestsType;
        LinearLayout mRewardsPrizesLayout;
        LinearLayout mEntriesLayout;
        LinearLayout mEntryFeeLayout;

        public NonJoinablaContestViewHolder(View itemView) {
            super(itemView);
            mTvPoolName = (TextView) itemView.findViewById(R.id.pool_row_tv_name);
            mBtnJoin = (Button) itemView.findViewById(R.id.pool_row_btn_join);
            mTvEntryFee = (TextView) itemView.findViewById(R.id.pool_row_tv_entry_fee);
            mTvMaxEntries = (TextView) itemView.findViewById(R.id.pool_row_tv_member_count);
            mTvPrizes = (TextView) itemView.findViewById(R.id.pool_row_tv_reward);
            mTvNumberOfPrizes = (TextView) itemView.findViewById(R.id.pool_row_tv_number_of_prizes);
            mTvFilledContests = (TextView) itemView.findViewById(R.id.pool_row_tv_rooms_filled);
            mRlContestLayout = (RelativeLayout) itemView.findViewById(R.id.pool_rl_layout);
            mIvContestsType = (ImageView) itemView.findViewById(R.id.pool_row_iv_contest_type);
            mRewardsPrizesLayout = (LinearLayout) itemView.findViewById(R.id.pool_row_ll_reward_layout);
            mEntriesLayout = (LinearLayout) itemView.findViewById(R.id.pool_row_ll_member_layout);
            mEntryFeeLayout = (LinearLayout) itemView.findViewById(R.id.pool_row_ll_entry_fee_layout);
        }
    }
}