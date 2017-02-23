package in.sportscafe.nostragamus.module.othersanswers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.Tournament;
import in.sportscafe.nostragamus.module.user.playerprofile.PlayerProfileActivity;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;
import in.sportscafe.nostragamus.module.user.powerups.PowerUp;

/**
 * Created by Jeeva on 15/6/16.
 */
public class OthersAnswersAdapter extends Adapter<Match, OthersAnswersAdapter.ViewHolder> {

    private PlayerInfo mPlayerInfo;

    public OthersAnswersAdapter(Context context) {
        super(context);
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

        if (match.getMatchPoints() == 0) {
            holder.mBtnMatchPoints.setVisibility(View.GONE);
            holder.mTvResultCorrectCount.setVisibility(View.GONE);
        } else {
            holder.mBtnMatchPoints.setVisibility(View.VISIBLE);
            holder.mTvResultCorrectCount.setVisibility(View.VISIBLE);
            holder.mViewResult.setVisibility(View.VISIBLE);
            holder.mTvResultWait.setVisibility(View.GONE);
            holder.mBtnMatchPoints.setVisibility(View.GONE);
            holder.mTvResultCorrectCount.setVisibility(View.GONE);


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
        TextView mBtnMatchPoints;
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
            mBtnMatchPoints = (TextView) V.findViewById(R.id.schedule_row_btn_points);
            mViewResult = (View) V.findViewById(R.id.schedule_row_v_result_line);
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

        if (-1 != question.getOption1AudPollPer()) {
            tvAnswerPoints.setVisibility(View.INVISIBLE);
            ((TextView) convertView.findViewById(R.id.my_predictions_row_tv_perc_1)).setText(question.getOption1AudPollPer() + "%");
            ((TextView) convertView.findViewById(R.id.my_predictions_row_tv_perc_2)).setText(question.getOption2AudPollPer() + "%");
        }

        int powerupIcons = PowerUp.getResultPowerupIcons(question.getAnswerPowerUpId());
        if(powerupIcons == -1) {
            powerupUsed.setVisibility(View.GONE);
            powerup.setVisibility(View.GONE);
        } else {
            powerupUsed.setVisibility(View.VISIBLE);
            powerupUsed.setBackgroundResource(powerupIcons);
        }

        int answerId = question.getAnswerId();

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