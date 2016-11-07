package in.sportscafe.scgame.module.play.myresults;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.volley.Volley;
import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.customfont.CustomButton;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.Adapter;
import in.sportscafe.scgame.module.feed.dto.Feed;
import in.sportscafe.scgame.module.feed.dto.Match;
import in.sportscafe.scgame.module.TournamentFeed.dto.Tournament;
import in.sportscafe.scgame.module.home.HomeActivity;
import in.sportscafe.scgame.module.play.prediction.PredictionActivity;
import in.sportscafe.scgame.module.play.prediction.dto.Question;
import in.sportscafe.scgame.utils.ViewUtils;
import in.sportscafe.scgame.utils.timeutils.TimeUtils;

/**
 * Created by Jeeva on 15/6/16.
 */
public class MyResultsAdapter extends Adapter<Feed, MyResultsAdapter.ViewHolder> implements View.OnClickListener {

    private OnMyResultsActionListener mResultsActionListener;

    private AlertDialog mAlertDialog;

    public MyResultsAdapter(Context context) {
        super(context);
    }

    public void setResultsActionListener(OnMyResultsActionListener mResultsActionListener) {
        this.mResultsActionListener = mResultsActionListener;
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


    class TourViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTvTournamentName;

        LinearLayout mLlScheduleParent;

        public TourViewHolder(View V) {
            super(V);

            mTvTournamentName = (TextView) V.findViewById(R.id.tour_row_tv_tour_name);
            mLlScheduleParent = (LinearLayout) V.findViewById(R.id.tour_row_ll_schedule_parent);

            V.findViewById(R.id.tour_row_ibtn_options).setOnClickListener(this);
            V.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tour_row_ibtn_options:
//                        mResultsActionListener.onClickLeaderBoard(mPosition);
                    break;
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
                match.getParties().get(0).getPartyImageUrl(),
                Volley.getInstance().getImageLoader(),
                false
        );

        holder.mIvPartyBPhoto.setImageUrl(
                match.getParties().get(1).getPartyImageUrl(),
                Volley.getInstance().getImageLoader(),
                false
        );


        if (null == match.getResult() || match.getResult().isEmpty()) {
            holder.mTvMatchResult.setVisibility(View.GONE);
            holder.mTvResultWait.setVisibility(View.VISIBLE);
            holder.mViewResult.setVisibility(View.VISIBLE);
            holder.mTvResultWait.setText(match.getMatchQuestionCount() + " predictions made, waiting for results");
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
            holder.mTvResultCorrectCount.setText("You got " + match.getCorrectCount() + "/" + match.getMatchQuestionCount() + " questions correct");

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
        }
    }


    private View getMyPrediction(ViewGroup parent, Question question) {
        View convertView = getLayoutInflater().inflate(R.layout.inflater_my_predictions_row, parent, false);

        ((TextView) convertView.findViewById(R.id.my_predictions_row_tv_question))
                .setText(question.getQuestionText());


        TextView tvAnswer = (TextView) convertView.findViewById(R.id.my_predictions_row_tv_answer);
        CustomButton powerupUsed = (CustomButton) convertView.findViewById(R.id.my_predictions_row_btn_answer_powerup_used);
        RelativeLayout powerup = (RelativeLayout) convertView.findViewById(R.id.my_predictions_row_rl);
        View vWrongAnswer = (View) convertView.findViewById(R.id.my_predictions_row_v_answer_line);
        TextView tvAnswerPoints = (TextView) convertView.findViewById(R.id.my_predictions_row_tv_answer_points);
        TextView tvCorrectAnswer = (TextView) convertView.findViewById(R.id.my_predictions_row_tv_correct_answer);
        LinearLayout leaderboard=(LinearLayout)convertView.findViewById(R.id.my_results_row_ll_leaderboardbtn);

        if (question.getAnswerPoints() != null) {

            if (question.getAnswerPoints().equals(0)) {
                setTextColor(tvAnswerPoints, R.color.textcolorlight);
            }

            tvAnswerPoints.setText(question.getAnswerPoints() + " Points");

        }

        String powerupused = question.getAnswerPowerUpId();
        if (powerupused.equals("null")) {
            powerupUsed.setVisibility(View.GONE);
            powerup.setVisibility(View.GONE);
        } else {
            powerupUsed.setVisibility(View.VISIBLE);
        }

        int answerId = Integer.parseInt(question.getAnswerId());

        if (answerId == 0) {
            tvAnswer.setText("Not Attempted");
            setTextColor(tvAnswer, R.color.monza);

            if (question.getQuestionAnswer() == 1) {
                tvCorrectAnswer.setText(question.getQuestionOption1());
            } else {
                tvCorrectAnswer.setText(question.getQuestionOption2());
            }

            tvAnswerPoints.setText("---");

        } else {
            if (answerId == 1) {
                tvAnswer.setText(question.getQuestionOption1());
            } else {
                tvAnswer.setText(question.getQuestionOption2());
            }

            if (null == question.getQuestionAnswer()) {
                setTextColor(tvAnswer, R.color.orange);
            } else if (question.getQuestionAnswer() == 0 || answerId == question.getQuestionAnswer()) {
                setTextColor(tvAnswer, R.color.lima);
            } else {
                setTextColor(tvAnswer, R.color.monza);
                vWrongAnswer.setVisibility(View.VISIBLE);
                tvCorrectAnswer.setVisibility(View.VISIBLE);

                if (question.getQuestionAnswer() == 1) {
                    tvCorrectAnswer.setText(question.getQuestionOption1());
                } else {
                    tvCorrectAnswer.setText(question.getQuestionOption2());
                }

            }

            leaderboard.addView(getLeaderBoardView(leaderboard));

        }

        return convertView;
    }


    private View getLeaderBoardView(ViewGroup parent) {


        View leaderboardView = getLayoutInflater().inflate(R.layout.inflater_leaderboard_btn_row, parent, false);

        CustomButton leaderBoardbtn = (CustomButton) leaderboardView.findViewById(R.id.leaderboard_btn);
        leaderBoardbtn.setOnClickListener(this);

        return leaderboardView;
    }

    @Override
    public void onClick(View v) {

          onclickLeaderBoardbtn(v);

    }

    private void onclickLeaderBoardbtn(View view) {

        Intent intent =  new Intent(view.getContext(), HomeActivity.class);
        ((Activity) view.getContext()).startActivityForResult(intent,1);

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