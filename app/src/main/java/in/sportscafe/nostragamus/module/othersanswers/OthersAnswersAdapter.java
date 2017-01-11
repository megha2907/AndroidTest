package in.sportscafe.nostragamus.module.othersanswers;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.customfont.CustomButton;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.IntentActions;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.feed.dto.Feed;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.Tournament;
import in.sportscafe.nostragamus.module.user.leaderboardsummary.LeaderBoardSummaryActivity;

/**
 * Created by Jeeva on 15/6/16.
 */
public class OthersAnswersAdapter extends Adapter<Feed, OthersAnswersAdapter.ViewHolder> {

    public OthersAnswersAdapter(Context context) {
        super(context);
    }

    @Override
    public Feed getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayoutInflater().inflate(R.layout.inflater_feed_match_result_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Feed feed = getItem(position);
        holder.mPosition = position;
        holder.mLlTourParent.removeAllViews();
        for (Tournament tournament : feed.getTournaments()) {
            holder.mLlTourParent.addView(getTourView(tournament, holder.mLlTourParent));
        }
    }

    private View getTourView(Tournament tournament, ViewGroup parent) {
        View tourView = getLayoutInflater().inflate(R.layout.inflater_tour_row, parent, false);
        TourViewHolder holder = new TourViewHolder(tourView);

        holder.mTvTournamentName.setText(tournament.getTournamentName());

        holder.mLlScheduleParent.removeAllViews();
        for (Match match : tournament.getMatches()) {
            holder.mLlScheduleParent.addView(getMyResultView(match, holder.mLlScheduleParent));
        }

        return tourView;
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

        if (match.getMatchPoints() == 0) {
            holder.mBtnMatchPoints.setVisibility(View.GONE);
            holder.mTvResultCorrectCount.setVisibility(View.GONE);
        } else {
            holder.mBtnMatchPoints.setVisibility(View.VISIBLE);
            holder.mTvResultCorrectCount.setVisibility(View.VISIBLE);
            holder.mViewResult.setVisibility(View.VISIBLE);
            holder.mTvResultWait.setVisibility(View.GONE);

            holder.mBtnMatchPoints.setText(match.getMatchPoints() + " Points");
            holder.mTvResultCorrectCount.setText(match.getCorrectCount() + "/" + match.getMatchQuestionCount() + " Correct");

        }

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
        CustomButton mBtnMatchPoints;
        View mViewResult;

        HmImageView mIvPartyAPhoto;
        TextView mTvResultWait;
        HmImageView mIvPartyBPhoto;


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
            mBtnMatchPoints = (CustomButton) V.findViewById(R.id.schedule_row_btn_points);
            mViewResult = (View) V.findViewById(R.id.schedule_row_v_party_a);
            mLlPredictionsParent = (LinearLayout) V.findViewById(R.id.my_results_row_ll_predictions);
            mTvResultWait = (TextView) V.findViewById(R.id.schedule_row_tv_match_result_wait);
            mleaderboard = (LinearLayout) V.findViewById(R.id.my_results_row_ll_leaderboardbtn);
        }
    }


    private View getMyPrediction(ViewGroup parent, final Question question) {
        View convertView = getLayoutInflater().inflate(R.layout.inflater_my_predictions_row, parent, false);

        ((TextView) convertView.findViewById(R.id.my_predictions_row_tv_question))
                .setText(question.getQuestionText());


        final TextView tvAnswer = (TextView) convertView.findViewById(R.id.my_predictions_row_tv_answer);
        CustomButton powerupUsed = (CustomButton) convertView.findViewById(R.id.my_predictions_row_btn_answer_powerup_used);
        RelativeLayout powerup = (RelativeLayout) convertView.findViewById(R.id.my_predictions_row_rl);
        TextView tvAnswerPoints = (TextView) convertView.findViewById(R.id.my_predictions_row_tv_answer_points);
        final TextView tvotheroption = (TextView) convertView.findViewById(R.id.my_predictions_row_tv_correct_answer);
        final ImageView mFlipPowerUp = (ImageView) convertView.findViewById(R.id.powerup_flip);

        tvAnswer.setCompoundDrawablePadding(10);
        tvotheroption.setCompoundDrawablePadding(10);

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

        if(null != question.getOption1AudPollPer()) {
            tvAnswerPoints.setVisibility(View.INVISIBLE);
            ((TextView) convertView.findViewById(R.id.my_predictions_row_tv_perc_1)).setText(question.getOption1AudPollPer());
            ((TextView) convertView.findViewById(R.id.my_predictions_row_tv_perc_2)).setText(question.getOption2AudPollPer());
        }

        String powerupused = question.getAnswerPowerUpId();

        if (powerupused.equals("null")) {
            powerupUsed.setVisibility(View.GONE);
            powerup.setVisibility(View.GONE);
        } else if (powerupused.equals("player_poll")) {
            powerupUsed.setBackgroundResource(R.drawable.powerup_audience_poll);
            powerupUsed.setVisibility(View.VISIBLE);
        } else if (powerupused.equals("2x")) {
            powerupUsed.setBackgroundResource(R.drawable.powerup_icon);
            powerupUsed.setVisibility(View.VISIBLE);
        } else if (powerupused.equals("no_negs")) {
            powerupUsed.setBackgroundResource(R.drawable.powerup_nonegs);
            powerupUsed.setVisibility(View.VISIBLE);
        } else if (powerupused.equals("answer_flip")) {
            powerupUsed.setBackgroundResource(R.drawable.powerup_flip);
            powerupUsed.setVisibility(View.VISIBLE);
        } else if (powerupused.equals("match_replay")) {
            powerupUsed.setBackgroundResource(R.drawable.replay_icon);
            powerupUsed.setVisibility(View.VISIBLE);
        }

        int answerId = Integer.parseInt(question.getAnswerId());

        if (answerId == 0) {
            tvAnswer.setText("Not Attempted");
            setTextColor(tvAnswer, R.color.tabcolor);

            if (question.getQuestionAnswer() == 1) {
                tvotheroption.setText(question.getQuestionOption1());
            } else {
                tvotheroption.setText(question.getQuestionOption2());
            }

            tvAnswerPoints.setText("---");

        } else {

            if (answerId == 1) {
                tvAnswer.setText(question.getQuestionOption1());
            } else {
                tvAnswer.setText(question.getQuestionOption2());
            }


            if (null == question.getQuestionAnswer()) {
                setTextColor(tvAnswer, R.color.white);
            } else if (answerId == question.getQuestionAnswer()) {

                Log.i("answer", "correct answer");

                //if your answer = correct answer
                setTextColor(tvAnswer, R.color.greencolor);
                tvAnswer.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_tick_icon, 0);
                tvotheroption.setVisibility(View.VISIBLE);
                tvotheroption.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_cross_icon, 0);

                if (question.getQuestionAnswer() == 1) {
                    tvotheroption.setText(question.getQuestionOption2());
                    setTextColor(tvotheroption, R.color.textcolorlight);
                } else {
                    tvotheroption.setText(question.getQuestionOption1());
                    setTextColor(tvotheroption, R.color.textcolorlight);
                }

            }  // if your answer & other option both are correct
            else if (question.getQuestionAnswer() == 0) {

                Log.i("answer", "both correct");

                tvotheroption.setVisibility(View.VISIBLE);
                if (question.getQuestionAnswer() == 1) {
                    tvotheroption.setText(question.getQuestionOption1());
                    setTextColor(tvotheroption, R.color.textcolorlight);
                } else {
                    tvotheroption.setText(question.getQuestionOption2());
                    setTextColor(tvotheroption, R.color.textcolorlight);
                }
                tvotheroption.setVisibility(View.VISIBLE);
                tvotheroption.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_tick_icon, 0);
                tvAnswer.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_tick_icon, 0);
                setTextColor(tvAnswer, R.color.greencolor);

            }  // if your answer is incorrect and other option is correct
            else {

                Log.i("answer", "not correct");

                setTextColor(tvAnswer, R.color.tabcolor);
                tvAnswer.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_cross_icon, 0);


                tvotheroption.setVisibility(View.VISIBLE);
                setTextColor(tvotheroption, R.color.textcolorlight);
                tvotheroption.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.result_tick_icon, 0);

                if (question.getQuestionAnswer() == 1) {
                    tvotheroption.setText(question.getQuestionOption1());
                } else {
                    tvotheroption.setText(question.getQuestionOption2());
                }

            }


        }

        return convertView;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        int mPosition;

        TextView mTvDate;

        private LinearLayout mLlTourParent;

        public ViewHolder(View V) {
            super(V);

            mTvDate = (TextView) V.findViewById(R.id.feed_row_tv_date);
            mLlTourParent = (LinearLayout) V.findViewById(R.id.feed_row_ll_tour_parent);
        }
    }


    private void setTextColor(TextView textView, int color) {
        textView.setTextColor(textView.getResources().getColor(color));
    }

    public interface OnMyResultsActionListener {

        void onClickLeaderBoard(int position);
    }
}