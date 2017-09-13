package in.sportscafe.nostragamus.module.inPlay.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayResponse;

/**
 * Created by deepanshi on 9/6/17.
 */

public class InPlayRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<InPlayResponse> mInPlayResponseList;
    private InPlayAdapterListener mInPlayAdapterListener;

    public InPlayRecyclerAdapter(Context cxt, @NonNull List<InPlayResponse> list,
                                        @NonNull InPlayAdapterListener listener) {
        mContext = cxt;
        mInPlayResponseList = list;
        mInPlayAdapterListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = InPlayAdapterItemType.JOINED_CONTEST;
        if (mInPlayResponseList != null && !mInPlayResponseList.isEmpty()) {
            viewType = mInPlayResponseList.get(position).getInPlayAdapterItemType();
        }
        return viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case InPlayAdapterItemType.JOINED_CONTEST:
                View v1 = inflater.inflate(R.layout.in_play_joined_card_layout, parent, false);
                viewHolder = new InPlayJoinedItemViewHolder(v1, mInPlayAdapterListener);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder != null) {
            switch (holder.getItemViewType()) {
                case InPlayAdapterItemType.JOINED_CONTEST:
                    bindInPlayValues(holder, position);
                    break;
            }
        }
    }

    private void bindInPlayValues(RecyclerView.ViewHolder holder, int position) {
        if (mInPlayResponseList != null && mInPlayResponseList.size() > position) {
            InPlayResponse inPlayResponse = mInPlayResponseList.get(position);
            InPlayJoinedItemViewHolder inPlayJoinedItemViewHolder = (InPlayJoinedItemViewHolder) holder;

            if (inPlayResponse != null) {

            }
        }
    }

    @Override
    public int getItemCount() {
        return (mInPlayResponseList != null) ? mInPlayResponseList.size() : 0;
    }

    public class InPlayJoinedItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public InPlayJoinedItemViewHolder(View itemView, @NonNull InPlayAdapterListener listener) {
            super(itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }

    public class InPlayCompletedItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public InPlayCompletedItemViewHolder(View itemView, @NonNull InPlayAdapterListener listener) {
            super(itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }

    public class InPlayHeadLessItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public InPlayHeadLessItemViewHolder(View itemView, @NonNull InPlayAdapterListener listener) {
            super(itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }
}
