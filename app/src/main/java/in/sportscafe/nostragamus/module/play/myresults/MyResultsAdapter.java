package in.sportscafe.nostragamus.module.play.myresults;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.customfont.CustomButton;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.IntentActions;
import in.sportscafe.nostragamus.Constants.LBLandingType;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.othersanswers.OthersAnswersActivity;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.resultspeek.ResultsPeekActivity;
import in.sportscafe.nostragamus.module.user.lblanding.LbLanding;
import in.sportscafe.nostragamus.module.user.points.PointsActivity;
import in.sportscafe.nostragamus.module.user.powerups.PowerUp;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by Jeeva on 15/6/16.
 */
public class MyResultsAdapter extends Adapter<Match, MyResultsAdapter.ViewHolder> implements View.OnClickListener {

    private OnMyResultsActionListener mResultsActionListener;

    private AlertDialog mAlertDialog;

    private static final int CODE_PROFILE_ACTIVITY = 1;

    private boolean mSaveAnswer = false;

    private Boolean isShowFlipOptn = false;

    private Boolean isFlipclicked = true;

    private int answerId;

    private int pos;

    private boolean changeAnswers = false;

    private RadioGroup mRadioGroup;

    private int mAnsweredQuestionCount = 0;

    private boolean mIsMyResults = true;

    private RelativeLayout mRlEditAnswers;

    private int mEditAnswerQuestionId;

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
        holder.mLlTourParent.addView(getMyResultView(getItem(position), holder.mLlTourParent));
    }

    public void showFlipOptnforQuestion() {
        isShowFlipOptn = true;
    }

    private View getMyResultView(Match match, ViewGroup parent) {
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

        holder.mTvPartyAName.setText(match.getParties().get(0).getPartyName());
        holder.mTvPartyBName.setText(match.getParties().get(1).getPartyName());

        holder.mIvPartyAPhoto.setImageUrl(
                match.getParties().get(0).getPartyImageUrl()
        );

        holder.mIvPartyBPhoto.setImageUrl(
                match.getParties().get(1).getPartyImageUrl()
        );


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
                holder.mTvMatchResult.setText(match.getStage() + " - " + match.getResult());
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
                holder.mTvResultWait.setVisibility(View.VISIBLE);
                holder.mTvResultWait.setText(match.getStage() + " - " + "Awaiting Results");
                holder.mTvResultWait.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

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

        List<Question> questions = match.getQuestions();
        for (Question question : questions) {
            holder.mLlPredictionsParent.addView(getMyPrediction(holder.mLlPredictionsParent, question));
        }

        if (match.isResultPublished() && mIsMyResults) {
            holder.mleaderboard.addView(getLeaderBoardView(holder.mleaderboard, match));
        }

        return myResultView;
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
                    break;
            }

        }
    }


    private View getMyPrediction(ViewGroup parent, final Question question) {

        final View convertView = getLayoutInflater().inflate(R.layout.inflater_my_predictions_row, parent, false);

        ((TextView) convertView.findViewById(R.id.my_predictions_row_tv_question))
                .setText(question.getQuestionText().replace("\n", ""));


        final TextView tvAnswer = (TextView) convertView.findViewById(R.id.my_predictions_row_tv_answer);
        final TextView tvNeitherAnswer = (TextView) convertView.findViewById(R.id.my_predictions_row_tv_neither_answer);
        HmImageView powerupUsed = (HmImageView) convertView.findViewById(R.id.my_predictions_row_btn_answer_powerup_used);
        RelativeLayout powerup = (RelativeLayout) convertView.findViewById(R.id.my_predictions_row_rl);
        final TextView tvAnswerPoints = (TextView) convertView.findViewById(R.id.my_predictions_row_tv_answer_points);
        final TextView tvotheroption = (TextView) convertView.findViewById(R.id.my_predictions_row_tv_correct_answer);
        final ImageView mFlipPowerUp = (ImageView) convertView.findViewById(R.id.powerup_flip);

        mRlEditAnswers = (RelativeLayout) convertView.findViewById(R.id.my_results_rl_edit_answers);
        final Button editAnswersBtn = (Button) convertView.findViewById(R.id.my_results_btn_edit_answers);
        final ImageView mIvEditAnswers = (ImageView) convertView.findViewById(R.id.my_results_iv_edit_answers_icon);
        mIvEditAnswers.setBackground(ContextCompat.getDrawable(mIvEditAnswers.getContext(), R.drawable.edit_answers_icon));

        tvAnswer.setCompoundDrawablePadding(10);
        tvotheroption.setCompoundDrawablePadding(10);
        tvNeitherAnswer.setCompoundDrawablePadding(10);

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

            if (isShowFlipOptn == true) {
                mFlipPowerUp.setVisibility(View.VISIBLE);
                ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(convertView.getContext(), R.animator.flip_anim);
                anim.setTarget(mFlipPowerUp);
                anim.setDuration(3000);
                anim.setRepeatCount(ObjectAnimator.INFINITE);
                anim.setRepeatMode(ObjectAnimator.REVERSE);
                anim.start();

                final int answerId = question.getAnswerId();
                if (answerId == 1) {
                    tvotheroption.setText(question.getQuestionOption2());
                } else {
                    tvotheroption.setText(question.getQuestionOption1());
                }


                mFlipPowerUp.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(v.getContext());
                        alertDialogBuilder.setMessage("Are you sure, You want to apply Flip Powerup on this Question?");
                        alertDialogBuilder.setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        if (answerId == 1) {
                                            tvAnswer.setText(question.getQuestionOption2());
                                            tvotheroption.setText(question.getQuestionOption1());
                                        } else {
                                            tvAnswer.setText(question.getQuestionOption1());
                                            tvotheroption.setText(question.getQuestionOption2());
                                        }

                                    }
                                });

                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

//                        if(isFlipclicked==true)
//                        {
//                            mFlipPowerUp.setEnabled(false);
//                        }
//                        isFlipclicked =false;
//                        notifyDataSetChanged();
                    }
                });

                tvotheroption.setVisibility(View.VISIBLE);
                setTextColor(tvotheroption, R.color.white_60);
            }
        }


        answerId = question.getAnswerId();


        int powerupIcons = PowerUp.getResultPowerupIcons(question.getAnswerPowerUpId());
        if (powerupIcons == -1) {
            powerupUsed.setVisibility(View.GONE);
            powerup.setVisibility(View.GONE);
        } else {
            powerupUsed.setVisibility(View.VISIBLE);
            powerupUsed.setBackgroundResource(powerupIcons);
        }


        /* BEFORE THE RESULT IS PUBLISHED SHOW ANSWERS */
        if (null == question.getQuestionAnswer()) {

            tvotheroption.setVisibility(View.VISIBLE);
            setTextColor(tvAnswer, R.color.white);
            setTextColor(tvotheroption, R.color.white_60);
            setTextColor(tvNeitherAnswer, R.color.white_60);

            if (answerId == 0) {
                setTextColor(tvAnswer, R.color.tabcolor);
                tvAnswer.setText(question.getQuestionOption1());
                tvotheroption.setText(question.getQuestionOption2());
                setTextColor(tvAnswer, R.color.white_60);
                setTextColor(tvotheroption, R.color.white_60);
                if (!TextUtils.isEmpty(question.getQuestionOption3())) {
                    tvNeitherAnswer.setVisibility(View.VISIBLE);
                    tvNeitherAnswer.setText(question.getQuestionOption3());
                    setTextColor(tvNeitherAnswer, R.color.white_60);
                }

            } else {
                /* if answer is 1st option */
                if (answerId == 1) {
                    tvAnswer.setText(question.getQuestionOption1());
                    tvotheroption.setText(question.getQuestionOption2());

                }  /* if answer is 2nd option */ else {
                    tvAnswer.setText(question.getQuestionOption2());
                    tvotheroption.setText(question.getQuestionOption1());
                }

                 /* if answer is 3rd option */
                if (!TextUtils.isEmpty(question.getQuestionOption3()) && answerId == 3) {
                    tvNeitherAnswer.setVisibility(View.VISIBLE);
                    tvAnswer.setText(question.getQuestionOption3());
                    tvotheroption.setText(question.getQuestionOption1());
                    tvNeitherAnswer.setText(question.getQuestionOption2());
                } else if (!TextUtils.isEmpty(question.getQuestionOption3())) {
                    tvNeitherAnswer.setVisibility(View.VISIBLE);
                    tvNeitherAnswer.setText(question.getQuestionOption3());
                }
            }

        }
        /* if played match but not attempted Question */
        else if (answerId == 0) {
            mRlEditAnswers.setVisibility(View.GONE);
            setTextColor(tvAnswer, R.color.tabcolor);
            tvotheroption.setVisibility(View.VISIBLE);
            tvAnswer.setText(question.getQuestionOption1());
            tvotheroption.setText(question.getQuestionOption2());
            setTextColor(tvAnswer, R.color.white_60);
            setTextColor(tvotheroption, R.color.white_60);
            if (!TextUtils.isEmpty(question.getQuestionOption3())) {
                tvNeitherAnswer.setVisibility(View.VISIBLE);
                tvNeitherAnswer.setText(question.getQuestionOption3());
                setTextColor(tvNeitherAnswer, R.color.white_60);
            }

            if (question.getQuestionAnswer() == 1 || question.getQuestionAnswer() == 2) {
                if (question.getQuestionAnswer() == 1) {
                    tvAnswer.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_tick_icon, 0);

                } else {
                    tvotheroption.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_tick_icon, 0);
                }
            }

            if (!TextUtils.isEmpty(question.getQuestionOption3())) {
                if (question.getQuestionAnswer() == 3) {
                    tvNeitherAnswer.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_tick_icon, 0);
                }
            }


        }
        /* if your answer = correct answer */
        else if (answerId == question.getQuestionAnswer()) {

            setTextColor(tvAnswer, R.color.greencolor);
            setTextColor(tvNeitherAnswer, R.color.white_60);
            setTextColor(tvotheroption, R.color.white_60);
            tvAnswer.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_tick_icon, 0);
            tvotheroption.setVisibility(View.VISIBLE);
            tvotheroption.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_cross_icon, 0);
            tvNeitherAnswer.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_cross_icon, 0);


           /* IF USER ANSWER = OPTION 1 OR OPTION 2 */

            if (question.getQuestionAnswer() == 1 || question.getQuestionAnswer() == 2) {
                if (question.getQuestionAnswer() == 1) {
                    tvAnswer.setText(question.getQuestionOption1());
                    tvotheroption.setText(question.getQuestionOption2());

                } else {
                    tvAnswer.setText(question.getQuestionOption2());
                    tvotheroption.setText(question.getQuestionOption1());
                }

                if (!TextUtils.isEmpty(question.getQuestionOption3())) {
                    tvNeitherAnswer.setVisibility(View.VISIBLE);
                    tvNeitherAnswer.setText(question.getQuestionOption3());
                }
            }

            /* IF USER ANSWER = THIRD OPTION (NEITHER OR DRAW) */
            if (answerId == 3) {
                tvAnswer.setVisibility(View.VISIBLE);
                tvAnswer.setText(question.getQuestionOption3());
                tvotheroption.setText(question.getQuestionOption1());
                tvNeitherAnswer.setVisibility(View.VISIBLE);
                tvNeitherAnswer.setText(question.getQuestionOption2());
            }

        }  /* if your answer & other option both are correct */ else if (question.getQuestionAnswer() == 0) {

            Log.i("answer", "both correct");

            tvotheroption.setVisibility(View.VISIBLE);
            if (question.getQuestionAnswer() == 1) {
                tvotheroption.setText(question.getQuestionOption1());
                setTextColor(tvotheroption, R.color.white_60);
            } else {
                tvotheroption.setText(question.getQuestionOption2());
                setTextColor(tvotheroption, R.color.white_60);
            }
            tvotheroption.setVisibility(View.VISIBLE);
            tvotheroption.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_tick_icon, 0);
            tvAnswer.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_tick_icon, 0);
            setTextColor(tvAnswer, R.color.greencolor);

        }     /* If questionAns == -1, means question was invalid ; Don't highlight anything and show split*/ else if (question.getQuestionAnswer() == -1) {
            tvAnswer.setText(question.getQuestionOption1());
            tvotheroption.setText(question.getQuestionOption2());

            tvAnswer.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            tvotheroption.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

            if (!TextUtils.isEmpty(question.getQuestionOption3())) {
                tvNeitherAnswer.setText(question.getQuestionOption3());
                setTextColor(tvNeitherAnswer, R.color.white_60);
                tvNeitherAnswer.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            setTextColor(tvNeitherAnswer, R.color.white_60);
            setTextColor(tvAnswer, R.color.white_60);
            convertView.findViewById(R.id.my_predictions_row_splitView).setVisibility(View.VISIBLE);
        }  /* if your answer is incorrect and other option is correct */ else {
            setTextColor(tvAnswer, R.color.tabcolor);
            tvAnswer.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_cross_icon, 0);
            tvotheroption.setVisibility(View.VISIBLE);
            setTextColor(tvotheroption, R.color.white_60);
            tvotheroption.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_tick_icon, 0);

            // IF USER ANSWER = OPTION 1 OR OPTION 2

            if (question.getQuestionAnswer() == 1 || question.getQuestionAnswer() == 2) {

                if (question.getQuestionAnswer() == 1) {
                    tvAnswer.setText(question.getQuestionOption2());
                    tvotheroption.setText(question.getQuestionOption1());
                } else {
                    tvAnswer.setText(question.getQuestionOption1());
                    tvotheroption.setText(question.getQuestionOption2());
                }

                if (!TextUtils.isEmpty(question.getQuestionOption3())) {
                    tvNeitherAnswer.setVisibility(View.VISIBLE);
                    tvNeitherAnswer.setText(question.getQuestionOption3());
                    setTextColor(tvNeitherAnswer, R.color.white_60);
                    tvNeitherAnswer.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_cross_icon, 0);
                }
            } else if (question.getQuestionAnswer() == 3) {

                if (answerId == 1) {
                    tvAnswer.setText(question.getQuestionOption1());
                    tvotheroption.setText(question.getQuestionOption2());
                } else {
                    tvAnswer.setText(question.getQuestionOption2());
                    tvotheroption.setText(question.getQuestionOption1());
                }

                tvNeitherAnswer.setVisibility(View.VISIBLE);
                tvNeitherAnswer.setText(question.getQuestionOption3());
                setTextColor(tvNeitherAnswer, R.color.white_60);
                tvotheroption.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_cross_icon, 0);
                tvNeitherAnswer.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_tick_icon, 0);

            }

            // IF USER ANSWER = NEITHER
            if (answerId == 3) {
                if (question.getQuestionAnswer() == 1) {
                    tvotheroption.setText(question.getQuestionOption1());
                    tvNeitherAnswer.setText(question.getQuestionOption2());
                } else {
                    tvotheroption.setText(question.getQuestionOption2());
                    tvNeitherAnswer.setText(question.getQuestionOption1());
                }
                tvAnswer.setVisibility(View.VISIBLE);
                tvNeitherAnswer.setVisibility(View.VISIBLE);
                tvAnswer.setText(question.getQuestionOption3());
//                setTextColor(tvNeitherAnswer, R.color.tabcolor);
                tvNeitherAnswer.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_cross_icon, 0);
            }

        }

        /* Showing graph based on percentage */
        if (question.getAnswer1percent() != null && question.getAnswer2percent() != null) {
            int maxPercent = Math.max(Math.max(question.getAnswer1percent(), question.getAnswer2percent()), question.getAnswer3percent());

            setPercentPoll((TextView) convertView.findViewById(R.id.my_predictions_row_tv_perc_1), question.getAnswer1percent(), maxPercent);
            setPercentPoll((TextView) convertView.findViewById(R.id.my_predictions_row_tv_perc_2), question.getAnswer2percent(), maxPercent);

            if (question.getAnswer3percent() != null && question.getAnswer3percent() > 0) {
                setPercentPoll((TextView) convertView.findViewById(R.id.my_predictions_row_tv_perc_3), question.getAnswer3percent(), maxPercent);
            }
        }

        /* edit Answer on click of Button and change edit Answer to save answer, If saving Answer ,callapi*/
        editAnswersBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                
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

                        onClickSaveAnswer(question, selectedAnswerId, tvAnswer, tvotheroption, tvNeitherAnswer);

                        /* call save Answer Api */
                        mResultsActionListener.saveUpdatedAnswer(question.getQuestionId(), selectedAnswerId);

                        /* change save Answer btn back to edit Answer */
                        editAnswersBtn.setText("Edit Answer");
                        mIvEditAnswers.setBackground(ContextCompat.getDrawable(mIvEditAnswers.getContext(), R.drawable.edit_answers_icon));

                    } else {
                        // TODO: 5/19/17 The moment a guys clicks on edit answer hide the other edit answer options . the moment he saves it the other edit options comeback
                        Log.i("save:-->", "save Answer First then Edit Other Question Answer");
                    }
                } else { /* Show radio buttons , hide Answers View and change edit Answer btn to Save Answer */
                    tvAnswer.setVisibility(View.GONE);
                    tvotheroption.setVisibility(View.GONE);
                    tvNeitherAnswer.setVisibility(View.GONE);
                    mIvEditAnswers.setBackground(ContextCompat.getDrawable(mIvEditAnswers.getContext(), R.drawable.edit_answers_tick));
                    editAnswersBtn.setText("Save Answer");
                    question.setEditAnswerQuestionId(question.getQuestionId());
                    onClickEditAnswer(convertView, question);
                }
            }
        });


        return convertView;
    }

    /* Update Answer and set Selected AnswerId */
    private void onClickSaveAnswer(Question question, int selectedAnswerId, TextView tvAnswer, TextView tvOtherOption, TextView tvNeitherAnswer) {

        mSaveAnswer = false;
        mRadioGroup.setVisibility(View.GONE);
        tvAnswer.setVisibility(View.VISIBLE);
        tvOtherOption.setVisibility(View.VISIBLE);
        tvNeitherAnswer.setVisibility(View.VISIBLE);

        question.setAnswerId(selectedAnswerId);

        if (selectedAnswerId == 1) {
            tvAnswer.setText(question.getQuestionOption1());
            tvOtherOption.setText(question.getQuestionOption2());

            if (!TextUtils.isEmpty(question.getQuestionOption3())) {
                tvNeitherAnswer.setText(question.getQuestionOption3());
            }

        } else if (selectedAnswerId == 2) {

            tvAnswer.setText(question.getQuestionOption2());
            tvOtherOption.setText(question.getQuestionOption1());

            if (!TextUtils.isEmpty(question.getQuestionOption3())) {
                tvNeitherAnswer.setText(question.getQuestionOption3());
            }

        } else if (selectedAnswerId == 3) {

            tvAnswer.setText(question.getQuestionOption3());
            tvOtherOption.setText(question.getQuestionOption1());

            if (!TextUtils.isEmpty(question.getQuestionOption3())) {
                tvNeitherAnswer.setText(question.getQuestionOption2());
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
            TextView tvcommentary = (TextView) leaderboardView.findViewById(R.id.schedule_row_tv_match_result_commentary);
            tvcommentary.setVisibility(View.VISIBLE);
            tvcommentary.setText(Html.fromHtml(match.getResultdesc()));
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
}