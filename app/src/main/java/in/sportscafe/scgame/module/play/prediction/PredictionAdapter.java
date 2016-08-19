package in.sportscafe.scgame.module.play.prediction;

import android.content.ClipData;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.volley.Volley;
import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.customfont.CustomButton;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.common.CircularProgressBar;
import in.sportscafe.scgame.module.common.CountDownTimer;
import in.sportscafe.scgame.module.play.prediction.dto.Answer;
import in.sportscafe.scgame.module.play.prediction.dto.Question;
import in.sportscafe.scgame.module.play.tindercard.SwipeFlingAdapterView;
import in.sportscafe.scgame.module.user.login.dto.UserInfo;

public class PredictionAdapter extends ArrayAdapter<Question>  {

    private static final int ONE_SECOND_IN_MS = 1000;

    private LayoutInflater mLayoutInflater;

    private int mInitialCount;

    private PredictionTimer mPredictionTimer;

    private List<ViewHolder> mViewHolderList = new ArrayList<>();

    private OnPredictionTimerListener mPredictionTimerListener;

    public PredictionAdapter(Context context, OnPredictionTimerListener predictionTimerListener, int count) {
        super(context, android.R.layout.simple_list_item_1);
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mInitialCount = count;
        this.mPredictionTimerListener = predictionTimerListener;
    }

    public void startTimer() {
        if (mPredictionTimer == null && mViewHolderList.size() > 0) {
            int questionTime = getItem(0).getQuestionTime();
            Log.d("MaxTime", questionTime + " seconds");
            ViewHolder topViewHolder = mViewHolderList.get(0);
            topViewHolder.cpbTimer.setMax(questionTime);
            topViewHolder.cpbTimer.setProgress(questionTime);

            mPredictionTimer = new PredictionTimer(topViewHolder.cpbTimer,
                    questionTime * ONE_SECOND_IN_MS, ONE_SECOND_IN_MS);
            mPredictionTimer.start();
        }
    }

    public void stopTimer() {
        if (null != mPredictionTimer) {
            mPredictionTimer.cancel();
            mPredictionTimer = null;
        }
    }

    public int getRemainingTime() {
        if (null != mPredictionTimer) {
            return mPredictionTimer.mCircularProgressBar.getProgress();
        }
        return 0;
    }

    @Override
    public void remove(Question object) {
        super.remove(object);
        mViewHolderList.remove(0);
       // powerUpApplied = false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.inflater_swipe_card_row, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolderList.add(viewHolder);


        Question question = getItem(position);

//        viewHolder.tvQuestionNumber.setText((position + 1 + mInitialCount - getCount()) + "/" + mInitialCount);
        viewHolder.tvQuestionNumber.setText(question.getQuestionNumber() + "/" + mInitialCount);

        viewHolder.tvQuestion.setText(question.getQuestionText());
        viewHolder.tvContext.setText(question.getQuestionContext());

        viewHolder.tvLeftOption.setText(question.getQuestionOption1());
        viewHolder.ivLeftOption.setImageUrl(question.getQuestionImage1(),
                Volley.getInstance().getImageLoader(), false);

        viewHolder.tvRightOption.setText(question.getQuestionOption2());
        viewHolder.ivRightOption.setImageUrl(question.getQuestionImage2(),
                Volley.getInstance().getImageLoader(), false);

     //   viewHolder.btnquestionValue.setText("2X");

      //  viewHolder.btnquestionValue.setOnTouchListener(this);
    //    viewHolder.rlquestion.setOnDragListener(this);
        question.setPowerUpId("null");

//        int powerUps = ScGameDataHandler.getInstance().getNumberofPowerups();
//        viewHolder.btnpowerUpCount.setText(String.valueOf(powerUps));

//        if (powerUps < 1) {
//            powerUpApplied = true;
//        }

        return convertView;
    }
//
//    private boolean powerUpApplied = false;
//
//    @Override
//    public boolean onTouch(View v, MotionEvent arg1) {
//        if(!powerUpApplied) {
//            ClipData data = ClipData.newPlainText("", "");
//            View.DragShadowBuilder shadow = new View.DragShadowBuilder(v);
//            v.startDrag(data, shadow, null, 0);
//        }
//        return false;
//    }
//
//    @Override
//    public boolean onDrag(View view, DragEvent dragEvent) {
//        final int action = dragEvent.getAction();
//        switch(action) {
//
//            case DragEvent.ACTION_DRAG_STARTED:
//                break;
//
//            case DragEvent.ACTION_DRAG_EXITED:
//                break;
//
//            case DragEvent.ACTION_DRAG_ENTERED:
//                break;
//
//            case DragEvent.ACTION_DROP:
//                break;
//
//            case DragEvent.ACTION_DRAG_ENDED:
//                if(!powerUpApplied) {
//
//                    if (ScGameDataHandler.getInstance().getNumberofPowerups() > 0) {
//                    powerUpApplied = true;
//                    mViewHolderList.get(0).rlquestion.setTag(getItem(0));
//                    Question question = (Question) mViewHolderList.get(0).rlquestion.getTag();
//                    question.setPowerUpId("2x");
//
//                    mViewHolderList.get(0).rlquestion.setBackgroundColor(
//                            ContextCompat.getColor(getContext(), R.color.powerup_color));
//                    mViewHolderList.get(0).tvpowerUptext.setVisibility(view.VISIBLE);
//
//                    ScGameDataHandler.getInstance().setNumberofPowerups(ScGameDataHandler.getInstance().getNumberofPowerups() - 1);
//                    String UpdatedPowerUps = String.valueOf(ScGameDataHandler.getInstance().getNumberofPowerups());
//
//                    if (mViewHolderList.size() == 1) {
//                        mViewHolderList.get(0).btnpowerUpCount.setText(UpdatedPowerUps);
//
//                    } else {
//                        mViewHolderList.get(0).btnpowerUpCount.setText(UpdatedPowerUps);
//                        mViewHolderList.get(1).btnpowerUpCount.setText(UpdatedPowerUps);
//                    }
//
//                    }
//                }
//                break;
//        }
//        return true;
//    }

    public void UpdatePowerup() {

                mViewHolderList.get(0).rlquestion.setTag(getItem(0));
                Question question = (Question) mViewHolderList.get(0).rlquestion.getTag();
                question.setPowerUpId("2x");

                mViewHolderList.get(0).rlquestion.setBackgroundColor(
                        ContextCompat.getColor(getContext(), R.color.powerup_color));
                mViewHolderList.get(0).tvpowerUptext.setVisibility(View.VISIBLE);


    }

    public interface OnPredictionTimerListener {

        void onTimeUp();
    }

    static class ViewHolder {

        CircularProgressBar cpbTimer;

        TextView tvQuestionNumber;

        TextView tvQuestion;

        TextView tvContext;

        TextView tvLeftOption;

        TextView tvRightOption;

        HmImageView ivLeftOption;

        HmImageView ivRightOption;

        CustomButton btnquestionValue;

        CustomButton btnpowerUpCount;

        RelativeLayout rlquestion;

        TextView tvpowerUptext;

        public ViewHolder(View rootView) {
            cpbTimer = (CircularProgressBar) rootView.findViewById(R.id.swipe_card_cpb_timer);
            tvQuestionNumber = (TextView) rootView.findViewById(R.id.swipe_card_tv_question_number);
            tvQuestion = (TextView) rootView.findViewById(R.id.swipe_card_tv_question);
            tvContext = (TextView) rootView.findViewById(R.id.swipe_card_tv_context);
            tvLeftOption = (TextView) rootView.findViewById(R.id.swipe_card_tv_left);
            tvRightOption = (TextView) rootView.findViewById(R.id.swipe_card_tv_right);
            ivLeftOption = (HmImageView) rootView.findViewById(R.id.swipe_card_iv_left);
            ivRightOption = (HmImageView) rootView.findViewById(R.id.swipe_card_iv_right);
           // btnquestionValue = (CustomButton) rootView.findViewById(R.id.swipe_card_question_value);
          //  btnpowerUpCount = (CustomButton) rootView.findViewById(R.id.swipe_card_tv_powerup_count);
            rlquestion = (RelativeLayout) rootView.findViewById(R.id.swipe_relative_layout_question);
            tvpowerUptext = (TextView) rootView.findViewById(R.id.swipe_card_tv_powerup);
        }
    }

    class PredictionTimer extends CountDownTimer {

        CircularProgressBar mCircularProgressBar;

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public PredictionTimer(CircularProgressBar circularProgressBar,
                               long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            this.mCircularProgressBar = circularProgressBar;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            updateProgress(millisUntilFinished);
        }

        @Override
        public void onFinish() {
            mCircularProgressBar.setProgress(0);
            mPredictionTimerListener.onTimeUp();
        }

        private void updateProgress(long millisUntilFinished) {
            final int secondsRemaining = (int) (millisUntilFinished / ONE_SECOND_IN_MS);
            mCircularProgressBar.setProgress(secondsRemaining);
        }

    }
}