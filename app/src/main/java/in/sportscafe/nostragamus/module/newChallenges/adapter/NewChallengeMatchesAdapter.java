package in.sportscafe.nostragamus.module.newChallenges.adapter;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
import in.sportscafe.nostragamus.module.inPlay.adapter.MatchesAdapterAction;
import in.sportscafe.nostragamus.module.inPlay.adapter.MatchesAdapterItemType;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatch;
import in.sportscafe.nostragamus.module.newChallenges.dto.MatchParty;
import in.sportscafe.nostragamus.module.newChallenges.helpers.DateTimeHelper;
import in.sportscafe.nostragamus.module.nostraHome.helper.TimerHelper;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

public class NewChallengeMatchesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private NewChallengeMatchAdapterListener mMatchAdapterListener;
    private List<InPlayMatch> mInPlayMatchList;

    public NewChallengeMatchesAdapter(@NonNull List<InPlayMatch> matches,
                                        @NonNull NewChallengeMatchAdapterListener listener) {
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
                View v1 = inflater.inflate(R.layout.new_challenge_match_two_party_item, parent, false);
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

            if (shouldDisableMatchClickAction(match)) {
                viewHolder.matchParentLayout.setEnabled(false);
                viewHolder.actionButton.setVisibility(View.GONE);
                viewHolder.vsBtn.setVisibility(View.VISIBLE);
            } else {
                viewHolder.matchParentLayout.setEnabled(true);
                viewHolder.actionButton.setVisibility(View.VISIBLE);
                viewHolder.vsBtn.setVisibility(View.GONE);
            }

            /* Timer - match start */
            String matchStartTime = match.getMatchStartTime();
            if (!TextUtils.isEmpty(matchStartTime)) {
                if (DateTimeHelper.isTimerRequired(matchStartTime)) {
                    setTimer(viewHolder, matchStartTime);
                } else {
                    viewHolder.dateTimeTextView.setText(getDateTimeValue(matchStartTime));
                }
            }

            viewHolder.venueTextView.setText(match.getMatchVenue());

            /* Match status */
            String matchStatus = match.getMatchStatus();
            if (!TextUtils.isEmpty(matchStatus)) {

                if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.POINTS)) {
                    viewHolder.actionButton.setClickable(true);
                    matchStatus = match.getMatchPoints() + " " + matchStatus;
                    viewHolder.actionButton.setTextColor(ContextCompat.getColor(viewHolder.actionButton.getContext(), R.color.black));
                    viewHolder.actionButton.setBackground(ContextCompat.getDrawable(viewHolder.actionButton.getContext(), R.drawable.btn_points_bg));
                    viewHolder.actionButton.setLayoutParams(new LinearLayout.LayoutParams
                            (viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_90)
                                    , viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_32)));

                } else if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.PLAY)) {
                    viewHolder.actionButton.setClickable(true);
                    viewHolder.actionButton.setTextColor(ContextCompat.getColor(viewHolder.actionButton.getContext(), R.color.white));
                    viewHolder.actionButton.setBackground(ContextCompat.getDrawable(viewHolder.actionButton.getContext(), R.drawable.btn_play_bg));
                    viewHolder.actionButton.setLayoutParams(new LinearLayout.LayoutParams
                            (viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_90)
                                    , viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_32)));

                } else if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.DID_NOT_PLAY)) {
                    viewHolder.actionButton.setClickable(true);
                    viewHolder.actionButton.setTextColor(ContextCompat.getColor(viewHolder.actionButton.getContext(), R.color.black));
                    viewHolder.actionButton.setBackground(ContextCompat.getDrawable(viewHolder.actionButton.getContext(), R.drawable.btn_did_not_play_bg));
                    viewHolder.actionButton.setLayoutParams(new LinearLayout.LayoutParams
                            (viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_100)
                                    , viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_32)));

                } else if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.COMING_UP)) {
                    viewHolder.actionButton.setClickable(false);
                    viewHolder.actionButton.setTextColor(ContextCompat.getColor(viewHolder.actionButton.getContext(), R.color.grey_a1a1a1));
                    viewHolder.actionButton.setBackground(ContextCompat.getDrawable(viewHolder.actionButton.getContext(), R.drawable.btn_coming_up_bg));
                    viewHolder.actionButton.setLayoutParams(new LinearLayout.LayoutParams
                            (viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_90)
                                    , viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_32)));

                }else if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.ANSWER)) {
                    viewHolder.actionButton.setClickable(true);
                    viewHolder.actionButton.setTextColor(ContextCompat.getColor(viewHolder.actionButton.getContext(), R.color.white));
                    viewHolder.actionButton.setBackground(ContextCompat.getDrawable(viewHolder.actionButton.getContext(), R.drawable.btn_play_bg));
                    viewHolder.actionButton.setLayoutParams(new LinearLayout.LayoutParams
                            (viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_90)
                                    , viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_32)));

                }else if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.CONTINUE)) {
                    viewHolder.actionButton.setClickable(true);
                    viewHolder.actionButton.setTextColor(ContextCompat.getColor(viewHolder.actionButton.getContext(), R.color.white));
                    viewHolder.actionButton.setBackground(ContextCompat.getDrawable(viewHolder.actionButton.getContext(), R.drawable.btn_play_bg));
                    viewHolder.actionButton.setLayoutParams(new LinearLayout.LayoutParams
                            (viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_90)
                                    , viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_32)));

                }else if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.CANCELLED)) {
                    viewHolder.actionButton.setClickable(false);
                    viewHolder.actionButton.setTextColor(ContextCompat.getColor(viewHolder.actionButton.getContext(), R.color.black));
                    viewHolder.actionButton.setBackground(ContextCompat.getDrawable(viewHolder.actionButton.getContext(), R.drawable.btn_did_not_play_bg));
                    viewHolder.actionButton.setLayoutParams(new LinearLayout.LayoutParams
                            (viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_100)
                                    , viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_32)));

                }else {
                    viewHolder.actionButton.setClickable(true);
                    viewHolder.actionButton.setTextColor(ContextCompat.getColor(viewHolder.actionButton.getContext(), R.color.white));
                    viewHolder.actionButton.setBackground(ContextCompat.getDrawable(viewHolder.actionButton.getContext(), R.drawable.btn_play_bg));
                    viewHolder.actionButton.setLayoutParams(new LinearLayout.LayoutParams
                            (viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_90)
                                    , viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_32)));
                }

                viewHolder.actionButton.setText(matchStatus);

            }

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

    private void setTimer(final TwoPartyMatchItemViewHolder viewHolder, final String matchStartTime) {
        CountDownTimer countDownTimer = new CountDownTimer(TimerHelper.getCountDownFutureTime(matchStartTime), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                viewHolder.dateTimeTextView.setText(TimerHelper.getTimerFormatFromMillis(millisUntilFinished));
            }

            @Override
            public void onFinish() {
            }
        };
        countDownTimer.start();
    }

    private boolean shouldDisableMatchClickAction(InPlayMatch match) {
        boolean shouldDisable = true;

        String status = match.getMatchStatus();
        if (!TextUtils.isEmpty(status)) {
            if (status.equalsIgnoreCase(Constants.MatchStatusStrings.PLAY)) {
                shouldDisable = false;
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

                String matchStatus = match.getMatchStatus();
                if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.PLAY)) {
                    mMatchAdapterListener.onMatchActionClicked(MatchesAdapterAction.PLAY, args);

                } else if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.CONTINUE)) {
                    mMatchAdapterListener.onMatchActionClicked(MatchesAdapterAction.CONTINUE, args);

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
        Button vsBtn;

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
            vsBtn = (Button) itemView.findViewById(R.id.newChallenge_match_vs_button);

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