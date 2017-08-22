package in.sportscafe.nostragamus.module.play.myresults;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.ShadowLayout;

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.LBLandingType;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.common.EnhancedLinkMovementMethod;
import in.sportscafe.nostragamus.module.common.NostraTextViewLinkClickMovementMethod;
import in.sportscafe.nostragamus.module.feed.FeedWebView;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.othersanswers.OthersAnswersActivity;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.resultspeek.ResultsPeekActivity;
import in.sportscafe.nostragamus.module.user.lblanding.LbLanding;
import in.sportscafe.nostragamus.module.user.points.PointsActivity;
import in.sportscafe.nostragamus.utils.timeutils.TimeAgo;
import in.sportscafe.nostragamus.utils.timeutils.TimeUnit;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by Jeeva on 15/6/16.
 */
public class MyResultsAdapter extends Adapter<Match, MyResultsAdapter.ViewHolder> implements View.OnClickListener {

    public static final String SAVE_ANSWER_STR = "Save Answer";
    private OnMyResultsActionListener mResultsActionListener;

    private boolean mSaveAnswer = false;

    private int answerId;

    private RadioGroup mRadioGroup;

    private int mAnsweredQuestionCount = 0;

    private boolean mIsMyResults = true;

    private RelativeLayout mRlEditAnswers;

    private boolean mIsMatchStarted = false;
    private long mMatchStartTimeMs = 0;

    public MyResultsAdapter(Context context, boolean isMyResults) {
        super(context);
        this.mIsMyResults = isMyResults;
    }

    public void setResultsActionListener(OnMyResultsActionListener mResultsActionListener) {
        this.mResultsActionListener = mResultsActionListener;
    }

    @Override
    public Match getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayoutInflater().inflate(R.layout.inflater_feed_match_result_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mPosition = position;
        holder.mLlTourParent.removeAllViews();
        holder.mLlTourParent.addView(getMyResultView(getItem(position), holder.mLlTourParent, position));
    }

    private View getMyResultView(Match match, ViewGroup parent, int position) {
        View myResultView = getLayoutInflater().inflate(R.layout.inflater_schedule_match_results_row, parent, false);
        MyResultViewHolder holder = new MyResultViewHolder(myResultView);

        String startTime = match.getStartTime().replace("+00:00", ".000Z");
        com.jeeva.android.Log.d("StartTime", startTime);
//        String startTime = "2017-01-27T18:00:00.000Z";
        long startTimeMs = TimeUtils.getMillisecondsFromDateString(
                startTime,
                Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                Constants.DateFormats.GMT
        );

        mMatchStartTimeMs = startTimeMs;

        int dayOfMonth = Integer.parseInt(TimeUtils.getDateStringFromMs(startTimeMs, "d"));
        // Setting date of the match
        holder.mTvDate.setText(dayOfMonth + AppSnippet.ordinalOnly(dayOfMonth) + " " +
                TimeUtils.getDateStringFromMs(startTimeMs, "MMM") + ", "
                + TimeUtils.getDateStringFromMs(startTimeMs, Constants.DateFormats.HH_MM_AA)
        );


        if (null == match.getStage()) {
            holder.mTvMatchStage.setVisibility(View.GONE);
        } else {
            holder.mTvMatchStage.setText(match.getStage());
        }

        if (match.getParties() == null) {
            holder.mTvOnePartyName.setText(match.getTopics().getTopicName());
            holder.mIvOnePartyImage.setImageUrl(match.getTopics().getTopicUrl());
            holder.mLlMultiParty.setVisibility(View.GONE);
            holder.mLlOneParty.setVisibility(View.VISIBLE);
            holder.mTvOnePartyDate.setText(holder.mTvDate.getText().toString());

            RelativeLayout.LayoutParams paramsFour = (RelativeLayout.LayoutParams) holder.mLeaderBoardLayout.getLayoutParams();
            paramsFour.addRule(RelativeLayout.BELOW, R.id.schedule_row_one_party_ll);
            paramsFour.setMargins(20, 30, 20, 20);
            holder.mLeaderBoardLayout.setLayoutParams(paramsFour);

        } else {
            holder.mTvPartyAName.setText(match.getParties().get(0).getPartyName());
            holder.mTvPartyBName.setText(match.getParties().get(1).getPartyName());
            holder.mIvPartyAPhoto.setImageUrl(
                    match.getParties().get(0).getPartyImageUrl()
            );

            holder.mIvPartyBPhoto.setImageUrl(
                    match.getParties().get(1).getPartyImageUrl()
            );
        }


//        if (null == match.getResult() || match.getResult().isEmpty()) {
//            holder.mTvMatchResult.setVisibility(View.GONE);
//            holder.mTvResultWait.setVisibility(View.VISIBLE);
//            holder.mTvResultWait.setText(match.getStage()+" - "+"Awaiting Results");
//            holder.mTvResultWait.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//            holder.mTvResultCorrectCount.setText(match.getQuestions().size() +"/" + match.getMatchQuestionCount() + " Questions Answered");
//
//            if (null != match.getUserRank()) {
//                holder.mTvLeaderBoardRank.setText("Rank " + String.valueOf(match.getUserRank()) + "/" + String.valueOf(match.getCountPlayers()));
//            }else {
//                holder.mTvLeaderBoardRank.setText("No Rank");
//            }
//
//            if (null != match.getCountPowerUps()) {
//                holder.mTvNumberofPowerupsUsed.setText(String.valueOf(match.getCountPowerUps()));
//            }else {
//                holder.mTvNumberofPowerupsUsed.setText("0");
//            }
//
//        } else {
//            holder.mTvMatchResult.setVisibility(View.VISIBLE);
//            holder.mTvMatchResult.setText(match.getStage()+" - "+match.getResult());
//        }
//
//
//        if (null == match.getMatchPoints()) {
//
//            holder.mBtnMatchPoints.setVisibility(View.GONE);
//            holder.mLlMatchScores.setVisibility(View.GONE);
//
//        } else {
//            holder.mBtnMatchPoints.setVisibility(View.VISIBLE);
//            holder.mTvResultCorrectCount.setVisibility(View.VISIBLE);
//            holder.mTvResultWait.setVisibility(View.GONE);
//            holder.mBtnMatchPoints.setText(match.getMatchPoints().toString());
//            holder.mTvResultCorrectCount.setText(match.getCorrectCount() + "/" + match.getMatchQuestionCount() + " Answered Correctly");
//            holder.mTvAvgMatchPoints.setText(String.valueOf(match.getAvgMatchPoints().intValue()));
//            holder.mTvHighestMatchPoints.setText(String.valueOf(match.getHighestMatchPoints()));
//
//            if (null != match.getUserRank()) {
//                holder.mTvLeaderBoardRank.setText("Rank " + String.valueOf(match.getUserRank()) + "/" + String.valueOf(match.getCountPlayers()));
//            }else {
//                holder.mTvLeaderBoardRank.setText("No Rank");
//            }
//
//            if (null !=match.getRankChange()) {
//                holder.mIvRankStatus.setVisibility(View.VISIBLE);
//
//                if (match.getRankChange() < 0) {
//                    holder.mIvRankStatus.setImageResource(R.drawable.lb_rank_change_icon);
//                    holder.mIvRankStatus.setRotation(180);
//                } else {
//                    holder.mIvRankStatus.setImageResource(R.drawable.lb_rank_change_icon);
//                }
//            }
//
//            holder.mTvNumberofPowerupsUsed.setText(String.valueOf(match.getCountPowerUps()));
//            holder.mRlHighestMatchPoints.setTag(match);
//        }


        Integer attemptedStatus = match.getisAttempted();

        if (match.getMatchQuestionCount() > 0) {

            //if results published
            if (match.isResultPublished()) {

                holder.mTvMatchResult.setVisibility(View.VISIBLE);
                if (match.getParties() == null) {
                    holder.mTvOnePartyMatchResult.setText(match.getResult());
                } else {
                    holder.mTvMatchResult.setText(match.getStage() + " - " + match.getResult());
                }
                holder.mTvAvgMatchPoints.setText(String.valueOf(match.getAvgMatchPoints().intValue()));
                holder.mTvHighestMatchPoints.setText(String.valueOf(match.getHighestMatchPoints()));
                holder.mRlHighestMatchPoints.setTag(match);

                if (null != match.getUserRank()) {
                    holder.mTvLeaderBoardRank.setText("Rank " + String.valueOf(match.getUserRank()) + "/" + String.valueOf(match.getCountPlayers()));
                } else {
                    holder.mTvLeaderBoardRank.setText("No Rank");
                }

                if (null != match.getRankChange()) {
                    holder.mIvRankStatus.setVisibility(View.VISIBLE);

                    if (match.getRankChange() < 0) {
                        holder.mIvRankStatus.setImageResource(R.drawable.lb_rank_change_icon);
                        holder.mIvRankStatus.setRotation(180);
                    } else {
                        holder.mIvRankStatus.setImageResource(R.drawable.lb_rank_change_icon);
                    }
                }


                //if not attempted
                if (null == match.getisAttempted() || Constants.GameAttemptedStatus.NOT == attemptedStatus) {

                    holder.mBtnMatchPoints.setText("Did Not Play");
                    holder.mBtnMatchPoints.setTextSize(14);
                    holder.mTvNumberofPowerupsUsed.setText("No");
                    holder.mTvResultCorrectCount.setText("Not Answered");
                    holder.mTvMatchPointsTxt.setVisibility(View.GONE);

                    //if partially or completely attempted
                } else if (Constants.GameAttemptedStatus.COMPLETELY == attemptedStatus || Constants.GameAttemptedStatus.PARTIALLY == attemptedStatus) {

                    holder.mBtnMatchPoints.setVisibility(View.VISIBLE);
                    holder.mTvResultCorrectCount.setVisibility(View.VISIBLE);
                    holder.mTvResultWait.setVisibility(View.GONE);
                    holder.mBtnMatchPoints.setText(match.getMatchPoints().toString());
                    holder.mTvResultCorrectCount.setText(match.getCorrectCount() + "/" + match.getMatchQuestionCount() + " Answered Correctly");
                    holder.mTvNumberofPowerupsUsed.setText(String.valueOf(match.getCountPowerUps()));

                }
                //if results not published
            } else {

                holder.mBtnMatchPoints.setVisibility(View.GONE);
                holder.mLlMatchScores.setVisibility(View.GONE);
                holder.mTvMatchResult.setVisibility(View.GONE);
                holder.mTvOnePartyMatchResultWait.setVisibility(View.GONE);

                if (match.getParties() == null) {
                    holder.mTvOnePartyMatchResultWait.setVisibility(View.VISIBLE);
                    holder.mTvOnePartyMatchResultWait.setText("Awaiting Results");
                    holder.mTvOnePartyMatchResultWait.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else {
                    holder.mTvResultWait.setVisibility(View.VISIBLE);
                    holder.mTvResultWait.setText(match.getStage() + " - " + "Awaiting Results");
                    holder.mTvResultWait.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }

                if (null != match.getQuestions()) {
                    for (Question question : match.getQuestions()) {
                        if (question.getAnswerId() != 0) {
                            mAnsweredQuestionCount++;
                        }
                    }
                }

                holder.mTvResultCorrectCount.setText(String.valueOf(mAnsweredQuestionCount) + "/" + match.getMatchQuestionCount() + " Questions Answered");

                if (null != match.getUserRank()) {
                    holder.mTvLeaderBoardRank.setText("Rank " + String.valueOf(match.getUserRank()) + "/" + String.valueOf(match.getCountPlayers()));
                } else {
                    holder.mTvLeaderBoardRank.setText("No Rank");
                }

                if (null != match.getCountPowerUps()) {
                    holder.mTvNumberofPowerupsUsed.setText(String.valueOf(match.getCountPowerUps()));
                } else {
                    holder.mTvNumberofPowerupsUsed.setText("0");
                }

                if (null == match.getisAttempted() || Constants.GameAttemptedStatus.NOT == attemptedStatus) {

                    holder.mTvNumberofPowerupsUsed.setText("No");
                    holder.mTvResultCorrectCount.setText("Not Answered");


                }
            }
        }

        initMatchStarted(startTimeMs);

        List<Question> questions = match.getQuestions();
        for (Question question : questions) {
            holder.mLlPredictionsParent.addView(getMyPrediction(holder.mLlPredictionsParent, question, position));
        }

        if (match.isResultPublished() && mIsMyResults) {
            holder.mleaderboard.addView(getLeaderBoardView(holder.mleaderboard, match));
        }

        return myResultView;
    }

    /**
     * Check that the match started or not
     */
    private void initMatchStarted(long matchStartTimeMs) {
        long timeSpent = matchStartTimeMs - Nostragamus.getInstance().getServerTime();
        mIsMatchStarted = timeSpent <= 0;
    }

    class MyResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTvDate;

        TextView mTvMatchStage;

        TextView mTvPartyAName;

        TextView mTvPartyAScore;

        TextView mTvPartyBName;

        TextView mTvPartyBScore;

        TextView mTvMatchResult;

        TextView mTvStartTime;

        LinearLayout mLlPredictionsParent;

        LinearLayout mleaderboard;

        TextView mTvResultCorrectCount;
        TextView mBtnMatchPoints;

        HmImageView mIvPartyAPhoto;
        TextView mTvResultWait;
        HmImageView mIvPartyBPhoto;

        RelativeLayout mRlAvgMatchPoints;
        TextView mTvAvgMatchPoints;
        RelativeLayout mRlHighestMatchPoints;
        TextView mTvHighestMatchPoints;
        TextView mTvLeaderBoardRank;
        TextView mTvNumberofPowerupsUsed;
        RelativeLayout mRlLeaderBoard;
        LinearLayout mLlMatchScores;
        ImageView mIvRankStatus;
        TextView mTvMatchPointsTxt;

        LinearLayout mLlMultiParty;

        LinearLayout mLlOneParty;
        HmImageView mIvOnePartyImage;
        TextView mTvOnePartyName;
        TextView mTvOnePartyDate;
        TextView mTvOnePartyMatchResultWait;
        TextView mTvOnePartyMatchResult;


        ShadowLayout mLeaderBoardLayout;


        public MyResultViewHolder(View V) {
            super(V);

            mTvDate = (TextView) V.findViewById(R.id.schedule_row_tv_date);
            mTvMatchStage = (TextView) V.findViewById(R.id.schedule_row_tv_match_stage);
            mTvPartyAName = (TextView) V.findViewById(R.id.schedule_row_tv_party_a_name);
            mTvPartyBName = (TextView) V.findViewById(R.id.schedule_row_tv_party_b_name);
            mTvMatchResult = (TextView) V.findViewById(R.id.schedule_row_tv_match_result);
            mTvStartTime = (TextView) V.findViewById(R.id.schedule_row_tv_match_time);
            mIvPartyAPhoto = (HmImageView) V.findViewById(R.id.swipe_card_iv_left);
            mIvPartyBPhoto = (HmImageView) V.findViewById(R.id.swipe_card_iv_right);
            mTvResultCorrectCount = (TextView) V.findViewById(R.id.schedule_row_tv_match_correct_questions);
            mBtnMatchPoints = (TextView) V.findViewById(R.id.schedule_row_tv_my_score);
            mTvMatchPointsTxt = (TextView) V.findViewById(R.id.schedule_row_tv_my_score_txt);
            mLlPredictionsParent = (LinearLayout) V.findViewById(R.id.my_results_row_ll_predictions);
            mTvResultWait = (TextView) V.findViewById(R.id.schedule_row_tv_match_result_wait);
            mleaderboard = (LinearLayout) V.findViewById(R.id.my_results_row_ll_leaderboardbtn);
            mTvAvgMatchPoints = (TextView) V.findViewById(R.id.schedule_row_tv_average_score);
            mTvHighestMatchPoints = (TextView) V.findViewById(R.id.schedule_row_tv_highest_score);
            mTvLeaderBoardRank = (TextView) V.findViewById(R.id.schedule_row_tv_leaderboard_rank);
            mTvNumberofPowerupsUsed = (TextView) V.findViewById(R.id.schedule_row_tv_no_of_powerups_used);
            mRlLeaderBoard = (RelativeLayout) V.findViewById(R.id.schedule_row_rl_leaderboard);
            mRlAvgMatchPoints = (RelativeLayout) V.findViewById(R.id.schedule_row_rl_average_score);
            mRlHighestMatchPoints = (RelativeLayout) V.findViewById(R.id.schedule_row_rl_highest_score);
            mLlMatchScores = (LinearLayout) V.findViewById(R.id.schedule_row_scores_ll);
            mIvRankStatus = (ImageView) V.findViewById(R.id.schedule_row_rank_status_iv);

            mLlMultiParty = (LinearLayout) V.findViewById(R.id.schedule_row_ll);

            mLlOneParty = (LinearLayout) V.findViewById(R.id.schedule_row_one_party_ll);
            mIvOnePartyImage = (HmImageView) V.findViewById(R.id.swipe_card_one_party_iv_left);
            mTvOnePartyName = (TextView) V.findViewById(R.id.schedule_row_tv_one_party_a_name);
            mTvOnePartyDate = (TextView) V.findViewById(R.id.schedule_row_tv_one_party_date);
            mTvOnePartyMatchResultWait = (TextView) V.findViewById(R.id.schedule_row_one_party_tv_match_result_wait);
            mTvOnePartyMatchResult = (TextView) V.findViewById(R.id.schedule_row_one_party_tv_match_result);

            mLeaderBoardLayout = (ShadowLayout) V.findViewById(R.id.schedule_row_rl_points_summary);


            mRlLeaderBoard.setOnClickListener(this);
            mRlAvgMatchPoints.setOnClickListener(this);
            mRlHighestMatchPoints.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.schedule_row_rl_leaderboard:
                    navigateToLeaderboards(v.getContext());
                    break;

                case R.id.schedule_row_rl_average_score:
                    navigateToOthersAnswers(v.getContext());
                    break;

                case R.id.schedule_row_rl_highest_score:

                    Match match = (Match) v.getTag();

                    Bundle bundle = new Bundle();
                    bundle.putInt(BundleKeys.PLAYER_ID, match.getHighestScorerId());
                    bundle.putInt(BundleKeys.MATCH_ID, match.getId());
                    bundle.putString(BundleKeys.PLAYER_NAME, match.getHighestScorerName());
                    bundle.putString(BundleKeys.PLAYER_PHOTO, match.getHighestScorerPhoto());
                    navigateToResultsPeek(v.getContext(), bundle);

                    NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.COMPARE_PROFILE, Constants.AnalyticsActions.OPENED);

                    break;
            }

        }
    }


    private View getMyPrediction(final ViewGroup parent, final Question question, final int position) {

        final View convertView = getLayoutInflater().inflate(R.layout.inflater_my_predictions_row, parent, false);
        convertView.setId(position);    // A unique id of dynamically created view

        ((TextView) convertView.findViewById(R.id.my_predictions_row_tv_question))
                .setText(question.getQuestionText().replace("\n", ""));

        mRlEditAnswers = (RelativeLayout) convertView.findViewById(R.id.my_results_rl_edit_answers);
        final TextView tvAnswerPoints = (TextView) convertView.findViewById(R.id.my_predictions_row_tv_answer_points);
        final TextView tvOption1 = (TextView) convertView.findViewById(R.id.my_predictions_row_tv_option1_answer);
        final TextView tvOption2 = (TextView) convertView.findViewById(R.id.my_predictions_row_tv_option2_answer);
        final TextView tvOption3 = (TextView) convertView.findViewById(R.id.my_predictions_row_tv_option3_answer);

        showOrHidePowerUps(question, convertView);

        /*tvAnswer.setCompoundDrawablePadding(10);
        tvotheroption.setCompoundDrawablePadding(10);
        tvNeitherAnswer.setCompoundDrawablePadding(10);*/

        answerId = question.getAnswerId();

        if (question.getAnswerPoints() != null) {

            if (question.getAnswerPoints().equals(0)) {
                setTextColor(tvAnswerPoints, R.color.white_60);
            }

            if (question.getAnswerPoints() > 0) {
                tvAnswerPoints.setText("+" + question.getAnswerPoints() + " Points");
            } else {
                tvAnswerPoints.setText(question.getAnswerPoints() + " Points");
            }

            tvAnswerPoints.setVisibility(View.VISIBLE);
            mRlEditAnswers.setVisibility(View.GONE);

        } else {
            tvAnswerPoints.setVisibility(View.GONE);
            mRlEditAnswers.setVisibility(View.VISIBLE);
        }


           /* BEFORE THE RESULT IS PUBLISHED SHOW ANSWERS */
        if (null == question.getQuestionAnswer()) {

            tvOption1.setText(question.getQuestionOption1());
            tvOption2.setText(question.getQuestionOption2());

            tvOption2.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(question.getQuestionOption3())) {
                tvOption3.setVisibility(View.VISIBLE);
                tvOption3.setText(question.getQuestionOption3());
            }

            if (answerId == 0) {

                /* if question not answered then answerId=0 , set All Options Color as grey */
                setTextColor(tvOption1, R.color.white_60);
                setTextColor(tvOption2, R.color.white_60);
                if (!TextUtils.isEmpty(question.getQuestionOption3())) {
                    setTextColor(tvOption3, R.color.white_60);
                }

            } else {

                /* if answer is 1st option , set Option1 Color as white and other grey */
                if (answerId == 1) {
                    setTextColor(tvOption1, R.color.white);
                    setTextColor(tvOption2, R.color.white_60);
                    setTextColor(tvOption3, R.color.white_60);

                }  /* if answer is 2nd option , set Option2 Color as white and other grey */ else {
                    setTextColor(tvOption2, R.color.white);
                    setTextColor(tvOption1, R.color.white_60);
                    setTextColor(tvOption3, R.color.white_60);
                }

                 /* if answer is 3rd option , set Option3 Color as white and others grey */
                if (!TextUtils.isEmpty(question.getQuestionOption3()) && answerId == 3) {
                    setTextColor(tvOption1, R.color.white_60);
                    setTextColor(tvOption2, R.color.white_60);
                    setTextColor(tvOption3, R.color.white);
                }
            }

        }
        /* if played match but not attempted Question */
        else if (answerId == 0) {

            /* We can't edit answer if not attempted a Question */
            mRlEditAnswers.setVisibility(View.GONE);

            /* Set All options color as grey */
            tvOption1.setText(question.getQuestionOption1());
            tvOption2.setText(question.getQuestionOption2());

            tvOption2.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(question.getQuestionOption3())) {
                tvOption3.setVisibility(View.VISIBLE);
                tvOption3.setText(question.getQuestionOption3());
                setTextColor(tvOption3, R.color.white_60);
            }

            setTextColor(tvOption1, R.color.white_60);
            setTextColor(tvOption2, R.color.white_60);

            if (question.getQuestionAnswer() == 1 || question.getQuestionAnswer() == 2) {
                if (question.getQuestionAnswer() == 1) {
                    tvOption1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.correct_ans_drawable, 0);

                } else {
                    tvOption2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.correct_ans_drawable, 0);
                }
            }

            if (!TextUtils.isEmpty(question.getQuestionOption3())) {
                if (question.getQuestionAnswer() == 3) {
                    tvOption3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.correct_ans_drawable, 0);
                }
            }


        }
        /* if YOUR ANSWER = CORRECT ANSWER */
        else if (answerId == question.getQuestionAnswer()) {

            tvOption1.setText(question.getQuestionOption1());
            tvOption2.setText(question.getQuestionOption2());

            tvOption2.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(question.getQuestionOption3())) {
                tvOption3.setVisibility(View.VISIBLE);
                tvOption3.setText(question.getQuestionOption3());
            }

           /* IF USER ANSWER = OPTION 1 OR OPTION 2 */

            if (question.getQuestionAnswer() == 1 || question.getQuestionAnswer() == 2) {

                /* If your Answer = Option 1 , Set Option 1 color as green and tick icon next to option 1 */
                if (question.getQuestionAnswer() == 1) {
                    setTextColor(tvOption1, R.color.greencolor);
                    setTextColor(tvOption2, R.color.white_60);
                    tvOption1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.correct_ans_drawable, 0);
                    tvOption2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_cross_icon, 0);

                }   /* If your Answer = Option 2 , Set Option 2 color as green and tick icon next to option 2 */ else {
                    setTextColor(tvOption2, R.color.greencolor);
                    setTextColor(tvOption1, R.color.white_60);
                    tvOption2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.correct_ans_drawable, 0);
                    tvOption1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_cross_icon, 0);
                }

                if (!TextUtils.isEmpty(question.getQuestionOption3())) {
                    tvOption3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_cross_icon, 0);
                }

            }

             /* If your Answer = Option 3 , Set Option 3 color as green and tick icon next to option 3 */
            if (answerId == 3) {
                setTextColor(tvOption3, R.color.greencolor);
                setTextColor(tvOption1, R.color.white_60);
                setTextColor(tvOption2, R.color.white_60);
                tvOption3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.correct_ans_drawable, 0);
                tvOption1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_cross_icon, 0);
                tvOption2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_cross_icon, 0);
            }

        }

        /* If questionAns == -1, means question was invalid ; Don't highlight anything and show split*/
        else if (question.getQuestionAnswer() == -1) {

            tvOption1.setText(question.getQuestionOption1());
            tvOption2.setText(question.getQuestionOption2());

            tvOption2.setVisibility(View.VISIBLE);

            /* Set All Options Color as grey */
            setTextColor(tvOption1, R.color.white_60);
            setTextColor(tvOption2, R.color.white_60);

             /* Set All Options Compound Drawables as null */
            tvOption1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            tvOption2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

            if (!TextUtils.isEmpty(question.getQuestionOption3())) {
                tvOption3.setText(question.getQuestionOption3());
                tvOption3.setVisibility(View.VISIBLE);
                setTextColor(tvOption3, R.color.white_60);
                tvOption3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            convertView.findViewById(R.id.my_predictions_row_splitView).setVisibility(View.VISIBLE);
        }

        /* if your answer is incorrect and other option is correct */
        else {

            tvOption1.setText(question.getQuestionOption1());
            tvOption2.setText(question.getQuestionOption2());

            tvOption2.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(question.getQuestionOption3())) {
                tvOption3.setVisibility(View.VISIBLE);
                tvOption3.setText(question.getQuestionOption3());
            }

            // IF USER ANSWER = OPTION 1 OR OPTION 2

            if (question.getQuestionAnswer() == 1 || question.getQuestionAnswer() == 2) {

                if (answerId == 1) {
                    setTextColor(tvOption1, R.color.tabcolor);
                    setTextColor(tvOption2, R.color.white_60);
                    tvOption1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_cross_icon, 0);
                    tvOption2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.correct_ans_drawable, 0);
                } else {
                    setTextColor(tvOption2, R.color.tabcolor);
                    setTextColor(tvOption1, R.color.white_60);
                    tvOption1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.correct_ans_drawable, 0);
                    tvOption2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_cross_icon, 0);
                }

                if (!TextUtils.isEmpty(question.getQuestionOption3())) {
                    setTextColor(tvOption3, R.color.white_60);
                    tvOption3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_cross_icon, 0);
                }
            } else if (question.getQuestionAnswer() == 3) {

                if (answerId == 1) {
                    setTextColor(tvOption1, R.color.tabcolor);
                    setTextColor(tvOption2, R.color.white_60);
                } else {
                    setTextColor(tvOption2, R.color.tabcolor);
                    setTextColor(tvOption1, R.color.white_60);
                }

                setTextColor(tvOption3, R.color.white_60);
                tvOption1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_cross_icon, 0);
                tvOption2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_cross_icon, 0);
                tvOption3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.correct_ans_drawable, 0);

            }

            // IF USER ANSWER = NEITHER
            if (answerId == 3) {

                if (question.getQuestionAnswer() == 1) {
                    tvOption1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.correct_ans_drawable, 0);
                    tvOption2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_cross_icon, 0);
                } else {
                    tvOption1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_cross_icon, 0);
                    tvOption2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.correct_ans_drawable, 0);
                }
                setTextColor(tvOption1, R.color.white_60);
                setTextColor(tvOption2, R.color.white_60);
                setTextColor(tvOption3, R.color.tabcolor);
                tvOption3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_cross_icon, 0);
            }

        }

        /* Hide Edit button if match started  */
        showOrHideEditAnswerButton();

        /* Showing graph based on percentage */
        showGraphPercentage(question, convertView);

        /* edit Answer on click of Button and change edit Answer to save answer, If saving Answer ,callapi*/
        handleEditAnswerButtonClick(parent, question, convertView, tvOption1,
                tvOption2, tvOption3);

        return convertView;
    }

    private void showGraphPercentage(Question question, View convertView) {
        if (question.getAnswer1percent() != null && question.getAnswer2percent() != null) {
            int maxPercent = Math.max(Math.max(question.getAnswer1percent(), question.getAnswer2percent()), question.getAnswer3percent());

            setPercentPoll((TextView) convertView.findViewById(R.id.my_predictions_row_tv_perc_1), question.getAnswer1percent(), maxPercent);
            setPercentPoll((TextView) convertView.findViewById(R.id.my_predictions_row_tv_perc_2), question.getAnswer2percent(), maxPercent);

            if (question.getAnswer3percent() != null && question.getAnswer3percent() > 0) {
                setPercentPoll((TextView) convertView.findViewById(R.id.my_predictions_row_tv_perc_3), question.getAnswer3percent(), maxPercent);
            }
        }
    }

    private void showOrHideEditAnswerButton() {

        if (BuildConfig.IS_PAID_VERSION) {
            if (mIsMatchStarted && mRlEditAnswers != null) {
                // If match started, answers can not be edited
                mRlEditAnswers.setVisibility(View.GONE);
            }
        } else {
            // If Playstore App , answers can not be edited
            mRlEditAnswers.setVisibility(View.GONE);
        }
    }

    private void showOrHidePowerUps(Question question, View convertView) {
        LinearLayout powerUpLayout = (LinearLayout) convertView.findViewById(R.id.powerup_bottom_layout);
        ImageView powerUp2xImageView = (ImageView) convertView.findViewById(R.id.my_predictions_answer_powerup_used_2x);
        ImageView powerUpNoNegativeImageView = (ImageView) convertView.findViewById(R.id.my_predictions_answer_powerup_used_noNeg);
        ImageView powerUpAudienceImageView = (ImageView) convertView.findViewById(R.id.my_predictions_answer_powerup_used_audience);

        ArrayList<String> powerUpArrayList = question.getPowerUpArrayList();
        if (powerUpArrayList != null && !powerUpArrayList.isEmpty()) {
            for (int temp = 0; temp < powerUpArrayList.size(); temp++) {

                String powerUp = powerUpArrayList.get(temp);
                if (powerUp.equalsIgnoreCase(Constants.Powerups.XX)) {
                    powerUp2xImageView.setBackgroundResource(R.drawable.double_powerup_small);
                    powerUp2xImageView.setVisibility(View.VISIBLE);
                    powerUpLayout.setVisibility(View.VISIBLE);
                } else if (powerUp.equalsIgnoreCase(Constants.Powerups.NO_NEGATIVE)) {
                    powerUpNoNegativeImageView.setBackgroundResource(R.drawable.no_negative_powerup_small);
                    powerUpNoNegativeImageView.setVisibility(View.VISIBLE);
                    powerUpLayout.setVisibility(View.VISIBLE);
                } else if (powerUp.equalsIgnoreCase(Constants.Powerups.AUDIENCE_POLL)) {
                    powerUpAudienceImageView.setBackgroundResource(R.drawable.audience_poll_powerup_small);
                    powerUpAudienceImageView.setVisibility(View.VISIBLE);
                    powerUpLayout.setVisibility(View.VISIBLE);
                }
            }
        } else {
            powerUpLayout.setVisibility(View.GONE);
        }
    }

    /**
     * Edit Answers clicks
     *
     * @param parent
     * @param question
     * @param convertView
     * @param tvOption1
     * @param tvOption2
     * @param tvOption3
     */
    private void handleEditAnswerButtonClick(final ViewGroup parent, final Question question,
                                             final View convertView, final TextView tvOption1,
                                             final TextView tvOption2, final TextView tvOption3) {

        final Button editAnswersBtn = (Button) convertView.findViewById(R.id.my_results_btn_edit_answers);
        final ImageView mIvEditAnswers = (ImageView) convertView.findViewById(R.id.my_results_iv_edit_answers_icon);
        mIvEditAnswers.setBackground(ContextCompat.getDrawable(mIvEditAnswers.getContext(), R.drawable.edit_answers_icon));

        editAnswersBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                /* If match not started, then only save */
                initMatchStarted(mMatchStartTimeMs);
                if (!mIsMatchStarted) {

                /* check if btn is save Answer , call save Answer Api , hide radio buttons , show Answers View. */
                    if (mSaveAnswer) {

                    /* check if other Question edit Answer btn is clicked, if yes don't do anything else save Answer */
                        if (question.getEditAnswerQuestionId() == question.getQuestionId()) {
                            int selectedId = mRadioGroup.getCheckedRadioButtonId();
                            View radioButton = mRadioGroup.findViewById(selectedId);
                            int selectedIdx = mRadioGroup.indexOfChild(radioButton);
                            int selectedAnswerId = 0;

                            if (selectedIdx == 0) {
                                selectedAnswerId = 1;
                            } else if (selectedIdx == 1) {
                                selectedAnswerId = 2;
                            } else if (selectedIdx == 2) {
                                selectedAnswerId = 3;
                            }


                            onClickSaveAnswer(question, selectedAnswerId, tvOption1, tvOption2, tvOption3);


                        /* call save Answer Api */
                            mResultsActionListener.saveUpdatedAnswer(question.getQuestionId(), selectedAnswerId);

                        /* change save Answer btn back to edit Answer */
                            editAnswersBtn.setText("Edit Answer");
                            mIvEditAnswers.setBackground(ContextCompat.getDrawable(mIvEditAnswers.getContext(), R.drawable.edit_answers_icon));

                            showEditQuestionButtons(parent);

                        } else {

                        }
                    } else { /* Show radio buttons , hide Answers View and change edit Answer btn to Save Answer */
                        tvOption1.setVisibility(View.GONE);
                        tvOption2.setVisibility(View.GONE);
                        tvOption3.setVisibility(View.GONE);
                        mIvEditAnswers.setBackground(ContextCompat.getDrawable(mIvEditAnswers.getContext(), R.drawable.edit_answers_tick));
                        editAnswersBtn.setText(SAVE_ANSWER_STR);
                        question.setEditAnswerQuestionId(question.getQuestionId());
                        onClickEditAnswer(convertView, question);

                        hideEditQuestionButtons(parent);
                    }

                } else {
                    Toast.makeText(parent.getContext(), "Oops! Match already started.", Toast.LENGTH_SHORT).show();
                    hideEditQuestionButtons(parent);
                }
            }
        });
    }

    private void hideEditQuestionButtons(ViewGroup parentView) {
        if (parentView != null) {
            for (int i = 0; i < parentView.getChildCount(); i++) {
                View view = parentView.getChildAt(i);
                Button button = (Button) view.findViewById(R.id.my_results_btn_edit_answers);
                String btnText = button.getText().toString().trim();
                if (!TextUtils.isEmpty(btnText) && !btnText.equals(SAVE_ANSWER_STR)) {
                    View editAnswerView = view.findViewById(R.id.my_results_rl_edit_answers);
                    editAnswerView.setVisibility(View.INVISIBLE);   // This operation is performed with all added views
                }
            }
        }
    }

    private void showEditQuestionButtons(ViewGroup parentView) {
        if (parentView != null) {
            for (int i = 0; i < parentView.getChildCount(); i++) {
                View view = parentView.getChildAt(i);
                view.findViewById(R.id.my_results_rl_edit_answers).setVisibility(View.VISIBLE);
            }
        }
    }

    /* Update Answer and set Selected AnswerId */
    private void onClickSaveAnswer(Question question, int selectedAnswerId, TextView tvOption1, TextView tvOption2, TextView tvOption3) {

        mSaveAnswer = false;
        mRadioGroup.setVisibility(View.GONE);
        tvOption1.setVisibility(View.VISIBLE);
        tvOption2.setVisibility(View.VISIBLE);

        question.setAnswerId(selectedAnswerId);

        tvOption1.setText(question.getQuestionOption1());
        tvOption2.setText(question.getQuestionOption2());

        if (!TextUtils.isEmpty(question.getQuestionOption3())) {
            tvOption3.setText(question.getQuestionOption3());
            tvOption3.setVisibility(View.VISIBLE);
        }

        if (selectedAnswerId == 1) {
            setTextColor(tvOption1, R.color.white);
            setTextColor(tvOption2, R.color.white_60);

            if (!TextUtils.isEmpty(question.getQuestionOption3())) {
                setTextColor(tvOption3, R.color.white_60);
            }

        } else if (selectedAnswerId == 2) {

            setTextColor(tvOption1, R.color.white_60);
            setTextColor(tvOption2, R.color.white);

            if (!TextUtils.isEmpty(question.getQuestionOption3())) {
                setTextColor(tvOption3, R.color.white_60);
            }

        } else if (selectedAnswerId == 3) {

            setTextColor(tvOption1, R.color.white_60);
            setTextColor(tvOption2, R.color.white_60);

            if (!TextUtils.isEmpty(question.getQuestionOption3())) {
                setTextColor(tvOption3, R.color.white);
            }
        }

    }

    /* Show Radio Buttons to change Answer */
    private void onClickEditAnswer(View convertView, Question question) {

        mSaveAnswer = true;
        mRadioGroup = (RadioGroup) convertView.findViewById(R.id.my_predictions_rg);
        final RadioButton cbAnswer = (RadioButton) convertView.findViewById(R.id.my_predictions_row_rb_answer);
        final RadioButton cbOtherAnswer = (RadioButton) convertView.findViewById(R.id.my_predictions_row_rb_correct_answer);
        final RadioButton cbNeitherAnswer = (RadioButton) convertView.findViewById(R.id.my_predictions_row_rb_neither_answer);

        mRadioGroup.setVisibility(View.VISIBLE);
        cbAnswer.setText(question.getQuestionOption1());
        cbOtherAnswer.setText(question.getQuestionOption2());

        if (!TextUtils.isEmpty(question.getQuestionOption3())) {
            cbNeitherAnswer.setVisibility(View.VISIBLE);
            cbNeitherAnswer.setText(question.getQuestionOption3());
        } else {
            cbNeitherAnswer.setVisibility(View.GONE);
        }

        if (question.getAnswerId() == 1) {
            cbAnswer.setChecked(true);
            cbOtherAnswer.setChecked(false);
            cbNeitherAnswer.setChecked(false);
        } else if (question.getAnswerId() == 2) {
            cbOtherAnswer.setChecked(true);
            cbAnswer.setChecked(false);
            cbNeitherAnswer.setChecked(false);
        } else if (question.getAnswerId() == 3) {
            cbNeitherAnswer.setChecked(true);
            cbOtherAnswer.setChecked(false);
            cbAnswer.setChecked(false);
        }

    }

    private void setPercentPoll(TextView textView, int percent, int maxPercent) {
        Resources resources = textView.getContext().getResources();

        int width = resources.getDimensionPixelSize(R.dimen.dp_150);
        int height = resources.getDimensionPixelOffset(R.dimen.dp_16);

        textView.setVisibility(View.VISIBLE);
        textView.setText(percent + "%");
        textView.setBackground(getPercentDrawable(resources,
                width,
                height,
                width * percent / 100,
                resources.getColor(percent >= maxPercent ? R.color.mine_shaft_3d : R.color.mine_shaft_32)
        ));
    }

    private Drawable getPercentDrawable(Resources resources,
                                        int fullWidth, int fullHeight, int percentWidth, int percentColor) {
        Bitmap outputBitmap = Bitmap.createBitmap(fullWidth, fullHeight, Bitmap.Config.ARGB_4444);
        Canvas outputCanvas = new Canvas(outputBitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(percentColor);
        paint.setStyle(Paint.Style.FILL);

        outputCanvas.drawRect(fullWidth - percentWidth, 0, fullWidth, fullHeight, paint);
        return new BitmapDrawable(resources, outputBitmap);
    }


    private View getLeaderBoardView(ViewGroup parent, Match match) {
        View leaderboardView = getLayoutInflater().inflate(R.layout.inflater_leaderboard_btn_row, parent, false);

        leaderboardView.findViewById(R.id.my_results_ll_others_answers).setOnClickListener(this);
        leaderboardView.findViewById(R.id.my_results_ll_leaderboards).setOnClickListener(this);
        leaderboardView.findViewById(R.id.my_results_ll_share_score).setOnClickListener(this);

        if (null != match.getResultdesc() && !match.getResultdesc().trim().isEmpty()) {
            final TextView tvcommentary = (TextView) leaderboardView.findViewById(R.id.schedule_row_tv_match_result_commentary);
            tvcommentary.setVisibility(View.VISIBLE);
            tvcommentary.setText(Html.fromHtml(match.getResultdesc()));
            tvcommentary.setMovementMethod(new NostraTextViewLinkClickMovementMethod() {
                @Override
                public void onLinkClick(String url) {
                    OpenWebView(tvcommentary,url);
                }
            });
        }

        return leaderboardView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_results_ll_others_answers:
                navigateToOthersAnswers(view.getContext());
                break;
            case R.id.my_results_ll_leaderboards:
                navigateToLeaderboards(view.getContext());
                break;
        }
    }

    private void navigateToOthersAnswers(Context context) {
        Intent intent = new Intent(context, OthersAnswersActivity.class);
        intent.putExtra(BundleKeys.MATCH_DETAILS, Parcels.wrap(getItem(0)));
        context.startActivity(intent);
    }

    private void navigateToResultsPeek(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ResultsPeekActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void navigateToLeaderboards(Context context) {
        Match match = getItem(0);

        LbLanding lbLanding = new LbLanding(
                match.getChallengeId(), 0, //groupid=0
                match.getChallengeName(),
                match.getChallengeImgUrl(),
                LBLandingType.CHALLENGE
        );

        Intent intent = new Intent(context, PointsActivity.class);
        intent.putExtra(BundleKeys.LB_LANDING_DATA, Parcels.wrap(lbLanding));
        intent.putExtra(BundleKeys.MATCH_ID, match.getId());
//        intent.putExtra(BundleKeys.TOURNAMENT_ID, match.getTournamentId());
        context.startActivity(intent);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        int mPosition;

        private LinearLayout mLlTourParent;

        public ViewHolder(View V) {
            super(V);

            mLlTourParent = (LinearLayout) V.findViewById(R.id.feed_row_ll_tour_parent);
        }
    }


    private void setTextColor(TextView textView, int color) {
        textView.setTextColor(textView.getResources().getColor(color));
    }

    public interface OnMyResultsActionListener {

        void onClickLeaderBoard(int position);

        void onClickEditAnswer(int selectedQuestionId, Question question);

        void saveUpdatedAnswer(int QuestionId, int AnswerId);
    }

    public void changeToEditAnswers(int selectedQuestionId, Question question) {

    }

    /**
     * On on click of link open NostragamusWebView Activity for handling links
     */
    private void OpenWebView(View view,String url) {
        if (url != null) {
            view.getContext().startActivity(new Intent(view.getContext(), FeedWebView.class).putExtra("url", url));
        }
    }

}