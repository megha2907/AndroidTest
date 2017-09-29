package in.sportscafe.nostragamus.module.navigation.help.dummygame;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.TextUtils;
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

import java.util.ArrayList;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.Powerups;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.help.dummygame.tindercard.FlingCardListener;
import in.sportscafe.nostragamus.module.resultspeek.dto.Question;
import in.sportscafe.nostragamus.utils.ViewUtils;

public class DGPlayAdapter extends ArrayAdapter<Question> {

    interface QuestionType {
        String LEFT_RIGHT = "leftright";
        String NEITHER = "neither";
        String POINTS = "points";
    }

    /* Layout dynamic height is same as in Play (PredictionActivity) */
    private static final float HEADER_PERECENTAGE = 8.75f / 100;

    private static final float GAP_BW_HEADER_CARD_PERECENTAGE = 8.5f / 100;

    private static final float CARD_HEIGHT_PERECENTAGE = 53f / 100;

    private static final float OPTION_PERECENTAGE = 6.25f / 100;

    private static final float FOOTER_POWERUP_LAYOUT_PERCENTAGE = 10f / 100;

    private static final float FOOTER_NEITHER_BUTTON_PERCENTAGE = 12f / 100;
    private static final float EXTRA = 1.5f / 100;

    private LayoutInflater mLayoutInflater;

    private boolean mBgUpdateDone = false;

    private int mTopMargin;

    private View vBgFrame0;

    private View vBgFrame1;

    private View vBgFrame2;

    private FlingCardListener mFlingCardListener;

    private Question mTopQuestion;

    private boolean mNeitherOptionAvailable = false;

    private ViewHolder mTopViewHolder;

    private float mOptionHeight;

    private float mCardWidth;

    private float mCardHeight;

    private float mCardMargin;

    private float mImageWidth;

    private float mImageHeight;

    private View.OnClickListener mRemovePowerUpListener;

    private String mQuestionType;

    public DGPlayAdapter(Context context, View.OnClickListener removePowerUpListener, String questionType) {
        super(context, android.R.layout.simple_list_item_1);
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mRemovePowerUpListener = removePowerUpListener;
        this.mQuestionType = questionType;
    }

    public void setRootView(View rootView) {
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

        vBgFrame0 = rootView.findViewById(R.id.prediction_cv_bg_0);
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) vBgFrame0.getLayoutParams();
        rlp.width = (int) mCardWidth;
        rlp.height = (int) (mCardHeight + mOptionHeight);
        mTopMargin = (int) (screenHeight * GAP_BW_HEADER_CARD_PERECENTAGE);
        rlp.topMargin = mTopMargin;

        /*int headerHeight = rootView.findViewById(R.id.prediction_rl_header).getMeasuredHeight();
        ((RelativeLayout.LayoutParams) rootView.findViewById(R.id.prediction_iv_dummy_left_right_indicator).getLayoutParams())
                .topMargin = (int) (mTopMargin + rlp.height - screenHeight * 1.5f / 100 - mOptionHeight / 2f + headerHeight);

        ((RelativeLayout.LayoutParams) rootView.findViewById(R.id.prediction_iv_dummy_neither_indicator).getLayoutParams())
                .topMargin = (int) (mTopMargin + rlp.height - mOptionHeight + headerHeight);*/

        vBgFrame1 = rootView.findViewById(R.id.prediction_cv_bg_1);
        vBgFrame1.getLayoutParams().height = (int) (mCardHeight + mOptionHeight);

        vBgFrame2 = rootView.findViewById(R.id.prediction_cv_bg_2);
        vBgFrame2.getLayoutParams().height = (int) (mCardHeight + mOptionHeight);

        /*rlp = (RelativeLayout.LayoutParams) rootView.findViewById(R.id.prediction_rl_header).getLayoutParams();
        rlp.height = (int) (screenHeight * HEADER_PERECENTAGE);*/

        mTopMargin += (int) (screenHeight * HEADER_PERECENTAGE);

//        rootView.findViewById(R.id.prediction_rl_play_page).setPadding(0, rlp.height, 0, 0);

        rlp = (RelativeLayout.LayoutParams) rootView.findViewById(R.id.prediction_rl_footer).getLayoutParams();
        rlp.height = (int) (screenHeight * (FOOTER_NEITHER_BUTTON_PERCENTAGE + FOOTER_POWERUP_LAYOUT_PERCENTAGE));

        rlp = (RelativeLayout.LayoutParams) rootView.findViewById(R.id.prediction_ll_powerup_layout).getLayoutParams();
        rlp.height = (int) (screenHeight * FOOTER_POWERUP_LAYOUT_PERCENTAGE);

        rlp = (RelativeLayout.LayoutParams) rootView.findViewById(R.id.neither_btn_layout).getLayoutParams();
        rlp.height = (int) (screenHeight * FOOTER_NEITHER_BUTTON_PERCENTAGE);
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

        ViewHolder viewHolder = new ViewHolder(convertView);
        convertView.setTag(viewHolder);

        applyMainCardPercentages(viewHolder);

        Question question = getItem(position);

        viewHolder.mainView.setVisibility(View.INVISIBLE);

        // It is to highlight the points layer
//        viewHolder.llPointsLayout.setBackgroundResource(R.drawable.dg_points_bg);

        viewHolder.tvQuestion.setText(question.getQuestionText());
        viewHolder.tvContext.setText(Html.fromHtml(question.getQuestionContext()));
        viewHolder.ivLeftOption.setImageUrl(question.getQuestionImage1());
        viewHolder.ivRightOption.setImageUrl(question.getQuestionImage2());
        viewHolder.tvLeftOption.setText(question.getQuestionOption1());
        viewHolder.tvRightOption.setText(question.getQuestionOption2());

        showAudiencePollIfAlreadyAppliedForThisQuestion(viewHolder, question);
        updatePowerUpOmUi(viewHolder, question);

        return convertView;
    }

    private void showAudiencePollIfAlreadyAppliedForThisQuestion(DGPlayAdapter.ViewHolder viewHolder, Question question) {
        ArrayList<String> powerupArray = question.getPowerUpArrayList();
        viewHolder.llPowerUpHolder.removeAllViews();
        if (powerupArray != null) {
            for (String str : powerupArray) {
                if (Powerups.AUDIENCE_POLL.equalsIgnoreCase(str)) {
                    addAudiencePoll();
                }
            }
        }
    }

    public void refreshPowerUps(String powerupTag) {
//        updatePowerUpOmUi(mTopViewHolder, mTopQuestion);
    }

    public void add2xPowerup() {
        final View powerUpAppliedView = getPowerUpAppliedView(Powerups.XX, mTopViewHolder.llPowerUpHolder);
        if (null != powerUpAppliedView) {
            powerUpAppliedView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View powerupClickedView) {
                    // Animate powerup view removal
                    dismissPowerUpAnimation(powerupClickedView, new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mRemovePowerUpListener.onClick(powerUpAppliedView);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                }
            });

            mTopViewHolder.llPowerUpHolder.addView(powerUpAppliedView);
            showPowerUpAnimation(powerUpAppliedView);
            updatePowerUpOmUi(mTopViewHolder, mTopQuestion);
        }
    }

    public void remove2xPowerup(View powerUpAppliedView) {
        mTopViewHolder.llPowerUpHolder.removeView(powerUpAppliedView);
        animateOtherPowerupWhenAnyOneRemoved();
        mTopQuestion.removeAppliedPowerUp(Powerups.XX);
        updatePowerUpOmUi(mTopViewHolder, mTopQuestion);
    }

    public void removeNoNegativePowerup(View powerUpAppliedView) {
        mTopViewHolder.llPowerUpHolder.removeView(powerUpAppliedView);
        animateOtherPowerupWhenAnyOneRemoved();
        mTopQuestion.removeAppliedPowerUp(Powerups.NO_NEGATIVE);
        updatePowerUpOmUi(mTopViewHolder, mTopQuestion);
    }

    public void addNoNegativePowerup() {
        final View powerUpAppliedView = getPowerUpAppliedView(Powerups.NO_NEGATIVE, mTopViewHolder.llPowerUpHolder);
        if (null != powerUpAppliedView) {
            powerUpAppliedView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View powerupClickedView) {
                    // Animate powerup view removal
                    dismissPowerUpAnimation(powerupClickedView, new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mRemovePowerUpListener.onClick(powerUpAppliedView);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                }
            });

            mTopViewHolder.llPowerUpHolder.addView(powerUpAppliedView);
            showPowerUpAnimation(powerUpAppliedView);
            updatePowerUpOmUi(mTopViewHolder, mTopQuestion);
        }
    }

    private void animatePowerupViewsIfAddedMoreThanOne() {
        if (mTopViewHolder.llPowerUpHolder.getChildCount() > 1) {
            View childView1 = mTopViewHolder.llPowerUpHolder.getChildAt(0);
            View childView2 = mTopViewHolder.llPowerUpHolder.getChildAt(1);

            int moveUpt0Dp = (int)getContext().getResources().getDimension(R.dimen.dp_30);
            childView1.animate().setDuration(750).translationX(-moveUpt0Dp).start();
            childView2.animate().setDuration(750).translationX(moveUpt0Dp).start();
        }
    }

    private void animateOtherPowerupWhenAnyOneRemoved() {
        if (mTopViewHolder.llPowerUpHolder.getChildCount() == 1) {
            View childView1 = mTopViewHolder.llPowerUpHolder.getChildAt(0);
            int center = mTopViewHolder.llPowerUpHolder.getLayoutParams().width / 2;
            childView1.animate().setDuration(750).translationX(center).start();
        }
    }

    public void addAudiencePoll() {
        if (mTopQuestion != null) {
            mTopViewHolder.btnanswer1Percentage.setVisibility(View.VISIBLE);
            mTopViewHolder.btnanswer2Percentage.setVisibility(View.VISIBLE);

            mTopViewHolder.btnanswer1Percentage.setText(mTopQuestion.getOption1AudPollPer() + "%");
            mTopViewHolder.btnanswer2Percentage.setText(mTopQuestion.getOption2AudPollPer() + "%");

            /* Update powerup points on ui */
            updatePowerUpOmUi(mTopViewHolder, mTopQuestion);
        }
    }

    private void updatePowerUpOmUi(ViewHolder viewHolder, Question question) {
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
        } else {
            viewHolder.tvquestionNegativePoints.setText("" + negativePoint + " pts");
            viewHolder.tvquestionNegativePoints.setTag(negativePoint);
            viewHolder.negativePointsCardView.setVisibility(View.VISIBLE);
        }

        if (null != question.getAudiencePoll()) {
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

    private View getPowerUpAppliedView(String powerup, ViewGroup parent) {
        View powerUpView = mLayoutInflater.inflate(R.layout.inflater_powerup_applied, parent, false);
        ImageView icon = (ImageView) powerUpView.findViewById(R.id.powerup_applied_iv_icon);
        powerUpView.setTag(powerup);

        switch (powerup) {
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
                animatePowerupViewsIfAddedMoreThanOne();
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
        this.mTopQuestion = getItem(0);
        this.mNeitherOptionAvailable = !TextUtils.isEmpty(mTopQuestion.getQuestionOption3());

        this.mTopViewHolder = new ViewHolder(mFlingCardListener.getFrame());

        mBgUpdateDone = false;
        updateBg(0);

        animateCard();
        checkQuestionType();
    }

    private void setOptionVisibility(int leftVis, int rightVis) {
        if (leftVis != mTopViewHolder.tvLeftOption.getVisibility()) {
            mTopViewHolder.tvLeftOption.setVisibility(leftVis);
            mTopViewHolder.leftOptionArrow.setVisibility(leftVis);
        }

        if (rightVis != mTopViewHolder.tvRightOption.getVisibility()) {
            mTopViewHolder.tvRightOption.setVisibility(rightVis);
            mTopViewHolder.rightOptionArrow.setVisibility(rightVis);
        }
    }

    private int getColor(int colorRes) {
        return ViewUtils.getColor(mTopViewHolder.mainView.getContext(), colorRes);
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

        View mainView;

        CardView cvMainCard;

        LinearLayout llPointsLayout;

        TextView tvQuestion;

        TextView tvContext;

        FrameLayout flLeftArea;

        FrameLayout flRightArea;

        HmImageView ivLeftOption;

        HmImageView ivRightOption;

        TextView btnanswer1Percentage;

        TextView btnanswer2Percentage;

        TextView tvquestionPositivePoints;

        TextView tvquestionNegativePoints;

        LinearLayout cardViewpoints;

        View viewPoints;

        TextView tvLockingOption;

        LinearLayout llOptionLabels;

        LinearLayout llQuestionDesc;

        RelativeLayout llPowerUpHolder;

        ImageView ivTouchPointer;

        private TextView tvLeftOption;

        private TextView tvRightOption;

        private View leftOptionArrow;

        private View rightOptionArrow;

        CardView positivePointsCardView;
        CardView negativePointsCardView;

        public ViewHolder(View rootView) {
            mainView = rootView;
            cvMainCard = (CardView) rootView.findViewById(R.id.swipe_card_cv_main);
//            llPointsLayout = (LinearLayout) rootView.findViewById(R.id.swipe_card_question_points_rl);
            tvQuestion = (TextView) rootView.findViewById(R.id.swipe_card_tv_question);
            tvContext = (TextView) rootView.findViewById(R.id.swipe_card_tv_context);
            flLeftArea = (FrameLayout) rootView.findViewById(R.id.swipe_card_fl_left_area);
            flRightArea = (FrameLayout) rootView.findViewById(R.id.swipe_card_fl_right_area);
            ivLeftOption = (HmImageView) rootView.findViewById(R.id.swipe_card_iv_left);
            ivRightOption = (HmImageView) rootView.findViewById(R.id.swipe_card_iv_right);
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
            llPowerUpHolder = (RelativeLayout) rootView.findViewById(R.id.swipe_card_ll_powerup_holder);
            ivTouchPointer = (ImageView) rootView.findViewById(R.id.swipe_card_iv_pointer);
            tvLeftOption = (TextView) rootView.findViewById(R.id.swipe_card_tv_left);
            tvRightOption = (TextView) rootView.findViewById(R.id.swipe_card_tv_right);
            leftOptionArrow = rootView.findViewById(R.id.swipe_card_iv_left_arrow);
            rightOptionArrow = rootView.findViewById(R.id.swipe_card_iv_right_arrow);
        }
    }

    private void animateCard() {
        mTopViewHolder.mainView.setAlpha(0);
        mTopViewHolder.mainView.setScaleX(0.9f);
        mTopViewHolder.mainView.setScaleY(0.9f);

        mTopViewHolder.mainView.setVisibility(View.VISIBLE);

        mTopViewHolder.mainView.animate().alpha(1).scaleX(1).scaleY(1).setDuration(1000);

        if (null != mQuestionType && (QuestionType.LEFT_RIGHT.equalsIgnoreCase(mQuestionType)
                || QuestionType.NEITHER.equalsIgnoreCase(mQuestionType))) {

            mTopViewHolder.cvMainCard.setEnabled(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTopViewHolder.ivTouchPointer.animate().alpha(1).setDuration(500);
                }
            }, 1000);
        }
    }

    private void checkQuestionType() {
        if (null != mQuestionType) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    switch (mQuestionType) {
                        case QuestionType.LEFT_RIGHT:
                            getCardAutoMover().startLeftRightAnimation();
                            break;
                        case QuestionType.NEITHER:
                            getCardAutoMover().startNeitherAnimation();
                            break;
                        case QuestionType.POINTS:
//                            mTopViewHolder.tvPointsLabel.animate().alpha(1).setDuration(1000);
                            break;
                    }
                }
            }, 1500);
        }
    }

    private DGCardAutoMover getCardAutoMover() {
        return new DGCardAutoMover(mTopViewHolder.cvMainCard, mFlingCardListener,
                new DGCardAutoMover.OnDGAutoMoverListener() {
                    @Override
                    public void onAnimationStarted() {}

                    @Override
                    public void onAnimationEnd() {
                        mTopViewHolder.cvMainCard.setEnabled(true);
                        mTopViewHolder.ivTouchPointer.animate().alpha(0).setDuration(500);
                    }
                });
    }
}