package in.sportscafe.nostragamus.module.contest.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import in.sportscafe.nostragamus.module.contest.adapter.viewHolder.ContestViewHolder;
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
                /*View v1 = inflater.inflate(R.layout.card, parent, false);
                viewHolder = new ContestViewHolder(v1, mContestAdapterListener);*/
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

            if (contest != null) {}
        }
    }

    @Override
    public int getItemCount() {
        return (mContestList != null) ? mContestList.size() : 0;
    }

}
