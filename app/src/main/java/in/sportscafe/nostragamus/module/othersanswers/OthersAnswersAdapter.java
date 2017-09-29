package in.sportscafe.nostragamus.module.othersanswers;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.ShadowLayout;

import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.nostraHome.NostraHomeActivity;
import in.sportscafe.nostragamus.module.resultspeek.dto.Match;
import in.sportscafe.nostragamus.module.resultspeek.dto.Question;
import in.sportscafe.nostragamus.module.user.playerprofile.PlayerProfileActivity;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by Jeeva on 15/6/16.
 */
public class OthersAnswersAdapter extends Adapter<Match, OthersAnswersAdapter.ViewHolder> {

    private PlayerInfo mPlayerInfo;

    private int answerId;

    private Resources mResources;

    public OthersAnswersAdapter(Context context) {
        super(context);
        mResources = context.getResources();
    }

    public void setPlayerInfo(PlayerInfo playerInfo) {
        this.mPlayerInfo = playerInfo;
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

        if (position == 0 && null != mPlayerInfo) {
            holder.mLlTourParent.addView(getPlayerView(mPlayerInfo, holder.mLlTourParent));
        }

        holder.mLlTourParent.addView(getMyResultView(getItem(position), holder.mLlTourParent));
    }

    private View getPlayerView(PlayerInfo playerInfo, ViewGroup parent) {
        View playerView = getLayoutInflater().inflate(R.layout.inflater_player_points, parent, false);
        PlayerViewHolder holder = new PlayerViewHolder(playerView);

        holder.mTvPlayerName.setText(playerInfo.getUserNickName());
        holder.mTvPlayerPoints.setText(playerInfo.getTotalPoints() + "");
        holder.mIvPlayerPhoto.setImageUrl(playerInfo.getPhoto());

        return playerView;
    }

    class PlayerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTvPlayerName;

        TextView mTvPlayerPoints;

        HmImageView mIvPlayerPhoto;

        public PlayerViewHolder(View V) {
            super(V);

            mTvPlayerName = (TextView) V.findViewById(R.id.player_points_tv_name);
            mTvPlayerPoints = (TextView) V.findViewById(R.id.player_points_tv_points);
            mIvPlayerPhoto = (HmImageView) V.findViewById(R.id.player_points_iv_photo);
            V.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Integer playerId = mPlayerInfo.getId();

            if (playerId.equals(NostragamusDataHandler.getInstance().getUserId())) {
                Intent homeintent = new Intent(view.getContext(), NostraHomeActivity.class);
                homeintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                homeintent.putExtra("group", "openprofile");
                view.getContext().startActivity(homeintent);
            } else {
                Bundle mBundle = new Bundle();
                mBundle.putInt(Constants.BundleKeys.PLAYER_ID, playerId);
                Intent mintent2 = new Intent(view.getContext(), PlayerProfileActivity.class);
                mintent2.putExtras(mBundle);
                view.getContext().startActivity(mintent2);
            }
        }
    }

    private View getMyResultView(Match match, ViewGroup parent) {
        View myResultView = getLayoutInflater().inflate(R.layout.inflater_schedule_match_results_row, parent, false);
        MyResultViewHolder holder = new MyResultViewHolder(myResultView);

        String startTime = match.getStartTime().replace("+00:00", ".000Z");
//        String startTime = "2017-01-27T18:00:00.000Z";
        long startTimeMs = TimeUtils.getMillisecondsFromDateString(
                startTime,
                Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                Constants.DateFormats.GMT
        );

        int dayOfMonth = Integer.parseInt(TimeUtils.getDateStringFromMs(startTimeMs, "d"));
        // Setting date of the Match
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


        if (null == match.getResult() || match.getResult().isEmpty()) {
            holder.mTvMatchResult.setVisibility(View.GONE);
            holder.mTvResultWait.setVisibility(View.VISIBLE);
            holder.mViewResult.setVisibility(View.VISIBLE);
            holder.mTvResultWait.setText(match.getMatchQuestionCount() + " predictions made, waiting for results");
            holder.mTvResultWait.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        } else {
            holder.mTvMatchResult.setVisibility(View.VISIBLE);
            holder.mTvMatchResult.setText(match.getStage() + " - " + match.getResult());
        }

        holder.mBtnMatchPoints.setText(String.valueOf(match.getAvgMatchPoints().intValue()));
        holder.mRlAvgMatchPoints.setVisibility(View.GONE);
        holder.mRlHighestMatchPoints.setVisibility(View.GONE);
        holder.mRlLeaderBoard.setVisibility(View.GONE);
        holder.mTvMatchPointsTxt.setText("Average Score");
        holder.mTvNumberofPowerupsUsed.setText(String.valueOf(match.getCountMatchPowerupsUsed()));
        holder.mTvResultCorrectCount.setText(match.getCountMatchPlayers().toString() + " People Answered");

        List<Question> questions = match.getQuestions();
        for (Question question : questions) {
            holder.mLlPredictionsParent.addView(getMyPrediction(holder.mLlPredictionsParent, question));
        }

        return myResultView;
    }


    class MyResultViewHolder extends RecyclerView.ViewHolder {

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
        View mViewResult;

        HmImageView mIvPartyAPhoto;
        TextView mTvResultWait;
        HmImageView mIvPartyBPhoto;

        RelativeLayout mRlMatchPoints;
        TextView mTvMatchPointsTxt;

        RelativeLayout mRlAvgMatchPoints;
        TextView mTvAvgMatchPoints;
        RelativeLayout mRlHighestMatchPoints;
        TextView mTvHighestMatchPoints;
        TextView mTvLeaderBoardRank;
        TextView mTvNumberofPowerupsUsed;
        RelativeLayout mRlLeaderBoard;
        ShadowLayout mSlScrores;

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
            mViewResult = V.findViewById(R.id.schedule_row_v_result_line);
            mLlPredictionsParent = (LinearLayout) V.findViewById(R.id.my_results_row_ll_predictions);
            mTvResultWait = (TextView) V.findViewById(R.id.schedule_row_tv_match_result_wait);
            mleaderboard = (LinearLayout) V.findViewById(R.id.my_results_row_ll_leaderboardbtn);

            mTvAvgMatchPoints = (TextView) V.findViewById(R.id.schedule_row_tv_average_score);
            mTvHighestMatchPoints = (TextView) V.findViewById(R.id.schedule_row_tv_highest_score);
            mTvLeaderBoardRank = (TextView) V.findViewById(R.id.schedule_row_tv_leaderboard_rank);
            mTvNumberofPowerupsUsed = (TextView) V.findViewById(R.id.schedule_row_tv_no_of_powerups_used);
            mRlLeaderBoard = (RelativeLayout) V.findViewById(R.id.schedule_row_rl_leaderboard);
            mSlScrores = (ShadowLayout) V.findViewById(R.id.schedule_row_scores_sl);
            mSlScrores.setPadding(0, 0, 0, 0);
            mRlAvgMatchPoints = (RelativeLayout) V.findViewById(R.id.schedule_row_rl_average_score);
            mRlHighestMatchPoints = (RelativeLayout) V.findViewById(R.id.schedule_row_rl_highest_score);
            mRlMatchPoints = (RelativeLayout) V.findViewById(R.id.schedule_row_rl_my_score);
            mTvMatchPointsTxt = (TextView) V.findViewById(R.id.schedule_row_tv_my_score_txt);
        }
    }


    private View getMyPrediction(ViewGroup parent, final Question question) {
        View convertView = getLayoutInflater().inflate(R.layout.others_answer_adapter_row_item, parent, false);

        ((TextView) convertView.findViewById(R.id.my_predictions_row_tv_question))
                .setText(question.getQuestionText().replace("\n", ""));


        TextView tvAnswerPoints = (TextView) convertView.findViewById(R.id.my_predictions_row_tv_answer_points);
        final ImageView mFlipPowerUp = (ImageView) convertView.findViewById(R.id.powerup_flip);

        final TextView tvOption1 = (TextView) convertView.findViewById(R.id.my_predictions_row_tv_option1_answer);
        final TextView tvOption2 = (TextView) convertView.findViewById(R.id.my_predictions_row_tv_option2_answer);
        final TextView tvOption3 = (TextView) convertView.findViewById(R.id.my_predictions_row_tv_option3_answer);

        tvOption1.setCompoundDrawablePadding(10);
        tvOption2.setCompoundDrawablePadding(10);
        tvOption3.setCompoundDrawablePadding(10);

        if (question.getAnswerPoints() != null) {

            if (question.getAnswerPoints().equals(0)) {
                setTextColor(tvAnswerPoints, R.color.textcolorlight);
            }

            if (question.getAnswerPoints() > 0) {
                tvAnswerPoints.setText("+" + question.getAnswerPoints() + " Points");
            } else {
                tvAnswerPoints.setText(question.getAnswerPoints() + " Points");
            }
        }

        if (-1 != question.getOption1AudPollPer()) {
            int maxPercent = Math.max(Math.max(question.getOption1AudPollPer(), question.getOption2AudPollPer()), question.getOption3AudPollPer());
            tvAnswerPoints.setVisibility(View.INVISIBLE);

            setPercentPoll((TextView) convertView.findViewById(R.id.my_predictions_row_tv_perc_1), question.getOption1AudPollPer(), maxPercent);
            setPercentPoll((TextView) convertView.findViewById(R.id.my_predictions_row_tv_perc_2), question.getOption2AudPollPer(), maxPercent);

            if (!TextUtils.isEmpty(question.getQuestionOption3())) {
                setPercentPoll((TextView) convertView.findViewById(R.id.my_predictions_row_tv_perc_3), question.getOption3AudPollPer(), maxPercent);
            }
        }

//        int powerupIcons = PowerUp.getResultPowerupIcons(question.getAnswerPowerUpId());
//        if(powerupIcons == -1) {
//            powerupUsed.setVisibility(View.GONE);
//            powerup.setVisibility(View.GONE);
//        } else {
//            powerupUsed.setVisibility(View.VISIBLE);
//            powerupUsed.setBackgroundResource(powerupIcons);
//        }

        showOrHidePowerUps(question, convertView);

        answerId = question.getAnswerId();


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
        /* if played Match but not attempted Question */
        else if (answerId == 0) {

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

                /* If your Answer = Option 1 , Set tick icon next to option 1 */
                if (question.getQuestionAnswer() == 1) {
                    setTextColor(tvOption2, R.color.white_60);
                    tvOption1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.correct_ans_drawable, 0);
                    tvOption2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_cross_icon, 0);

                }   /* If your Answer = Option 2 , Set tick icon next to option 2 */ else {
                    setTextColor(tvOption1, R.color.white_60);
                    tvOption2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.correct_ans_drawable, 0);
                    tvOption1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_cross_icon, 0);
                }

                if (!TextUtils.isEmpty(question.getQuestionOption3())) {
                    tvOption3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_cross_icon, 0);
                }

            }

             /* If your Answer = Option 3 , Set tick icon next to option 3 */
            if (answerId == 3) {
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
                    setTextColor(tvOption2, R.color.white_60);
                    tvOption1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_cross_icon, 0);
                    tvOption2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.correct_ans_drawable, 0);
                } else {
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
                    setTextColor(tvOption2, R.color.white_60);
                } else {
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
                tvOption3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_cross_icon, 0);
            }

        }

        return convertView;
    }

    private void showOrHidePowerUps(Question question, View convertView) {
        LinearLayout powerUpLayout = (LinearLayout) convertView.findViewById(R.id.powerup_bottom_layout);
        ImageView powerUp2xImageView = (ImageView) convertView.findViewById(R.id.my_predictions_answer_powerup_used_2x);
        ImageView powerUpNoNegativeImageView = (ImageView) convertView.findViewById(R.id.my_predictions_answer_powerup_used_noNeg);
        ImageView powerUpAudienceImageView = (ImageView) convertView.findViewById(R.id.my_predictions_answer_powerup_used_audience);

        TextView powerUp2xTextView = (TextView) convertView.findViewById(R.id.my_predictions_answer_powerup_used_2x_count);
        TextView powerUpNoNegativeTextView = (TextView) convertView.findViewById(R.id.my_predictions_answer_powerup_used_noNeg_count);
        TextView powerUpAudienceTextView = (TextView) convertView.findViewById(R.id.my_predictions_answer_powerup_used_audience_count);


        int powerUp2xCount = question.getTotal2xPowerupsUsed();
        int powerUpNonNegsCount = question.getTotalNoNegsPowerupsUsed();
        int powerUpPlayerPollCount = question.getTotalPlayerPollPowerupsUsed();

        if (powerUp2xCount == 0 && powerUpNonNegsCount == 0 && powerUpPlayerPollCount == 0) {
            powerUpLayout.setVisibility(View.GONE);
        } else {
            powerUpLayout.setVisibility(View.VISIBLE);

            if (powerUp2xCount != 0) {
                powerUp2xImageView.setBackgroundResource(R.drawable.double_powerup_small);
                powerUp2xImageView.setVisibility(View.VISIBLE);
                powerUp2xTextView.setText(String.valueOf(powerUp2xCount));
            } else {
                powerUp2xImageView.setVisibility(View.GONE);
                powerUp2xTextView.setVisibility(View.GONE);
            }

            if (powerUpNonNegsCount != 0) {
                powerUpNoNegativeImageView.setBackgroundResource(R.drawable.no_negative_powerup_small);
                powerUpNoNegativeImageView.setVisibility(View.VISIBLE);
                powerUpNoNegativeTextView.setText(String.valueOf(powerUpNonNegsCount));
            } else {
                powerUpNoNegativeImageView.setVisibility(View.GONE);
                powerUpNoNegativeTextView.setVisibility(View.GONE);
            }

            if (powerUpPlayerPollCount != 0) {
                powerUpAudienceImageView.setBackgroundResource(R.drawable.audience_poll_powerup_small);
                powerUpAudienceImageView.setVisibility(View.VISIBLE);
                powerUpAudienceTextView.setText(String.valueOf(powerUpPlayerPollCount));
            } else {
                powerUpAudienceImageView.setVisibility(View.GONE);
                powerUpAudienceTextView.setVisibility(View.GONE);
            }
        }
    }


    private void setPercentPoll(TextView textView, int percent, int maxPercent) {
        int width = mResources.getDimensionPixelSize(R.dimen.dp_150);
        int height = mResources.getDimensionPixelOffset(R.dimen.dp_16);

        textView.setVisibility(View.VISIBLE);
        textView.setText(percent + "%");
        textView.setBackground(getPercentDrawable(
                width,
                height,
                width * percent / 100,
                mResources.getColor(percent >= maxPercent ? R.color.mine_shaft_3d : R.color.mine_shaft_32)
        ));
    }

    private Drawable getPercentDrawable(int fullWidth, int fullHeight, int percentWidth, int percentColor) {
        Bitmap outputBitmap = Bitmap.createBitmap(fullWidth, fullHeight, Bitmap.Config.ARGB_4444);
        Canvas outputCanvas = new Canvas(outputBitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(percentColor);
        paint.setStyle(Paint.Style.FILL);

        outputCanvas.drawRect(fullWidth - percentWidth, 0, fullWidth, fullHeight, paint);
        return new BitmapDrawable(mResources, outputBitmap);
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
}