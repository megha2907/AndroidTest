package in.sportscafe.nostragamus.module.play.prediction;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.volley.Volley;
import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.customfont.CustomButton;
import com.jeeva.android.widgets.customfont.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.tindercard.FlingCardListener;

public class PredictionAdapter extends ArrayAdapter<Question>  {

    private static final int ONE_SECOND_IN_MS = 1000;

    private LayoutInflater mLayoutInflater;

    private int mInitialCount;
    private FlingCardListener mFlingCardListener;
    private int questionlineCount;
    private int contextlineCount;

    private List<ViewHolder> mViewHolderList = new ArrayList<>();

    public PredictionAdapter(Context context, int count) {
        super(context, android.R.layout.simple_list_item_1);
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mInitialCount = count;

    }

    @Override
    public void remove(Question object) {
        super.remove(object);

        if (mViewHolderList.size() > 0){
            mViewHolderList.remove(0);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.inflater_swipe_card_row, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolderList.add(viewHolder);


        Question question = getItem(position);

        viewHolder.tvQuestion.setText(question.getQuestionText());
        viewHolder.tvContext.setText(question.getQuestionContext());
        viewHolder.ivLeftOption.setImageUrl(question.getQuestionImage1(),
                Volley.getInstance().getImageLoader(), false);
        viewHolder.ivRightOption.setImageUrl(question.getQuestionImage2(),
                Volley.getInstance().getImageLoader(), false);

        viewHolder.view1.setTag(question);

        if (question.getQuestionPositivePoints() == 0) {
            viewHolder.tvquestionPositivePoints.setVisibility(View.GONE);
            viewHolder.cardViewpoints.setVisibility(View.GONE);
        } else {
            viewHolder.tvquestionPositivePoints.setText("+" + question.getQuestionPositivePoints());
            viewHolder.tvquestionPositivePoints.setTag(question.getQuestionPositivePoints());
            viewHolder.tvquestionPositivePoints.setVisibility(View.VISIBLE);
        }

        if (question.getQuestionNegativePoints() == 0) {
            viewHolder.tvquestionNegativePoints.setVisibility(View.GONE);
            viewHolder.viewPoints.setVisibility(View.GONE);
        } else {
            viewHolder.tvquestionNegativePoints.setText(""+question.getQuestionNegativePoints());
            viewHolder.tvquestionNegativePoints.setTag(question.getQuestionNegativePoints());
            viewHolder.tvquestionNegativePoints.setVisibility(View.VISIBLE);
        }

        if(question.getPowerUpId().equalsIgnoreCase("no_negs")) {
            viewHolder.btnpowerupicon.setImageResource(R.drawable.powerup_nonegs_white);
            viewHolder.btnpowerupicon.setVisibility(View.VISIBLE);
        } else if(question.getPowerUpId().equalsIgnoreCase("2x")) {
            viewHolder.btnpowerupicon.setImageResource(R.drawable.powerup_2x_white);
            viewHolder.btnpowerupicon.setVisibility(View.VISIBLE);
        } else if(question.getPowerUpId().equalsIgnoreCase("player_poll")) {
            viewHolder.btnpowerupicon.setImageResource(R.drawable.powerup_audience_poll_white);
            viewHolder.btnpowerupicon.setVisibility(View.VISIBLE);

            viewHolder.btnanswer1Percentage.setText(question.getOption1AudPollPer());
            viewHolder.btnanswer1Percentage.setVisibility(View.VISIBLE);

            viewHolder.btnanswer2Percentage.setText(question.getOption2AudPollPer());
            viewHolder.btnanswer2Percentage.setVisibility(View.VISIBLE);
        } else {
            viewHolder.btnpowerupicon.setVisibility(View.GONE);
        }


        viewHolder.tvQuestion.post(new Runnable() {
            @Override
            public void run() {
                questionlineCount = viewHolder.tvQuestion.getLineCount();

                if (questionlineCount==3){
                    viewHolder.tvContext.setMaxLines(3);
                    viewHolder.tvContext.setEllipsize(TextUtils.TruncateAt.END);
                }else if(questionlineCount==2){
                    viewHolder.tvContext.setMaxLines(4);
                    viewHolder.tvContext.setEllipsize(TextUtils.TruncateAt.END);
                }else if(questionlineCount==1){
                    viewHolder.tvContext.setMaxLines(5);
                    viewHolder.tvContext.setEllipsize(TextUtils.TruncateAt.END);
                }else {
                    viewHolder.tvContext.setMaxLines(3);
                    viewHolder.tvContext.setEllipsize(TextUtils.TruncateAt.END);
                }

            }
        });





        return convertView;
    }

    public void changeCardViewBackground(){

        Question question = (Question) mViewHolderList.get(0).view1.getTag();

        if (mInitialCount== question.getQuestionNumber()){
            mViewHolderList.get(0).cardbg.setVisibility(View.INVISIBLE);
        }
        else if (mInitialCount-1==question.getQuestionNumber()){
            mViewHolderList.get(0).view1.setVisibility(View.VISIBLE);
        }
        else if (mInitialCount-2==question.getQuestionNumber()){
            mViewHolderList.get(0).view1.setVisibility(View.VISIBLE);
            mViewHolderList.get(0).view2.setVisibility(View.VISIBLE);
        }
        else {
            mViewHolderList.get(0).view1.setVisibility(View.VISIBLE);
            mViewHolderList.get(0).view2.setVisibility(View.VISIBLE);
            mViewHolderList.get(0).view3.setVisibility(View.VISIBLE);
        }

    }


    public void update2xPowerUp() {

        Log.i("inside","2x");
        mViewHolderList.get(0).rlquestion.setTag(getItem(0));
        Question question = (Question) mViewHolderList.get(0).rlquestion.getTag();
        question.setPowerUpId("2x");
        question.setQuestionPositivePoints(2 * question.getQuestionPositivePoints());
        question.setQuestionNegativePoints(2 * question.getQuestionNegativePoints());
    }

    public void updateNonegsPowerUp() {
        mViewHolderList.get(0).rlquestion.setTag(getItem(0));
        Question question = (Question) mViewHolderList.get(0).rlquestion.getTag();
        question.setPowerUpId("no_negs");
        question.setQuestionNegativePoints(0);
    }

    public void updateAudiencePollPowerUp(String answer1percentage,String answer2percentage) {
        mViewHolderList.get(0).rlquestion.setTag(getItem(0));
        Question question = (Question) mViewHolderList.get(0).rlquestion.getTag();
        question.setPowerUpId("player_poll");
        question.setOption1AudPollPer(answer1percentage);
        question.setOption2AudPollPer(answer2percentage);
    }

    public Question getTopQuestion(){
        return getItem(0);
    }

//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()) {
//
//            case R.id.swipe_card_iv_left:
//                mFlingCardListener.selectLeft();
//                break;
//
//            case R.id.swipe_card_iv_right:
//                mFlingCardListener.selectRight();
//                break;
//
//
//        }
//    }

    public void setFlingCardListener(FlingCardListener flingCardListener) {
        mFlingCardListener = flingCardListener;
    }


    static class ViewHolder {

        TextView tvQuestion;

        TextView tvContext;

        HmImageView ivLeftOption;

        HmImageView ivRightOption;

        RelativeLayout rlquestion;

        ImageButton btnpowerupicon;

        Button btnanswer1Percentage;

        Button btnanswer2Percentage;

        CustomTextView tvquestionPositivePoints;

        CustomTextView tvquestionNegativePoints;

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
            rlquestion = (RelativeLayout) rootView.findViewById(R.id.swipe_relative_layout_question);
            btnpowerupicon = (ImageButton) rootView.findViewById(R.id.swipe_card_powerup_icon);
            btnanswer1Percentage = (Button) rootView.findViewById(R.id.swipe_card_answer1_percentage);
            btnanswer2Percentage = (Button) rootView.findViewById(R.id.swipe_card_answer2_percentage);
            tvquestionPositivePoints = (CustomTextView) rootView.findViewById(R.id.swipe_card_question_positive_points);
            tvquestionNegativePoints = (CustomTextView) rootView.findViewById(R.id.swipe_card_question_negative_points);
            cardbg=(FrameLayout)rootView.findViewById(R.id.background);
            view1=(View) rootView.findViewById(R.id.bg_view1);
            view2=(View) rootView.findViewById(R.id.bg_view2);
            view3=(View) rootView.findViewById(R.id.bg_view3);
            viewPoints=(View) rootView.findViewById(R.id.swipe_card_question_points_line);
            cardViewpoints=(CardView)rootView.findViewById(R.id.points_cardview);
        }
    }




}