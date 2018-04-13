package in.sportscafe.nostragamus.module.privateContest.adapter;

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
import in.sportscafe.nostragamus.module.inPlay.adapter.MatchesAdapterAction;
import in.sportscafe.nostragamus.module.inPlay.adapter.MatchesAdapterItemType;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatch;
import in.sportscafe.nostragamus.module.newChallenges.adapter.NewChallengeMatchAdapterListener;
import in.sportscafe.nostragamus.module.newChallenges.dto.MatchParty;
import in.sportscafe.nostragamus.module.newChallenges.helpers.DateTimeHelper;
import in.sportscafe.nostragamus.module.nostraHome.helper.TimerHelper;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by sc on 28/3/18.
 */

public class PrivateContestMatchesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private NewChallengeMatchAdapterListener mMatchAdapterListener;
    private List<InPlayMatch> mInPlayMatchList;

    public PrivateContestMatchesRecyclerAdapter(@NonNull List<InPlayMatch> matches,
                                      @NonNull NewChallengeMatchAdapterListener listener) {
        mInPlayMatchList = matches;
        mMatchAdapterListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = MatchesAdapterItemType.TWO_PARTY_MATCH;

        // Change view Type if single party adapter is
        if (mInPlayMatchList != null && mInPlayMatchList.size() > position && mInPlayMatchList.get(position) != null) {
            if (mInPlayMatchList.get(position).getMatchType().equalsIgnoreCase("topic")) {
                viewType = MatchesAdapterItemType.SINGLE_PARTY_MATCH;
            }
        }

        return viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case MatchesAdapterItemType.TWO_PARTY_MATCH:
                View v1 = inflater.inflate(R.layout.new_challenge_match_two_party_item, parent, false);
                viewHolder = new PrivateContestTwoPartyMatchItemViewHolder(v1);
                break;

            case MatchesAdapterItemType.SINGLE_PARTY_MATCH:
                View v2 = inflater.inflate(R.layout.new_challenge_match_one_party_item, parent, false);
                viewHolder = new PrivateContestOnePartyMatchItemViewHolder(v2);
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

                case MatchesAdapterItemType.SINGLE_PARTY_MATCH:
                    bindSinglePartyMatchData(holder, position);
                    break;
            }
        }
    }

    private void bindTwoPartyMatchData(RecyclerView.ViewHolder holder, int position) {
        PrivateContestTwoPartyMatchItemViewHolder viewHolder = (PrivateContestTwoPartyMatchItemViewHolder) holder;

        if (mInPlayMatchList != null && mInPlayMatchList.size() > position) {
            InPlayMatch match = mInPlayMatchList.get(position);

            viewHolder.matchParentLayout.setEnabled(true);
            viewHolder.actionButton.setVisibility(View.VISIBLE);

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

            viewHolder.venueTextView.setText(match.getMatchStage() + ", " + match.getMatchVenue());

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

    private void setTimer(final PrivateContestTwoPartyMatchItemViewHolder viewHolder, final String matchStartTime) {
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

        // NOTE: Disable every action

        /*String status = match.getMatchStatus();
        if (!TextUtils.isEmpty(status)) {
            if (status.equalsIgnoreCase(Constants.MatchStatusStrings.PLAY)) {
                shouldDisable = false;
            }
        }*/

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

    private void bindSinglePartyMatchData(RecyclerView.ViewHolder holder, int position) {

        PrivateContestOnePartyMatchItemViewHolder onePartyViewHolder = (PrivateContestOnePartyMatchItemViewHolder) holder;

        if (mInPlayMatchList != null && mInPlayMatchList.size() > position) {
            InPlayMatch match = mInPlayMatchList.get(position);

            onePartyViewHolder.matchParentOPLayout.setEnabled(true);
            onePartyViewHolder.actionButtonOP.setVisibility(View.VISIBLE);

            Typeface latoBold = Typefaces.get(onePartyViewHolder.actionButtonOP.getContext(), "fonts/lato/Lato-Bold.ttf");
            Typeface latoRegular = Typefaces.get(onePartyViewHolder.actionButtonOP.getContext(), "fonts/lato/Lato-Regular.ttf");

            /* Timer - match start */
            String matchStartTime = match.getMatchStartTime();
            if (!TextUtils.isEmpty(matchStartTime)) {
                if (DateTimeHelper.isTimerRequired(matchStartTime)) {
                    setOnePartyTimer(onePartyViewHolder, matchStartTime);
                } else {
                    onePartyViewHolder.dateTimeOPTextView.setText(getDateTimeValue(matchStartTime));
                }
            }

            onePartyViewHolder.venueOPTextView.setText(match.getMatchStage());
            onePartyViewHolder.resultOPTextView.setText(match.getMatchVenue());

            /* Match status */
            String matchStatus = match.getMatchStatus();
            if (!TextUtils.isEmpty(matchStatus)) {

                if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.PLAY)) {
                    onePartyViewHolder.actionButtonOP.setClickable(true);
                    onePartyViewHolder.actionButtonOPTextView.setTextColor(ContextCompat.getColor(onePartyViewHolder.actionButtonOP.getContext(), R.color.white));
                    onePartyViewHolder.actionButtonOPTextView.setTypeface(latoBold);
                    onePartyViewHolder.actionButtonOP.setBackground(ContextCompat.getDrawable(onePartyViewHolder.actionButtonOP.getContext(), R.drawable.btn_play_bg));
                    onePartyViewHolder.actionButtonOP.setLayoutParams(new LinearLayout.LayoutParams
                            (onePartyViewHolder.actionButtonOP.getResources().getDimensionPixelSize(R.dimen.dim_84)
                                    , onePartyViewHolder.actionButtonOP.getResources().getDimensionPixelSize(R.dimen.dim_32)));

                    onePartyViewHolder.actionButtonOPImageView.setBackgroundResource(R.drawable.right_arrow_play_btn);
                    onePartyViewHolder.actionButtonOPImageView.setVisibility(View.VISIBLE);

                } else if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.COMING_UP)) {
                    onePartyViewHolder.actionButtonOP.setClickable(false);
                    onePartyViewHolder.actionButtonOPTextView.setTextColor(ContextCompat.getColor(onePartyViewHolder.actionButtonOP.getContext(), R.color.grey_a1a1a1));
                    onePartyViewHolder.actionButtonOPTextView.setTypeface(latoRegular);
                    onePartyViewHolder.actionButtonOP.setBackground(ContextCompat.getDrawable(onePartyViewHolder.actionButtonOP.getContext(), R.drawable.btn_coming_up_bg));
                    onePartyViewHolder.actionButtonOP.setLayoutParams(new LinearLayout.LayoutParams
                            (onePartyViewHolder.actionButtonOP.getResources().getDimensionPixelSize(R.dimen.dim_90)
                                    , onePartyViewHolder.actionButtonOP.getResources().getDimensionPixelSize(R.dimen.dim_32)));

                } else if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.ANSWER)) {
                    onePartyViewHolder.actionButtonOP.setClickable(true);
                    onePartyViewHolder.actionButtonOPTextView.setTextColor(ContextCompat.getColor(onePartyViewHolder.actionButtonOP.getContext(), R.color.white));
                    onePartyViewHolder.actionButtonOPTextView.setTypeface(latoBold);
                    onePartyViewHolder.actionButtonOP.setBackground(ContextCompat.getDrawable(onePartyViewHolder.actionButtonOP.getContext(), R.drawable.btn_answer_bg));
                    onePartyViewHolder.actionButtonOP.setLayoutParams(new LinearLayout.LayoutParams
                            (onePartyViewHolder.actionButtonOP.getResources().getDimensionPixelSize(R.dimen.dim_84)
                                    , onePartyViewHolder.actionButtonOP.getResources().getDimensionPixelSize(R.dimen.dim_32)));

                } else if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.CONTINUE)) {
                    onePartyViewHolder.actionButtonOP.setClickable(true);
                    onePartyViewHolder.actionButtonOPTextView.setTextColor(ContextCompat.getColor(onePartyViewHolder.actionButtonOP.getContext(), R.color.white));
                    onePartyViewHolder.actionButtonOPTextView.setTypeface(latoBold);
                    onePartyViewHolder.actionButtonOP.setBackground(ContextCompat.getDrawable(onePartyViewHolder.actionButtonOP.getContext(), R.drawable.btn_continue_bg));
                    onePartyViewHolder.actionButtonOP.setLayoutParams(new LinearLayout.LayoutParams
                            (onePartyViewHolder.actionButtonOP.getResources().getDimensionPixelSize(R.dimen.dim_84)
                                    , onePartyViewHolder.actionButtonOP.getResources().getDimensionPixelSize(R.dimen.dim_32)));

                } else if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.CANCELLED)) {
                    onePartyViewHolder.actionButtonOP.setClickable(false);
                    onePartyViewHolder.actionButtonOPTextView.setTextColor(ContextCompat.getColor(onePartyViewHolder.actionButtonOP.getContext(), R.color.black));
                    onePartyViewHolder.actionButtonOPTextView.setTypeface(latoRegular);
                    onePartyViewHolder.actionButtonOP.setBackground(ContextCompat.getDrawable(onePartyViewHolder.actionButtonOP.getContext(), R.drawable.btn_did_not_play_bg));
                    onePartyViewHolder.actionButtonOP.setLayoutParams(new LinearLayout.LayoutParams
                            (onePartyViewHolder.actionButtonOP.getResources().getDimensionPixelSize(R.dimen.dim_100)
                                    , onePartyViewHolder.actionButtonOP.getResources().getDimensionPixelSize(R.dimen.dim_32)));

                } else {
                    onePartyViewHolder.actionButtonOP.setClickable(true);
                    onePartyViewHolder.actionButtonOPTextView.setTextColor(ContextCompat.getColor(onePartyViewHolder.actionButtonOP.getContext(), R.color.white));
                    onePartyViewHolder.actionButtonOPTextView.setTypeface(latoRegular);
                    onePartyViewHolder.actionButtonOP.setBackground(ContextCompat.getDrawable(onePartyViewHolder.actionButtonOP.getContext(), R.drawable.btn_play_bg));
                    onePartyViewHolder.actionButtonOP.setLayoutParams(new LinearLayout.LayoutParams
                            (onePartyViewHolder.actionButtonOP.getResources().getDimensionPixelSize(R.dimen.dim_90)
                                    , onePartyViewHolder.actionButtonOP.getResources().getDimensionPixelSize(R.dimen.dim_32)));
                }

                if (matchStatus.equalsIgnoreCase(Constants.MatchStatusStrings.CONTINUE)){
                    onePartyViewHolder.actionButtonOPTextView.setText(matchStatus+"...");
                }else {
                    onePartyViewHolder.actionButtonOPTextView.setText(matchStatus);
                }


            }

            if (shouldDisableMatchClickAction(match)) {

                if (match.getQuestionCount() <= 0) {
                    onePartyViewHolder.actionButtonOP.setClickable(false);
                    onePartyViewHolder.actionButtonOPTextView.setText("Coming Up");
                    onePartyViewHolder.actionButtonOPTextView.setTypeface(latoRegular);
                    onePartyViewHolder.actionButtonOPImageView.setVisibility(View.GONE);
                    onePartyViewHolder.actionButtonOPTextView.setTextColor(ContextCompat.getColor(onePartyViewHolder.actionButtonOP.getContext(), R.color.grey_a1a1a1));
                    onePartyViewHolder.actionButtonOP.setBackground(ContextCompat.getDrawable(onePartyViewHolder.actionButtonOP.getContext(), R.drawable.btn_coming_up_bg));
                    onePartyViewHolder.actionButtonOP.setLayoutParams(new LinearLayout.LayoutParams
                            (onePartyViewHolder.actionButtonOP.getResources().getDimensionPixelSize(R.dimen.dim_100)
                                    , onePartyViewHolder.actionButtonOP.getResources().getDimensionPixelSize(R.dimen.dim_34)));

                    onePartyViewHolder.actionButtonOPImageView.setBackgroundResource(R.drawable.play_lock_icon);
                    onePartyViewHolder.actionButtonOPImageView.getLayoutParams().height =
                            onePartyViewHolder.actionButtonOP.getResources().getDimensionPixelSize(R.dimen.dim_12);
                    onePartyViewHolder.actionButtonOPImageView.getLayoutParams().width =
                            onePartyViewHolder.actionButtonOP.getResources().getDimensionPixelSize(R.dimen.dim_12);
                    onePartyViewHolder.actionButtonOPImageView.setVisibility(View.VISIBLE);

                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(onePartyViewHolder.actionButtonOPImageView.getLayoutParams());
                    lp.setMargins(onePartyViewHolder.actionButtonOP.getResources().getDimensionPixelSize(R.dimen.dim_7),
                            onePartyViewHolder.actionButtonOP.getResources().getDimensionPixelSize(R.dimen.dim_6_5), 0, 0);
                    lp.addRule(RelativeLayout.RIGHT_OF, R.id.newChallenge_one_party_match_action_button_tv);
                    onePartyViewHolder.actionButtonOPImageView.setLayoutParams(lp);

                } else {
                    onePartyViewHolder.actionButtonOP.setClickable(false);
                    onePartyViewHolder.actionButtonOPTextView.setText("Play");
                    onePartyViewHolder.actionButtonOPTextView.setTypeface(latoRegular);
                    onePartyViewHolder.actionButtonOPTextView.setTextColor(ContextCompat.getColor(onePartyViewHolder.actionButtonOP.getContext(), R.color.grey_a1a1a1));
                    onePartyViewHolder.actionButtonOP.setBackground(ContextCompat.getDrawable(onePartyViewHolder.actionButtonOP.getContext(), R.drawable.btn_play_locked_bg));
                    onePartyViewHolder.actionButtonOP.setLayoutParams(new LinearLayout.LayoutParams
                            (onePartyViewHolder.actionButtonOP.getResources().getDimensionPixelSize(R.dimen.dim_84)
                                    , onePartyViewHolder.actionButtonOP.getResources().getDimensionPixelSize(R.dimen.dim_34)));
                    onePartyViewHolder.actionButtonOP.setPadding(
                            onePartyViewHolder.actionButtonOP.getResources().getDimensionPixelSize(R.dimen.dim_2),
                            0, 0, onePartyViewHolder.actionButtonOP.getResources().getDimensionPixelSize(R.dimen.dim_2));

                    onePartyViewHolder.actionButtonOPImageView.setBackgroundResource(R.drawable.play_lock_icon);
                    onePartyViewHolder.actionButtonOPImageView.getLayoutParams().height =
                            onePartyViewHolder.actionButtonOP.getResources().getDimensionPixelSize(R.dimen.dim_12);
                    onePartyViewHolder.actionButtonOPImageView.getLayoutParams().width =
                            onePartyViewHolder.actionButtonOP.getResources().getDimensionPixelSize(R.dimen.dim_12);
                    onePartyViewHolder.actionButtonOPImageView.setVisibility(View.VISIBLE);

                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(onePartyViewHolder.actionButtonOPImageView.getLayoutParams());
                    lp.setMargins(onePartyViewHolder.actionButtonOP.getResources().getDimensionPixelSize(R.dimen.dim_7),
                            onePartyViewHolder.actionButtonOP.getResources().getDimensionPixelSize(R.dimen.dim_6_5), 0, 0);
                    lp.addRule(RelativeLayout.RIGHT_OF, R.id.newChallenge_one_party_match_action_button_tv);
                    onePartyViewHolder.actionButtonOPImageView.setLayoutParams(lp);
                }

            }

             /* one Party Match */
            if (match.getTopics() != null) {
                onePartyViewHolder.party1OPImageView.setImageUrl(match.getTopics().getTopicUrl());
                onePartyViewHolder.party1NameOPTextView.setText(match.getTopics().getTopicName());
            }
        }

    }

    private void setOnePartyTimer(final PrivateContestOnePartyMatchItemViewHolder viewHolder, final String matchStartTime) {
        CountDownTimer countDownTimer = new CountDownTimer(TimerHelper.getCountDownFutureTime(matchStartTime), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                viewHolder.dateTimeOPTextView.setText(TimerHelper.getTimerFormatFromMillis(millisUntilFinished));
            }

            @Override
            public void onFinish() {
            }
        };
        countDownTimer.start();
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

    public class PrivateContestTwoPartyMatchItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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

        public PrivateContestTwoPartyMatchItemViewHolder(View itemView) {
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


    public class PrivateContestOnePartyMatchItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout matchParentOPLayout;
        TextView dateTimeOPTextView;
        HmImageView party1OPImageView;
        TextView party1NameOPTextView;
        TextView venueOPTextView;
        RelativeLayout actionButtonOP;
        TextView actionButtonOPTextView;
        ImageView actionButtonOPImageView;
        TextView resultOPTextView;
        LinearLayout resultOPLayout;

        public PrivateContestOnePartyMatchItemViewHolder(View itemView) {
            super(itemView);
            matchParentOPLayout = (LinearLayout) itemView.findViewById(R.id.newChallenge_one_party_match_item_parent);
            dateTimeOPTextView = (TextView) itemView.findViewById(R.id.newChallenge_one_party_match_date_time_textView);
            party1OPImageView = (HmImageView) itemView.findViewById(R.id.match_one_party_1_imgView);
            party1NameOPTextView = (TextView) itemView.findViewById(R.id.match_one_party_1_textView);
            venueOPTextView = (TextView) itemView.findViewById(R.id.newChallenge_one_party_match_venue_textView);
            actionButtonOP = (RelativeLayout) itemView.findViewById(R.id.newChallenge_one_party_match_action_button);
            actionButtonOPTextView = (TextView) itemView.findViewById(R.id.newChallenge_one_party_match_action_button_tv);
            actionButtonOPImageView = (ImageView) itemView.findViewById(R.id.newChallenge_one_party_match_action_button_iv);
            resultOPTextView = (TextView) itemView.findViewById(R.id.newChallenge_one_party_match_result_textView);
            resultOPLayout = (LinearLayout) itemView.findViewById(R.id.newChallenge_one_party_match_result_layout);

            matchParentOPLayout.setOnClickListener(this);
            actionButtonOP.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                /* Since taking same action on both the clicks */
                case R.id.newChallenge_one_party_match_item_parent:
                case R.id.newChallenge_one_party_match_action_button:
                    onActionButtonClicked(getAdapterPosition());
                    break;
            }
        }
    }
}
