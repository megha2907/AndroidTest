package in.sportscafe.nostragamus.module.newChallenges.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.newChallenges.adapter.viewHolder.NewChallengesItemViewHolder;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengesResponse;

/**
 * Created by sandip on 23/08/17.
 */

public class NewChallengesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<NewChallengesResponse> mNewChallengesResponseList;
    private NewChallengeAdapterListener mChallengeListener;

    public NewChallengesRecyclerAdapter(Context cxt, @NonNull List<NewChallengesResponse> list,
                                        @NonNull NewChallengeAdapterListener listener) {
        mContext = cxt;
        mNewChallengesResponseList = list;
        mChallengeListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = NewChallengeAdapterItemType.CHALLENGE;
        if (mNewChallengesResponseList != null && !mNewChallengesResponseList.isEmpty()) {
            viewType = mNewChallengesResponseList.get(position).getChallengeAdapterItemType();
        }
        return viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case NewChallengeAdapterItemType.CHALLENGE:
                View v1 = inflater.inflate(R.layout.challenge_card_layout, parent, false);
                viewHolder = new NewChallengesItemViewHolder(v1, mChallengeListener);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder != null) {
            switch (holder.getItemViewType()) {
                case NewChallengeAdapterItemType.CHALLENGE:
                    bindChallengeValues(holder, position);
                    break;
            }
        }
    }

    private void bindChallengeValues(RecyclerView.ViewHolder holder, int position) {
        if (mNewChallengesResponseList != null && mNewChallengesResponseList.size() > position) {
            NewChallengesResponse newChallengesResponse = mNewChallengesResponseList.get(position);
            NewChallengesItemViewHolder newChallengesItemViewHolder = (NewChallengesItemViewHolder) holder;

            if (newChallengesResponse != null) {
                newChallengesItemViewHolder.challengeNameTextView.setText(newChallengesResponse.getChallengeName());
                newChallengesItemViewHolder.challengeDateTextView.setText(newChallengesResponse.getChallengeStartTime());
            }
        }
    }

    @Override
    public int getItemCount() {
        return (mNewChallengesResponseList != null) ? mNewChallengesResponseList.size() : 0;
    }


}
