package in.sportscafe.nostragamus.module.play.prediction;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.tindercard.FlingCardListener;

public class PredictionAdapter extends ArrayAdapter<Question> {

    private LayoutInflater mLayoutInflater;

    private int mInitialCount;

    private Boolean isRightSwiped;

    private FlingCardListener mFlingCardListener;

    private View mTopView, mLeftOption, mRightOption, mLeftOptionArrow, mRightOptionArrow;

    public PredictionAdapter(Context context, int count) {
        super(context, android.R.layout.simple_list_item_1);
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mInitialCount = count;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mLayoutInflater.inflate(R.layout.inflater_swipe_card_row, parent, false);
        final ViewHolder viewHolder = new ViewHolder(convertView);
        convertView.setTag(viewHolder);

        Question question = getItem(position);

        viewHolder.tvQuestion.setText(question.getQuestionText());
        viewHolder.tvContext.setText(question.getQuestionContext());
        viewHolder.ivLeftOption.setImageUrl(question.getQuestionImage1());
        viewHolder.ivRightOption.setImageUrl(question.getQuestionImage2());
        viewHolder.tvLeftOption.setText(question.getQuestionOption1());
        viewHolder.tvRightOption.setText(question.getQuestionOption2());

        if (null == question.getQuestionPositivePoints()
                || question.getQuestionPositivePoints() == 0) {
            viewHolder.tvquestionPositivePoints.setVisibility(View.GONE);
            viewHolder.cardViewpoints.setVisibility(View.GONE);
        } else {
            viewHolder.tvquestionPositivePoints.setText("+" + question.getQuestionPositivePoints());
            viewHolder.tvquestionPositivePoints.setTag(question.getQuestionPositivePoints());
            viewHolder.tvquestionPositivePoints.setVisibility(View.VISIBLE);
        }

        if (null == question.getQuestionNegativePoints()
                || question.getQuestionNegativePoints() == 0) {
            viewHolder.tvquestionNegativePoints.setVisibility(View.GONE);
            viewHolder.viewPoints.setVisibility(View.GONE);
            viewHolder.tvquestionPositivePoints.setPadding(32, 0, 32, 0);
        } else {
            viewHolder.tvquestionNegativePoints.setText("" + question.getQuestionNegativePoints());
            viewHolder.tvquestionNegativePoints.setTag(question.getQuestionNegativePoints());
            viewHolder.tvquestionNegativePoints.setVisibility(View.VISIBLE);
        }


        if (question.getPowerUpId().equalsIgnoreCase("no_negs")) {
            viewHolder.btnpowerupicon.setImageResource(R.drawable.powerup_nonegs_white);
            viewHolder.btnpowerupicon.setVisibility(View.VISIBLE);
        } else if (question.getPowerUpId().equalsIgnoreCase("2x")) {
            viewHolder.btnpowerupicon.setImageResource(R.drawable.powerup_2x_white);
            viewHolder.btnpowerupicon.setVisibility(View.VISIBLE);
        } else if (question.getPowerUpId().equalsIgnoreCase("player_poll")) {
            viewHolder.btnpowerupicon.setImageResource(R.drawable.powerup_audience_poll_white);
            viewHolder.btnpowerupicon.setVisibility(View.VISIBLE);

            viewHolder.btnanswer1Percentage.setText(question.getOption1AudPollPer());
            viewHolder.btnanswer1Percentage.setVisibility(View.VISIBLE);

            viewHolder.btnanswer2Percentage.setText(question.getOption2AudPollPer());
            viewHolder.btnanswer2Percentage.setVisibility(View.VISIBLE);
        } else {
            viewHolder.btnpowerupicon.setVisibility(View.GONE);
        }


        if (mInitialCount == question.getQuestionNumber()) {
            viewHolder.cardbg.setVisibility(View.INVISIBLE);
        } else if (mInitialCount - 1 == question.getQuestionNumber()) {
            viewHolder.view1.setVisibility(View.VISIBLE);
        } else if (mInitialCount - 2 == question.getQuestionNumber()) {
            viewHolder.view1.setVisibility(View.VISIBLE);
            viewHolder.view2.setVisibility(View.VISIBLE);
        } else {
            viewHolder.view1.setVisibility(View.VISIBLE);
            viewHolder.view2.setVisibility(View.VISIBLE);
        }


        viewHolder.tvQuestion.post(new Runnable() {
            @Override
            public void run() {
                int questionlineCount = viewHolder.tvQuestion.getLineCount();

                if (questionlineCount == 3) {
                    viewHolder.tvContext.setMaxLines(3);
                    viewHolder.tvContext.setEllipsize(TextUtils.TruncateAt.END);
                } else if (questionlineCount == 2) {
                    viewHolder.tvContext.setMaxLines(4);
                    viewHolder.tvContext.setEllipsize(TextUtils.TruncateAt.END);
                } else if (questionlineCount == 1) {
                    viewHolder.tvContext.setMaxLines(5);
                    viewHolder.tvContext.setEllipsize(TextUtils.TruncateAt.END);
                } else {
                    viewHolder.tvContext.setMaxLines(3);
                    viewHolder.tvContext.setEllipsize(TextUtils.TruncateAt.END);
                }

            }
        });

        return convertView;
    }


    public void update2xPowerUp() {
        Question question = getItem(0);
        question.setPowerUpId("2x");
        question.setQuestionPositivePoints(2 * question.getQuestionPositivePoints());
        question.setQuestionNegativePoints(2 * question.getQuestionNegativePoints());
    }

    public void updateNonegsPowerUp() {
        Question question = getItem(0);
        question.setPowerUpId("no_negs");
        question.setQuestionNegativePoints(0);
    }

    public void updateAudiencePollPowerUp(String answer1percentage, String answer2percentage) {
        Question question = getItem(0);
        question.setPowerUpId("player_poll");
        question.setOption1AudPollPer(answer1percentage);
        question.setOption2AudPollPer(answer2percentage);
    }

    public Question getTopQuestion() {
        return getItem(0);
    }

    public void onCardMovedRight() {
        setOptionVisibility(View.INVISIBLE, View.VISIBLE);
    }

    public void onCardMovedLeft() {
        setOptionVisibility(View.VISIBLE, View.INVISIBLE);
    }

    public void onCardMoveStopped() {
        setOptionVisibility(View.VISIBLE, View.VISIBLE);
    }

    public void setFlingCardListener(FlingCardListener listener) {
        this.mFlingCardListener = listener;
        this.mTopView = mFlingCardListener.getFrame();
        this.mLeftOption = mTopView.findViewById(R.id.swipe_card_tv_left);
        this.mRightOption = mTopView.findViewById(R.id.swipe_card_tv_right);
        this.mLeftOptionArrow = mTopView.findViewById(R.id.swipe_card_iv_left_arrow);
        this.mRightOptionArrow = mTopView.findViewById(R.id.swipe_card_iv_right_arrow);
    }

    private void setOptionVisibility(int leftVis, int rightVis) {
        if(leftVis != mLeftOption.getVisibility()) {
            mLeftOption.setVisibility(leftVis);
            mLeftOptionArrow.setVisibility(leftVis);
        }

        if(rightVis != mRightOption.getVisibility()) {
            mRightOption.setVisibility(rightVis);
            mRightOptionArrow.setVisibility(rightVis);
        }
    }

    static class ViewHolder  {

        TextView tvQuestion;

        TextView tvContext;

        HmImageView ivLeftOption;

        HmImageView ivRightOption;

        TextView tvLeftOption;

        TextView tvRightOption;

        ImageButton btnpowerupicon;

        TextView btnanswer1Percentage;

        TextView btnanswer2Percentage;

        TextView tvquestionPositivePoints;

        TextView tvquestionNegativePoints;

        FrameLayout cardbg;

        CardView cardViewpoints;

        View view1;

        View view2;

        View view3;

        View viewPoints;

        public ViewHolder(View rootView) {
            tvQuestion = (TextView) rootView.findViewById(R.id.swipe_card_tv_question);
            tvContext = (TextView) rootView.findViewById(R.id.swipe_card_tv_context);
            ivLeftOption = (HmImageView) rootView.findViewById(R.id.swipe_card_iv_left);
            ivRightOption = (HmImageView) rootView.findViewById(R.id.swipe_card_iv_right);
            tvLeftOption = (TextView) rootView.findViewById(R.id.swipe_card_tv_left);
            tvRightOption = (TextView) rootView.findViewById(R.id.swipe_card_tv_right);
            btnpowerupicon = (ImageButton) rootView.findViewById(R.id.swipe_card_powerup_icon);
            btnanswer1Percentage = (TextView) rootView.findViewById(R.id.swipe_card_tv_left_poll);
            btnanswer2Percentage = (TextView) rootView.findViewById(R.id.swipe_card_answer2_percentage);
            tvquestionPositivePoints = (TextView) rootView.findViewById(R.id.swipe_card_question_positive_points);
            tvquestionNegativePoints = (TextView) rootView.findViewById(R.id.swipe_card_question_negative_points);
            cardbg = (FrameLayout) rootView.findViewById(R.id.background);
            view1 = rootView.findViewById(R.id.bg_view1);
            view2 = rootView.findViewById(R.id.bg_view2);
            view3 = rootView.findViewById(R.id.bg_view3);
            viewPoints = rootView.findViewById(R.id.swipe_card_question_points_line);
            cardViewpoints = (CardView) rootView.findViewById(R.id.points_cardview);
        }
    }

}
