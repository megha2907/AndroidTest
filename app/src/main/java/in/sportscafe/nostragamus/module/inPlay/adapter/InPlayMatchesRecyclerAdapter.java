package in.sportscafe.nostragamus.module.inPlay.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatch;
import in.sportscafe.nostragamus.module.newChallenges.adapter.NewChallengeAdapterItemType;
import in.sportscafe.nostragamus.module.newChallenges.adapter.NewChallengesRecyclerAdapter;
import in.sportscafe.nostragamus.module.newChallenges.dto.MatchParty;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by sandip on 09/09/17.
 */

public class InPlayMatchesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private InPlayMatchAdapterListener mMatchAdapterListener;
    private List<InPlayMatch> mInPlayMatchList;

    public InPlayMatchesRecyclerAdapter(@NonNull List<InPlayMatch> matches, @NonNull InPlayMatchAdapterListener listener) {
        mInPlayMatchList = matches;
        mMatchAdapterListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = InPlayMatchAdapterItemType.DOUBLE_PARTY_MATCH;

        // TODO : change if other type introduced
        /*if (mInPlayMatchList != null && mInPlayMatchList.size() > position && mInPlayMatchList.get(position) != null) {
            if (mInPlayMatchList.get(position).getMatchType().equalsIgnoreCase("parties")) {
                viewType = InPlayMatchAdapterItemType.SINGLE_PARTY_MATCH;
            }
        }*/

        return viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case InPlayMatchAdapterItemType.DOUBLE_PARTY_MATCH:
                View v1 = inflater.inflate(R.layout.inplay_match_list_double_party_item, parent, false);
                viewHolder = new InPlayTwoPartyMatchItemViewHolder(v1);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder != null) {
            switch (holder.getItemViewType()) {
                case InPlayMatchAdapterItemType.DOUBLE_PARTY_MATCH:
                    bindDoublePartyMatchData(holder, position);
                    break;
            }
        }
    }

    private void bindDoublePartyMatchData(RecyclerView.ViewHolder holder, int position) {
        InPlayTwoPartyMatchItemViewHolder viewHolder = (InPlayTwoPartyMatchItemViewHolder) holder;

        if (mInPlayMatchList != null && mInPlayMatchList.size() > position) {
            InPlayMatch match = mInPlayMatchList.get(position);
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

    /* ---------------------
     View Holders
      ---------------------*/

    public class InPlayTwoPartyMatchItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView dateTimeTextView;
        HmImageView party1ImageView;
        TextView party1NameTextView;
        HmImageView party2ImageView;
        TextView party2NameTextView;
        TextView venueTextView;
        Button actionButton;

        public InPlayTwoPartyMatchItemViewHolder(View itemView) {
            super(itemView);
            dateTimeTextView = (TextView) itemView.findViewById(R.id.inplay_match_date_time_textView);
            party1ImageView = (HmImageView) itemView.findViewById(R.id.match_party_1_imgView);
            party1NameTextView = (TextView) itemView.findViewById(R.id.match_party_1_textView);
            party2ImageView = (HmImageView) itemView.findViewById(R.id.match_party_2_imgView);
            party2NameTextView = (TextView) itemView.findViewById(R.id.match_party_2_textView);
            venueTextView = (TextView) itemView.findViewById(R.id.inplay_match_venue_textView);
            actionButton = (Button) itemView.findViewById(R.id.inplay_match_action_button);

            itemView.setOnClickListener(this);
            actionButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.inplay_match_item_parent:
                    if (mMatchAdapterListener != null) {
                        mMatchAdapterListener.onMatchClicked(null);
                    }
                    break;

                case R.id.inplay_match_action_button:
                    if (mMatchAdapterListener != null) {
                        mMatchAdapterListener.onMatchActionClicked(1, null);
                    }
                    break;
            }
        }
    }
}
