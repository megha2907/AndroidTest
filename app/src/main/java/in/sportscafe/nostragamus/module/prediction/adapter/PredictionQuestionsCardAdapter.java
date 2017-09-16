package in.sportscafe.nostragamus.module.prediction.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.prediction.dto.PredictionQuestion;

/**
 * Created by sandip on 10/08/17.
 */

public class PredictionQuestionsCardAdapter extends ArrayAdapter<PredictionQuestion> {

    private PredictionQuestionAdapterListener mAdapterListener;

    public PredictionQuestionsCardAdapter(@NonNull Context context,
                                          @NonNull List<PredictionQuestion> list,
                                          @NonNull PredictionQuestionAdapterListener listener) {
        super(context, R.layout.prediction_card_layout, list);
        mAdapterListener = listener;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(final int position, View contentView, ViewGroup parent) {
        if (contentView != null) {
            TextView questionTitleTextView;
            TextView questionDescriptionTextView;
            HmImageView option1ImgView;
            LinearLayout option1Layout;
            TextView option1TextView;
            HmImageView option2ImgView;
            LinearLayout option2Layout;
            TextView option2TextView;
            /*CardView positivePointsTextView;
            CardView negativePointsTextView;

            positivePointsTextView = (CardView) contentView.findViewById(R.id.prediction_card_positive_button);
            negativePointsTextView = (CardView) contentView.findViewById(R.id.prediction_card_negative_button);*/
            questionTitleTextView = (TextView) contentView.findViewById(R.id.question_title_textView);
            questionDescriptionTextView = (TextView) contentView.findViewById(R.id.question_description_textView);
            option1ImgView = (HmImageView) contentView.findViewById(R.id.prediction_question_option_1_imgView);
            option2ImgView = (HmImageView) contentView.findViewById(R.id.prediction_question_option_2_imgView);
            option1Layout = (LinearLayout) contentView.findViewById(R.id.option_1_button_layout);
            option2Layout = (LinearLayout) contentView.findViewById(R.id.option_2_button_layout);
            option1TextView = (TextView) contentView.findViewById(R.id.option_1_textView);
            option2TextView = (TextView) contentView.findViewById(R.id.option_2_textView);

            option1Layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAdapterListener != null) {
                        mAdapterListener.onLeftOptionClicked(position);
                    }
                }
            });
            option2Layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAdapterListener != null) {
                        mAdapterListener.onRightOptionClicked(position);
                    }
                }
            });

            PredictionQuestion question = getItem(position);
            if (question != null) {
                if (!TextUtils.isEmpty(question.getQuestionText())) {
                    questionTitleTextView.setText(question.getQuestionText());
                }
                if (!TextUtils.isEmpty(question.getQuestionDescription())) {
                    questionDescriptionTextView.setText(question.getQuestionDescription());
                }
                if (!TextUtils.isEmpty(question.getOption1ImgUrl())) {
                    option1ImgView.setImageUrl(question.getOption1ImgUrl());
                }
                if (!TextUtils.isEmpty(question.getOption2ImgUrl())) {
                    option2ImgView.setImageUrl(question.getOption2ImgUrl());
                }
                if (!TextUtils.isEmpty(question.getOption1())) {
                    option1TextView.setText(question.getOption1());
                }
                if (!TextUtils.isEmpty(question.getOption2())) {
                    option2TextView.setText(question.getOption2());
                }
            }
        } else {
            contentView = new View(parent.getContext());    // Just to handle null warning, not much important as contentView can not be null anytime
        }
        return contentView;
    }


}
