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
import in.sportscafe.nostragamus.module.feed.FeedWebView;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.feed.dto.Parties;
import in.sportscafe.nostragamus.module.feed.dto.TournamentPowerupInfo;
import in.sportscafe.nostragamus.module.play.myresults.MyResultsActivity;
import in.sportscafe.nostragamus.module.play.prediction.PredictionActivity;
import in.sportscafe.nostragamus.module.resultspeek.ResultsPeekActivity;
import in.sportscafe.nostragamus.utils.ViewUtils;
import in.sportscafe.nostragamus.utils.timeutils.TimeAgo;
import in.sportscafe.nostragamus.utils.timeutils.TimeUnit;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by Jeeva on 15/6/16.
 */
public class ChallengesTimelineAdapter extends Adapter<Match, ChallengesTimelineAdapter.ViewHolder> {

    private static final String COMMENTARY = "commentary";

    public ChallengesTimelineAdapter(Context context) {
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

        holder.mLlTourParent.addView(getScheduleView(match, holder.mLlTourParent).mMainView);
    }

    private ScheduleViewHolder getScheduleView(Match match, ViewGroup parent) {
        ScheduleViewHolder holder = new ScheduleViewHolder(getLayoutInflater().inflate(R.layout.inflater_other_view_schedule_row, parent, false));

        String startTime = match.getStartTime().replace("+00:00", ".000Z");
        Log.d("StartTime", startTime);
//        String startTime = "2017-01-27T18:00:00.000Z";
        long startTimeMs = TimeUtils.getMillisecondsFromDateString(
                startTime,
                DateFormats.FORMAT_DATE_T_TIME_ZONE,
                DateFormats.GMT
        );

        int dayOfMonth = Integer.parseInt(TimeUtils.getDateStringFromMs(startTimeMs, "d"));
        // Setting date of the match
        holder.mTvDate.setText(
                TimeUtils.getDateStringFromMs(startTimeMs, "MMM")
                        + dayOfMonth + AppSnippet.ordinalOnly(dayOfMonth) + ", "
                + TimeUtils.getDateStringFromMs(startTimeMs, DateFormats.HH_MM_AA)
        );

        String matchStage = match.getStage();
        if (COMMENTARY.equalsIgnoreCase(matchStage)) {
            holder.mLlCardLayout.setVisibility(View.GONE);
        } else {

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

            Integer attemptedStatus = match.getisAttempted();

            if (match.getMatchQuestionCount() > 0) {

                if (match.isResultPublished()) { // if match Result Published

                    //if match Completely Attempted then IsAttempted = 2 else if Partially Attempted then is Attempted =1
                    //show Match Results
                    if (GameAttemptedStatus.COMPLETELY == attemptedStatus || GameAttemptedStatus.PARTIALLY == attemptedStatus) {

                        holder.mBtnMatchPoints.setVisibility(View.VISIBLE);

                        holder.mTvMatchResult.setVisibility(View.VISIBLE);
                        holder.mTvMatchResult.setText(Html.fromHtml(match.getResult()));
                        holder.mBtnMatchPoints.setTag(match);
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
                    }

                    //if match not Attempted then IsAttempted=0
                    if (GameAttemptedStatus.NOT == attemptedStatus) {
                        // Show Opportunity missed at scoring!
                        holder.mTvMatchResult.setVisibility(View.VISIBLE);
                        holder.mTvMatchResult.setText(Html.fromHtml(match.getResult()));
                        holder.mTvInfo.setVisibility(View.VISIBLE);
                        holder.mTvInfo.setText("Did Not Play");
                    }

                } else { // if Results not published
                    if (GameAttemptedStatus.NOT == attemptedStatus || GameAttemptedStatus.PARTIALLY == attemptedStatus) {
                        if (isMatchStarted) {
                            if (attemptedStatus == GameAttemptedStatus.PARTIALLY) {

                                //  Waiting for results
                                holder.mLlResultWait.setVisibility(View.VISIBLE);
                                holder.mLlResultWait.setTag(match);
                            } else {

                                // You cannot play the match as the match already started
                                holder.mTvInfo.setVisibility(View.VISIBLE);
                                holder.mTvInfo.setText("Did Not Play");
                            }
                        } else {

                            // show Play button
                            holder.mBtnPlayMatch.setVisibility(View.VISIBLE);
                            holder.mBtnPlayMatch.setTag(match);

                            if (GameAttemptedStatus.PARTIALLY == attemptedStatus) {
                                holder.mBtnPlayMatch.setAllCaps(false);
                                holder.mBtnPlayMatch.setText(("Continue"));
                            }
                        }
                    } else if (attemptedStatus == GameAttemptedStatus.COMPLETELY) {
                        //  Waiting for results
                        holder.mLlResultWait.setVisibility(View.VISIBLE);
                        holder.mLlResultWait.setTag(match);
                    }

                }
            } else { // No questions prepared
                if (!isMatchStarted) { // Still the question is not prepared for these matches
                    holder.mTvInfo.setVisibility(View.VISIBLE);
                    holder.mTvInfo.setText("Coming Up");
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

        LinearLayout mLlCardLayout;

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

        TextView mTvDate;

        public ScheduleViewHolder(View V) {
            super(V);

            mMainView = V;
            mTvDate = (TextView) V.findViewById(R.id.schedule_row_tv_date);
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

            mLlCardLayout = (LinearLayout) V.findViewById(R.id.schedule_row_ll);

            mBtnPlayMatch.setOnClickListener(this);
            mBtnMatchPoints.setOnClickListener(this);
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

                if(null != match.getSportId()) {
                    bundle.putInt(BundleKeys.SPORT_ID, match.getSportId());
                }
            }

            switch (view.getId()) {
                case R.id.schedule_row_btn_playmatch:
                    NostragamusAnalytics.getInstance().trackTimeline(
                            GameAttemptedStatus.PARTIALLY == match.getisAttempted() ? AnalyticsActions.CONTINUE : AnalyticsActions.PLAY
                    );

//                    bundle.putParcelable(BundleKeys.TOURNAMENT_POWERUPS, Parcels.wrap(mPowerupInfo));
                    navigateToPrediction(context, bundle);
                    break;
                case R.id.schedule_row_btn_points:
                    NostragamusAnalytics.getInstance().trackTimeline(AnalyticsActions.VIEW_ANSWERS);

                    navigateToMyResults(context, bundle);
                    break;
                case R.id.schedule_row_ll_waiting_for_result:
                    NostragamusAnalytics.getInstance().trackTimeline(AnalyticsActions.RESULT_WAITING);

                    navigateToMyResults(context, bundle);
                    break;
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
}