package in.sportscafe.nostragamus.module.play.myresultstimeline;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.customfont.CustomButton;
import com.jeeva.android.widgets.customfont.Typefaces;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.DateFormats;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.feed.FeedWebView;
import in.sportscafe.nostragamus.module.feed.dto.Feed;
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
public class TimelineAdapter extends Adapter<Feed, TimelineAdapter.ViewHolder> {

    private static final String COMMENTARY = "commentary";

    private List<Match> mMyResultList = new ArrayList<>();

    public TimelineAdapter(Context context) {
        super(context);
    }

    @Override
    public Feed getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayoutInflater().inflate(R.layout.inflater_feed_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Feed feed = getItem(position);
        holder.mPosition = position;
        holder.mLlTourParent.removeAllViews();
        for (Tournament tournament : feed.getTournaments()) {
            holder.mLlTourParent.addView(getTourView(tournament, holder.mLlTourParent, position));
        }
    }

    private View getTourView(Tournament tournament, ViewGroup parent, int position) {
        View tourView = getLayoutInflater().inflate(R.layout.inflater_tour_row, parent, false);
        TourViewHolder holder = new TourViewHolder(tourView);

        holder.mTvTournamentName.setText(tournament.getTournamentName());

        holder.mLlScheduleParent.removeAllViews();
        for (Match match : tournament.getMatches()) {
            holder.mLlScheduleParent.addView(getScheduleView(match, holder.mLlScheduleParent, position));
        }

        return tourView;
    }

    private View getScheduleView(Match match, ViewGroup parent, int position) {
        /*if (!(TextUtils.isEmpty(matchStage) || COMMENTARY.equalsIgnoreCase(matchStage))) {

        }*/

        /*holder.mTvDate.setText(Html.fromHtml(
                TimeUtils.getDateStringFromMs(startTimeMs, "MMM").toUpperCase() + "<br>" + String.valueOf(dayOfMonth).toUpperCase()
                        + "<sup>" +AppSnippet.ordinalOnly(dayOfMonth) + "</sup>")
        );*/

        View scheduleView = getLayoutInflater().inflate(R.layout.inflater_schedule_row, parent, false);
        ScheduleViewHolder holder = new ScheduleViewHolder(scheduleView);

        String startTime = match.getStartTime();
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

            if(match.getMatchQuestionCount() > 0) {
                String result = match.getResult();
                if(TextUtils.isEmpty(result)) {
                    if(match.getisAttempted()) { // Waiting for results
                        holder.mTvResultWait.setVisibility(View.VISIBLE);
                        holder.mVResultLine.setVisibility(View.VISIBLE);
//                        holder.mTvResultWait.setText(match.getMatchQuestionCount() + " predictions made, waiting for results");
                        holder.mTvResultWait.setTag(match);
                    } else { // You can now play the match
                        holder.mBtnPlayMatch.setVisibility(View.VISIBLE);
                        holder.mBtnPlayMatch.setTag(match);
                    }
                } else if(match.getisAttempted()) { // Played match result published
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

                }
            } else { // No questions prepared

                TimeAgo timeAgo = TimeUtils.calcTimeAgo(Calendar.getInstance().getTimeInMillis(), startTimeMs);
                if (timeAgo.timeDiff > 0 && timeAgo.timeUnit != TimeUnit.MILLISECOND && timeAgo.timeUnit != TimeUnit.SECOND) {

                    // Still the question is not prepared for these matches
                    holder.mTvResultWait.setVisibility(View.VISIBLE);
                    holder.mTvResultWait.setText("Coming up");
                    holder.mTvResultWait.setOnClickListener(null);
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

        TextView mTvPartyAName;

        TextView mTvPartyAScore;

        TextView mTvPartyBName;

        TextView mTvPartyBScore;

        TextView mTvMatchResult;

        TextView mTvResultCorrectCount;

        TextView mTvResultWait;

        TextView mTvStartTime;

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
            mTvPartyAName = (TextView) V.findViewById(R.id.schedule_row_tv_party_a_name);
            mTvPartyBName = (TextView) V.findViewById(R.id.schedule_row_tv_party_b_name);
            mIvPartyAPhoto = (HmImageView) V.findViewById(R.id.swipe_card_iv_left);
            mIvPartyBPhoto = (HmImageView) V.findViewById(R.id.swipe_card_iv_right);
            mTvMatchResult = (TextView) V.findViewById(R.id.schedule_row_tv_match_result);
            mTvStartTime = (TextView) V.findViewById(R.id.schedule_row_tv_match_time);
            mTvResultCorrectCount = (TextView) V.findViewById(R.id.schedule_row_tv_match_correct_questions);
            mTvResultWait = (TextView) V.findViewById(R.id.schedule_row_btn_waiting_for_result);
            mBtnPlayMatch = (CustomButton) V.findViewById(R.id.schedule_row_btn_playmatch);
            mBtnMatchPoints = (CustomButton) V.findViewById(R.id.schedule_row_btn_points);

            mLlCardLayout = (LinearLayout) V.findViewById(R.id.schedule_row_ll);
            mVResultLine = V.findViewById(R.id.schedule_row_v_result_line);
            mLlMatchCommentaryParent = (LinearLayout) V.findViewById(R.id.schedule_row_ll_match_commentary_parent);
            mRlMatchPoints = (RelativeLayout) V.findViewById(R.id.rl_points);

            mBtnPlayMatch.setOnClickListener(this);
            mRlMatchPoints.setOnClickListener(this);
            mTvResultWait.setOnClickListener(this);
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
                case R.id.schedule_row_tv_match_result_wait:
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

}