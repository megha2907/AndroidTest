package in.sportscafe.nostragamus.module.inPlay.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatch;
import in.sportscafe.nostragamus.module.newChallenges.adapter.NewChallengeMatchAdapterListener;
import in.sportscafe.nostragamus.module.newChallenges.dto.MatchParty;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

public class InPlayHeadLessMatchesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private InPlayHeadLessMatchAdapterListener mMatchAdapterListener;
    private List<InPlayMatch> mInPlayMatchList;

    public InPlayHeadLessMatchesAdapter(@NonNull List<InPlayMatch> matches,
                                        @NonNull InPlayHeadLessMatchAdapterListener listener) {
        mInPlayMatchList = matches;
        mMatchAdapterListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = MatchesAdapterItemType.TWO_PARTY_MATCH;

        // Change view Type if single party adapter is

        return viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case MatchesAdapterItemType.TWO_PARTY_MATCH:
                View v1 = inflater.inflate(R.layout.in_play_headless_match_two_party_item, parent, false);
                viewHolder = new TwoPartyMatchItemViewHolder(v1);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder != null) {
            switch (holder.getItemViewType()) {
                case MatchesAdapterItemType.TWO_PARTY_MATCH:
                    bindTwoPartyMatchData(holder, position);
                    break;
            }
        }
    }

    private void bindTwoPartyMatchData(RecyclerView.ViewHolder holder, int position) {
        TwoPartyMatchItemViewHolder viewHolder = (TwoPartyMatchItemViewHolder) holder;

        if (mInPlayMatchList != null && mInPlayMatchList.size() > position) {
            InPlayMatch match = mInPlayMatchList.get(position);

            match.setMatchStatus("play");

            if (shouldDisableMatchClickAction(match)) {
                viewHolder.matchParentLayout.setEnabled(false);
                viewHolder.actionButton.setVisibility(View.GONE);
                viewHolder.vsTextView.setVisibility(View.VISIBLE);
            } else {
                viewHolder.matchParentLayout.setEnabled(true);
                viewHolder.actionButton.setVisibility(View.VISIBLE);
                viewHolder.vsTextView.setVisibility(View.GONE);
            }

            viewHolder.dateTimeTextView.setText(getDateTimeValue(match.getMatchStartTime()));
            viewHolder.venueTextView.setText(match.getMatchVenue());
            viewHolder.actionButton.setText(match.getMatchStatus());

            if (match.getMatchParties() != null)  {
                MatchParty party1 = match.getMatchParties().get(0);
                if (party1 != null) {
                    viewHolder.party1ImageView.setImageUrl(party1.getPartyImgUrl());
                    viewHolder.party1NameTextView.setText(party1.getPartyName());
                }

                MatchParty party2 = match.getMatchParties().get(1);
                if (party2 != null) {
                    viewHolder.party2ImageView.setImageUrl(party2.getPartyImgUrl());
                    viewHolder.party2NameTextView.setText(party2.getPartyName());
                }
            }
        }
    }

    private boolean shouldDisableMatchClickAction(InPlayMatch match) {
        boolean shouldDisable = false;

        String status = match.getMatchStatus();
        if (!TextUtils.isEmpty(status)) {
            if (status.equalsIgnoreCase(Constants.MatchStatusStrings.COMING_UP)) {
                shouldDisable = true;
            }
        }

        return shouldDisable;
    }

    private String getDateTimeValue(String startTime) {
        long startTimeMs = TimeUtils.getMillisecondsFromDateString(
                startTime,
                Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                Constants.DateFormats.GMT
        );

        int dayOfMonth = Integer.parseInt(TimeUtils.getDateStringFromMs(startTimeMs, "d"));
        return dayOfMonth + AppSnippet.ordinalOnly(dayOfMonth) + " " +
                TimeUtils.getDateStringFromMs(startTimeMs, "MMM") + ", "
                + TimeUtils.getDateStringFromMs(startTimeMs, Constants.DateFormats.HH_MM_AA).replace("AM", "am").replace("PM", "pm");
    }

    @Override
    public int getItemCount() {
        return (mInPlayMatchList != null) ? mInPlayMatchList.size() : 0;
    }

    private void onActionButtonClicked(int adapterPos) {
        if (mInPlayMatchList != null && mInPlayMatchList.size() > adapterPos && mMatchAdapterListener != null) {
            InPlayMatch match = mInPlayMatchList.get(adapterPos);
            Bundle args = new Bundle();

            if (match != null) {
                args.putParcelable(Constants.BundleKeys.INPLAY_MATCH, Parcels.wrap(match));
                args.putBoolean(Constants.BundleKeys.IS_HEADLESS_FLOW, true);

                String matchStatus = match.getMatchStatus();
                if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.DID_NOT_PLAY)) {
                    mMatchAdapterListener.onMatchActionClicked(MatchesAdapterAction.DID_NOT_PLAY, args);

                } else if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.COMING_UP)) {
                    mMatchAdapterListener.onMatchActionClicked(MatchesAdapterAction.COMING_UP, args);

                } else if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.PLAY)) {
                    mMatchAdapterListener.onMatchActionClicked(MatchesAdapterAction.PLAY, args);

                } else if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.CONTINUE)) {
                    mMatchAdapterListener.onMatchActionClicked(MatchesAdapterAction.CONTINUE, args);

                } else if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.ANSWER)) {
                    mMatchAdapterListener.onMatchActionClicked(MatchesAdapterAction.ANSWER, args);

                } else if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.POINTS)) {
                    mMatchAdapterListener.onMatchActionClicked(MatchesAdapterAction.POINTS, args);

                }
            }
        }
    }

    /* ---------------------
     View Holders
      ---------------------*/

    public class TwoPartyMatchItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout matchParentLayout;
        TextView dateTimeTextView;
        HmImageView party1ImageView;
        TextView party1NameTextView;
        HmImageView party2ImageView;
        TextView party2NameTextView;
        TextView venueTextView;
        Button actionButton;
        TextView vsTextView;

        public TwoPartyMatchItemViewHolder(View itemView) {
            super(itemView);
            matchParentLayout = (LinearLayout) itemView.findViewById(R.id.newChallenge_match_item_parent);
            dateTimeTextView = (TextView) itemView.findViewById(R.id.newChallenge_match_date_time_textView);
            party1ImageView = (HmImageView) itemView.findViewById(R.id.match_party_1_imgView);
            party1NameTextView = (TextView) itemView.findViewById(R.id.match_party_1_textView);
            party2ImageView = (HmImageView) itemView.findViewById(R.id.match_party_2_imgView);
            party2NameTextView = (TextView) itemView.findViewById(R.id.match_party_2_textView);
            venueTextView = (TextView) itemView.findViewById(R.id.newChallenge_match_venue_textView);
            actionButton = (Button) itemView.findViewById(R.id.newChallenge_match_action_button);
            vsTextView = (TextView) itemView.findViewById(R.id.newChallenge_match_vs_textView);

            matchParentLayout.setOnClickListener(this);
            actionButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                /* Since taking same action on both the clicks */
                case R.id.newChallenge_match_item_parent:
                case R.id.newChallenge_match_action_button:
                    onActionButtonClicked(getAdapterPosition());
                    break;
            }
        }
    }
}
