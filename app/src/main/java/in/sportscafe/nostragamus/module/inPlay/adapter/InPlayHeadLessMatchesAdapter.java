package in.sportscafe.nostragamus.module.inPlay.adapter;

import android.graphics.Typeface;
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
import in.sportscafe.nostragamus.module.newChallenges.adapter.NewChallengeMatchAdapterListener;
import in.sportscafe.nostragamus.module.newChallenges.adapter.NewChallengeMatchesAdapter;
import in.sportscafe.nostragamus.module.newChallenges.dto.MatchParty;
import in.sportscafe.nostragamus.module.newChallenges.helpers.DateTimeHelper;
import in.sportscafe.nostragamus.module.nostraHome.helper.TimerHelper;
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

            Typeface latoBold = Typefaces.get(viewHolder.actionButton.getContext(), "fonts/lato/Lato-Bold.ttf");
            Typeface latoRegular = Typefaces.get(viewHolder.actionButton.getContext(), "fonts/lato/Lato-Regular.ttf");

            /* Timer - match start */
            String matchStartTime = match.getMatchStartTime();
            if (!TextUtils.isEmpty(matchStartTime)) {
                if (DateTimeHelper.isTimerRequired(matchStartTime)) {
                    setTimer(viewHolder, matchStartTime);
                } else {
                    viewHolder.dateTimeTextView.setText(getDateTimeValue(matchStartTime));
                }
            }

            viewHolder.venueTextView.setText(match.getMatchStage()+", "+match.getMatchVenue());

            /* Action Button */
            /* Match status */
            String matchStatus = match.getMatchStatus();
            if (!TextUtils.isEmpty(matchStatus)) {

                if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.PLAY)) {
                    viewHolder.actionButton.setClickable(true);
                    viewHolder.actionButtonTextView.setTextColor(ContextCompat.getColor(viewHolder.actionButton.getContext(), R.color.white));
                    viewHolder.actionButtonTextView.setTypeface(latoBold);
                    viewHolder.actionButton.setBackground(ContextCompat.getDrawable(viewHolder.actionButton.getContext(), R.drawable.btn_play_bg));
                    viewHolder.actionButton.setLayoutParams(new LinearLayout.LayoutParams
                            (viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_84)
                                    , viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_32)));

                    viewHolder.actionButtonImageView.setBackgroundResource(R.drawable.right_arrow_play_btn);
                    viewHolder.actionButtonImageView.setVisibility(View.VISIBLE);

                } else if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.COMING_UP)) {
                    viewHolder.actionButton.setClickable(false);
                    viewHolder.actionButtonTextView.setTextColor(ContextCompat.getColor(viewHolder.actionButton.getContext(), R.color.grey_a1a1a1));
                    viewHolder.actionButtonTextView.setTypeface(latoRegular);
                    viewHolder.actionButton.setBackground(ContextCompat.getDrawable(viewHolder.actionButton.getContext(), R.drawable.btn_coming_up_bg));
                    viewHolder.actionButton.setLayoutParams(new LinearLayout.LayoutParams
                            (viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_90)
                                    , viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_32)));

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
                }else {
                    viewHolder.actionButtonTextView.setText(matchStatus);
                }

            }

            if (shouldDisableMatchClickAction(match)) {

                if (match.getQuestionCount() <= 0) {
                    viewHolder.actionButton.setClickable(false);
                    viewHolder.actionButtonTextView.setText("Coming Up");
                    viewHolder.actionButtonTextView.setTypeface(latoRegular);
                    viewHolder.actionButtonImageView.setVisibility(View.GONE);
                    viewHolder.actionButtonTextView.setTextColor(ContextCompat.getColor(viewHolder.actionButton.getContext(), R.color.grey_a1a1a1));
                    viewHolder.actionButton.setBackground(ContextCompat.getDrawable(viewHolder.actionButton.getContext(), R.drawable.btn_coming_up_bg));
                    viewHolder.actionButton.setLayoutParams(new LinearLayout.LayoutParams
                            (viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_100)
                                    , viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_34)));

                    viewHolder.actionButtonImageView.setBackgroundResource(R.drawable.play_lock_icon);
                    viewHolder.actionButtonImageView.getLayoutParams().height =
                            viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_12);
                    viewHolder.actionButtonImageView.getLayoutParams().width =
                            viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_12);
                    viewHolder.actionButtonImageView.setVisibility(View.VISIBLE);

                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(viewHolder.actionButtonImageView.getLayoutParams());
                    lp.setMargins(viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_7),
                            viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_6_5), 0, 0);
                    lp.addRule(RelativeLayout.RIGHT_OF, R.id.newChallenge_match_action_button_tv);
                    viewHolder.actionButtonImageView.setLayoutParams(lp);

                } else {
                    viewHolder.actionButton.setClickable(false);
                    viewHolder.actionButtonTextView.setText("Play");
                    viewHolder.actionButtonTextView.setTypeface(latoRegular);
                    viewHolder.actionButtonTextView.setTextColor(ContextCompat.getColor(viewHolder.actionButton.getContext(), R.color.grey_a1a1a1));
                    viewHolder.actionButton.setBackground(ContextCompat.getDrawable(viewHolder.actionButton.getContext(), R.drawable.btn_play_locked_bg));
                    viewHolder.actionButton.setLayoutParams(new LinearLayout.LayoutParams
                            (viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_84)
                                    , viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_34)));
                    viewHolder.actionButton.setPadding(
                            viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_2),
                            0, 0, viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_2));

                    viewHolder.actionButtonImageView.setBackgroundResource(R.drawable.play_lock_icon);
                    viewHolder.actionButtonImageView.getLayoutParams().height =
                            viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_12);
                    viewHolder.actionButtonImageView.getLayoutParams().width =
                            viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_12);
                    viewHolder.actionButtonImageView.setVisibility(View.VISIBLE);

                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(viewHolder.actionButtonImageView.getLayoutParams());
                    lp.setMargins(viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_7),
                            viewHolder.actionButton.getResources().getDimensionPixelSize(R.dimen.dim_6_5), 0, 0);
                    lp.addRule(RelativeLayout.RIGHT_OF, R.id.newChallenge_match_action_button_tv);
                    viewHolder.actionButtonImageView.setLayoutParams(lp);
                }

            }

            /*  Parties */
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
        boolean shouldDisable = false;

        String status = match.getMatchStatus();
        if (!TextUtils.isEmpty(status)) {
            if (status.equalsIgnoreCase(Constants.MatchStatusStrings.COMING_UP) ||
                    status.equalsIgnoreCase(Constants.MatchStatusStrings.DID_NOT_PLAY) ||
                    status.equalsIgnoreCase(Constants.MatchStatusStrings.POINTS)) {
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
        RelativeLayout actionButton;
        TextView actionButtonTextView;
        ImageView actionButtonImageView;
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
            actionButton = (RelativeLayout) itemView.findViewById(R.id.newChallenge_match_action_button);
            actionButtonTextView = (TextView) itemView.findViewById(R.id.newChallenge_match_action_button_tv);
            actionButtonImageView = (ImageView) itemView.findViewById(R.id.newChallenge_match_action_button_iv);
            vsBtn = (Button) itemView.findViewById(R.id.newChallenge_match_vs_btn);

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
