package in.sportscafe.nostragamus.module.play.prediction;

import android.content.Context;
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

        viewHolder.tvQuestion.setText(question.getQuestionText());
        viewHolder.tvContext.setText(question.getQuestionContext());
        viewHolder.ivLeftOption.setImageUrl(question.getQuestionImage1(),
                Volley.getInstance().getImageLoader(), false);
        viewHolder.ivRightOption.setImageUrl(question.getQuestionImage2(),
                Volley.getInstance().getImageLoader(), false);
        question.setPowerUpId("null");

        viewHolder.view1.setTag(question);


        if (question.getQuestionNegativePoints()!=0 || question.getQuestionPositivePoints() !=0 ){
            viewHolder.tvquestionPositivePoints.setText("+"+question.getQuestionPositivePoints());
            viewHolder.tvquestionNegativePoints.setText("  |  "+question.getQuestionNegativePoints());
            viewHolder.tvquestionNegativePoints.setTag(question.getQuestionNegativePoints());
            viewHolder.tvquestionPositivePoints.setTag(question.getQuestionPositivePoints());
        }
        else if (question.getQuestionNegativePoints()==0){
            viewHolder.tvquestionNegativePoints.setVisibility(View.GONE);
        }
        else if (question.getQuestionPositivePoints()==0){
            viewHolder.tvquestionPositivePoints.setVisibility(View.GONE);
        }
        else {
            viewHolder.tvquestionPositivePoints.setVisibility(View.GONE);
        }

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
        mViewHolderList.get(0).rlquestion.setTag(getItem(0));
        Question question = (Question) mViewHolderList.get(0).rlquestion.getTag();
        question.setPowerUpId("2x");
        mViewHolderList.get(0).btnpowerupicon.setImageResource(R.drawable.powerup_2x_white);
        mViewHolderList.get(0).btnpowerupicon.setVisibility(View.VISIBLE);

        Integer positivevalue = (Integer) mViewHolderList.get(0).tvquestionPositivePoints.getTag();
        Integer Value = 2 * positivevalue;

        Integer negativevalue = (Integer) mViewHolderList.get(0).tvquestionNegativePoints.getTag();
        Integer Value2 = 2 * negativevalue;

        mViewHolderList.get(0).tvquestionPositivePoints.setText("+"+String.valueOf(Value));
        mViewHolderList.get(0).tvquestionNegativePoints.setText("  |  "+String.valueOf(Value2));

    }

    public void updateNonegsPowerUp() {
        mViewHolderList.get(0).rlquestion.setTag(getItem(0));
        Question question = (Question) mViewHolderList.get(0).rlquestion.getTag();
        question.setPowerUpId("no_negs");
        mViewHolderList.get(0).btnpowerupicon.setImageResource(R.drawable.powerup_nonegs_white);
        mViewHolderList.get(0).btnpowerupicon.setVisibility(View.VISIBLE);
        mViewHolderList.get(0).tvquestionNegativePoints.setText("");
    }

    public void updateAudiencePollPowerUp(String answer1percentage,String answer2percentage) {
        mViewHolderList.get(0).rlquestion.setTag(getItem(0));
        Question question = (Question) mViewHolderList.get(0).rlquestion.getTag();
        question.setPowerUpId("player_poll");
        mViewHolderList.get(0).btnanswer1Percentage.setText(answer1percentage);
        mViewHolderList.get(0).btnanswer2Percentage.setText(answer2percentage);
        mViewHolderList.get(0).btnanswer1Percentage.setVisibility(View.VISIBLE);
        mViewHolderList.get(0).btnanswer2Percentage.setVisibility(View.VISIBLE);
        mViewHolderList.get(0).btnpowerupicon.setImageResource(R.drawable.powerup_audience_poll_white);
        mViewHolderList.get(0).btnpowerupicon.setVisibility(View.VISIBLE);
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

        View view1;

        View view2;

        View view3;

        LinearLayout points;

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
            points= (LinearLayout) rootView.findViewById(R.id.swipe_card_question_points_rl);
            cardbg=(FrameLayout)rootView.findViewById(R.id.background);
            view1=(View) rootView.findViewById(R.id.bg_view1);
            view2=(View) rootView.findViewById(R.id.bg_view2);
            view3=(View) rootView.findViewById(R.id.bg_view3);
        }
    }




}