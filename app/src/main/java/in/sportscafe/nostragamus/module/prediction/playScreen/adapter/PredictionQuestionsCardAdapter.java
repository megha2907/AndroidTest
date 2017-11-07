package in.sportscafe.nostragamus.module.prediction.playScreen.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraTextViewLinkClickMovementMethod;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PlayersPoll;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PowerUp;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PowerUpEnum;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PredictionQuestion;
import in.sportscafe.nostragamus.module.prediction.playScreen.helper.PlayerPollHelper;
import in.sportscafe.nostragamus.utils.BitmapMemoryLruCache;
import in.sportscafe.nostragamus.utils.CodeSnippet;

/**
 * Created by sandip on 10/08/17.
 */

public class PredictionQuestionsCardAdapter extends ArrayAdapter<PredictionQuestion> {

    private PredictionQuestionAdapterListener mAdapterListener;
    private BitmapMemoryLruCache mImageCache;

    public PredictionQuestionsCardAdapter(@NonNull Context context,
                                          @NonNull List<PredictionQuestion> list,
                                          @NonNull PredictionQuestionAdapterListener listener) {
        super(context, R.layout.prediction_card_layout, list);
        mAdapterListener = listener;
        mImageCache = new BitmapMemoryLruCache();
    }

    @NonNull
    @Override
    public View getView(final int position, View contentView, ViewGroup parent) {
        if (contentView != null) {
            TextView positivePointsTextView = (TextView) contentView.findViewById(R.id.prediction_card_positive_textView);
            TextView negativePointsTextView = (TextView) contentView.findViewById(R.id.prediction_card_negative_textView);
            TextView questionTitleTextView = (TextView) contentView.findViewById(R.id.question_title_textView);
            TextView questionDescriptionTextView = (TextView) contentView.findViewById(R.id.question_description_textView);
            HmImageView option1ImgView = (HmImageView) contentView.findViewById(R.id.prediction_question_option_1_imgView);
            HmImageView option2ImgView = (HmImageView) contentView.findViewById(R.id.prediction_question_option_2_imgView);
            LinearLayout option1Layout = (LinearLayout) contentView.findViewById(R.id.option_1_button_layout);
            LinearLayout option2Layout = (LinearLayout) contentView.findViewById(R.id.option_2_button_layout);
            TextView option1TextView = (TextView) contentView.findViewById(R.id.option_1_textView);
            TextView option2TextView = (TextView) contentView.findViewById(R.id.option_2_textView);
            TextView playerPollOption1TextView = (TextView) contentView.findViewById(R.id.prediction_card_player_poll_1_textView);
            TextView playerPollOption2TextView = (TextView) contentView.findViewById(R.id.prediction_card_player_poll_2_textView);

            setActionListeners(position, option1Layout, option2Layout);
            setImageListenerAndCacheImages(option1ImgView, option2ImgView);
            setWebLinkListener(questionDescriptionTextView);

            /* Populate data on ui */
            PredictionQuestion question = getItem(position);

            populateData(positivePointsTextView, negativePointsTextView,
                    questionTitleTextView, questionDescriptionTextView,
                    option1ImgView, option2ImgView, option1TextView,
                    option2TextView, question);

            /* If Player-poll already applied */
            preFillIfPlayerPollAlreadyApplied(position, playerPollOption1TextView, playerPollOption2TextView, question);

        } else {
            contentView = new View(parent.getContext());    // Just to handle null warning, not much important as contentView can not be null anytime
        }
        return contentView;
    }

    /**
     * Fills images if cached, else setImageUrl will download it & cache
     * @param option1ImgView
     * @param option2ImgView
     * @param question
     */
    private void showCachedImagesOrFill(final HmImageView option1ImgView, final HmImageView option2ImgView, PredictionQuestion question) {
        if (!TextUtils.isEmpty(question.getQuestionImage1())) {
            final String url = CodeSnippet.formatUrl(question.getQuestionImage1());
            if (mImageCache.contains(url)) {
                Drawable drawable = new BitmapDrawable(getContext().getResources(), Bitmap.createBitmap(mImageCache.getBitmap(url)));
                option1ImgView.setBackground(drawable);
            } else {
                option1ImgView.setImageUrl(question.getQuestionImage1());
            }
        }

        if (!TextUtils.isEmpty(question.getQuestionImage2())) {
            final String url = CodeSnippet.formatUrl(question.getQuestionImage2());
            if (mImageCache.contains(url)) {
                Drawable drawable = new BitmapDrawable(option2ImgView.getResources(), Bitmap.createBitmap(mImageCache.getBitmap(url)));
                option2ImgView.setBackground(drawable);
            } else {
                option2ImgView.setImageUrl(question.getQuestionImage2());
            }
        }
    }


    private void setWebLinkListener(TextView questionDescriptionTextView) {
        questionDescriptionTextView.setMovementMethod(new NostraTextViewLinkClickMovementMethod() {
            @Override
            public void onLinkClick(String url) {
                if (mAdapterListener != null) {
                    mAdapterListener.onWebLinkClicked(url);
                }
            }
        });
    }

    private void setImageListenerAndCacheImages(HmImageView option1ImgView, HmImageView option2ImgView) {
        option1ImgView.setImageLoadedListener(new HmImageView.OnImageLoadedListener() {
            @Override
            public void onImageLoaded(String url, Bitmap bitmap) {
                mImageCache.putBitmap(url, bitmap);

            }
        });

        option2ImgView.setImageLoadedListener(new HmImageView.OnImageLoadedListener() {
            @Override
            public void onImageLoaded(String url, Bitmap bitmap) {
                mImageCache.putBitmap(url, bitmap);

            }
        });
    }

    private void setActionListeners(final int position, LinearLayout option1Layout, LinearLayout option2Layout) {
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
    }

    private void populateData(TextView positivePointsTextView, TextView negativePointsTextView,
                              TextView questionTitleTextView, TextView questionDescriptionTextView,
                              HmImageView option1ImgView, HmImageView option2ImgView,
                              TextView option1TextView, TextView option2TextView, PredictionQuestion question) {

        if (question != null) {
            showCachedImagesOrFill(option1ImgView, option2ImgView, question);

            if (!TextUtils.isEmpty(question.getQuestionText())) {
                questionTitleTextView.setText(question.getQuestionText());
            }
            if (!TextUtils.isEmpty(question.getQuestionContext())) {
                Spanned spanned = Html.fromHtml(question.getQuestionContext());
                if (Build.VERSION.SDK_INT >= 24) {
                    spanned = Html.fromHtml(question.getQuestionContext(), Html.FROM_HTML_MODE_LEGACY);
                }
                questionDescriptionTextView.setText(spanned);
            }
            if (!TextUtils.isEmpty(question.getQuestionOption1())) {
                option1TextView.setText(question.getQuestionOption1());
            }
            if (!TextUtils.isEmpty(question.getQuestionOption2())) {
                option2TextView.setText(question.getQuestionOption2());
            }
            positivePointsTextView.setText("+ " + String.valueOf(question.getPositivePoints()) + " PTS");
            negativePointsTextView.setText(String.valueOf(question.getNegativePoints()) + " PTS");
        }
    }

    private void preFillIfPlayerPollAlreadyApplied(int position, TextView playerPollOption1TextView,
                                                   TextView playerPollOption2TextView, PredictionQuestion question) {
        /* NOTE: Same logic for PredictionFragment.onPlayerPollSuccess() tobe followed */

        if (question != null && question.getPlayersPollList() != null && !question.getPlayersPollList().isEmpty()) {
            List<PlayersPoll> playersPollList = question.getPlayersPollList();

            String leftPollAnswer = PlayerPollHelper.getLeftAnswerString(playersPollList);
            String rightPollAnswer = PlayerPollHelper.getRightAnswerString(playersPollList);

            playerPollOption1TextView.setText(leftPollAnswer);
            playerPollOption2TextView.setText(rightPollAnswer);

            playerPollOption1TextView.setVisibility(View.VISIBLE);
            playerPollOption2TextView.setVisibility(View.VISIBLE);

            applyOrRemovePowerUp(PowerUpEnum.PLAYER_POLL, position, true);

            /* Set Minority Answer */
            PlayerPollHelper.setMinorityAnswerId(question, leftPollAnswer, rightPollAnswer);
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
                    if (shouldAdd) {
                        powerUp.setDoubler(1);
                        question.setPositivePoints(Constants.PredictionPoints.POSITIVE_POINTS * 2);
                        question.setNegativePoints(Constants.PredictionPoints.NEGATIVE_POINTS * 2);
                        
                        if (powerUp.getNoNegative() == 1) {
                            question.setNegativePoints(0);
                        }
                    } else {
                        powerUp.setDoubler(0);
                        question.setPositivePoints(Constants.PredictionPoints.POSITIVE_POINTS);
                        if (powerUp.getNoNegative() == 1) {
                            question.setNegativePoints(0);
                        } else {
                            question.setNegativePoints(Constants.PredictionPoints.NEGATIVE_POINTS);
                        }
                    }
                    break;

                case NO_NEGATIVE:
                    if (shouldAdd) {
                        powerUp.setNoNegative(1);
                        question.setNegativePoints(0);  // No negative points, positive as they are
                    } else {
                        powerUp.setNoNegative(0);
                        if (powerUp.getDoubler() == 1) {
                            question.setNegativePoints(Constants.PredictionPoints.NEGATIVE_POINTS * 2);
                        } else {
                            question.setNegativePoints(Constants.PredictionPoints.NEGATIVE_POINTS);
                        }
                    }

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
