package in.sportscafe.nostragamus.module.contest.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.contest.adapter.viewHolder.ContestViewHolder;
import in.sportscafe.nostragamus.module.contest.dto.Contest;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;

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
                View v1 = inflater.inflate(R.layout.inflater_pool_new_row, parent, false);
                viewHolder = new ContestViewHolder(v1, mContestAdapterListener);
                break;

            case ContestAdapterItemType.REFER_FRIEND_AD:
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
                    break;
            }
        }
    }

    private void bindContestValues(RecyclerView.ViewHolder holder, int position) {
        if (mContestList != null && mContestList.size() > position) {
            Contest contest = mContestList.get(position);
            ContestViewHolder viewHolder = (ContestViewHolder) holder;

            if (contest != null) {

                viewHolder.mTvPoolName.setText(contest.getTitle());

                if(contest.noPrizes()){
                    viewHolder.mTvPrizes.setText("No Prizes");
                }else {
                    viewHolder.mTvPrizes.setText(Constants.RUPEE_SYMBOL+String.valueOf(contest.getPrizes()));
                }

                viewHolder.mTvNumberOfPrizes.setText("(" + contest.getSubtitle() + ")");
                viewHolder.mTvFilledContests.setText(String.valueOf(contest.getFilledRooms()));
                viewHolder.mTvContestsAvailable.setText(String.valueOf(contest.getFillingRooms()));

                if (contest.getCategory().equalsIgnoreCase(Constants.ContestType.GUARANTEED)) {
                    viewHolder.mIvContestsType.setBackgroundResource(R.drawable.guaranteed_icon);
                } else if (contest.getCategory().equalsIgnoreCase(Constants.ContestType.POOL)) {
                    viewHolder.mIvContestsType.setBackgroundResource(R.drawable.pool_icon);
                }

                if (contest.isFreeEntry()){

                    viewHolder.mTvEntryFee.setText("Free");
                    viewHolder.mTvEntryFee.setAllCaps(true);

                    if (contest.isUnlimitedEntries()){
                        viewHolder.mTvMaxEntries.setText("unlimited");
                    }else {
                        viewHolder.mTvMaxEntries.setText(String.valueOf(contest.getRoomSize()));
                    }

                }else {

                    viewHolder.mTvEntryFee.setText(Constants.RUPEE_SYMBOL+String.valueOf(contest.getEntryFee()));
                    viewHolder.mTvMaxEntries.setText(String.valueOf(contest.getRoomSize()));

                }

            }
        }
    }

    @Override
    public int getItemCount() {
        return (mContestList != null) ? mContestList.size() : 0;
    }

}
