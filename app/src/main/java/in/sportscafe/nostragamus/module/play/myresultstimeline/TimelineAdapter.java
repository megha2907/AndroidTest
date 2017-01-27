package in.sportscafe.nostragamus.module.play.myresultstimeline;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
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
import com.jeeva.android.widgets.customfont.CustomButton;
import com.jeeva.android.widgets.customfont.CustomTextView;
import com.jeeva.android.widgets.customfont.Typefaces;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.DateFormats;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.feed.FeedWebView;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.feed.dto.Parties;
import in.sportscafe.nostragamus.module.play.myresults.MyResultsActivity;
import in.sportscafe.nostragamus.module.play.prediction.PredictionActivity;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.Tournament;
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

    public TimelineAdapter(Context context) {
        super(context);
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
        holder.mLlTourParent.removeAllViews();
        holder.mLlTourParent.addView(getScheduleView(match, holder.mLlTourParent));
    }

    private View getTourView(Tournament tournament, ViewGroup parent, int position) {
        View tourView = getLayoutInflater().inflate(R.layout.inflater_tour_row, parent, false);
        TourViewHolder holder = new TourViewHolder(tourView);

        holder.mTvTournamentName.setText(tournament.getTournamentName());

        holder.mLlScheduleParent.removeAllViews();
        for (Match match : tournament.getMatches()) {
            holder.mLlScheduleParent.addView(getScheduleView(match, holder.mLlScheduleParent));
        }

        return tourView;
    }

    private View getScheduleView(Match match, ViewGroup parent) {

        /*holder.mTvDate.setText(Html.fromHtml(
                TimeUtils.getDateStringFromMs(startTimeMs, "MMM").toUpperCase() + "<br>" + String.valueOf(dayOfMonth).toUpperCase()
                        + "<sup>" +AppSnippet.ordinalOnly(dayOfMonth) + "</sup>")
        );*/

        View scheduleView = getLayoutInflater().inflate(R.layout.inflater_schedule_row, parent, false);
        ScheduleViewHolder holder = new ScheduleViewHolder(scheduleView);

//        String startTime = match.getStartTime();
        String startTime = "2017-01-27T18:00:00.000Z";
        long startTimeMs = TimeUtils.getMillisecondsFromDateString(
                startTime,
                DateFormats.FORMAT_DATE_T_TIME_ZONE,
                DateFormats.GMT
        );

        int dayOfMonth = Integer.parseInt(TimeUtils.getDateStringFromMs(startTimeMs, "d"));
        // Setting date of the match
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

            // Setting match stage, if the stage is not empty & not the "commentary"
            if(!TextUtils.isEmpty(matchStage)) {
                holder.mTvMatchStage.setVisibility(View.VISIBLE);
                holder.mTvMatchStage.setText(match.getStage());
            }

            // Setting starting time of the match
            holder.mTvStartTime.setVisibility(View.VISIBLE);
            holder.mTvStartTime.setText(TimeUtils.getDateStringFromMs(startTimeMs, DateFormats.HH_MM_AA));

            // Setting party details
            List<Parties> parties = match.getParties();
            holder.mTvPartyAName.setText(parties.get(0).getPartyName());
            holder.mTvPartyBName.setText(parties.get(1).getPartyName());
            holder.mIvPartyAPhoto.setImageUrl(parties.get(0).getPartyImageUrl());
            holder.mIvPartyBPhoto.setImageUrl(parties.get(1).getPartyImageUrl());

            TimeAgo timeAgo = TimeUtils.calcTimeAgo(Calendar.getInstance().getTimeInMillis(), startTimeMs);
            boolean isMatchStarted = timeAgo.timeDiff <= 0
                    || timeAgo.timeUnit == TimeUnit.MILLISECOND
                    || timeAgo.timeUnit == TimeUnit.SECOND;

            if(match.getMatchQuestionCount() > 0) {
                String result = match.getResult();
                if(TextUtils.isEmpty(result)) {
                    if(match.getisAttempted()) { // Waiting for results
                        holder.mLlResultWait.setVisibility(View.VISIBLE);
                        holder.mLlResultWait.setTag(match);
                    } else if(isMatchStarted) { // You cannot play the match as the match already started
                        holder.mVResultLine.setVisibility(View.VISIBLE);
                        holder.mTvInfo.setVisibility(View.VISIBLE);
                        holder.mTvInfo.setText("Opportunity missed at scoring!");
                    } else { // You can now play the match
                        holder.mBtnPlayMatch.setVisibility(View.VISIBLE);
                        holder.mBtnPlayMatch.setTag(match);

                        if(timeAgo.totalDiff < ONE_DAY_IN_MS) {
                            holder.mTvExpiresIn.setVisibility(View.VISIBLE);
                            new TimerRunnable(timeAgo.totalDiff, holder.mTvExpiresIn);
//                            holder.mTvExpiresIn.setText("Expires in " + TimeUtils.convertTimeAgoToDefaultString(timeAgo));
                        }
                    }
                } else {
                    holder.mTvMatchResult.setVisibility(View.VISIBLE);
                    holder.mTvMatchResult.setText(result);

                    if(match.getisAttempted()) { // Played match result published
                        holder.mRlMatchPoints.setVisibility(View.VISIBLE);
                        holder.mVResultLine.setVisibility(View.VISIBLE);

                        holder.mRlMatchPoints.setTag(match);
                        holder.mBtnMatchPoints.setText(match.getMatchPoints() + " Points");
                        holder.mTvResultCorrectCount.setText("You got " + match.getCorrectCount() + "/" + match.getMatchQuestionCount() + " answers correct");

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
                    } else { // Not played match result published
                        holder.mVResultLine.setVisibility(View.VISIBLE);
                        holder.mTvInfo.setVisibility(View.VISIBLE);
                        holder.mTvInfo.setText("Opportunity missed at scoring!");
                    }
                }
            } else { // No questions prepared

                if (!isMatchStarted) { // Still the question is not prepared for these matches
                    holder.mVResultLine.setVisibility(View.VISIBLE);
                    holder.mTvInfo.setVisibility(View.VISIBLE);
                    holder.mTvInfo.setText("Games coming up!");
                }
            }
        }

        return scheduleView;
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

        LinearLayout mLlCardLayout;

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

        LinearLayout mLlResultWait;

        HmImageView mIvPartyAPhoto;

        HmImageView mIvPartyBPhoto;

        CustomButton mBtnMatchPoints;

        CustomButton mBtnPlayMatch;

        View mVResultLine;

        LinearLayout mLlMatchCommentaryParent;

        RelativeLayout mRlMatchPoints;

        TextView mTvDate;

        public ScheduleViewHolder(View V) {
            super(V);

            mTvDate = (TextView) V.findViewById(R.id.schedule_row_tv_date);
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
            mLlResultWait = (LinearLayout) V.findViewById(R.id.schedule_row_ll_waiting_for_result);
            mBtnPlayMatch = (CustomButton) V.findViewById(R.id.schedule_row_btn_playmatch);
            mBtnMatchPoints = (CustomButton) V.findViewById(R.id.schedule_row_btn_points);

            mLlCardLayout = (LinearLayout) V.findViewById(R.id.schedule_row_ll);
            mVResultLine = V.findViewById(R.id.schedule_row_v_result_line);
            mLlMatchCommentaryParent = (LinearLayout) V.findViewById(R.id.schedule_row_ll_match_commentary_parent);
            mRlMatchPoints = (RelativeLayout) V.findViewById(R.id.rl_points);

            mBtnPlayMatch.setOnClickListener(this);
            mRlMatchPoints.setOnClickListener(this);
            mLlResultWait.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Bundle bundle = null;

            Match match = (Match) view.getTag();
            if(null != match) {
                bundle = new Bundle();
                bundle.putSerializable(BundleKeys.MATCH_LIST, match);
            }

            switch (view.getId()) {
                case R.id.schedule_row_btn_playmatch:
                    navigateToPrediction(context, bundle);
                    break;
                case R.id.rl_points:
                    navigateToMyResults(context, bundle);
                    break;
                case R.id.schedule_row_ll_waiting_for_result:
                    navigateToMyResults(context, bundle);
                    break;
            }
        }

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

            if (match.getMatchPoints() == 0) {
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

    private class LinkSpan extends URLSpan {

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

    private class TimerRunnable implements Runnable {

        private CustomTextView tvTimerValue;

        private Handler customHandler;

        private long updatedTime;

        private TimerRunnable(long initialTimeInMs, CustomTextView timerValue) {
            updatedTime = initialTimeInMs + 1000;
            tvTimerValue = timerValue;

            customHandler = new Handler();
            customHandler.postDelayed(this, 0);
        }

        public void run() {
            if(null == tvTimerValue || null == tvTimerValue.getContext()) {
                Log.d("TimelineAdapter", null == tvTimerValue ? "textview null" : "textview destroyed");
                customHandler.removeCallbacks(this);
                customHandler = null;
                tvTimerValue = null;
            } else {
                updateTimer();

                customHandler.postDelayed(this, 1000);
            }
        }

        private void updateTimer() {
            updatedTime -= 1000;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int hours = mins / 60;
            mins = mins % 60;
            secs = secs % 60;

            tvTimerValue.setText("Expires in " +
                    String.format("%02d", hours) + ":"
                    + String.format("%02d", mins) + ":"
                    + String.format("%02d", secs)
            );
        }

    };

}