package in.sportscafe.nostragamus.module.othersanswers;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.ShadowLayout;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.user.playerprofile.PlayerProfileActivity;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;
import in.sportscafe.nostragamus.module.user.powerups.PowerUp;

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
                Intent homeintent = new Intent(view.getContext(), HomeActivity.class);
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
            holder.mTvMatchResult.setText(match.getResult());
        }

        holder.mBtnMatchPoints.setText(String.valueOf(match.getAvgMatchPoints().intValue()));
        holder.mRlAvgMatchPoints.setVisibility(View.GONE);
        holder.mRlHighestMatchPoints.setVisibility(View.GONE);
        holder.mRlLeaderBoard.setVisibility(View.GONE);
        holder.mTvMatchPointsTxt.setText("Average Score");
        holder.mTvNumberofPowerupsUsed.setText(String.valueOf(match.getCountMatchPowerupsUsed()));
        holder.mTvResultCorrectCount.setText(match.getCountMatchPlayers().toString()+" People Answered");

        List<Question> questions = match.getQuestions();
        for (Question question : questions) {
            holder.mLlPredictionsParent.addView(getMyPrediction(holder.mLlPredictionsParent, question));
        }

        return myResultView;
    }


    class MyResultViewHolder extends RecyclerView.ViewHolder {


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

            mTvMatchStage = (TextView) V.findViewById(R.id.schedule_row_tv_match_stage);
            mTvPartyAName = (TextView) V.findViewById(R.id.schedule_row_tv_party_a_name);
            mTvPartyBName = (TextView) V.findViewById(R.id.schedule_row_tv_party_b_name);
            mTvMatchResult = (TextView) V.findViewById(R.id.schedule_row_tv_match_result);
            mTvStartTime = (TextView) V.findViewById(R.id.schedule_row_tv_match_time);
            mIvPartyAPhoto = (HmImageView) V.findViewById(R.id.swipe_card_iv_left);
            mIvPartyBPhoto = (HmImageView) V.findViewById(R.id.swipe_card_iv_right);
            mTvResultCorrectCount = (TextView) V.findViewById(R.id.schedule_row_tv_match_correct_questions);
            mBtnMatchPoints = (TextView) V.findViewById(R.id.schedule_row_tv_my_score);
            mViewResult = (View) V.findViewById(R.id.schedule_row_v_result_line);
            mLlPredictionsParent = (LinearLayout) V.findViewById(R.id.my_results_row_ll_predictions);
            mTvResultWait = (TextView) V.findViewById(R.id.schedule_row_tv_match_result_wait);
            mleaderboard = (LinearLayout) V.findViewById(R.id.my_results_row_ll_leaderboardbtn);

            mTvAvgMatchPoints = (TextView) V.findViewById(R.id.schedule_row_tv_average_score);
            mTvHighestMatchPoints = (TextView) V.findViewById(R.id.schedule_row_tv_highest_score);
            mTvLeaderBoardRank = (TextView) V.findViewById(R.id.schedule_row_tv_leaderboard_rank);
            mTvNumberofPowerupsUsed= (TextView) V.findViewById(R.id.schedule_row_tv_no_of_powerups_used);
            mRlLeaderBoard = (RelativeLayout) V.findViewById(R.id.schedule_row_rl_leaderboard);
            mSlScrores = (ShadowLayout) V.findViewById(R.id.schedule_row_scores_sl);
            mSlScrores.setPadding(0, 0, 0, 0);
            mRlAvgMatchPoints= (RelativeLayout) V.findViewById(R.id.schedule_row_rl_average_score);
            mRlHighestMatchPoints= (RelativeLayout) V.findViewById(R.id.schedule_row_rl_highest_score);
            mRlMatchPoints= (RelativeLayout) V.findViewById(R.id.schedule_row_rl_my_score);
            mTvMatchPointsTxt = (TextView) V.findViewById(R.id.schedule_row_tv_my_score_txt);
        }
    }


    private View getMyPrediction(ViewGroup parent, final Question question) {
        View convertView = getLayoutInflater().inflate(R.layout.inflater_my_predictions_row, parent, false);

        ((TextView) convertView.findViewById(R.id.my_predictions_row_tv_question))
                .setText(question.getQuestionText().replace("\n", ""));


        final TextView tvAnswer = (TextView) convertView.findViewById(R.id.my_predictions_row_tv_answer);
        HmImageView powerupUsed = (HmImageView) convertView.findViewById(R.id.my_predictions_row_btn_answer_powerup_used);
        RelativeLayout powerup = (RelativeLayout) convertView.findViewById(R.id.my_predictions_row_rl);
        TextView tvAnswerPoints = (TextView) convertView.findViewById(R.id.my_predictions_row_tv_answer_points);
        final TextView tvotheroption = (TextView) convertView.findViewById(R.id.my_predictions_row_tv_correct_answer);
        final ImageView mFlipPowerUp = (ImageView) convertView.findViewById(R.id.powerup_flip);
        final TextView tvNeitherAnswer = (TextView) convertView.findViewById(R.id.my_predictions_row_tv_neither_answer);

        tvAnswer.setCompoundDrawablePadding(10);
        tvotheroption.setCompoundDrawablePadding(10);
        tvNeitherAnswer.setCompoundDrawablePadding(10);

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

        int powerupIcons = PowerUp.getResultPowerupIcons(question.getAnswerPowerUpId());
        if(powerupIcons == -1) {
            powerupUsed.setVisibility(View.GONE);
            powerup.setVisibility(View.GONE);
        } else {
            powerupUsed.setVisibility(View.VISIBLE);
            powerupUsed.setBackgroundResource(powerupIcons);
        }

        answerId = question.getAnswerId();


        //BEFORE THE RESULT IS PUBLISHED SHOW ANSWERS
        if (null==question.getQuestionAnswer()) {

            setTextColor(tvAnswer, R.color.white);

            if (answerId == 0) {
                tvAnswer.setText("Not Attempted");
                setTextColor(tvAnswer, R.color.tabcolor);
                tvAnswerPoints.setText("---");

            } else {
                if (answerId == 1) {
                    tvAnswer.setText(question.getQuestionOption1());
                } else if (answerId == 3) {
                    tvAnswer.setVisibility(View.GONE);
                    tvNeitherAnswer.setVisibility(View.VISIBLE);
                    tvNeitherAnswer.setTextColor(Color.WHITE);
                    tvNeitherAnswer.setText(question.getQuestionOption3());
                } else {
                    tvAnswer.setText(question.getQuestionOption2());
                }
            }

        }
        // if played match but not attempted Question
        else if (answerId == 0){
            tvAnswer.setText("Not Attempted");
            setTextColor(tvAnswer, R.color.white);
            tvAnswerPoints.setText("---");
        }
        //if your answer = correct answer
        else if (answerId == question.getQuestionAnswer()) {

            setTextColor(tvAnswer, R.color.greencolor);
            tvAnswer.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.result_tick_icon, 0);
            tvotheroption.setVisibility(View.VISIBLE);
            tvotheroption.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.result_cross_icon, 0);


            // IF USER ANSWER = OPTION 1 OR OPTION 2

            if (question.getQuestionAnswer() == 1 || question.getQuestionAnswer() == 2) {
                if (question.getQuestionAnswer() == 1) {
                    tvAnswer.setText(question.getQuestionOption1());
                    tvotheroption.setText(question.getQuestionOption2());
                    setTextColor(tvotheroption, R.color.white_60);

                } else {
                    tvAnswer.setText(question.getQuestionOption2());
                    tvotheroption.setText(question.getQuestionOption1());
                    setTextColor(tvotheroption, R.color.white_60);
                }

                if (!TextUtils.isEmpty(question.getQuestionOption3())) {
                    tvNeitherAnswer.setVisibility(View.VISIBLE);
                    tvNeitherAnswer.setText(question.getQuestionOption3());
                    setTextColor(tvNeitherAnswer, R.color.white_60);
                    tvNeitherAnswer.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_cross_icon, 0);
                }
            }


            // IF USER ANSWER = THIRD OPTION (NEITHER OR DRAW)
            if (answerId==3){
                tvAnswer.setVisibility(View.VISIBLE);
                tvAnswer.setText(question.getQuestionOption1());
                tvotheroption.setText(question.getQuestionOption2());
                tvotheroption.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.result_cross_icon, 0);
                tvAnswer.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.result_cross_icon, 0);
                setTextColor(tvAnswer, R.color.white_60);

                tvNeitherAnswer.setVisibility(View.VISIBLE);
                tvNeitherAnswer.setText(question.getQuestionOption3());
                setTextColor(tvNeitherAnswer, R.color.greencolor);
                tvNeitherAnswer.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.result_tick_icon, 0);
            }

        }  // if your answer & other option both are correct
        else if(question.getQuestionAnswer() == 0){

            Log.i("answer","both correct");

            tvotheroption.setVisibility(View.VISIBLE);
            if (question.getQuestionAnswer() == 1) {
                tvotheroption.setText(question.getQuestionOption1());
                setTextColor(tvotheroption, R.color.white_60);
            } else {
                tvotheroption.setText(question.getQuestionOption2());
                setTextColor(tvotheroption, R.color.white_60);
            }
            tvotheroption.setVisibility(View.VISIBLE);
            tvotheroption.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.result_tick_icon, 0);
            tvAnswer.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.result_tick_icon, 0);
            setTextColor(tvAnswer, R.color.greencolor);

        }  // if your answer is incorrect and other option is correct
        else {
            setTextColor(tvAnswer, R.color.tabcolor);
            tvAnswer.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.result_cross_icon, 0);
            tvotheroption.setVisibility(View.VISIBLE);
            setTextColor(tvotheroption, R.color.white_60);
            tvotheroption.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.result_tick_icon, 0);


            // IF USER ANSWER = OPTION 1 OR OPTION 2

            if (question.getQuestionAnswer() == 1 || question.getQuestionAnswer() == 2) {

                if (question.getQuestionAnswer() == 1) {
                    tvAnswer.setText(question.getQuestionOption2());
                    tvotheroption.setText(question.getQuestionOption1());
                }else {
                    tvAnswer.setText(question.getQuestionOption1());
                    tvotheroption.setText(question.getQuestionOption2());
                }

                if (!TextUtils.isEmpty(question.getQuestionOption3())) {
                    tvNeitherAnswer.setVisibility(View.VISIBLE);
                    tvNeitherAnswer.setText(question.getQuestionOption3());
                    setTextColor(tvNeitherAnswer, R.color.white_60);
                    tvNeitherAnswer.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_cross_icon, 0);
                }
            }else if (question.getQuestionAnswer()==3){

                if (answerId == 1) {
                    tvAnswer.setText(question.getQuestionOption1());
                    tvotheroption.setText(question.getQuestionOption2());
                }else {
                    tvAnswer.setText(question.getQuestionOption2());
                    tvotheroption.setText(question.getQuestionOption1());
                }

                tvNeitherAnswer.setVisibility(View.VISIBLE);
                tvNeitherAnswer.setText(question.getQuestionOption3());
                setTextColor(tvNeitherAnswer, R.color.white_60);
                tvotheroption.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.result_cross_icon, 0);
                tvNeitherAnswer.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_tick_icon, 0);

            }

            // IF USER ANSWER = NEITHER
            if (answerId==3){
                if (question.getQuestionAnswer() == 1) {
                    tvotheroption.setText(question.getQuestionOption1());
                    tvAnswer.setText(question.getQuestionOption2());
                }else {
                    tvotheroption.setText(question.getQuestionOption2());
                    tvAnswer.setText(question.getQuestionOption1());
                }
                tvAnswer.setVisibility(View.VISIBLE);
                setTextColor(tvAnswer, R.color.white_60);
                tvNeitherAnswer.setVisibility(View.VISIBLE);
                tvNeitherAnswer.setText(question.getQuestionOption3());
                setTextColor(tvNeitherAnswer, R.color.tabcolor);
                tvNeitherAnswer.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.result_cross_icon, 0);
            }



        }

        return convertView;
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