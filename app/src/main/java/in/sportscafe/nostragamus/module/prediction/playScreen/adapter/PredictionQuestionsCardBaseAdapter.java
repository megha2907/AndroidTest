package in.sportscafe.nostragamus.module.prediction.playScreen.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PlayersPoll;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PowerUp;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PowerUpEnum;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PredictionQuestion;
import in.sportscafe.nostragamus.module.prediction.playScreen.helper.PredictionUiHelper;

/**
 * Created by sandip on 10/08/17.
 */

public class PredictionQuestionsCardBaseAdapter extends BaseAdapter {

    private PredictionQuestionAdapterListener mAdapterListener;
    private PredictionUiHelper uiHelper;
    private Context mContext;
    private List<PredictionQuestion> mQuestionList;

    public PredictionQuestionsCardBaseAdapter(@NonNull Context context,
                                          @NonNull List<PredictionQuestion> list,
                                          @NonNull PredictionQuestionAdapterListener listener) {
        mAdapterListener = listener;
        mContext = context;
        mQuestionList = list;
        uiHelper = new PredictionUiHelper();
    }

    @Override
    public int getCount() {
        return (mQuestionList != null) ? mQuestionList.size() : 0;
    }

    @Override
    public PredictionQuestion getItem(int position) {
        PredictionQuestion question = null;
        if (mQuestionList != null && position < mQuestionList.size()) {
            question = mQuestionList.get(position);
        }
        return question;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, View contentView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (contentView == null) {
            viewHolder = new ViewHolder();
            contentView = LayoutInflater.from(mContext).inflate(R.layout.prediction_card_layout, parent, false);

            viewHolder.positivePointsTextView = (TextView) contentView.findViewById(R.id.positive_points_cardview);
            viewHolder.negativePointsTextView = (TextView) contentView.findViewById(R.id.negative_points_cardview);
            viewHolder.questionTitleTextView = (TextView) contentView.findViewById(R.id.question_title_textView);
            viewHolder.questionDescriptionTextView = (TextView) contentView.findViewById(R.id.question_description_textView);
            viewHolder.option1ImgView = (HmImageView) contentView.findViewById(R.id.prediction_question_option_1_imgView);
            viewHolder.option2ImgView = (HmImageView) contentView.findViewById(R.id.prediction_question_option_2_imgView);
            viewHolder.option1Layout = (LinearLayout) contentView.findViewById(R.id.option_1_button_layout);
            viewHolder.option2Layout = (LinearLayout) contentView.findViewById(R.id.option_2_button_layout);
            viewHolder.option1TextView = (TextView) contentView.findViewById(R.id.option_1_textView);
            viewHolder.option2TextView = (TextView) contentView.findViewById(R.id.option_2_textView);
            viewHolder.playerPollOption1TextView = (TextView) contentView.findViewById(R.id.prediction_card_player_poll_1_textView);
            viewHolder.playerPollOption2TextView = (TextView) contentView.findViewById(R.id.prediction_card_player_poll_2_textView);

            viewHolder.option1Layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAdapterListener != null) {
                        mAdapterListener.onLeftOptionClicked(position);
                    }
                }
            });
            viewHolder.option2Layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAdapterListener != null) {
                        mAdapterListener.onRightOptionClicked(position);
                    }
                }
            });

            /* Tag is used for showing Third option */
            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        populateData(position, viewHolder);
        showPlayerPollIfAlreadyApplied(position, viewHolder);

        return contentView;
    }

    private void showPlayerPollIfAlreadyApplied(int position, ViewHolder viewHolder) {
        if (mQuestionList != null && position < mQuestionList.size() && viewHolder != null) {
            PredictionQuestion question = mQuestionList.get(position);

            if (question != null && question.getPlayersPollList() != null && question.getPlayersPollList().size() == 2) {
                List<PlayersPoll> playersPollList = question.getPlayersPollList();

                int pollPercentForOption1 = Integer.parseInt(playersPollList.get(0).getAnswerPercentage());
                int pollPercentForOption2 = Integer.parseInt(playersPollList.get(1).getAnswerPercentage());

                if (pollPercentForOption1 > 0 && pollPercentForOption2 > 0) {
                    viewHolder.playerPollOption1TextView.setText(pollPercentForOption1);
                    viewHolder.playerPollOption2TextView.setText(pollPercentForOption2);

                    viewHolder.playerPollOption1TextView.setVisibility(View.VISIBLE);
                    viewHolder.playerPollOption2TextView.setVisibility(View.VISIBLE);

                    applyOrRemovePowerUp(PowerUpEnum.PLAYER_POLL, position, true);

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
    }

    private void populateData(int position, ViewHolder viewHolder) {
        if (mQuestionList != null && position < mQuestionList.size() && viewHolder != null) {
            PredictionQuestion question = mQuestionList.get(position);
            if (question != null) {
                if (!TextUtils.isEmpty(question.getQuestionText())) {
                    viewHolder.questionTitleTextView.setText(question.getQuestionText());
                }
                if (!TextUtils.isEmpty(question.getQuestionContext())) {
                    viewHolder.questionDescriptionTextView.setText(question.getQuestionContext());
                }
                if (!TextUtils.isEmpty(question.getQuestionImage1())) {
                    viewHolder.option1ImgView.setImageUrl(question.getQuestionImage1());
                }
                if (!TextUtils.isEmpty(question.getQuestionImage2())) {
                    viewHolder.option2ImgView.setImageUrl(question.getQuestionImage2());
                }
                if (!TextUtils.isEmpty(question.getQuestionOption1())) {
                    viewHolder.option1TextView.setText(question.getQuestionOption1());
                }
                if (!TextUtils.isEmpty(question.getQuestionOption2())) {
                    viewHolder.option2TextView.setText(question.getQuestionOption2());
                }
                viewHolder.positivePointsTextView.setText("+ " + String.valueOf(question.getPositivePoints()) + " PTS");
                viewHolder.negativePointsTextView.setText("- " + String.valueOf(question.getNegativePoints()) + " PTS");
            }
        }
    }

    public void applyOrRemovePowerUp(PowerUpEnum powerUpEnum, int pos, boolean shouldAdd) {
        PredictionQuestion question = getItem(pos);
        if (question != null) {
            if (question.getPowerUp() == null) {
                question.setPowerUp(new PowerUp());
            }
            PowerUp powerUp = question.getPowerUp();

            switch (powerUpEnum) {
                case DOUBLER:
                    powerUp.setDoubler((shouldAdd) ? 1 : 0);

                    // Update points
                    if (question.getPositivePoints() > 0) {
                        question.setPositivePoints(question.getPositivePoints() * 2);
                    }
                    if (question.getNegativePoints() > 0) {
                        question.setNegativePoints(question.getNegativePoints() * 2);
                    }
                    break;

                case NO_NEGATIVE:
                    powerUp.setNoNegative((shouldAdd) ? 1 : 0);

                    question.setNegativePoints(0);  // No negative points, positive as they are
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

    /**
     * Removes question from adapter list & adds at last pos
     * Shuffle
     * @param topPos - visible top position
     */
    public void onShuffleQuestion(int topPos) {
        if (mQuestionList != null && topPos < mQuestionList.size()) {
            PredictionQuestion question = mQuestionList.get(topPos);
            mQuestionList.remove(topPos);
            mQuestionList.add(question);

            notifyDataSetChanged();
        }
    }

    /**
     * On every success response of prediction question, remove it from adapter list
     * @param topPos - position in adapter
     */
    public void onSuccessfulAnswer(int topPos) {
        if (mQuestionList != null && topPos < mQuestionList.size()) {
            mQuestionList.remove(topPos);
        }
    }

    private static class ViewHolder {
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
    }
}
