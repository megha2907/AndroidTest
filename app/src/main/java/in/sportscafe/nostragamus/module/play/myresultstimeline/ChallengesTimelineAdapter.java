package in.sportscafe.nostragamus.module.play.myresultstimeline;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.customfont.CustomButton;
import com.jeeva.android.widgets.customfont.Typefaces;

import org.parceler.Parcels;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AnalyticsActions;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.DateFormats;
import in.sportscafe.nostragamus.Constants.GameAttemptedStatus;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.challenge.ChallengeTimelineAdapterListener;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.allchallenges.info.ChallengeConfigsDialogFragment;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.feed.dto.Parties;
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
public class ChallengesTimelineAdapter extends Adapter<Match, ChallengesTimelineAdapter.ScheduleViewHolder> {

    private static final String COMMENTARY = "commentary";

    private ChallengeTimelineAdapterListener mChallengeTimelineAdapterListener;
    private Context mContext;
    private Challenge mChallengeInfo;
    private String mThisScreenCategory = "";

    public ChallengesTimelineAdapter(Context context,
                                     @NonNull ChallengeTimelineAdapterListener listener,
                                     String thisScreenCategory) {
        super(context);
        mContext = context;
        mChallengeTimelineAdapterListener = listener;
        mThisScreenCategory = thisScreenCategory;
    }

    public void updateChallengeInfo(Challenge challengeInfo) {
        this.mChallengeInfo = challengeInfo;
    }

    @Override
    public Match getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScheduleViewHolder(getLayoutInflater().inflate(R.layout.inflater_other_view_schedule_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ScheduleViewHolder holder, int position) {
        populateMatchDetails(getItem(position), holder);
    }

    private void populateMatchDetails(Match match, ScheduleViewHolder holder) {
        holder.mBtnMatchPoints.setVisibility(View.GONE);
        holder.mTvInfo.setVisibility(View.GONE);
        holder.mTvMatchResult.setVisibility(View.GONE);
        holder.mLlResultWait.setVisibility(View.GONE);
        holder.mBtnPlayMatch.setVisibility(View.GONE);

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
        holder.mTvDate.setText(dayOfMonth + AppSnippet.ordinalOnly(dayOfMonth) + " " +
                TimeUtils.getDateStringFromMs(startTimeMs, "MMM") + ", "
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
            if (attemptedStatus == null) {
                attemptedStatus = 0;
            }

            holder.mTvMatchResult.setVisibility(View.VISIBLE);
            holder.mTvMatchResult.setText(match.getStage());

            if (mChallengeInfo.getChallengeUserInfo().isUserJoined()) {

                holder.mBtnMatchLock.setVisibility(View.GONE);

                if (match.getMatchQuestionCount() > 0) {

                    if (match.isResultPublished()) { // if match Result Published

                        //if match Completely Attempted then IsAttempted = 2 else if Partially Attempted then is Attempted =1
                        //show Match Results
                        if (GameAttemptedStatus.COMPLETELY == attemptedStatus || GameAttemptedStatus.PARTIALLY == attemptedStatus) {

                            holder.mBtnMatchPoints.setVisibility(View.VISIBLE);

                            holder.mTvMatchResult.setVisibility(View.VISIBLE);
                            holder.mTvMatchResult.setText(match.getStage() + " - " + Html.fromHtml(match.getResult()));
                            holder.mBtnMatchPoints.setTag(match);
                            holder.mBtnMatchPoints.setText(match.getMatchPoints() + " Points");

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
                            holder.mTvMatchResult.setText(match.getStage() + " - " + Html.fromHtml(match.getResult()));
                            holder.mTvInfo.setVisibility(View.VISIBLE);
                            holder.mTvInfo.setText("Did Not Play");
                            holder.mTvInfo.setTag(match);
                            holder.mTvInfo.setClickable(true);
                            holder.mTvInfo.setBackground(holder.mTvInfo.getContext().getResources().getDrawable(R.drawable.btn_not_played_shadow_bg));
                        }

                    } else { // if Results not published
                        if (GameAttemptedStatus.NOT == attemptedStatus || GameAttemptedStatus.PARTIALLY == attemptedStatus) {
                            if (isMatchStarted) {
                                if (attemptedStatus == GameAttemptedStatus.PARTIALLY) {

                                    //  Waiting for results
                                    holder.mLlResultWait.setVisibility(View.VISIBLE);
                                    holder.mTvMatchResult.setVisibility(View.VISIBLE);
//                                holder.mTvMatchResult.setText(match.getStage());
                                    holder.mLlResultWait.setTag(match);
                                } else {

                                    // You cannot play the match as the match already started
                                    holder.mTvInfo.setVisibility(View.VISIBLE);
                                    holder.mTvInfo.setText("Did Not Play");
                                    holder.mTvInfo.setTag(match);
                                    holder.mTvInfo.setClickable(true);
                                    holder.mTvInfo.setBackground(holder.mTvInfo.getContext().getResources().getDrawable(R.drawable.btn_not_played_shadow_bg));
                                }
                            } else {

                                // show Play button
                                holder.mBtnPlayMatch.setVisibility(View.VISIBLE);
                                holder.mBtnPlayMatch.setTag(match);
//                            holder.mTvMatchResult.setVisibility(View.VISIBLE);
//                            holder.mTvMatchResult.setText(match.getStage());


                                if (GameAttemptedStatus.PARTIALLY == attemptedStatus) {
                                    holder.mBtnPlayMatch.setAllCaps(false);
                                    holder.mBtnPlayMatch.setText(("Continue"));
                                }
                            }
                        } else if (attemptedStatus == GameAttemptedStatus.COMPLETELY) {
                            //  Waiting for results
                            holder.mLlResultWait.setVisibility(View.VISIBLE);
//                        holder.mTvMatchResult.setVisibility(View.VISIBLE);
//                        holder.mTvMatchResult.setText(match.getStage());
                            holder.mLlResultWait.setTag(match);
                        }

                    }
                } else { // No questions prepared
                    if (!isMatchStarted) { // Still the question is not prepared for these matches
                        holder.mTvInfo.setVisibility(View.VISIBLE);
                        holder.mTvInfo.setText("Coming Up");
                        holder.mTvInfo.setClickable(false);
                        holder.mTvInfo.setBackground(holder.mTvInfo.getContext().getResources().getDrawable(R.drawable.btn_not_played_bg));
//                    holder.mTvMatchResult.setVisibility(View.VISIBLE);
//                    holder.mTvMatchResult.setText(match.getStage());
                    }
                }
            } else {
                if (!mChallengeInfo.getCountMatchesLeft().equals("0")) {

                    holder.mBtnMatchLock.setVisibility(View.VISIBLE);
                    holder.mBtnMatchLock.setAllCaps(false);
                    holder.mBtnMatchLock.setTextSize(12);
                    Typeface newFont1=Typeface.createFromAsset(holder.mBtnMatchLock.getContext().getAssets(), "fonts/lato/Lato-Regular.ttf");
                    holder.mBtnMatchLock.setTypeface(newFont1);

                    if (match.getMatchQuestionCount() > 0) {

                        if (match.isResultPublished()) { // if match Result Published

                            //if match not Attempted then IsAttempted=0
                            if (GameAttemptedStatus.NOT == attemptedStatus) {
                                // Show Opportunity missed at scoring!
                                holder.mBtnMatchLock.setText("Completed");
                            }

                        } else { // if Results not published
                            if (GameAttemptedStatus.NOT == attemptedStatus || GameAttemptedStatus.PARTIALLY == attemptedStatus) {
                                if (isMatchStarted) {
                                    // You cannot play the match as the match already started
                                    holder.mBtnMatchLock.setText("Completed");
                                } else {
                                    // show Play button
                                    holder.mBtnMatchLock.setText("PLAY");
                                    holder.mBtnMatchLock.setTextSize(13);
                                    holder.mBtnMatchLock.setPadding(25,0,25,5);
                                    holder.mBtnMatchLock.setAllCaps(true);
                                    Typeface newFont=Typeface.createFromAsset(holder.mBtnMatchLock.getContext().getAssets(), "fonts/lato/Lato-Bold.ttf");
                                    holder.mBtnMatchLock.setTypeface(newFont);
                                }
                            }
                        }
                    } else { // No questions prepared
                        if (!isMatchStarted) { // Still the question is not prepared for these matches
                            holder.mBtnMatchLock.setText("Coming up");
                        }
                    }
                } else {
                    holder.mBtnMatchLock.setVisibility(View.GONE);
                    holder.mTvInfo.setVisibility(View.VISIBLE);
                    holder.mTvInfo.setText("Did Not Play");
                    holder.mTvInfo.setTag(match);
                    holder.mTvInfo.setClickable(true);
                    holder.mTvInfo.setBackground(holder.mTvInfo.getContext().getResources().getDrawable(R.drawable.btn_not_played_shadow_bg));
                }

            }
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

        Button mBtnMatchLock;

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
            mBtnMatchLock = (Button) V.findViewById(R.id.schedule_row_btn_match_locked);

            mLlCardLayout = (LinearLayout) V.findViewById(R.id.schedule_row_ll);

            mBtnMatchLock.setOnClickListener(this);
            mBtnPlayMatch.setOnClickListener(this);
            mBtnMatchPoints.setOnClickListener(this);
            mLlResultWait.setOnClickListener(this);
            mTvInfo.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

//            /* Only for 'NEW' screen/section, no click action should be listened as No match is yet Joined
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
                // Nostragamus.getInstance().getServerDataManager().setMatchInfo(match);
                bundle.putParcelable(BundleKeys.MATCH_LIST, Parcels.wrap(match));
                bundle.putString(BundleKeys.SCREEN, Constants.ScreenNames.PROFILE);

                if (null != match.getSportId()) {
                    bundle.putInt(BundleKeys.SPORT_ID, match.getSportId());
                }
            }

            switch (view.getId()) {
                case R.id.schedule_row_btn_playmatch:
                    NostragamusAnalytics.getInstance().trackTimeline(
                            GameAttemptedStatus.PARTIALLY == match.getisAttempted() ? AnalyticsActions.CONTINUE : AnalyticsActions.PLAY
                    );

                    if (null != mChallengeInfo) {
                        //Nostragamus.getInstance().getServerDataManager().setChallengeInfo(mChallengeInfo);
                        bundle.putParcelable(BundleKeys.CHALLENGE_INFO, Parcels.wrap(mChallengeInfo));
                    }
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

                case R.id.schedule_row_tv_info:
                    NostragamusAnalytics.getInstance().trackTimeline(AnalyticsActions.DID_NOT_PLAY);
                    navigateToMyResults(context, bundle);
                    break;

                case R.id.schedule_row_btn_match_locked:
                    mChallengeTimelineAdapterListener.showChallengeJoinDialog(mChallengeInfo);
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