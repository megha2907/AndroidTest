package in.sportscafe.nostragamus.module.resultspeek;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.customfont.CustomButton;

import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.resultspeek.dto.ResultsPeek;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;
import in.sportscafe.nostragamus.module.user.powerups.PowerUp;

/**
 * Created by deepanshi on 2/15/17.
 */

public class ResultsPeekAdapter extends Adapter<ResultsPeek, ResultsPeekAdapter.ViewHolder> {

    private PlayerInfo mPlayerInfo;

    public ResultsPeekAdapter(Context context) {
        super(context);
    }

    public void setPlayerInfo(PlayerInfo playerInfo) {
        this.mPlayerInfo = playerInfo;
    }

    @Override
    public ResultsPeek getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayoutInflater().inflate(R.layout.inflater_feed_match_result_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ResultsPeek resultsPeek = getItem(position);
        holder.mPosition = position;
        holder.mLlTourParent.removeAllViews();

        holder.mLlTourParent.addView(getMyPrediction(holder.mLlTourParent, resultsPeek.getPlayerOneQuestions(),resultsPeek.getPlayerTwoQuestions()));
    }

    private View getMyPrediction(ViewGroup parent, Question myQuestion, Question playerQuestion) {
        View convertView = getLayoutInflater().inflate(R.layout.inflater_results_peek, parent, false);


        ((TextView) convertView.findViewById(R.id.results_peek_row_question_tv))
                .setText(playerQuestion.getQuestionText().replaceAll("\n",""));

        final TextView tvPlayerOneAnswer = (TextView) convertView.findViewById(R.id.results_peek_row_player_one_answer);
        final TextView tvPlayerTwoAnswer = (TextView) convertView.findViewById(R.id.results_peek_row_player_two_answer);

        TextView tvPlayerOneAnswerPoints = (TextView) convertView.findViewById(R.id.results_peek_row_player_one_points);
        TextView tvPlayerTwoAnswerPoints = (TextView) convertView.findViewById(R.id.results_peek_row_player_two_points);


        HmImageView powerUpUsedPlayerOne = (HmImageView) convertView.findViewById(R.id.results_peek_row_player_one_powerup_used);
        RelativeLayout powerUpPlayerOne = (RelativeLayout) convertView.findViewById(R.id.results_peek_row_player_one_powerup_rl);

        HmImageView powerUpUsedPlayerTwo = (HmImageView) convertView.findViewById(R.id.results_peek_row_player_two_powerup_used);
        RelativeLayout powerUpPlayerTwo = (RelativeLayout) convertView.findViewById(R.id.results_peek_row_player_two_powerup_rl);


        if (myQuestion==null){
            tvPlayerOneAnswer.setText("Did not Play");
            setTextColor(tvPlayerOneAnswer, R.color.white_60);
            tvPlayerOneAnswerPoints.setText("-");
        } else {
            int myAnswerId = myQuestion.getAnswerId();

            // if played match but not attempted Question
            if (myAnswerId == 0) {
                tvPlayerOneAnswer.setText("Did not Play");
                setTextColor(tvPlayerOneAnswer, R.color.white_60);
                tvPlayerOneAnswerPoints.setText("-");
            }
            //if your answer = correct answer
            else if (myAnswerId == myQuestion.getQuestionAnswer()) {

                setTextColor(tvPlayerOneAnswer, R.color.greencolor);

                // IF USER ANSWER = OPTION 1 OR OPTION 2
                if (myQuestion.getQuestionAnswer() == 1) {
                    tvPlayerOneAnswer.setText(myQuestion.getQuestionOption1());
                } else {
                    tvPlayerOneAnswer.setText(myQuestion.getQuestionOption2());
                }

                // IF USER ANSWER = NEITHER
                if (myAnswerId == 3) {
                    tvPlayerOneAnswer.setText(myQuestion.getQuestionOption3());
                    setTextColor(tvPlayerOneAnswer, R.color.greencolor);
                }

            }  // if your answer & other option both are correct
            else if (myQuestion.getQuestionAnswer() == 0) {

                Log.i("answer", "both correct");
                setTextColor(tvPlayerOneAnswer, R.color.greencolor);

            }  // if your answer is incorrect and other option is correct
            else {
                setTextColor(tvPlayerOneAnswer, R.color.tabcolor);

                // IF USER ANSWER = OPTION 1 OR OPTION 2
                if (myQuestion.getQuestionAnswer() == 1) {
                    tvPlayerOneAnswer.setText(myQuestion.getQuestionOption2());
                } else {
                    tvPlayerOneAnswer.setText(myQuestion.getQuestionOption1());
                }

                // IF USER ANSWER = NEITHER
                if (myAnswerId == 3) {
                    tvPlayerOneAnswer.setText(myQuestion.getQuestionOption3());
                    setTextColor(tvPlayerOneAnswer, R.color.tabcolor);
                }

            }

            if (myQuestion.getAnswerPoints() != null) {

                if (myQuestion.getAnswerPoints().equals(0)) {
                    setTextColor(tvPlayerOneAnswerPoints, R.color.textcolorlight);
                }

                if (myQuestion.getAnswerPoints() > 0) {
                    tvPlayerOneAnswerPoints.setText("+" + myQuestion.getAnswerPoints() + " Points");
                } else {
                    tvPlayerOneAnswerPoints.setText(myQuestion.getAnswerPoints() + " Points");
                }

            }

            int powerUpMyIcons = PowerUp.getResultPowerupIcons(myQuestion.getAnswerPowerUpId());
            if (powerUpMyIcons == -1) {
                powerUpUsedPlayerOne.setVisibility(View.GONE);
                powerUpPlayerOne.setVisibility(View.GONE);
            } else {
                powerUpUsedPlayerOne.setVisibility(View.VISIBLE);
                powerUpUsedPlayerOne.setBackgroundResource(powerUpMyIcons);
            }

        }


            int otherPlayerAnswerId = playerQuestion.getAnswerId();

            // if played match but not attempted Question
            if (otherPlayerAnswerId == 0) {
                tvPlayerTwoAnswer.setText("Not Attempted");
                setTextColor(tvPlayerTwoAnswer, R.color.white);
                tvPlayerTwoAnswerPoints.setText("---");
            }
            //if your answer = correct answer
            else if (otherPlayerAnswerId == playerQuestion.getQuestionAnswer()) {

                setTextColor(tvPlayerTwoAnswer, R.color.greencolor);

                // IF USER ANSWER = OPTION 1 OR OPTION 2
                if (playerQuestion.getQuestionAnswer() == 1) {
                    tvPlayerTwoAnswer.setText(playerQuestion.getQuestionOption1());
                } else {
                    tvPlayerTwoAnswer.setText(playerQuestion.getQuestionOption2());
                }

                // IF USER ANSWER = NEITHER
                if (otherPlayerAnswerId == 3) {
                    tvPlayerTwoAnswer.setText(playerQuestion.getQuestionOption3());
                    setTextColor(tvPlayerTwoAnswer, R.color.greencolor);
                }

            }  // if your answer & other option both are correct
            else if (playerQuestion.getQuestionAnswer() == 0) {

                Log.i("answer", "both correct");
                setTextColor(tvPlayerTwoAnswer, R.color.greencolor);

            }  // if your answer is incorrect and other option is correct
            else {
                setTextColor(tvPlayerTwoAnswer, R.color.tabcolor);

                // IF USER ANSWER = OPTION 1 OR OPTION 2
                if (playerQuestion.getQuestionAnswer() == 1) {
                    tvPlayerTwoAnswer.setText(playerQuestion.getQuestionOption2());
                } else {
                    tvPlayerTwoAnswer.setText(playerQuestion.getQuestionOption1());
                }

                // IF USER ANSWER = NEITHER
                if (otherPlayerAnswerId == 3) {
                    tvPlayerTwoAnswer.setText(playerQuestion.getQuestionOption3());
                    setTextColor(tvPlayerTwoAnswer, R.color.tabcolor);
                }

            }


        if (playerQuestion.getAnswerPoints() != null) {

            if (playerQuestion.getAnswerPoints().equals(0)) {
                setTextColor(tvPlayerTwoAnswerPoints, R.color.textcolorlight);
            }

            if (playerQuestion.getAnswerPoints() > 0) {
                tvPlayerTwoAnswerPoints.setText("+" + playerQuestion.getAnswerPoints() + " Points");
            } else {
                tvPlayerTwoAnswerPoints.setText(playerQuestion.getAnswerPoints() + " Points");
            }

        }

        int powerUpOtherPlayerIcons = PowerUp.getResultPowerupIcons(playerQuestion.getAnswerPowerUpId());
        if(powerUpOtherPlayerIcons == -1) {
            powerUpUsedPlayerTwo.setVisibility(View.GONE);
            powerUpPlayerTwo.setVisibility(View.GONE);
        } else {
            powerUpUsedPlayerTwo.setVisibility(View.VISIBLE);
            powerUpUsedPlayerTwo.setBackgroundResource(powerUpOtherPlayerIcons);
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