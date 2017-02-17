package in.sportscafe.nostragamus.module.play.myresults;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.IntentActions;
import in.sportscafe.nostragamus.Constants.LBLandingType;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.othersanswers.OthersAnswersActivity;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.user.lblanding.LbLanding;
import in.sportscafe.nostragamus.module.user.points.PointsActivity;
import in.sportscafe.nostragamus.module.user.powerups.PowerUp;

/**
 * Created by Jeeva on 15/6/16.
 */
public class MyResultsAdapter extends Adapter<Match, MyResultsAdapter.ViewHolder> implements View.OnClickListener {

    private OnMyResultsActionListener mResultsActionListener;

    private AlertDialog mAlertDialog;

    private static final int CODE_PROFILE_ACTIVITY = 1;

    private Boolean isShowFlipOptn = false;

    private Boolean isFlipclicked = true;

    private int answerId;

    private boolean mIsMyResults = true;

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

        if(match.isResultPublished() && mIsMyResults) {
            holder.mleaderboard.addView(getLeaderBoardView(holder.mleaderboard, match));
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
            mViewResult = (View) V.findViewById(R.id.schedule_row_v_result_line);
            mLlPredictionsParent = (LinearLayout) V.findViewById(R.id.my_results_row_ll_predictions);
            mTvResultWait = (TextView) V.findViewById(R.id.schedule_row_tv_match_result_wait);
            mleaderboard=(LinearLayout)V.findViewById(R.id.my_results_row_ll_leaderboardbtn);
        }
    }


    private View getMyPrediction(ViewGroup parent, final Question question)   {
        View convertView = getLayoutInflater().inflate(R.layout.inflater_my_predictions_row, parent, false);

        ((TextView) convertView.findViewById(R.id.my_predictions_row_tv_question))
                .setText(question.getQuestionText());


        final TextView tvAnswer = (TextView) convertView.findViewById(R.id.my_predictions_row_tv_answer);
        final TextView tvNeitherAnswer = (TextView) convertView.findViewById(R.id.my_predictions_row_tv_neither_answer);
        CustomButton powerupUsed = (CustomButton) convertView.findViewById(R.id.my_predictions_row_btn_answer_powerup_used);
        RelativeLayout powerup = (RelativeLayout) convertView.findViewById(R.id.my_predictions_row_rl);
        TextView tvAnswerPoints = (TextView) convertView.findViewById(R.id.my_predictions_row_tv_answer_points);
        final TextView tvotheroption = (TextView) convertView.findViewById(R.id.my_predictions_row_tv_correct_answer);
        final ImageView mFlipPowerUp = (ImageView) convertView.findViewById(R.id.powerup_flip);

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

        }else {
            if(isShowFlipOptn==true){
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

                        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
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
                if (question.getQuestionAnswer() == 1) {
                    tvAnswer.setText(question.getQuestionOption1());
                    tvotheroption.setText(question.getQuestionOption2());
                    setTextColor(tvotheroption, R.color.textcolorlight);
                } else {
                    tvAnswer.setText(question.getQuestionOption2());
                    tvotheroption.setText(question.getQuestionOption1());
                    setTextColor(tvotheroption, R.color.textcolorlight);
                }


             // IF USER ANSWER = NEITHER
                if (answerId==3){
                    tvAnswer.setVisibility(View.VISIBLE);
                    tvAnswer.setText(question.getQuestionOption1());
                    tvotheroption.setText(question.getQuestionOption2());
                    tvotheroption.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.result_cross_icon, 0);
                    tvAnswer.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.result_cross_icon, 0);
                    setTextColor(tvAnswer, R.color.textcolorlight);

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
                    setTextColor(tvotheroption, R.color.textcolorlight);
                } else {
                    tvotheroption.setText(question.getQuestionOption2());
                    setTextColor(tvotheroption, R.color.textcolorlight);
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
                setTextColor(tvotheroption, R.color.textcolorlight);
                tvotheroption.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.result_tick_icon, 0);


            // IF USER ANSWER = OPTION 1 OR OPTION 2
                if (question.getQuestionAnswer() == 1) {
                    tvAnswer.setText(question.getQuestionOption2());
                    tvotheroption.setText(question.getQuestionOption1());
                }else {
                    tvAnswer.setText(question.getQuestionOption1());
                    tvotheroption.setText(question.getQuestionOption2());
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
                    setTextColor(tvAnswer, R.color.textcolorlight);
                    tvNeitherAnswer.setVisibility(View.VISIBLE);
                    tvNeitherAnswer.setText(question.getQuestionOption3());
                    setTextColor(tvNeitherAnswer, R.color.tabcolor);
                    tvNeitherAnswer.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.result_cross_icon, 0);
                }

            }


        return convertView;
    }


    private View getLeaderBoardView(ViewGroup parent,Match match) {
        View leaderboardView = getLayoutInflater().inflate(R.layout.inflater_leaderboard_btn_row, parent, false);

        leaderboardView.findViewById(R.id.my_results_ll_others_answers).setOnClickListener(this);
        leaderboardView.findViewById(R.id.my_results_ll_leaderboards).setOnClickListener(this);
        leaderboardView.findViewById(R.id.my_results_ll_share_score).setOnClickListener(this);

        if (null != match.getResultdesc() && !match.getResultdesc().trim().isEmpty()){
            TextView tvcommentary = (TextView) leaderboardView.findViewById(R.id.schedule_row_tv_match_result_commentary);
            tvcommentary.setVisibility(View.VISIBLE);
            tvcommentary.setText(match.getResultdesc());
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
            case R.id.my_results_ll_share_score:
                broadcastShareScore(view.getContext());
                break;
        }
    }

    private void navigateToOthersAnswers(Context context) {
        Intent intent =  new Intent(context, OthersAnswersActivity.class);
        intent.putExtra(BundleKeys.MATCH_DETAILS, Parcels.wrap(getItem(0)));
        context.startActivity(intent);
    }

    private void navigateToLeaderboards(Context context) {
        Match match = getItem(0);

        LbLanding lbLanding = new LbLanding(
                match.getChallengeId(),
                match.getChallengeName(),
                match.getChallengeImgUrl(),
                LBLandingType.CHALLENGE
        );

        Intent intent =  new Intent(context, PointsActivity.class);
        intent.putExtra(BundleKeys.LB_LANDING_DATA, Parcels.wrap(lbLanding));
//        intent.putExtra(BundleKeys.TOURNAMENT_ID, match.getTournamentId());
        context.startActivity(intent);
    }

    private void broadcastShareScore(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(IntentActions.ACTION_SHARE_SCORE));
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
    }
}