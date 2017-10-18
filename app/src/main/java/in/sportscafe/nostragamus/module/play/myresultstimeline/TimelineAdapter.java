package in.sportscafe.nostragamus.module.play.myresultstimeline;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.ShadowLayout;
import com.jeeva.android.widgets.customfont.CustomButton;
import com.jeeva.android.widgets.customfont.CustomTextView;
import com.jeeva.android.widgets.customfont.Typefaces;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AnalyticsActions;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.DateFormats;
import in.sportscafe.nostragamus.Constants.GameAttemptedStatus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.common.dto.TournamentPowerupInfo;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestDto;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatch;
import in.sportscafe.nostragamus.module.inPlay.ui.ResultsScreenDataDto;
import in.sportscafe.nostragamus.module.play.myresults.MyResultsActivity;
import in.sportscafe.nostragamus.module.prediction.playScreen.PredictionActivity;
import in.sportscafe.nostragamus.module.resultspeek.FeedWebView;
import in.sportscafe.nostragamus.module.resultspeek.ResultsPeekActivity;
import in.sportscafe.nostragamus.module.resultspeek.dto.Match;
import in.sportscafe.nostragamus.module.resultspeek.dto.Parties;
import in.sportscafe.nostragamus.utils.ViewUtils;
import in.sportscafe.nostragamus.utils.timeutils.TimeAgo;
import in.sportscafe.nostragamus.utils.timeutils.TimeUnit;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by Jeeva on 15/6/16.
 */
public class TimelineAdapter extends Adapter<Match, TimelineAdapter.ViewHolder> {

    private static final long ONE_DAY_IN_MS = 24 * 60 * 60 * 1000;

    private static final String COMMENTARY = "commentary";

    private List<Match> mMyResultList = new ArrayList<>();

    private TimerRunnable mTimerRunnable;

    private int mAnsweredQuestionCount = 0;

    private Integer mPlayerId;

    private String mPlayerName;

    private String mPlayerPhoto;

    private int mRoomId = 0;

    private TournamentPowerupInfo mPowerupInfo;

    public TimelineAdapter(Context context) {
        super(context);
        mTimerRunnable = new TimerRunnable();
    }

    public TimelineAdapter(Context context, int roomId, Integer playerId, String playerName, String playerPhoto) {
        this(context);
        this.mPlayerId = playerId;
        this.mPlayerName = playerName;
        this.mPlayerPhoto = playerPhoto;
        this.mRoomId = roomId;
    }

    public TimelineAdapter(Context context, TournamentPowerupInfo powerupInfo) {
        this(context);
        mPowerupInfo = powerupInfo;
    }

    @Override
    public Match getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayoutInflater().inflate(R.layout.inflater_feed_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Match match = getItem(position);

        ScheduleViewHolder scheduleVH;
        if (holder.mLlTourParent.getChildCount() > 0) {
            View mainView = holder.mLlTourParent.getChildAt(1);

            scheduleVH = mScheduleMap.get(mainView);
            if (null != scheduleVH) {
                Log.d("TimelineAdapter", scheduleVH.mTvMatchStage.getText().toString());
                mScheduleVHList.remove(scheduleVH);
                mScheduleMap.remove(mainView);
            }
        }

        holder.mLlTourParent.removeAllViews();

        if (position == 0) {
            holder.mLlTourParent.addView(getLayoutInflater().inflate(R.layout.inflater_timeline_top_dot,
                    holder.mLlTourParent, false));
        }

        scheduleVH = getScheduleView(match, holder.mLlTourParent);
        holder.mLlTourParent.addView(scheduleVH.mMainView);

        if (View.VISIBLE == scheduleVH.mTvExpiresIn.getVisibility()) {
            mScheduleVHList.add(scheduleVH);
            mScheduleMap.put(scheduleVH.mMainView, scheduleVH);
        }
    }

    private ScheduleViewHolder getScheduleView(Match match, ViewGroup parent) {

        /*holder.mTvDate.setText(Html.fromHtml(
                TimeUtils.getDateStringFromMs(startTimeMs, "MMM").toUpperCase() + "<br>" + String.valueOf(dayOfMonth).toUpperCase()
                        + "<sup>" +AppSnippet.ordinalOnly(dayOfMonth) + "</sup>")
        );*/

        ScheduleViewHolder holder = new ScheduleViewHolder(getLayoutInflater().inflate(R.layout.inflater_schedule_row, parent, false));

        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams) holder.mTvVs.getLayoutParams();

        String startTime = match.getStartTime();
//        String startTime = "2017-01-27T18:00:00.000Z";
        long startTimeMs = TimeUtils.getMillisecondsFromDateString(
                startTime,
                DateFormats.FORMAT_DATE_T_TIME_ZONE,
                DateFormats.GMT
        );

        int dayOfMonth = Integer.parseInt(TimeUtils.getDateStringFromMs(startTimeMs, "d"));
        // Setting date of the Match
        holder.mTvDate.setText(
                TimeUtils.getDateStringFromMs(startTimeMs, "MMM") + "\n"
                        + dayOfMonth + AppSnippet.ordinalOnly(dayOfMonth)
        );

        String matchStage = match.getStage();
        if (COMMENTARY.equalsIgnoreCase(matchStage)) {
            holder.mLlCardLayout.setVisibility(View.GONE);
            holder.mLlMatchCommentaryParent.setVisibility(View.VISIBLE);
            holder.mLlMatchCommentaryParent.addView(getCommentary(holder.mLlMatchCommentaryParent, match));
        } else {

            // Setting the challenge name, if it is not empty
            if (!TextUtils.isEmpty(match.getChallengeName())) {
                holder.mTvChallengeName.setVisibility(View.VISIBLE);
                holder.mTvChallengeName.setText(match.getChallengeName());
            }

            // Setting Match stage, if the stage is not empty & not the "commentary"
            if (!TextUtils.isEmpty(matchStage)) {
                holder.mTvMatchStage.setVisibility(View.VISIBLE);
                holder.mTvMatchStage.setText(match.getStage());
            }

            // Setting starting time of the Match
            holder.mTvStartTime.setVisibility(View.VISIBLE);
            holder.mTvStartTime.setText(TimeUtils.getDateStringFromMs(startTimeMs, DateFormats.HH_MM_AA));

            // Setting party details
            List<Parties> parties = new ArrayList<>();

            // Setting party details : if one party view then set topics for one party Match
            if (match.getParties() == null) {
                holder.mTvPartyAName.setText(match.getTopics().getTopicName());
                holder.mIvPartyAPhoto.setImageUrl(match.getTopics().getTopicUrl());
                holder.mTvVs.setVisibility(View.GONE);
                holder.mLlRightPartyLayout.setVisibility(View.GONE);

                RelativeLayout.LayoutParams paramsFour = (RelativeLayout.LayoutParams) holder.mLlLeftPartyLayout.getLayoutParams();
                paramsFour.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
                paramsFour.addRule(RelativeLayout.CENTER_IN_PARENT);
                holder.mLlLeftPartyLayout.setLayoutParams(paramsFour);

            } else {
                parties = match.getParties();
                holder.mLlRightPartyLayout.setVisibility(View.VISIBLE);
                holder.mTvPartyAName.setText(parties.get(0).getPartyName());
                holder.mTvPartyBName.setText(parties.get(1).getPartyName());
                holder.mIvPartyAPhoto.setImageUrl(parties.get(0).getPartyImageUrl());
                holder.mIvPartyBPhoto.setImageUrl(parties.get(1).getPartyImageUrl());
                holder.mTvVs.setVisibility(View.VISIBLE);

                RelativeLayout.LayoutParams paramsFour = (RelativeLayout.LayoutParams) holder.mLlLeftPartyLayout.getLayoutParams();
                paramsFour.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                paramsFour.addRule(RelativeLayout.LEFT_OF, R.id.schedule_row_btn_vs);
                holder.mLlLeftPartyLayout.setLayoutParams(paramsFour);
            }


            TimeAgo timeAgo = TimeUtils.calcTimeAgo(Calendar.getInstance().getTimeInMillis(), startTimeMs);
            boolean isMatchStarted = timeAgo.timeDiff <= 0
                    || timeAgo.timeUnit == TimeUnit.MILLISECOND
                    || timeAgo.timeUnit == TimeUnit.SECOND;

            Integer attemptedStatus = match.getisAttempted();

            if (match.getMatchQuestionCount() > 0) {

                if (match.isResultPublished()) { // if Match Result Published

                    //if Match Completely Attempted then IsAttempted = 2 else if Partially Attempted then is Attempted =1
                    //show Match Results
                    if (GameAttemptedStatus.COMPLETELY == attemptedStatus || GameAttemptedStatus.PARTIALLY == attemptedStatus) {

                        holder.mRlMatchPoints.setVisibility(View.VISIBLE);
                        holder.mVResultLine.setVisibility(View.VISIBLE);

                        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                        holder.mTvVs.setLayoutParams(layoutParams);

                        holder.mTvMatchResult.setVisibility(View.VISIBLE);
                        holder.mTvMatchResult.setText(Html.fromHtml(match.getResult()));
                        holder.mRlMatchPoints.setTag(match);
                        holder.mBtnMatchPoints.setText(match.getMatchPoints() + " Points");
                        holder.mTvResultCorrectCount.setText(/*"You got " + */match.getCorrectCount() + "/" + match.getMatchQuestionCount() + " Correct Predictions");

                        Integer winnerPartyId = match.getWinnerPartyId();
                        if (null != winnerPartyId) {
                            int whiteSixty = ViewUtils.getColor(holder.mTvPartyAName.getContext(), R.color.white_60);
                            Typeface latoBold = Typefaces.get(holder.mTvPartyAName.getContext(), "fonts/lato/Lato-Bold.ttf");
                            if (winnerPartyId.equals(parties.get(0).getPartyId())) {
                                holder.mTvPartyAName.setTypeface(latoBold);
                                holder.mTvPartyBName.setTextColor(whiteSixty);
                            } else if (winnerPartyId.equals(parties.get(1).getPartyId())) {
                                holder.mTvPartyBName.setTypeface(latoBold);
                                holder.mTvPartyAName.setTextColor(whiteSixty);
                            }
                        }
                    }

                    //if Match not Attempted then IsAttempted=0
                    if (GameAttemptedStatus.NOT == attemptedStatus) {
                        // Show Opportunity missed at scoring!
                        holder.mTvMatchResult.setVisibility(View.VISIBLE);
                        holder.mTvMatchResult.setText(Html.fromHtml(match.getResult()));
                        holder.mVResultLine.setVisibility(View.VISIBLE);
                        holder.mTvInfo.setVisibility(View.VISIBLE);
                        holder.mTvInfo.setText("Opportunity missed at scoring!");

                        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                        holder.mTvVs.setLayoutParams(layoutParams);
                    }

                } else { // if Results not published
                    if (GameAttemptedStatus.NOT == attemptedStatus || GameAttemptedStatus.PARTIALLY == attemptedStatus) {
                        if (isMatchStarted) {
                            if (attemptedStatus == GameAttemptedStatus.PARTIALLY) {

                                //  Waiting for results
                                holder.mVResultLine.setVisibility(View.VISIBLE);
                                holder.mTvMatchResult.setVisibility(View.VISIBLE);
                                holder.mTvMatchResult.setText("Awaiting Results");
                                holder.mTvMatchResult.setTextColor(ContextCompat.getColor(holder.mTvDate.getContext(), R.color.white_60));
                                holder.mQuestionsAnswered.setText(match.getNoOfQuestionsAnswered() + "/" + match.getMatchQuestionCount() + " Predictions Made");
                                holder.mLlResultWait.setVisibility(View.VISIBLE);
                                holder.mLlResultWait.setTag(match);
                            } else {

                                // You cannot play the Match as the Match already started
                                holder.mVResultLine.setVisibility(View.VISIBLE);
                                holder.mTvInfo.setVisibility(View.VISIBLE);
                                holder.mTvInfo.setText("Opportunity missed at scoring!");

                                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                                holder.mTvVs.setLayoutParams(layoutParams);
                            }
                        } else {

                            // show Play button
                            holder.mBtnPlayMatch.setVisibility(View.GONE);
                            holder.mBtnPlayMatch.setTag(match);

                            if (GameAttemptedStatus.PARTIALLY == attemptedStatus) {
                                holder.mBtnPlayMatch.setAllCaps(false);
                                holder.mBtnPlayMatch.setText(("Continue"));
                            }
//                            if (timeAgo.totalDiff < ONE_DAY_IN_MS) {
//                                holder.mTvExpiresIn.setVisibility(View.VISIBLE);
//                                holder.mTvExpiresIn.setTag(timeAgo.totalDiff);
//                            }
                        }
                    } else if (attemptedStatus == GameAttemptedStatus.COMPLETELY) {
                        //  Waiting for results
                        holder.mVResultLine.setVisibility(View.VISIBLE);
                        holder.mTvMatchResult.setVisibility(View.VISIBLE);
                        holder.mTvMatchResult.setText("Awaiting Results");
                        holder.mTvMatchResult.setTextColor(ContextCompat.getColor(holder.mTvDate.getContext(), R.color.white_60));
                        holder.mQuestionsAnswered.setText(match.getNoOfQuestionsAnswered() + "/" + match.getMatchQuestionCount() + " Predictions Made");
                        holder.mLlResultWait.setVisibility(View.VISIBLE);
                        holder.mLlResultWait.setTag(match);
                    }

                }
            } else { // No questions prepared
                if (!isMatchStarted) { // Still the question is not prepared for these matches
                    holder.mVResultLine.setVisibility(View.VISIBLE);
                    holder.mTvInfo.setVisibility(View.VISIBLE);
                    holder.mTvInfo.setText("Games coming up!");

                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    holder.mTvVs.setLayoutParams(layoutParams);
                }
            }
        }

        return holder;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        int mPosition;

        private LinearLayout mLlTourParent;

        public ViewHolder(View V) {
            super(V);
            mLlTourParent = (LinearLayout) V.findViewById(R.id.feed_row_ll_tour_parent);
        }
    }

    class TourViewHolder extends RecyclerView.ViewHolder {

        TextView mTvTournamentName;

        LinearLayout mLlScheduleParent;

        public TourViewHolder(View V) {
            super(V);

            mTvTournamentName = (TextView) V.findViewById(R.id.tour_row_tv_tour_name);
            mLlScheduleParent = (LinearLayout) V.findViewById(R.id.tour_row_ll_schedule_parent);
        }


    }

    class ScheduleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View mMainView;

        ShadowLayout mLlCardLayout;

        TextView mTvChallengeName;

        TextView mTvMatchStage;

        TextView mTvStartTime;

        CustomTextView mTvExpiresIn;

        TextView mTvPartyAName;

        TextView mTvPartyAScore;

        TextView mTvPartyBName;

        TextView mTvPartyBScore;

        TextView mTvMatchResult;

        TextView mTvResultCorrectCount;

        TextView mTvInfo;

        RelativeLayout mLlResultWait;

        HmImageView mIvPartyAPhoto;

        HmImageView mIvPartyBPhoto;

        CustomButton mBtnMatchPoints;

        CustomButton mBtnPlayMatch;

        View mVResultLine;

        LinearLayout mLlMatchCommentaryParent;

        RelativeLayout mRlMatchPoints;

        TextView mTvDate;

        TextView mTvVs;

        TextView mQuestionsAnswered;

        LinearLayout mLlRightPartyLayout;

        LinearLayout mLlLeftPartyLayout;

        public ScheduleViewHolder(View V) {
            super(V);

            mMainView = V;
            mTvDate = (TextView) V.findViewById(R.id.schedule_row_tv_date);
            mTvChallengeName = (TextView) V.findViewById(R.id.schedule_row_tv_challenge_name);
            mTvMatchStage = (TextView) V.findViewById(R.id.schedule_row_tv_match_stage);
            mTvStartTime = (TextView) V.findViewById(R.id.schedule_row_tv_match_time);
            mTvExpiresIn = (CustomTextView) V.findViewById(R.id.schedule_row_tv_expires_in);
            mTvPartyAName = (TextView) V.findViewById(R.id.schedule_row_tv_party_a_name);
            mTvPartyBName = (TextView) V.findViewById(R.id.schedule_row_tv_party_b_name);
            mIvPartyAPhoto = (HmImageView) V.findViewById(R.id.swipe_card_iv_left);
            mIvPartyBPhoto = (HmImageView) V.findViewById(R.id.swipe_card_iv_right);
            mTvMatchResult = (TextView) V.findViewById(R.id.schedule_row_tv_match_result);
            mTvResultCorrectCount = (TextView) V.findViewById(R.id.schedule_row_tv_match_correct_questions);
            mTvInfo = (TextView) V.findViewById(R.id.schedule_row_tv_info);
            mLlResultWait = (RelativeLayout) V.findViewById(R.id.schedule_row_ll_waiting_for_result);
            mBtnPlayMatch = (CustomButton) V.findViewById(R.id.schedule_row_btn_playmatch);
            mBtnMatchPoints = (CustomButton) V.findViewById(R.id.schedule_row_btn_points);
            mTvVs = (TextView) V.findViewById(R.id.schedule_row_btn_vs);
            mQuestionsAnswered = (TextView) V.findViewById(R.id.schedule_row_ll_questions_answered);


            mLlCardLayout = (ShadowLayout) V.findViewById(R.id.schedule_row_ll);
            mVResultLine = V.findViewById(R.id.schedule_row_v_result_line);
            mLlMatchCommentaryParent = (LinearLayout) V.findViewById(R.id.schedule_row_ll_match_commentary_parent);
            mRlMatchPoints = (RelativeLayout) V.findViewById(R.id.rl_points);
            mLlRightPartyLayout = (LinearLayout) V.findViewById(R.id.schedule_row_ll_right_party);
            mLlLeftPartyLayout = (LinearLayout) V.findViewById(R.id.schedule_row_ll_left_party);

            mBtnPlayMatch.setOnClickListener(this);
            mRlMatchPoints.setOnClickListener(this);
            mLlResultWait.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Bundle bundle = null;

            Match match = (Match) view.getTag();
            if (null != match) {
                bundle = new Bundle();
                bundle.putParcelable(BundleKeys.MATCH_LIST, Parcels.wrap(match));
                bundle.putString(BundleKeys.SCREEN, Constants.ScreenNames.PROFILE);

                if (null != match.getSportId()) {
                    bundle.putInt(BundleKeys.SPORT_ID, match.getSportId());
                }

                if (null != mPlayerId) {
                    bundle.putInt(BundleKeys.PLAYER_ID, mPlayerId);
                    bundle.putInt(BundleKeys.MATCH_ID, match.getId());
                    bundle.putString(BundleKeys.PLAYER_NAME, mPlayerName);
                    bundle.putString(BundleKeys.PLAYER_PHOTO, mPlayerPhoto);
                    bundle.putInt(BundleKeys.ROOM_ID, match.getRoomId());
                }
            }

            switch (view.getId()) {
                case R.id.schedule_row_btn_playmatch:
                    NostragamusAnalytics.getInstance().trackTimeline(
                            GameAttemptedStatus.PARTIALLY == match.getisAttempted() ? AnalyticsActions.CONTINUE : AnalyticsActions.PLAY
                    );

                    bundle.putParcelable(BundleKeys.TOURNAMENT_POWERUPS, Parcels.wrap(mPowerupInfo));
                    navigateToPrediction(context, bundle);
                    break;
                case R.id.rl_points:
                    NostragamusAnalytics.getInstance().trackTimeline(AnalyticsActions.VIEW_OTHERS_ANSWERS);

                    if (mPlayerId != null) {
                        navigateToResultsPeek(context, bundle);
                    } else {
                        launchResultsScreen(context, match,Constants.MatchStatusStrings.POINTS);
                    }

                    break;
                case R.id.schedule_row_ll_waiting_for_result:
                    NostragamusAnalytics.getInstance().trackTimeline(AnalyticsActions.OTHERS_RESULTS_WAITING);
                    launchResultsScreen(context,match,Constants.MatchStatusStrings.ANSWER);
                    break;
            }
        }

    }

    private void launchResultsScreen(Context context, Match match,String matchStatus) {

        if (context != null) {
            if (match != null) {
                Bundle bundle = new Bundle();
                ResultsScreenDataDto resultsScreenData = new ResultsScreenDataDto();
                resultsScreenData.setMatchId(match.getId());
                resultsScreenData.setRoomId(match.getRoomId());
                resultsScreenData.setMatchStatus(matchStatus);

                if (resultsScreenData != null) {
                    bundle.putParcelable(Constants.BundleKeys.RESULTS_SCREEN_DATA, Parcels.wrap(resultsScreenData));
                    if (mPlayerId != null) {
                        bundle.putInt(BundleKeys.PLAYER_ID, mPlayerId);
                    }
                    Intent resultsIntent = new Intent(context, MyResultsActivity.class);
                    resultsIntent.putExtras(bundle);
                    resultsIntent.putExtra(Constants.BundleKeys.SCREEN_LAUNCHED_FROM_PARENT,
                            MyResultsActivity.LaunchedFrom.IN_PLAY_SCREEN_MATCH_AWAITING_RESULTS);
                    context.startActivity(resultsIntent);

                }
            }
        }
    }

    private void navigateToResultsPeek(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ResultsPeekActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void navigateToPrediction(Context context, Bundle bundle) {
        Intent intent = new Intent(context, PredictionActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void navigateToMyResults(Context context, Bundle bundle) {
        Intent intent = new Intent(context, MyResultsActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private View getCommentary(ViewGroup parent, Match match) {
        View commentary = getLayoutInflater().inflate(R.layout.inflater_match_result_commentary_row, parent, false);

        TextView tvcommentary = (TextView) commentary.findViewById(R.id.schedule_row_tv_match_result_commentary);

        TextView tvcommentarytitle = (TextView) commentary.findViewById(R.id.schedule_row_tv_match_result_commentary_title);

        String description = match.getResultdesc();
        if (!TextUtils.isEmpty(description)) {
            tvcommentary.setVisibility(View.VISIBLE);

            tvcommentary.setText(AppSnippet.noTrailingwhiteLines(Html.fromHtml(match.getResultdesc().toString().replaceAll("&nbsp;", ""))));
            tvcommentary.setMovementMethod(LinkMovementMethod.getInstance());

            CharSequence text = tvcommentary.getText();
            if (text instanceof Spannable) {
                int end = text.length();
                Spannable sp = (Spannable) tvcommentary.getText();
                URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
                SpannableStringBuilder style = new SpannableStringBuilder(text);
                style.clearSpans();//should clear old spans
                for (URLSpan url : urls) {
                    LinkSpan click = new LinkSpan(url.getURL());
                    style.setSpan(click, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                tvcommentary.setText(style);
            }

            if (match.getMatchPoints() == GameAttemptedStatus.NOT) {
                tvcommentarytitle.setVisibility(View.VISIBLE);
                tvcommentarytitle.setText(Html.fromHtml(match.getResult()));
            } else {
                tvcommentarytitle.setVisibility(View.GONE);
            }

            if (!match.getStage().equals("Commentary")) {
                tvcommentarytitle.setVisibility(View.GONE);
            }
        }

        return commentary;
    }

    public class LinkSpan extends URLSpan {

        private LinkSpan(String url) {
            super(url);
        }

        @Override
        public void onClick(View view) {
            String url = getURL();
            if (url != null) {
                view.getContext().startActivity(new Intent(view.getContext(), FeedWebView.class).putExtra("url", url));
            }
        }
    }

    @Override
    public void clear() {
        super.clear();
        mScheduleMap.clear();
        mScheduleVHList.clear();
    }

    @Override
    public void destroy() {
        super.destroy();
        mScheduleVHList.clear();
        mScheduleMap.clear();
        mTimerRunnable.destroy();
    }

    private Map<View, ScheduleViewHolder> mScheduleMap = new HashMap<>();

    private List<ScheduleViewHolder> mScheduleVHList = new ArrayList<>();

    private class TimerRunnable implements Runnable {

        private Handler customHandler;

        private TimerRunnable() {
            customHandler = new Handler();
            customHandler.postDelayed(this, 0);
        }

        public void run() {
            for (ScheduleViewHolder scheduleVH : mScheduleVHList) {
                if (View.VISIBLE == scheduleVH.mTvExpiresIn.getVisibility()) {
                    Log.d("TimelineAdapter", "Atleast one");

                    long updatedTime = Long.parseLong(scheduleVH.mTvExpiresIn.getTag().toString());
                    if (updatedTime > 1000) {
                        updateTimer(scheduleVH.mTvExpiresIn, updatedTime);
                    } else {
                        scheduleVH.mTvExpiresIn.setVisibility(View.GONE);
                    }
                }
            }

            customHandler.postDelayed(this, 1000);
        }

        private void updateTimer(TextView tvTimerValue, long updatedTime) {
            tvTimerValue.setTag(updatedTime - 1000);

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int hours = mins / 60;
            int days = hours / 24;
            hours = hours % 24;
            mins = mins % 60;
            secs = secs % 60;

            tvTimerValue.setText("Expires in " +
                    String.format("%02d", hours) + ":"
                    + String.format("%02d", mins) + ":"
                    + String.format("%02d", secs)
            );
        }

        private void destroy() {
            customHandler.removeCallbacks(this);
            customHandler = null;
        }

    }

}