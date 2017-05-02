package in.sportscafe.nostragamus.module.play.prediction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.HmImageView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.Powerups;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.feed.FeedWebView;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.tindercard.FlingCardListener;
import in.sportscafe.nostragamus.utils.ViewUtils;

public class PredictionAdapter extends ArrayAdapter<Question> {

    private static final float HEADER_PERECENTAGE = 8f / 100;

    private static final float FOOTER_PERECENTAGE = 22f / 100;

    private static final float GAP_BW_HEADER_CARD_PERECENTAGE = 7f / 100;

    private static final float OPTION_PERECENTAGE = 6.25f / 100;

    private static final float CARD_HEIGHT_PERECENTAGE = 53f / 100;

    private static final float L_R_DELTA_PERECENTAGE = 1.5f / 100;

    private static final float NEITHER_DELTA_PERECENTAGE = 1.5f / 100;

    private LayoutInflater mLayoutInflater;

    private View vBgFrame1;

    private View vBgFrame2;

    private ViewHolder mTopViewHolder;

    private boolean mBgUpdateDone = false;

    private int mTopMargin;

    private FlingCardListener mFlingCardListener;

    private Question mTopQuestion;

    private boolean mNeitherOptionAvailable = false;

    private float mOptionHeight;

    private float mCardWidth;

    private float mCardHeight;

    private float mCardMargin;

    private float mImageWidth;

    private float mImageHeight;

    private View.OnClickListener mRemovePowerUpListener;

    public PredictionAdapter(Context context, View.OnClickListener removePowerUpListener) {
        super(context, android.R.layout.simple_list_item_1);
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mRemovePowerUpListener = removePowerUpListener;
    }

    public void setRootView(View rootView) {
        /*Rect rect = new Rect();
        rootView.getLocalVisibleRect(rect);

        applyFrameCardPercentages(rootView, rect.height());*/
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) rootView.getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);

        applyFrameCardPercentages(rootView, dm.heightPixels);
    }

    private void applyFrameCardPercentages(View rootView, float screenHeight) {
        mCardHeight = screenHeight * CARD_HEIGHT_PERECENTAGE;
        mCardWidth = mCardHeight;
        mOptionHeight = screenHeight * OPTION_PERECENTAGE;
        mImageWidth = mCardWidth / 2f;
        mImageHeight = mImageWidth;

        View vBgFrame0 = rootView.findViewById(R.id.prediction_cv_bg_0);
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) vBgFrame0.getLayoutParams();
        rlp.width = (int) mCardWidth;
        rlp.height = (int) (mCardHeight + mOptionHeight);
        mTopMargin = (int) (screenHeight * GAP_BW_HEADER_CARD_PERECENTAGE);
        rlp.topMargin = mTopMargin;

        int headerHeight = rootView.findViewById(R.id.prediction_rl_header).getMeasuredHeight();
        ((RelativeLayout.LayoutParams) rootView.findViewById(R.id.prediction_iv_dummy_left_right_indicator).getLayoutParams())
                .topMargin = (int) (mTopMargin + rlp.height - screenHeight * 1.5f / 100 - mOptionHeight / 2f + headerHeight);

        ((RelativeLayout.LayoutParams) rootView.findViewById(R.id.prediction_iv_dummy_neither_indicator).getLayoutParams())
                .topMargin = (int) (mTopMargin + rlp.height - mOptionHeight + headerHeight);

        vBgFrame1 = rootView.findViewById(R.id.prediction_cv_bg_1);
        vBgFrame1.getLayoutParams().height = (int) (mCardHeight + mOptionHeight);

        vBgFrame2 = rootView.findViewById(R.id.prediction_cv_bg_2);
        vBgFrame2.getLayoutParams().height = (int) (mCardHeight + mOptionHeight);

        rlp = (RelativeLayout.LayoutParams) rootView.findViewById(R.id.prediction_rl_header).getLayoutParams();
        rlp.height = (int) (screenHeight * HEADER_PERECENTAGE);

        mTopMargin += (int) (screenHeight * HEADER_PERECENTAGE);

//        rootView.findViewById(R.id.prediction_rl_play_page).setPadding(0, rlp.height, 0, 0);

        rlp = (RelativeLayout.LayoutParams) rootView.findViewById(R.id.prediction_rl_footer).getLayoutParams();
        rlp.height = (int) (screenHeight * FOOTER_PERECENTAGE);
    }

    private void applyMainCardPercentages(ViewHolder viewHolder) {
        ViewGroup.LayoutParams lp = viewHolder.flLeftArea.getLayoutParams();
        lp.width = (int) mImageWidth;
        lp.height = (int) mImageHeight;

        lp = viewHolder.flRightArea.getLayoutParams();
        lp.height = (int) mImageHeight;

        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) viewHolder.llOptionLabels.getLayoutParams();
        rlp.height = (int) mOptionHeight;

        rlp = (RelativeLayout.LayoutParams) viewHolder.llQuestionDesc.getLayoutParams();
        rlp.height = (int) mImageHeight;

        rlp = (RelativeLayout.LayoutParams) viewHolder.tvLockingOption.getLayoutParams();
        rlp.height = (int) mOptionHeight;

        rlp = (RelativeLayout.LayoutParams) viewHolder.cvMainCard.getLayoutParams();
        rlp.width = (int) mCardWidth;
        rlp.topMargin = mTopMargin;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mLayoutInflater.inflate(R.layout.inflater_swipe_card_row, parent, false);

        final ViewHolder viewHolder = new ViewHolder(convertView);
        convertView.setTag(viewHolder);

        applyMainCardPercentages(viewHolder);

        Question question = getItem(position);

        viewHolder.tvQuestion.setText(question.getQuestionText());
        viewHolder.ivLeftOption.setImageUrl(question.getQuestionImage1());
        viewHolder.ivRightOption.setImageUrl(question.getQuestionImage2());
        viewHolder.tvLeftOption.setText(question.getQuestionOption1());
        viewHolder.tvRightOption.setText(question.getQuestionOption2());

        String questionContext = question.getQuestionContext();
        viewHolder.tvContext.setText(Html.fromHtml(questionContext));

        String submittedBy = question.getQuestionSubmittedBy();
        if (!TextUtils.isEmpty(submittedBy)) {
            viewHolder.questionBy.setText(submittedBy);
            viewHolder.questionByLayout.setVisibility(View.VISIBLE);
        } else {
            viewHolder.questionByLayout.setVisibility(View.GONE);
        }

        viewHolder.tvContext.setMovementMethod(LinkMovementMethod.getInstance());

        CharSequence text = viewHolder.tvContext.getText();
        if (text instanceof Spannable) {
            int end = text.length();
            Spannable sp = (Spannable) viewHolder.tvContext.getText();
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            style.clearSpans();//should clear old spans
            for (URLSpan url : urls) {
                LinkSpan click = new LinkSpan(url.getURL());
                style.setSpan(click, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            viewHolder.tvContext.setText(style);
        }

        updatePowerUpDetails(viewHolder, question);

        /*viewHolder.tvQuestion.post(new Runnable() {
            @Override
            public void run() {
                viewHolder.tvContext.setMaxLines(6 - viewHolder.tvQuestion.getLineCount());
            }
        });*/

        return convertView;
    }

    public void refreshPowerUps() {
        updatePowerUpDetails(mTopViewHolder, mTopQuestion);
    }

    private void updatePowerUpDetails(ViewHolder viewHolder, Question question) {
        Integer positivePoint = question.getUpdatedPositivePoints();
        if (null == positivePoint || positivePoint == 0) {
            viewHolder.positivePointsCardView.setVisibility(View.GONE);
            viewHolder.cardViewpoints.setVisibility(View.GONE);
        } else {
            viewHolder.tvquestionPositivePoints.setText("+" + positivePoint + " pts");
            viewHolder.tvquestionPositivePoints.setTag(positivePoint);
            viewHolder.positivePointsCardView.setVisibility(View.VISIBLE);
        }

        Integer negativePoint = question.getUpdatedNegativePoints();
        if (null == negativePoint || negativePoint == 0) {
            viewHolder.negativePointsCardView.setVisibility(View.GONE);
//            viewHolder.viewPoints.setVisibility(View.GONE);
        } else {
            viewHolder.tvquestionNegativePoints.setText("" + negativePoint + " pts");
//            viewHolder.viewPoints.setVisibility(View.VISIBLE);
            viewHolder.tvquestionNegativePoints.setTag(negativePoint);
            viewHolder.negativePointsCardView.setVisibility(View.VISIBLE);
        }

        String powerupId = question.getPowerUpId();
        viewHolder.llPowerUpHolder.removeAllViews();
        if (!TextUtils.isEmpty(powerupId)) {
            if (Powerups.AUDIENCE_POLL.equalsIgnoreCase(powerupId)) {
                viewHolder.btnanswer1Percentage.setVisibility(View.VISIBLE);
                viewHolder.btnanswer2Percentage.setVisibility(View.VISIBLE);

                viewHolder.btnanswer1Percentage.setText(question.getOption1AudPollPer() + "%");
                viewHolder.btnanswer2Percentage.setText(question.getOption2AudPollPer() + "%");
            } else {
                View powerUpAppliedView = getPowerUpAppliedView(powerupId, viewHolder.llPowerUpHolder);
                if (null != powerUpAppliedView) {
                    viewHolder.llPowerUpHolder.addView(powerUpAppliedView);
                    powerUpAppliedView.setOnClickListener(mRemovePowerUpListener);
                    showPowerUpAnimation(powerUpAppliedView);
                }
            }
        }


        if (null != question.getAudiencePoll()) {
            question.setPowerUpId(Powerups.AUDIENCE_POLL);
            int leftAnswerPercent = Integer.parseInt(question.getAudiencePoll().get(0).getAnswerPercentage().replaceAll("%", ""));
            int rightAnswerPercent = Integer.parseInt(question.getAudiencePoll().get(1).getAnswerPercentage().replaceAll("%", ""));

            viewHolder.btnanswer1Percentage.setVisibility(View.VISIBLE);
            viewHolder.btnanswer2Percentage.setVisibility(View.VISIBLE);

            viewHolder.btnanswer1Percentage.setText(leftAnswerPercent + "%");
            viewHolder.btnanswer2Percentage.setText(rightAnswerPercent + "%");

            if (leftAnswerPercent > rightAnswerPercent) {
                question.setMinorityAnswerId(Constants.AnswerIds.RIGHT);
            } else if (leftAnswerPercent < rightAnswerPercent) {
                question.setMinorityAnswerId(Constants.AnswerIds.LEFT);
            } else {
                question.setMinorityAnswerId(-1);
            }
        }
    }

    private View getPowerUpAppliedView(String powerupId, ViewGroup parent) {
        View powerUpView = mLayoutInflater.inflate(R.layout.inflater_powerup_applied, parent, false);
        ImageView icon = (ImageView) powerUpView.findViewById(R.id.powerup_applied_iv_icon);
        switch (powerupId) {
            case Powerups.XX:
            case Powerups.XX_GLOBAL:
                icon.setImageResource(R.drawable.powerup_2x_white);
                icon.setBackground(getPowerupDrawable(R.color.dodger_blue));
                break;
            case Powerups.NO_NEGATIVE:
                icon.setImageResource(R.drawable.powerup_nonegs_white);
                icon.setBackground(getPowerupDrawable(R.color.amaranth));
                break;
            default:
                return null;
        }
        return powerUpView;
    }

    private Drawable getPowerupDrawable(int colorRes) {
        GradientDrawable powerupDrawable = new GradientDrawable();
        powerupDrawable.setShape(GradientDrawable.OVAL);
        powerupDrawable.setColor(mLayoutInflater.getContext().getResources().getColor(colorRes));
        return powerupDrawable;
    }

    private void showPowerUpAnimation(final View view) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(500);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(scaleAnimation);
    }

    public void dismissPowerUpAnimation(final View view, Animation.AnimationListener listener) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(500);
        scaleAnimation.setAnimationListener(listener);
        view.startAnimation(scaleAnimation);
    }

    private void updateBg(int offset) {
        if (!mBgUpdateDone) {
            float elevation = vBgFrame1.getResources().getDimensionPixelSize(R.dimen.dp_6);
            if (offset != 1) {
                elevation = 0f;
            }
            int totalCount = getCount() - offset;
            if (totalCount > 0) {
                if (totalCount == 1) {
                    vBgFrame1.setVisibility(View.INVISIBLE);
                    vBgFrame2.setVisibility(View.INVISIBLE);
                } else if (totalCount == 2) {
                    vBgFrame1.setVisibility(View.VISIBLE);
                    vBgFrame2.setVisibility(View.INVISIBLE);
                } else {
                    vBgFrame1.setVisibility(View.VISIBLE);
                    vBgFrame2.setVisibility(View.VISIBLE);
                }
            }
            mTopViewHolder.cvMainCard.setCardElevation(elevation);
        }
    }

    public Question getTopQuestion() {
        return getItem(0);
    }

    public void setFlingCardListener(FlingCardListener listener) {
        this.mFlingCardListener = listener;
        this.mTopQuestion = getItem(0);
        this.mNeitherOptionAvailable = !TextUtils.isEmpty(mTopQuestion.getQuestionOption3());

        this.mTopViewHolder = new ViewHolder(mFlingCardListener.getFrame());

        mBgUpdateDone = false;
        updateBg(0);
    }

    private int getColor(int colorRes) {
        return ViewUtils.getColor(mTopViewHolder.cvMainCard.getContext(), colorRes);
    }

    private String mLockingOption;

    private float mLeftAlpha = 1f;

    private float mRightAlpha = 1f;

    public void onCardMoving(float xPercent, float yPercent) {
        updateBg(1);
        mBgUpdateDone = true;

        float alpha = Math.max(Math.abs(xPercent), Math.abs(yPercent)) / 100f;

        if (alpha > 1f) {
            alpha = 1f;
        }

        if (Math.abs(xPercent) > Math.abs(yPercent)) { // Locking horizontal options
            if (xPercent < 0f) { // Locking left option
                mLockingOption = mTopQuestion.getQuestionOption1();

                mLeftAlpha = 1f;
                mRightAlpha = 1f - alpha / 2f;
            } else { // Locking right option
                mLockingOption = mTopQuestion.getQuestionOption2();

                mLeftAlpha = 1f - alpha / 2f;
                mRightAlpha = 1f;
            }
        } else if (mNeitherOptionAvailable && yPercent < 0f) { // Locking top option
            mLockingOption = mTopQuestion.getQuestionOption3();

            mLeftAlpha = 1f - alpha / 2f;
            mRightAlpha = 1f - alpha / 2f;
        } else { // No locking
            mLockingOption = "";
            alpha = 0f;

            mBgUpdateDone = false;
            updateBg(0);

            mLeftAlpha = 1f;
            mRightAlpha = 1f;
        }

        alpha = 1f - alpha;

        mTopViewHolder.tvLockingOption.setText(mLockingOption);

        mTopViewHolder.tvLeftOption.setAlpha(alpha);
        mTopViewHolder.tvRightOption.setAlpha(alpha);
        mTopViewHolder.leftOptionArrow.setAlpha(alpha);
        mTopViewHolder.rightOptionArrow.setAlpha(alpha);
        /*mLeftImage.setAlpha(mLeftAlpha);
        mRightImage.setAlpha(mRightAlpha);*/

        alpha = 1f - alpha;
        /*mLeftOverlay.setAlpha(alpha / 2f);
        mRightOverlay.setAlpha(alpha / 2f);*/
        mTopViewHolder.tvLockingOption.setAlpha(alpha);

        Log.d("Image Alpha", mLeftAlpha + "  " + mRightAlpha);
    }

    class ViewHolder {

        CardView cvMainCard;

        TextView tvQuestion;

        TextView tvContext;

        FrameLayout flLeftArea;

        FrameLayout flRightArea;

        HmImageView ivLeftOption;

        HmImageView ivRightOption;

        TextView tvLeftOption;

        TextView tvRightOption;

        TextView btnanswer1Percentage;

        TextView btnanswer2Percentage;

        TextView tvquestionPositivePoints;

        TextView tvquestionNegativePoints;

        LinearLayout cardViewpoints;

        View viewPoints;

        TextView tvLockingOption;

        LinearLayout llOptionLabels;

        LinearLayout llQuestionDesc;

        LinearLayout llPowerUpHolder;

        private View leftOptionArrow;

        private View rightOptionArrow;

        TextView questionBy;

        LinearLayout questionByLayout;

        CardView positivePointsCardView;
        CardView negativePointsCardView;

        public ViewHolder(View rootView) {
            cvMainCard = (CardView) rootView.findViewById(R.id.swipe_card_cv_main);
            tvQuestion = (TextView) rootView.findViewById(R.id.swipe_card_tv_question);
            tvContext = (TextView) rootView.findViewById(R.id.swipe_card_tv_context);
            flLeftArea = (FrameLayout) rootView.findViewById(R.id.swipe_card_fl_left_area);
            flRightArea = (FrameLayout) rootView.findViewById(R.id.swipe_card_fl_right_area);
            ivLeftOption = (HmImageView) rootView.findViewById(R.id.swipe_card_iv_left);
            ivRightOption = (HmImageView) rootView.findViewById(R.id.swipe_card_iv_right);
            tvLeftOption = (TextView) rootView.findViewById(R.id.swipe_card_tv_left);
            tvRightOption = (TextView) rootView.findViewById(R.id.swipe_card_tv_right);
            btnanswer1Percentage = (TextView) rootView.findViewById(R.id.swipe_card_tv_left_poll);
            btnanswer2Percentage = (TextView) rootView.findViewById(R.id.swipe_card_answer2_percentage);
            tvquestionPositivePoints = (TextView) rootView.findViewById(R.id.swipe_card_question_positive_points_textview);
            tvquestionNegativePoints = (TextView) rootView.findViewById(R.id.swipe_card_question_negative_points_textview);
            positivePointsCardView = (CardView) rootView.findViewById(R.id.positive_points_cardview);
            negativePointsCardView = (CardView) rootView.findViewById(R.id.negative_points_cardview);
//            viewPoints = rootView.findViewById(R.id.swipe_card_question_points_line);
            cardViewpoints = (LinearLayout) rootView.findViewById(R.id.points_layout);
            tvLockingOption = (TextView) rootView.findViewById(R.id.swipe_card_tv_locking_option);
            llOptionLabels = (LinearLayout) rootView.findViewById(R.id.swipe_card_ll_option_labels);
            llQuestionDesc = (LinearLayout) rootView.findViewById(R.id.swipe_card_ll_question_desc);
            llPowerUpHolder = (LinearLayout) rootView.findViewById(R.id.swipe_card_ll_powerup_holder);
            leftOptionArrow = rootView.findViewById(R.id.swipe_card_iv_left_arrow);
            rightOptionArrow = rootView.findViewById(R.id.swipe_card_iv_right_arrow);
            questionBy = (TextView) rootView.findViewById(R.id.question_by_textview);
            questionByLayout = (LinearLayout) rootView.findViewById(R.id.question_by_layout);
        }
    }

    private class LinkSpan extends URLSpan {

        private LinkSpan(String url) {
            super(url);
        }

        @Override
        public void onClick(View view) {
            String url = getURL();
            if (url != null) {
                view.getContext().startActivity(new Intent(view.getContext(), FeedWebView.class).putExtra("url", url));
            }
        }
    }

}