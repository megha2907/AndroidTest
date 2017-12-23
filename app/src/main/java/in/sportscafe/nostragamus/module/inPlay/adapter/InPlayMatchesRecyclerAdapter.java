package in.sportscafe.nostragamus.module.inPlay.adapter;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.customfont.Typefaces;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatch;
import in.sportscafe.nostragamus.module.newChallenges.adapter.NewChallengeMatchesAdapter;
import in.sportscafe.nostragamus.module.newChallenges.dto.MatchParty;
import in.sportscafe.nostragamus.module.newChallenges.helpers.DateTimeHelper;
import in.sportscafe.nostragamus.module.nostraHome.helper.TimerHelper;
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
        int viewType = MatchesAdapterItemType.TWO_PARTY_MATCH;

        /*if (mInPlayMatchList != null && mInPlayMatchList.size() > position && mInPlayMatchList.get(position) != null) {
            if (mInPlayMatchList.get(position).getMatchType().equalsIgnoreCase("parties")) {
                viewType = MatchesAdapterItemType.SINGLE_PARTY_MATCH;
            }
        }*/

        return viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case MatchesAdapterItemType.TWO_PARTY_MATCH:
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
                case MatchesAdapterItemType.TWO_PARTY_MATCH:
                    bindDoublePartyMatchData(holder, position);
                    break;
            }
        }
    }

    private void bindDoublePartyMatchData(RecyclerView.ViewHolder holder, int position) {
        InPlayTwoPartyMatchItemViewHolder viewHolder = (InPlayTwoPartyMatchItemViewHolder) holder;

        if (mInPlayMatchList != null && mInPlayMatchList.size() > position) {
            InPlayMatch match = mInPlayMatchList.get(position);

            /* Enable / disable buttons */
            if (shouldDisableMatchClickAction(match)) {
                viewHolder.matchParentLayout.setEnabled(false);
                viewHolder.actionButton.setEnabled(false);
            }

            viewHolder.venueTextView.setText(match.getMatchStage()+", "+match.getMatchVenue());

               /* Set timer */
            String matchStartTime = match.getMatchStartTime();
            if (!TextUtils.isEmpty(matchStartTime)) {
                if (DateTimeHelper.isTimerRequired(matchStartTime)) {
                    setTimer(viewHolder, matchStartTime);
                } else {
                    viewHolder.dateTimeTextView.setText(getDateTimeValue(matchStartTime));
                }
            }

            Typeface latoBold = Typefaces.get(viewHolder.actionButton.getContext(), "fonts/lato/Lato-Bold.ttf");
            Typeface latoRegular = Typefaces.get(viewHolder.actionButton.getContext(), "fonts/lato/Lato-Regular.ttf");

            /* Match status */
            String matchStatus = match.getMatchStatus();
            if (!TextUtils.isEmpty(matchStatus)) {

                if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.POINTS)) {
                    viewHolder.actionButton.setClickable(true);
                    viewHolder.dateTimeTextView.setText("Completed");
                    viewHolder.actionButtonTextView.setTypeface(latoBold);
                    viewHolder.venueTextView.setText(match.getMatchStage()+" - "+match.getMatchResult());
                    matchStatus = match.getMatchPoints() + " " + matchStatus;
                    viewHolder.actionButtonTextView.setTextColor(ContextCompat.getColor(viewHolder.actionButton.getContext(), R.color.black));
                    viewHolder.actionButton.setBackground(ContextCompat.getDrawable(viewHolder.actionButton.getContext(), R.drawable.btn_points_bg));
                    viewHolder.actionButton.setLayoutParams(new LinearLayout.LayoutParams
                            (viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_86)
                                    , viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_32)));

                } else if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.PLAY)) {
                    viewHolder.actionButton.setClickable(true);
                    viewHolder.actionButtonTextView.setTextColor(ContextCompat.getColor(viewHolder.actionButton.getContext(), R.color.white));
                    viewHolder.actionButtonTextView.setTypeface(latoBold);
                    viewHolder.actionButton.setBackground(ContextCompat.getDrawable(viewHolder.actionButton.getContext(), R.drawable.btn_play_bg));
                    viewHolder.actionButton.setLayoutParams(new LinearLayout.LayoutParams
                            (viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_84)
                                    , viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_32)));

                    viewHolder.actionButtonImageView.setBackgroundResource(R.drawable.right_arrow_play_btn);
                    viewHolder.actionButtonImageView.setVisibility(View.VISIBLE);

                } else if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.DID_NOT_PLAY)) {
                    viewHolder.actionButton.setClickable(true);
                    viewHolder.actionButtonTextView.setTextColor(ContextCompat.getColor(viewHolder.actionButton.getContext(), R.color.black));
                    viewHolder.actionButtonTextView.setTypeface(latoBold);
                    viewHolder.actionButton.setBackground(ContextCompat.getDrawable(viewHolder.actionButton.getContext(), R.drawable.btn_did_not_play_bg));
                    viewHolder.actionButton.setLayoutParams(new LinearLayout.LayoutParams
                            (viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_80)
                                    , viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_32)));

                    if (match.getMatchResult() != null && !match.getMatchResult().isEmpty()) {
                        viewHolder.dateTimeTextView.setText("Completed");
                        viewHolder.venueTextView.setText(match.getMatchStage()+"-"+match.getMatchResult());
                    }else {
                        viewHolder.dateTimeTextView.setText("In Progress");
                    }

                } else if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.COMING_UP)) {
                    viewHolder.actionButton.setClickable(false);
                    viewHolder.actionButtonTextView.setTextColor(ContextCompat.getColor(viewHolder.actionButton.getContext(), R.color.grey_a1a1a1));
                    viewHolder.actionButtonTextView.setTypeface(latoRegular);
                    viewHolder.actionButton.setBackground(ContextCompat.getDrawable(viewHolder.actionButton.getContext(), R.drawable.btn_coming_up_bg));
                    viewHolder.actionButton.setLayoutParams(new LinearLayout.LayoutParams
                            (viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_90)
                                    , viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_34)));

                } else if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.ANSWER)) {
                    viewHolder.actionButton.setClickable(true);
                    viewHolder.actionButtonTextView.setTextColor(ContextCompat.getColor(viewHolder.actionButton.getContext(), R.color.white));
                    viewHolder.actionButtonTextView.setTypeface(latoBold);
                    viewHolder.actionButton.setBackground(ContextCompat.getDrawable(viewHolder.actionButton.getContext(), R.drawable.btn_answer_bg));
                    viewHolder.actionButton.setLayoutParams(new LinearLayout.LayoutParams
                            (viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_84)
                                    , viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_32)));

                } else if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.CONTINUE)) {
                    viewHolder.actionButton.setClickable(true);
                    viewHolder.actionButtonTextView.setTextColor(ContextCompat.getColor(viewHolder.actionButton.getContext(), R.color.white));
                    viewHolder.actionButtonTextView.setTypeface(latoBold);
                    viewHolder.actionButton.setBackground(ContextCompat.getDrawable(viewHolder.actionButton.getContext(), R.drawable.btn_continue_bg));
                    viewHolder.actionButton.setLayoutParams(new LinearLayout.LayoutParams
                            (viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_84)
                                    , viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_32)));

                } else if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.CANCELLED)) {
                    viewHolder.actionButton.setClickable(false);
                    viewHolder.actionButtonTextView.setTextColor(ContextCompat.getColor(viewHolder.actionButton.getContext(), R.color.black));
                    viewHolder.actionButtonTextView.setTypeface(latoRegular);
                    viewHolder.actionButton.setBackground(ContextCompat.getDrawable(viewHolder.actionButton.getContext(), R.drawable.btn_did_not_play_bg));
                    viewHolder.actionButton.setLayoutParams(new LinearLayout.LayoutParams
                            (viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_100)
                                    , viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_32)));

                } else {
                    viewHolder.actionButton.setClickable(true);
                    viewHolder.actionButtonTextView.setTextColor(ContextCompat.getColor(viewHolder.actionButton.getContext(), R.color.white));
                    viewHolder.actionButtonTextView.setTypeface(latoRegular);
                    viewHolder.actionButton.setBackground(ContextCompat.getDrawable(viewHolder.actionButton.getContext(), R.drawable.btn_play_bg));
                    viewHolder.actionButton.setLayoutParams(new LinearLayout.LayoutParams
                            (viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_90)
                                    , viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_32)));
                }

                if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.CONTINUE)){
                    viewHolder.actionButtonTextView.setText(matchStatus+"...");
                }else if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.DID_NOT_PLAY)){
                    viewHolder.actionButtonTextView.setText("DNP");
                }else  {
                    viewHolder.actionButtonTextView.setText(matchStatus);
                }

            }

            /* Match parties */
            if (match.getMatchParties() != null) {
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

    private void setTimer(final InPlayTwoPartyMatchItemViewHolder viewHolder, final String matchStartTime) {
        CountDownTimer countDownTimer = new CountDownTimer(TimerHelper.getCountDownFutureTime(matchStartTime), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                viewHolder.dateTimeTextView.setText(TimerHelper.getTimerFormatFromMillis(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                viewHolder.dateTimeTextView.setText("In Progress");
            }
        };
        countDownTimer.start();
    }

    private boolean shouldDisableMatchClickAction(InPlayMatch match) {
        boolean shouldDisable = false;

        String status = match.getMatchStatus();
        if (!TextUtils.isEmpty(status)) {
            String matchStatus = match.getMatchStatus();
            if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.COMING_UP)) {
                shouldDisable = true;
            }
        } else {
            shouldDisable = true;
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

    public class InPlayTwoPartyMatchItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout matchParentLayout;
        TextView dateTimeTextView;
        HmImageView party1ImageView;
        TextView party1NameTextView;
        HmImageView party2ImageView;
        TextView party2NameTextView;
        TextView venueTextView;
        RelativeLayout actionButton;
        TextView actionButtonTextView;
        ImageView actionButtonImageView;

        public InPlayTwoPartyMatchItemViewHolder(View itemView) {
            super(itemView);
            matchParentLayout = (LinearLayout) itemView.findViewById(R.id.inplay_match_item_parent);
            dateTimeTextView = (TextView) itemView.findViewById(R.id.inplay_match_date_time_textView);
            party1ImageView = (HmImageView) itemView.findViewById(R.id.match_party_1_imgView);
            party1NameTextView = (TextView) itemView.findViewById(R.id.match_party_1_textView);
            party2ImageView = (HmImageView) itemView.findViewById(R.id.match_party_2_imgView);
            party2NameTextView = (TextView) itemView.findViewById(R.id.match_party_2_textView);
            venueTextView = (TextView) itemView.findViewById(R.id.inplay_match_venue_textView);
            actionButton = (RelativeLayout) itemView.findViewById(R.id.inplay_match_action_button);
            actionButtonTextView = (TextView) itemView.findViewById(R.id.inplay_match_action_button_tv);
            actionButtonImageView = (ImageView) itemView.findViewById(R.id.inplay_match_action_button_iv);

            matchParentLayout.setOnClickListener(this);
            actionButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                /* Since taking same action on both the clicks */
                case R.id.inplay_match_item_parent:
                case R.id.inplay_match_action_button:
                    onActionButtonClicked(getAdapterPosition());
                    break;
            }
        }
    }
}
