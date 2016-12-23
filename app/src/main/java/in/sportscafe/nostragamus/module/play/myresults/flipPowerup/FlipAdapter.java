package in.sportscafe.nostragamus.module.play.myresults.flipPowerup;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.volley.Volley;
import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.customfont.CustomButton;

import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.feed.dto.Feed;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.play.myresults.MyResultsAdapter;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.Tournament;
import in.sportscafe.nostragamus.module.user.leaderboardsummary.LeaderBoardSummaryActivity;

/**
 * Created by deepanshi on 12/20/16.
 */

public class FlipAdapter extends Adapter<Match, FlipAdapter.ViewHolder> {

    private FlipAdapter.OnFlipActionListener mflipActionListener;

    private AlertDialog mAlertDialog;

    private static final int CODE_PROFILE_ACTIVITY = 1;

    private Boolean isShowFlipOptn = true;

    private Boolean isFlipclicked = true;

    public void setFlipActionListener(FlipAdapter.OnFlipActionListener mflipActionListener) {
        this.mflipActionListener = mflipActionListener;
    }

    public FlipAdapter(Context context) {
        super(context);
    }

    @Override
    public Match getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public FlipAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FlipAdapter.ViewHolder(getLayoutInflater().inflate(R.layout.inflater_feed_match_result_row, parent, false));
    }

    @Override
    public void onBindViewHolder(FlipAdapter.ViewHolder holder, int position) {
        Match match = getItem(position);
        holder.mPosition = position;
        holder.mLlTourParent.removeAllViews();
        List<Question> questions = match.getQuestions();
        for (Question question : questions) {
            holder.mLlTourParent.addView(getMyPrediction(holder.mLlTourParent, question));
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

        } else {
            if (isShowFlipOptn == true) {
                mFlipPowerUp.setVisibility(View.VISIBLE);

//                ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(convertView.getContext(), R.animator.flip_anim);
//                anim.setTarget(mFlipPowerUp);
//                anim.setDuration(6000);
//                anim.setRepeatCount(ObjectAnimator.INFINITE);
//                anim.setRepeatMode(ObjectAnimator.REVERSE);
//                anim.start();

                final int answerId = Integer.parseInt(question.getAnswerId());
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
                                            Log.i("inside","answerId == 1)");
                                            tvAnswer.setText(question.getQuestionOption2());
                                            tvotheroption.setText(question.getQuestionOption1());
                                        } else {
                                            Log.i("inside","aelse)");
                                            tvAnswer.setText(question.getQuestionOption1());
                                            tvotheroption.setText(question.getQuestionOption2());
                                        }
                                        mflipActionListener.callFlipQuestionApi(question.getQuestionId());

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
                setTextColor(tvotheroption, R.color.textcolorlight);
            }
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
        }else if (powerupused.equals("answer_flip")) {
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

    public interface OnFlipActionListener {

        void callFlipQuestionApi(Integer questionId);
    }

}