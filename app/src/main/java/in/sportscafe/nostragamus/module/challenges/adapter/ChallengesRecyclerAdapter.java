package in.sportscafe.nostragamus.module.challenges.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;

/**
 * Created by sandip on 23/08/17.
 */

public class ChallengesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<Challenge> mChallengeList;
    private ChallengeAdapterListener mChallengeListener;

    public ChallengesRecyclerAdapter(Context cxt, @NonNull ArrayList<Challenge> list,
                                     @NonNull ChallengeAdapterListener listener) {
        mContext = cxt;
        mChallengeList = list;
        mChallengeListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = ChallengeAdapterItemType.CHALLENGE;

        if (mChallengeList != null && !mChallengeList.isEmpty()) {
//            viewType = mChallengeList.get(position).getViewType();
        }

        return viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ChallengeAdapterItemType.CHALLENGE:
                /*View v1 = inflater.inflate(R.layout.payout_add_paytm_layout, parent, false);
                viewHolder = new PayoutHomeRecyclerAdapter.AddPaytmViewHolder(v1, mPayoutAdapterActionListener);*/
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder != null) {
            switch (holder.getItemViewType()) {
                case ChallengeAdapterItemType.CHALLENGE:
//                    bindAddPaytmData((PayoutHomeRecyclerAdapter.AddPaytmViewHolder)holder, position);
                    break;
            }
        }
    }


    @Override
    public int getItemCount() {
        return (mChallengeList != null) ? mChallengeList.size() : 0;
    }


}
