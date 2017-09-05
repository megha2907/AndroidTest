package in.sportscafe.nostragamus.module.contest.adapter.viewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.contest.adapter.ContestAdapterListener;
import in.sportscafe.nostragamus.module.newChallenges.adapter.NewChallengeAdapterListener;

/**
 * Created by sandip on 23/08/17.
 */

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

        itemView.setOnClickListener(this);
        mBtnJoin.setOnClickListener(this);
        mRlContestLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.pool_rl_layout:
                if (clickListener != null) {
                    clickListener.onContestClicked();
                }
                break;
            case R.id.pool_row_btn_join:
                if (clickListener != null) {
                    clickListener.onJoinContestClicked();
                }
                break;
        }
    }
}
