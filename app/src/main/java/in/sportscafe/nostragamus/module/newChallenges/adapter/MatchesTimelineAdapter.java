package in.sportscafe.nostragamus.module.newChallenges.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.customfont.CustomButton;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.common.CustomTypefaceSpan;
import in.sportscafe.nostragamus.module.newChallenges.dto.Match;
import in.sportscafe.nostragamus.module.newChallenges.dto.MatchParty;
import in.sportscafe.nostragamus.module.play.myresults.MyResultsActivity;
import in.sportscafe.nostragamus.module.play.prediction.PredictionActivity;
import in.sportscafe.nostragamus.module.resultspeek.ResultsPeekActivity;
import in.sportscafe.nostragamus.utils.timeutils.TimeAgo;
import in.sportscafe.nostragamus.utils.timeutils.TimeUnit;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by deepanshi on 9/1/17.
 */

public class MatchesTimelineAdapter extends Adapter<Match, MatchesTimelineAdapter.MatchesViewHolder> {

    private static final String COMMENTARY = "commentary";

    private static final long ONE_DAY_IN_MS = 24 * 60 * 60 * 1000;

    private MatchesTimelineAdapter.TimerRunnable mTimerRunnable;

    private final static int ONE_PARTY_VIEW = 0;
    private final static int MULTI_PARTY_VIEW = 1;

    private MatchesTimelineAdapterListener matchesTimelineAdapterListener;
    private Context mContext;
    private Challenge mChallengeInfo;

    public MatchesTimelineAdapter(Context context,
                                  @NonNull MatchesTimelineAdapterListener listener) {
        super(context);
        mContext = context;
        matchesTimelineAdapterListener = listener;
        mTimerRunnable = new MatchesTimelineAdapter.TimerRunnable();
    }

    public void updateChallengeInfo(Challenge challengeInfo) {
        this.mChallengeInfo = challengeInfo;
    }

    @Override
    public Match getItem(int position) {
        return super.getItem(position);
    }


    @Override
    public int getItemViewType(int position) {

        if (getItem(position).getMatchParties() == null) {
            return ONE_PARTY_VIEW;
        }

        return MULTI_PARTY_VIEW;
    }

    @Override
    public MatchesTimelineAdapter.MatchesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case ONE_PARTY_VIEW:
                View viewONE = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflater_one_party_schedule_row, parent, false);
                MatchesTimelineAdapter.MatchesViewHolder rowONE = new MatchesTimelineAdapter.MatchesViewHolder(viewONE);
                return rowONE;

            default:
                View viewTWO = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflater_other_view_schedule_row, parent, false);
                MatchesTimelineAdapter.MatchesViewHolder rowTWO = new MatchesTimelineAdapter.MatchesViewHolder(viewTWO);
                return rowTWO;
        }

    }

    @Override
    public void onBindViewHolder(MatchesTimelineAdapter.MatchesViewHolder holder, int position) {
        populateMatchDetails(getItem(position), holder, position);
        if (View.VISIBLE == holder.mTvExpiresIn.getVisibility()) {
            mScheduleVHList.add(holder);
            mScheduleMap.put(holder.mMainView, holder);
        }
    }

    private void populateMatchDetails(Match match, MatchesTimelineAdapter.MatchesViewHolder holder, int pos) {


        holder.mBtnMatchPoints.setVisibility(View.GONE);
        holder.mTvInfo.setVisibility(View.GONE);
        holder.mTvMatchResult.setVisibility(View.GONE);
        holder.mLlResultWait.setVisibility(View.GONE);
        holder.mBtnPlayMatch.setVisibility(View.GONE);
        holder.mTvDate.setVisibility(View.VISIBLE);
        holder.mTvExpiresIn.setVisibility(View.INVISIBLE);
        holder.mTvOnePartyDate.setVisibility(View.GONE);

        String startTime = match.getMatchStarttime().replace("+00:00", ".000Z");
        Log.d("StartTime", startTime);
        long startTimeMs = TimeUtils.getMillisecondsFromDateString(
                startTime,
                Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                Constants.DateFormats.GMT
        );

        int dayOfMonth = Integer.parseInt(TimeUtils.getDateStringFromMs(startTimeMs, "d"));
        // Setting date of the Match
        holder.mTvDate.setText(dayOfMonth + AppSnippet.ordinalOnly(dayOfMonth) + " " +
                TimeUtils.getDateStringFromMs(startTimeMs, "MMM") + ", "
                + TimeUtils.getDateStringFromMs(startTimeMs, Constants.DateFormats.HH_MM_AA).replace("AM", "am").replace("PM", "pm")
        );

        String matchStage = match.getMatchStage();
        if (COMMENTARY.equalsIgnoreCase(matchStage)) {
            holder.mLlCardLayout.setVisibility(View.GONE);
        } else {

            List<MatchParty> parties = new ArrayList<>();

            // Setting party details : if one party view then set topics for one party Match
            if (getItemViewType(pos) == ONE_PARTY_VIEW) {
                holder.mTvPartyAName.setText(match.getTopics().getTopicName());
                holder.mIvPartyAPhoto.setImageUrl(match.getTopics().getTopicUrl());
            } else {
                parties = match.getMatchParties();
                holder.mTvPartyAName.setText(parties.get(0).getPartyName());
                holder.mTvPartyBName.setText(parties.get(1).getPartyName());
                holder.mIvPartyAPhoto.setImageUrl(parties.get(0).getPartyImgUrl());
                holder.mIvPartyBPhoto.setImageUrl(parties.get(1).getPartyImgUrl());
            }

            holder.mTvDate.setTextColor(ContextCompat.getColor(holder.mTvDate.getContext(), R.color.white));

            Date d = new Date(Nostragamus.getInstance().getServerTime());
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            long serverTime = c.getTimeInMillis();

            TimeAgo timeAgo = TimeUtils.calcTimeAgo(Nostragamus.getInstance().getServerTime(), startTimeMs);

            boolean isMatchStarted = timeAgo.timeDiff <= 0
                    || timeAgo.timeUnit == TimeUnit.MILLISECOND
                    || timeAgo.timeUnit == TimeUnit.SECOND;

            Integer attemptedStatus = match.getIsAttempted();
            if (attemptedStatus == null) {
                attemptedStatus = 0;
            }

            if (getItemViewType(pos) == ONE_PARTY_VIEW) {
                holder.mTvMatchResult.setVisibility(View.VISIBLE);
                holder.mTvMatchResult.setText(match.getMatchStage());
            } else {
                holder.mTvMatchResult.setVisibility(View.VISIBLE);
                holder.mTvMatchResult.setText(match.getMatchStage());
            }


            holder.mBtnMatchLock.setVisibility(View.GONE);
            Typeface newFont1 = Typeface.createFromAsset(holder.mBtnMatchLock.getContext().getAssets(), "fonts/lato/Lato-Black.ttf");
            holder.mBtnMatchLock.setTypeface(newFont1);
            holder.mBtnMatchLock.setAllCaps(false);
            holder.mBtnMatchLock.setTextSize(12);


            if (!TextUtils.isEmpty(match.getMatchStatus())) {

                String matchStatus = match.getMatchStatus();

                if (Constants.MatchStatus.UPCOMING == matchStatus) {
                    /* if Match Questions are not ready , then show status coming Up with a lock button */
                    holder.mBtnMatchLock.setVisibility(View.VISIBLE);
                    holder.mBtnMatchLock.setText("Coming up");
                    holder.mBtnMatchLock.setClickable(false);

                } else if (Constants.MatchStatus.LIVE == matchStatus) {
                      /* if Match Questions are ready , then show status Live but with a lock button */
                    holder.mBtnMatchLock.setVisibility(View.VISIBLE);
                    holder.mBtnMatchLock.setText(matchStatus);
                    holder.mBtnMatchLock.setClickable(false);

                } else if (Constants.MatchStatus.CANCELLED == matchStage) {
                    /* if Match is cancelled due to any reason , then show status Cancelled with a lock button */
                    holder.mBtnMatchLock.setVisibility(View.VISIBLE);
                    holder.mBtnMatchLock.setText(matchStatus);
                    holder.mBtnMatchLock.setClickable(false);

                } else if (Constants.MatchStatus.PLAY == matchStage) {
                    /* if Match Status is Play , then check if Match is partially Attempted or Not Attempted */
                    if (Constants.GameAttemptedStatus.NOT == attemptedStatus || Constants.GameAttemptedStatus.PARTIALLY == attemptedStatus) {

                        /* if Match has started then check attempted Status
                         * if not attempted then Show Did Not Play
                          * else show InProgress for Match Results */

                        if (isMatchStarted) {
                            if (attemptedStatus == Constants.GameAttemptedStatus.PARTIALLY) {

                                    /*  Waiting for results */
                                holder.mLlResultWait.setVisibility(View.VISIBLE);
                                holder.mTvMatchResult.setVisibility(View.VISIBLE);
                                holder.mLlResultWait.setTag(match);
                                holder.mTvDate.setText("In Progress");
                            } else {

                                    /* You cannot play the Match as the Match already started */
                                holder.mTvInfo.setVisibility(View.VISIBLE);
                                holder.mTvInfo.setText("Did Not Play");
                                holder.mTvInfo.setTag(match);
                                holder.mTvInfo.setClickable(true);
                                holder.mTvInfo.setBackground(holder.mTvInfo.getContext().getResources().getDrawable(R.drawable.btn_not_played_shadow_bg));
                                holder.mTvDate.setText("In Progress");
                            }
                        } else {

                             /* if Match has not started then check attempted Status
                         * if partially attempted then Show Continue button
                          * else show Play Button */

                                /* show Play button */
                            holder.mBtnPlayMatch.setVisibility(View.VISIBLE);
                            holder.mBtnPlayMatch.setTag(match);

                                /* check if has partially played a Match */
                            if (Constants.GameAttemptedStatus.PARTIALLY == attemptedStatus) {
                                holder.mBtnPlayMatch.setAllCaps(false);
                                holder.mBtnPlayMatch.setText(("Continue"));
                            } else {
                                holder.mBtnPlayMatch.setAllCaps(true);
                                holder.mBtnPlayMatch.setText(("Play"));
                            }

                                /* check if one day left and Match not started , show countdown timer */
                            if (timeAgo.totalDiff < ONE_DAY_IN_MS) {
                                holder.mTvExpiresIn.setVisibility(View.VISIBLE);

                                    /* If one party view then set expiresIn on OnePartyDate */
                                if (getItemViewType(pos) == ONE_PARTY_VIEW) {
                                    holder.mTvDate.setVisibility(View.GONE);
                                    holder.mTvOnePartyDate.setTag(timeAgo.totalDiff);
                                } else {
                                    holder.mTvDate.setVisibility(View.INVISIBLE);
                                    holder.mTvExpiresIn.setTag(timeAgo.totalDiff);
                                }

                            }
                        }
                    }

                } else {
                    /* Set Match Status from server */
                    holder.mBtnMatchLock.setVisibility(View.VISIBLE);
                    holder.mBtnMatchLock.setText(matchStatus);
                    holder.mBtnMatchLock.setClickable(false);
                }

            }

            /* If one party view then set date on OnePartyDate */
            if (getItemViewType(pos) == ONE_PARTY_VIEW) {

                if (View.VISIBLE == holder.mTvDate.getVisibility()) {
                    String tvDateText = holder.mTvDate.getText().toString();
                    holder.mTvOnePartyDate.setText(tvDateText);
                    holder.mTvOnePartyDate.setVisibility(View.VISIBLE);
                    holder.mTvExpiresIn.setVisibility(View.GONE);
                    holder.mTvDate.setVisibility(View.GONE);
                    holder.mTvOnePartyDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }

            }

        }
    }

    public class MatchesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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

        RelativeLayout mRlPartyBLayout;

        HmImageView mIvPartyAPhoto;

        HmImageView mIvPartyBPhoto;

        CustomButton mBtnMatchPoints;

        CustomButton mBtnPlayMatch;

        public TextView mTvDate;

        Button mBtnMatchLock;

        TextView mTvExpiresIn;

        TextView mTvOnePartyDate;

        public MatchesViewHolder(View V) {
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
            mBtnMatchLock = (Button) V.findViewById(R.id.schedule_row_btn_match_locked);
            mTvExpiresIn = (TextView) V.findViewById(R.id.schedule_row_tv_expires_in);
            mRlPartyBLayout = (RelativeLayout) V.findViewById(R.id.schedule_rl_party_b);
            mTvOnePartyDate = (TextView) V.findViewById(R.id.schedule_row_tv_one_party_date);

            mLlCardLayout = (LinearLayout) V.findViewById(R.id.schedule_row_ll);

            mBtnMatchLock.setOnClickListener(this);
            mBtnPlayMatch.setOnClickListener(this);
            mBtnMatchPoints.setOnClickListener(this);
            mLlResultWait.setOnClickListener(this);
            mTvInfo.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

//            /* Only for 'NEW' screen/section, no click action should be listened as No Match is yet Joined
//             * But Joining (on play clicked) should be allowed */
//            if (mThisScreenCategory.equalsIgnoreCase(Constants.ChallengeTabs.NEW)) {
//                if (view.getId() == R.id.schedule_row_btn_playmatch &&
//                        !mChallengeInfo.getChallengeUserInfo().isUserJoined()) {
//
//                    mChallengeTimelineAdapterListener.showChallengeJoinDialog(mChallengeInfo);
//                }
//                return;
//            }

            Context context = view.getContext();
            Bundle bundle = null;

            Match match = (Match) view.getTag();
            if (null != match) {
                bundle = new Bundle();
                // Nostragamus.getInstance().getServerDataManager().setMatchInfo(Match);
                bundle.putParcelable(Constants.BundleKeys.MATCH_LIST, Parcels.wrap(match));
                bundle.putString(Constants.BundleKeys.SCREEN, Constants.ScreenNames.PROFILE);

                if (null != match.getSportId()) {
                    bundle.putInt(Constants.BundleKeys.SPORT_ID, match.getSportId());
                }
            }

            switch (view.getId()) {
                case R.id.schedule_row_btn_playmatch:
                    NostragamusAnalytics.getInstance().trackTimeline(
                            Constants.GameAttemptedStatus.PARTIALLY == match.getIsAttempted() ? Constants.AnalyticsActions.CONTINUE : Constants.AnalyticsActions.PLAY
                    );

                    if (null != mChallengeInfo) {
                        //Nostragamus.getInstance().getServerDataManager().setChallengeInfo(mChallengeInfo);
                        bundle.putParcelable(Constants.BundleKeys.CHALLENGE_INFO, Parcels.wrap(mChallengeInfo));
                    }
                    navigateToPrediction(context, bundle);
                    break;

                case R.id.schedule_row_btn_points:
                    NostragamusAnalytics.getInstance().trackTimeline(Constants.AnalyticsActions.VIEW_ANSWERS);
                    navigateToMyResults(context, bundle);
                    break;

                case R.id.schedule_row_ll_waiting_for_result:
                    NostragamusAnalytics.getInstance().trackTimeline(Constants.AnalyticsActions.RESULT_WAITING);
                    navigateToMyResults(context, bundle);
                    break;

                case R.id.schedule_row_tv_info:
                    NostragamusAnalytics.getInstance().trackTimeline(Constants.AnalyticsActions.DID_NOT_PLAY);
                    navigateToMyResults(context, bundle);
                    break;

                case R.id.schedule_row_btn_match_locked:
                    matchesTimelineAdapterListener.showJoinContest(mChallengeInfo);
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

    private Map<View, MatchesTimelineAdapter.MatchesViewHolder> mScheduleMap = new HashMap<>();

    private List<MatchesTimelineAdapter.MatchesViewHolder> mScheduleVHList = new ArrayList<>();

    private class TimerRunnable implements Runnable {

        private Handler customHandler;

        private TimerRunnable() {
            customHandler = new Handler();
            customHandler.postDelayed(this, 0);
        }

        public void run() {
            for (MatchesTimelineAdapter.MatchesViewHolder scheduleVH : mScheduleVHList) {
                if (View.VISIBLE == scheduleVH.mTvExpiresIn.getVisibility()) {

                    long updatedTime;
                    boolean isOnePartyMatch = false;
                    if (scheduleVH.mTvExpiresIn.getTag() != null) {
                        updatedTime = Long.parseLong(scheduleVH.mTvExpiresIn.getTag().toString());
                    } else {
                        updatedTime = Long.parseLong(scheduleVH.mTvOnePartyDate.getTag().toString());
                        isOnePartyMatch = true;
                    }

                    if (updatedTime > 1000) {
                        if (isOnePartyMatch) {
                            scheduleVH.mTvOnePartyDate.setVisibility(View.VISIBLE);
                            updateTimer(scheduleVH.mTvOnePartyDate, updatedTime);
                        } else {
                            updateTimer(scheduleVH.mTvExpiresIn, updatedTime);
                        }
                    } else {
                        scheduleVH.mTvExpiresIn.setVisibility(View.GONE);
                        if (isOnePartyMatch) {
                            scheduleVH.mTvOnePartyDate.setVisibility(View.VISIBLE);
                            scheduleVH.mTvOnePartyDate.setText("In Progress");
                        } else {
                            scheduleVH.mTvDate.setVisibility(View.VISIBLE);
                            scheduleVH.mTvDate.setText("In Progress");
                        }
                    }
                }
            }

            customHandler.postDelayed(this, 800);
        }

        private void updateTimer(TextView tvTimerValue, long updatedTime) {
            try {
                tvTimerValue.setTag(updatedTime - 1000);

                int secs = (int) (updatedTime / 1000);
                int mins = secs / 60;
                int hours = mins / 60;
                int days = hours / 24;
                hours = hours % 24;
                mins = mins % 60;
                secs = secs % 60;

                SpannableStringBuilder builder = new SpannableStringBuilder();


                Typeface latoBold = Typeface.createFromAsset(tvTimerValue.getContext().getAssets(), "fonts/lato/Lato-Bold.ttf");
                TypefaceSpan latoBoldSpan = new CustomTypefaceSpan("", latoBold);

                String hoursTxt = " " + String.format("%02d", hours);
                SpannableString hoursTxtSpannable = new SpannableString(hoursTxt);
                hoursTxtSpannable.setSpan(latoBoldSpan, 0, hoursTxt.length(), 0);
                builder.append(hoursTxtSpannable);

                String hoursTxt2 = "h ";
                SpannableString hoursTxt2Spannable = new SpannableString(hoursTxt2);
                hoursTxt2Spannable.setSpan(new ForegroundColorSpan(Color.WHITE), 0, hoursTxt2.length(), 0);
                builder.append(hoursTxt2Spannable);

                TypefaceSpan latoBoldSpan2 = new CustomTypefaceSpan("", latoBold);
                String minsTxt = " " + String.format("%02d", mins);
                SpannableString minsTxtSpannable = new SpannableString(minsTxt);
                minsTxtSpannable.setSpan(latoBoldSpan2, 0, minsTxt.length(), 0);
                builder.append(minsTxtSpannable);

                String minsTxt2 = "m ";
                SpannableString minsTxt2Spannable = new SpannableString(minsTxt2);
                minsTxt2Spannable.setSpan(new ForegroundColorSpan(Color.WHITE), 0, minsTxt2.length(), 0);
                builder.append(minsTxt2Spannable);

                TypefaceSpan latoBoldSpan3 = new CustomTypefaceSpan("", latoBold);
                String secTxt = " " + String.format("%02d", secs);
                SpannableString secTxtSpannable = new SpannableString(secTxt);
                secTxtSpannable.setSpan(latoBoldSpan3, 0, secTxt.length(), 0);
                builder.append(secTxtSpannable);

                String secTxt2 = "s";
                SpannableString secTxt2Spannable = new SpannableString(secTxt2);
                secTxt2Spannable.setSpan(new ForegroundColorSpan(Color.WHITE), 0, secTxt2.length(), 0);
                builder.append(secTxt2Spannable);

                tvTimerValue.setText(builder, TextView.BufferType.SPANNABLE);

                tvTimerValue.setCompoundDrawablesWithIntrinsicBounds(R.drawable.timer_icon, 0, 0, 0);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private void destroy() {
            customHandler.removeCallbacks(this);
            customHandler = null;
        }

    }

}
