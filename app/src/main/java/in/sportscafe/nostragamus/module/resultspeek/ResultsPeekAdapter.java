package in.sportscafe.nostragamus.module.resultspeek;

import android.content.Context;
import android.graphics.Color;
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
import com.jeeva.android.widgets.customfont.CustomButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.resultspeek.dto.Question;
import in.sportscafe.nostragamus.module.resultspeek.dto.ResultsPeek;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;

/**
 * Created by deepanshi on 2/15/17.
 */

public class ResultsPeekAdapter extends Adapter<ResultsPeek, ResultsPeekAdapter.ViewHolder> {

    private static final int PLAYER_ONE = 1;
    private static final int PLAYER_TWO = 2;

    private PlayerInfo mPlayerInfo;

    public ResultsPeekAdapter(Context context, HashMap<Integer, ResultsPeek> resultPeekMap) {
        super(context);
        for (Integer integer : resultPeekMap.keySet()) {
            add(resultPeekMap.get(integer));
        }
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

        holder.mLlTourParent.addView(getMyPrediction(holder.mLlTourParent, resultsPeek.getPlayerOneQuestions(), resultsPeek.getPlayerTwoQuestions()));
    }

    private View getMyPrediction(ViewGroup parent, Question myQuestion, Question playerQuestion) {
        View convertView = getLayoutInflater().inflate(R.layout.inflater_results_peek, parent, false);


        TextView tvPlayerQuestionText = (TextView) convertView.findViewById(R.id.results_peek_row_question_tv);

        final TextView tvPlayerOneAnswer = (TextView) convertView.findViewById(R.id.results_peek_row_player_one_answer);
        final TextView tvPlayerTwoAnswer = (TextView) convertView.findViewById(R.id.results_peek_row_player_two_answer);

        TextView tvPlayerOneAnswerPoints = (TextView) convertView.findViewById(R.id.results_peek_row_player_one_points);
        TextView tvPlayerTwoAnswerPoints = (TextView) convertView.findViewById(R.id.results_peek_row_player_two_points);


        HmImageView powerUpUsedPlayerOne = (HmImageView) convertView.findViewById(R.id.results_peek_row_player_one_powerup_used);
        RelativeLayout powerUpPlayerOne = (RelativeLayout) convertView.findViewById(R.id.results_peek_row_player_one_powerup_rl);

        HmImageView powerUpUsedPlayerTwo = (HmImageView) convertView.findViewById(R.id.results_peek_row_player_two_powerup_used);
        RelativeLayout powerUpPlayerTwo = (RelativeLayout) convertView.findViewById(R.id.results_peek_row_player_two_powerup_rl);

        RelativeLayout itemLayout = (RelativeLayout) convertView.findViewById(R.id.result_peek_item_content_layout);


        if (null == playerQuestion) {
            tvPlayerQuestionText.setText(myQuestion.getQuestionText().replaceAll("\n", ""));
        } else {
            tvPlayerQuestionText.setText(playerQuestion.getQuestionText().replaceAll("\n", ""));
        }

        int myAnswerId;

        if (myQuestion == null) {
            tvPlayerOneAnswer.setText("Did not Play");
            setTextColor(tvPlayerOneAnswer, R.color.white_60);
            tvPlayerOneAnswerPoints.setText("-");
        }
          /* Before the Result is published */
          else if (null == myQuestion.getQuestionAnswer()) {

            myAnswerId = myQuestion.getAnswerId();
            setTextColor(tvPlayerOneAnswer, R.color.white);
            tvPlayerOneAnswerPoints.setVisibility(View.GONE);

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvPlayerOneAnswer.getLayoutParams();
            layoutParams.setMargins(0, 1, 0,10);
            tvPlayerOneAnswer.setLayoutParams(layoutParams);

            if (myAnswerId == 0) {
                /* if question not answered then answerId=0 , set Not Answered */
                tvPlayerOneAnswer.setText("Not Answered");
                setTextColor(tvPlayerOneAnswer, R.color.white_60);

            } else {

                /* if answer is 1st option , set Option1 */
                if (myAnswerId == 1) {
                    tvPlayerOneAnswer.setText(myQuestion.getQuestionOption1());
                }  /* if answer is 2nd option , set Option2 */ else {
                    tvPlayerOneAnswer.setText(myQuestion.getQuestionOption2());
                }

                 /* if answer is 3rd option , set Option3  */
                if (!TextUtils.isEmpty(myQuestion.getQuestionOption3()) && myAnswerId == 3) {
                    tvPlayerOneAnswer.setText(myQuestion.getQuestionOption3());
                }

            }

        }/* After the Result is published */
          else {
            myAnswerId = myQuestion.getAnswerId();

            // if played Match but not attempted Question
            if (myAnswerId == 0) {
                tvPlayerOneAnswer.setText("Did not Play");
                setTextColor(tvPlayerOneAnswer, R.color.white_60);
                tvPlayerOneAnswerPoints.setText("-");
            }//if your answer = correct answer
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

            /* If questionAns == -1, means question was incorrect / invalid -- SPLIT Question  */
            /*if (myQuestion.getQuestionAnswer() == -1) {
                setTextColor(tvPlayerOneAnswer, R.color.white_60);
                convertView.findViewById(R.id.results_peek_row_player_one_points_splitView).setVisibility(View.VISIBLE);
            }*/

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

            /*int powerUpMyIcons = PowerUp.getResultPowerupIcons(myQuestion.getAnswerPowerUpId());
            if (powerUpMyIcons == -1) {
                powerUpUsedPlayerOne.setVisibility(View.GONE);
                powerUpPlayerOne.setVisibility(View.GONE);
            } else {
                powerUpUsedPlayerOne.setVisibility(View.VISIBLE);
                powerUpUsedPlayerOne.setBackgroundResource(powerUpMyIcons);
            }*/


        }


        int otherPlayerAnswerId;

        if (null == playerQuestion) {
            tvPlayerTwoAnswer.setText("Did not Play");
            setTextColor(tvPlayerTwoAnswer, R.color.white_60);
            tvPlayerTwoAnswerPoints.setText("-");
        }
        /* Before the Result is published */
         else if (null == playerQuestion.getQuestionAnswer()) {

            otherPlayerAnswerId = playerQuestion.getAnswerId();
            setTextColor(tvPlayerTwoAnswer, R.color.white);
            tvPlayerTwoAnswerPoints.setVisibility(View.GONE);

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvPlayerTwoAnswer.getLayoutParams();
            layoutParams.setMargins(0, 1, 0, 10);
            tvPlayerTwoAnswer.setLayoutParams(layoutParams);

            if (otherPlayerAnswerId == 0) {
                /* if question not answered then answerId=0 , set Not Answered */
                tvPlayerTwoAnswer.setText("Not Answered");
                setTextColor(tvPlayerTwoAnswer, R.color.white_60);

            } else {

                /* if answer is 1st option , set Option1 */
                if (otherPlayerAnswerId == 1) {
                    tvPlayerTwoAnswer.setText(playerQuestion.getQuestionOption1());
                }  /* if answer is 2nd option , set Option2 */ else {
                    tvPlayerTwoAnswer.setText(playerQuestion.getQuestionOption2());
                }

                 /* if answer is 3rd option , set Option3  */
                if (!TextUtils.isEmpty(playerQuestion.getQuestionOption3()) && otherPlayerAnswerId == 3) {
                    tvPlayerTwoAnswer.setText(playerQuestion.getQuestionOption3());
                }
            }

        } else {

            otherPlayerAnswerId = playerQuestion.getAnswerId();

            // if played Match but not attempted Question
            if (otherPlayerAnswerId == 0) {
                tvPlayerTwoAnswer.setText("Did not Play");
                setTextColor(tvPlayerTwoAnswer, R.color.white_60);
                tvPlayerTwoAnswerPoints.setText("-");
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

            /* If questionAns == -1, means question was incorrect / invalid -- SPLIT Question  */
            /*if (myQuestion.getQuestionAnswer() == -1) {
                setTextColor(tvPlayerTwoAnswer, R.color.white_60);
                convertView.findViewById(R.id.results_peek_row_player_two_points_splitView).setVisibility(View.VISIBLE);
            }*/

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

            /*int powerUpOtherPlayerIcons = PowerUp.getResultPowerupIcons(playerQuestion.getAnswerPowerUpId());
            if (powerUpOtherPlayerIcons == -1) {
                powerUpUsedPlayerTwo.setVisibility(View.GONE);
                powerUpPlayerTwo.setVisibility(View.GONE);
            } else {
                powerUpUsedPlayerTwo.setVisibility(View.VISIBLE);
                powerUpUsedPlayerTwo.setBackgroundResource(powerUpOtherPlayerIcons);
            }*/
        }


//        if (tvPlayerOneAnswerPoints.getVisibility()==View.GONE) {
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) itemLayout.getLayoutParams();
//            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
//            tvPlayerOneAnswer.setLayoutParams(layoutParams);
//            tvPlayerTwoAnswer.setLayoutParams(layoutParams);
//        }

        showOrHidePowerUps(myQuestion, playerQuestion, convertView);

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

    private void showOrHidePowerUps(Question myQuestion, Question playerQuestion, View convertView) {
        if (myQuestion != null && playerQuestion != null) {
            ArrayList<String> myPowerUpArray = myQuestion.getPowerUpArrayList();
            ArrayList<String> playerPowerUpArray = playerQuestion.getPowerUpArrayList();

            if ((myPowerUpArray == null || myPowerUpArray.isEmpty()) && (playerPowerUpArray == null || playerPowerUpArray.isEmpty())) {
                LinearLayout powerupLayout = (LinearLayout) convertView.findViewById(R.id.powerup_bottom_layout);
                powerupLayout.setVisibility(View.GONE);
            } else {
                populatePowerup(myQuestion, convertView, PLAYER_ONE);
                populatePowerup(playerQuestion, convertView, PLAYER_TWO);
            }
        }
    }

    private void populatePowerup(Question question, View convertView, int player) {
        ImageView powerUp2xImageView = null;
        ImageView powerUpNoNegativeImageView = null;
        ImageView powerUpAudienceImageView = null;

        if (player == PLAYER_ONE) {
            powerUp2xImageView = (ImageView) convertView.findViewById(R.id.result_peek_powerup_2x_left);
            powerUpNoNegativeImageView = (ImageView) convertView.findViewById(R.id.result_peek_powerup_noNeg_left);
            powerUpAudienceImageView = (ImageView) convertView.findViewById(R.id.result_peek_powerup_audience_left);
        }
        if (player == PLAYER_TWO) {
            powerUp2xImageView = (ImageView) convertView.findViewById(R.id.result_peek_powerup_2x_right);
            powerUpNoNegativeImageView = (ImageView) convertView.findViewById(R.id.result_peek_powerup_noNeg_right);
            powerUpAudienceImageView = (ImageView) convertView.findViewById(R.id.result_peek_powerup_audience_right);
        }

        if (powerUp2xImageView != null && powerUpNoNegativeImageView != null && powerUpAudienceImageView != null) {

            ArrayList<String> powerUpArrayList = question.getPowerUpArrayList();
            if (powerUpArrayList != null && !powerUpArrayList.isEmpty()) {
                for (int temp = 0; temp < powerUpArrayList.size(); temp++) {

                    String powerUp = powerUpArrayList.get(temp);
                    if (powerUp.equalsIgnoreCase(Constants.Powerups.XX)) {
                        powerUp2xImageView.setBackgroundResource(R.drawable.double_powerup_small);
                        powerUp2xImageView.setVisibility(View.VISIBLE);

                    } else if (powerUp.equalsIgnoreCase(Constants.Powerups.NO_NEGATIVE)) {
                        powerUpNoNegativeImageView.setBackgroundResource(R.drawable.no_negative_powerup_small);
                        powerUpNoNegativeImageView.setVisibility(View.VISIBLE);

                    } else if (powerUp.equalsIgnoreCase(Constants.Powerups.AUDIENCE_POLL)) {
                        powerUpAudienceImageView.setBackgroundResource(R.drawable.audience_poll_powerup_small);
                        powerUpAudienceImageView.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

}