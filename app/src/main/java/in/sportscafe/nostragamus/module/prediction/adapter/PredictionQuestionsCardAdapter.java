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

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.prediction.dto.PlayersPoll;
import in.sportscafe.nostragamus.module.prediction.dto.PowerUp;
import in.sportscafe.nostragamus.module.prediction.dto.PowerupEnum;
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
            TextView positivePointsTextView;
            TextView negativePointsTextView;
            TextView playerPollOption1TextView;
            TextView playerPollOption2TextView;

            positivePointsTextView = (TextView) contentView.findViewById(R.id.prediction_card_positive_textView);
            negativePointsTextView = (TextView) contentView.findViewById(R.id.prediction_card_negative_textView);
            questionTitleTextView = (TextView) contentView.findViewById(R.id.question_title_textView);
            questionDescriptionTextView = (TextView) contentView.findViewById(R.id.question_description_textView);
            option1ImgView = (HmImageView) contentView.findViewById(R.id.prediction_question_option_1_imgView);
            option2ImgView = (HmImageView) contentView.findViewById(R.id.prediction_question_option_2_imgView);
            option1Layout = (LinearLayout) contentView.findViewById(R.id.option_1_button_layout);
            option2Layout = (LinearLayout) contentView.findViewById(R.id.option_2_button_layout);
            option1TextView = (TextView) contentView.findViewById(R.id.option_1_textView);
            option2TextView = (TextView) contentView.findViewById(R.id.option_2_textView);
            playerPollOption1TextView = (TextView) contentView.findViewById(R.id.prediction_card_player_poll_1_textView);
            playerPollOption2TextView = (TextView) contentView.findViewById(R.id.prediction_card_player_poll_2_textView);

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
                if (!TextUtils.isEmpty(question.getQuestionContext())) {
                    questionDescriptionTextView.setText(question.getQuestionContext());
                }
                if (!TextUtils.isEmpty(question.getQuestionImage1())) {
                    option1ImgView.setImageUrl(question.getQuestionImage1());
                }
                if (!TextUtils.isEmpty(question.getQuestionImage2())) {
                    option2ImgView.setImageUrl(question.getQuestionImage2());
                }
                if (!TextUtils.isEmpty(question.getQuestionOption1())) {
                    option1TextView.setText(question.getQuestionOption1());
                }
                if (!TextUtils.isEmpty(question.getQuestionOption2())) {
                    option2TextView.setText(question.getQuestionOption2());
                }

                /* If Player-poll already applied */
                if (question.getPlayersPollList() != null && question.getPlayersPollList().size() == 2) {
                    List<PlayersPoll> playersPollList = question.getPlayersPollList();

                    int pollPercentForOption1 = Integer.parseInt(playersPollList.get(0).getAnswerPercentage());
                    int pollPercentForOption2 = Integer.parseInt(playersPollList.get(1).getAnswerPercentage());

                    if (pollPercentForOption1 > 0 && pollPercentForOption2 > 0) {
                        playerPollOption1TextView.setText(pollPercentForOption1);
                        playerPollOption2TextView.setText(pollPercentForOption2);

                        playerPollOption1TextView.setVisibility(View.VISIBLE);
                        playerPollOption2TextView.setVisibility(View.VISIBLE);

                        applyOrRemovePowerUp(PowerupEnum.PLAYER_POLL, position, true);

                        /* Set Minority Answer */
                        if (pollPercentForOption1 > pollPercentForOption2) {
                            question.setMinorityAnswerId(Constants.AnswerIds.RIGHT);
                        } else if (pollPercentForOption1 < pollPercentForOption2) {
                            question.setMinorityAnswerId(Constants.AnswerIds.LEFT);
                        } else {
                            question.setMinorityAnswerId(-1);
                        }
                    }
                }

            }
        } else {
            contentView = new View(parent.getContext());    // Just to handle null warning, not much important as contentView can not be null anytime
        }
        return contentView;
    }

    public void applyOrRemovePowerUp(PowerupEnum powerupEnum, int pos, boolean shouldAdd) {
        PredictionQuestion question = getItem(pos);
        if (question != null) {
            if (question.getPowerUp() == null) {
                question.setPowerUp(new PowerUp());
            }
            PowerUp powerUp = question.getPowerUp();

            switch (powerupEnum) {
                case DOUBLER:
                    powerUp.setDoubler((shouldAdd) ? 1 : 0);
                    break;

                case NO_NEGATIVE:
                    powerUp.setNoNegative((shouldAdd) ? 1 : 0);
                    break;

                case PLAYER_POLL:
                    if (shouldAdd) {
                        powerUp.setPlayerPoll(1);
                    } else {
                        // NOTE: Player-poll can not be removed
                    }
                    break;
            }
        }
    }

}
