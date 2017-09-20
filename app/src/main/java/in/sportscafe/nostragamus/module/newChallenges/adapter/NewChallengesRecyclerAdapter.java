package in.sportscafe.nostragamus.module.newChallenges.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengesResponse;
import in.sportscafe.nostragamus.module.newChallenges.helpers.DateTimeHelper;

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
                viewHolder = new NewChallengesItemViewHolder(v1);
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
                newChallengesItemViewHolder.challengeTournamentTextView.setText(getTournamentString(newChallengesResponse.getTournaments()));
                newChallengesItemViewHolder.challengeDateTextView.setText(DateTimeHelper.getChallengeDuration(newChallengesResponse.getChallengeStartTime(), newChallengesResponse.getChallengeEndTime()));
                newChallengesItemViewHolder.gameLeftTextView.setText(newChallengesResponse.getMatchesLeft() + "/" + newChallengesResponse.getTotalMatches());
                newChallengesItemViewHolder.prizeTextView.setText(Constants.RUPEE_SYMBOL+String.valueOf(newChallengesResponse.getPrizes()));
                newChallengesItemViewHolder.startTimeTextView.setText(DateTimeHelper.getStartTime(newChallengesResponse.getChallengeStartTime()));
            }
        }
    }

    private String getTournamentString(List<String> tournamentList) {
        String str = "";
        if (tournamentList != null && tournamentList.size() > 0) {
            if (tournamentList.size() > 1) {
                for (String s : tournamentList) {
                    str = str.concat(" . " + s);
                }
            }else {
                for (String s : tournamentList) {
                    str = s;
                }
            }
        }
        return str;
    }

    @Override
    public int getItemCount() {
        return (mNewChallengesResponseList != null) ? mNewChallengesResponseList.size() : 0;
    }

    /* ********************
            View Holders
    ***********************
     */

    private class NewChallengesItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView challengeNameTextView;
        TextView challengeTournamentTextView;
        TextView challengeDateTextView;
        TextView startTimeTextView;
        TextView gameLeftTextView;
        TextView prizeTextView;
        LinearLayout gameIconLinearLayout;

        public NewChallengesItemViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            challengeNameTextView = (TextView) itemView.findViewById(R.id.challenge_name_textView);
            challengeTournamentTextView = (TextView) itemView.findViewById(R.id.challenge_tournaments_textView);
            challengeDateTextView = (TextView) itemView.findViewById(R.id.challenge_date_textView);
            startTimeTextView = (TextView) itemView.findViewById(R.id.challenge_start_time_textView);
            gameLeftTextView = (TextView) itemView.findViewById(R.id.challenge_game_left_textView);
            prizeTextView = (TextView) itemView.findViewById(R.id.challenge_prizes_textView);
            gameIconLinearLayout = (LinearLayout) itemView.findViewById(R.id.challenge_sports_icons_container);
        }

        @Override
        public void onClick(View view) {
            int adapterPos = getAdapterPosition();

            if (mChallengeListener != null && mNewChallengesResponseList != null && mNewChallengesResponseList.size() > adapterPos) {
                Bundle args = new Bundle();
                args.putParcelable(Constants.BundleKeys.CHALLENGE, Parcels.wrap(mNewChallengesResponseList.get(adapterPos)));
                mChallengeListener.onChallengeClicked(args);
            }
        }
    }
}
