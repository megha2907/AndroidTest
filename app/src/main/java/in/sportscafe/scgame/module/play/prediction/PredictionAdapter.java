package in.sportscafe.scgame.module.play.prediction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.volley.Volley;
import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.customfont.CustomButton;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.play.prediction.dto.Question;
import in.sportscafe.scgame.module.play.tindercard.FlingCardListener;

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
        mViewHolderList.remove(0);
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

        //viewHolder.ivLeftOption.setOnClickListener(this);
        //viewHolder.ivRightOption.setOnClickListener(this);

        return convertView;
    }

    public void updatePowerUp() {

                mViewHolderList.get(0).rlquestion.setTag(getItem(0));
                Question question = (Question) mViewHolderList.get(0).rlquestion.getTag();
                question.setPowerUpId("2x");

         mViewHolderList.get(0).btnpowerupicon.setVisibility(View.VISIBLE);


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

        CustomButton btnpowerupicon;

        public ViewHolder(View rootView) {
            tvQuestion = (TextView) rootView.findViewById(R.id.swipe_card_tv_question);
            tvContext = (TextView) rootView.findViewById(R.id.swipe_card_tv_context);
            ivLeftOption = (HmImageView) rootView.findViewById(R.id.swipe_card_iv_left);
            ivRightOption = (HmImageView) rootView.findViewById(R.id.swipe_card_iv_right);
            rlquestion = (RelativeLayout) rootView.findViewById(R.id.swipe_relative_layout_question);
            btnpowerupicon = (CustomButton) rootView.findViewById(R.id.swipe_card_powerup_icon);
        }
    }


}