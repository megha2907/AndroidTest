package in.sportscafe.nostragamus.module.play.prediction;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.HmImageView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AnswerIds;
import in.sportscafe.nostragamus.Constants.Powerups;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.tindercard.FlingCardListener;
import in.sportscafe.nostragamus.module.user.powerups.PowerUp;

public class PredictionAdapter extends ArrayAdapter<Question> {

    private static final float CARD_SIZE_PERECENTAGE = 0.89f;

    private static final float OPTION_SIZE_PERECENTAGE = 1f - CARD_SIZE_PERECENTAGE;

    private LayoutInflater mLayoutInflater;

    private boolean mBgUpdateDone = false;

    private int mTopMargin;

    View vBgFrame1;

    View vBgFrame2;

    private FlingCardListener mFlingCardListener;

    private Question mTopQuestion;

    private boolean mNeitherOptionAvailable = false;

    private View mTopView;

    private CardView mCvMain;

    private View mLeftOption;

    private View mRightOption;

    private View mLeftOptionArrow;

    private View mRightOptionArrow;

    private View mLeftOverlay;

    private View mRightOverlay;

    private View mLeftImage;

    private View mRightImage;

    private TextView mTvLockingOption;

    private float mOptionHeight;

    private float mCardWidth;

    private float mCardHeight;

    private float mCardMargin;

    private float mImageWidth;

    private float mImageHeight;

    public PredictionAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public void setRootView(View rootView) {
        Rect rect = new Rect();
        rootView.getLocalVisibleRect(rect);

        final float SCREEN_WIDTH = rect.width() * 0.89f;
        final float SCREEN_HEIGHT = rect.height() * 0.89f;

        mCardWidth = SCREEN_WIDTH * CARD_SIZE_PERECENTAGE;
        mCardHeight = SCREEN_WIDTH;
        mOptionHeight = SCREEN_WIDTH * OPTION_SIZE_PERECENTAGE;
        mCardMargin = (rect.width() - mCardWidth) / 2;
        mImageWidth = mCardWidth / 2f;
        mImageHeight = mImageWidth;

        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) rootView.findViewById(R.id.prediction_cv_bg_0).getLayoutParams();
        rlp.height = (int) mCardHeight;
        rlp.leftMargin = (int) mCardMargin;
        rlp.rightMargin = (int) mCardMargin;
        mTopMargin = rlp.topMargin;

        vBgFrame1 = rootView.findViewById(R.id.prediction_cv_bg_1);
        vBgFrame1.getLayoutParams().height = (int) mCardHeight;

        vBgFrame2 = rootView.findViewById(R.id.prediction_cv_bg_2);
        vBgFrame2.getLayoutParams().height = (int) mCardHeight;

//        rootView.findViewById(R.id.activity_prediction_swipe).getLayoutParams().height = (int) mCardHeight;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = mLayoutInflater.inflate(R.layout.inflater_swipe_card_row, parent, false);

        final ViewHolder viewHolder = new ViewHolder(convertView);
        convertView.setTag(viewHolder);


        /*sdf*/

        ViewGroup.LayoutParams lp = viewHolder.flLeftArea.getLayoutParams();
        lp.width = (int) mImageWidth;

        /*lp = viewHolder.flRightArea.getLayoutParams();
        lp.width = (int) mImageWidth;*/

        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) viewHolder.llOptionLabels.getLayoutParams();
        rlp.height = (int) mOptionHeight;

        rlp = (RelativeLayout.LayoutParams) viewHolder.tvLockingOption.getLayoutParams();
        rlp.height = (int) mOptionHeight;

        rlp = (RelativeLayout.LayoutParams) viewHolder.llQuestionDesc.getLayoutParams();
        rlp.height = (int) (mCardWidth / 2);

        rlp = (RelativeLayout.LayoutParams) viewHolder.cvMainCard.getLayoutParams();
//        rlp.width = (int) mCardWidth;
        rlp.height = (int) mCardHeight;
        rlp.leftMargin = (int) mCardMargin;
        rlp.rightMargin = (int) mCardMargin;
        rlp.topMargin = (int) mTopMargin;
        /*sadf*/


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

        String powerupId = question.getPowerUpId();

        int powerupIcons = PowerUp.getPlayPowerupIcons(powerupId);
        if(powerupIcons != -1) {
            viewHolder.btnpowerupicon.setVisibility(View.VISIBLE);
            viewHolder.btnpowerupicon.setImageResource(powerupIcons);

            if (Powerups.AUDIENCE_POLL.equalsIgnoreCase(powerupId)) {
                viewHolder.btnanswer1Percentage.setVisibility(View.VISIBLE);
                viewHolder.btnanswer2Percentage.setVisibility(View.VISIBLE);

                viewHolder.btnanswer1Percentage.setText(question.getOption1AudPollPer() + "%");
                viewHolder.btnanswer2Percentage.setText(question.getOption2AudPollPer() + "%");
            }
        } else {
            viewHolder.btnpowerupicon.setVisibility(View.GONE);
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
            mCvMain.setCardElevation(elevation);
        }
    }

    public void update2xGlobalPowerUp() {
        mTopQuestion.setPowerUpId(Powerups.XX_GLOBAL);
        mTopQuestion.setQuestionPositivePoints(2 * mTopQuestion.getQuestionPositivePoints());
        mTopQuestion.setQuestionNegativePoints(2 * mTopQuestion.getQuestionNegativePoints());
    }

    public void update2xPowerUp() {
        mTopQuestion.setPowerUpId(Powerups.XX);
        mTopQuestion.setQuestionPositivePoints(2 * mTopQuestion.getQuestionPositivePoints());
        mTopQuestion.setQuestionNegativePoints(2 * mTopQuestion.getQuestionNegativePoints());
    }

    public void updateNonegsPowerUp() {
        mTopQuestion.setPowerUpId(Powerups.NO_NEGATIVE);
        mTopQuestion.setQuestionNegativePoints(0);
    }

    public void updateAudiencePollPowerUp(int leftAnswerPercent, int rightAnswerPercent) {
        mTopQuestion.setPowerUpId(Powerups.AUDIENCE_POLL);
        mTopQuestion.setOption1AudPollPer(leftAnswerPercent);
        mTopQuestion.setOption2AudPollPer(rightAnswerPercent);

        if (leftAnswerPercent > rightAnswerPercent) {
            mTopQuestion.setMinorityAnswerId(AnswerIds.RIGHT);
        } else if (leftAnswerPercent < rightAnswerPercent) {
            mTopQuestion.setMinorityAnswerId(AnswerIds.LEFT);
        } else {
            mTopQuestion.setMinorityAnswerId(-1);
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
        this.mTopView = mFlingCardListener.getFrame();
        this.mCvMain = (CardView) mTopView.findViewById(R.id.swipe_card_cv_main);
        this.mLeftOption = mTopView.findViewById(R.id.swipe_card_tv_left);
        this.mRightOption = mTopView.findViewById(R.id.swipe_card_tv_right);
        this.mLeftOptionArrow = mTopView.findViewById(R.id.swipe_card_iv_left_arrow);
        this.mRightOptionArrow = mTopView.findViewById(R.id.swipe_card_iv_right_arrow);
        this.mLeftOverlay = mTopView.findViewById(R.id.swipe_card_v_left_overlay);
        this.mRightOverlay = mTopView.findViewById(R.id.swipe_card_v_right_overlay);
        this.mLeftImage = mTopView.findViewById(R.id.swipe_card_iv_left);
        this.mRightImage = mTopView.findViewById(R.id.swipe_card_iv_right);
        this.mTvLockingOption = (TextView) mTopView.findViewById(R.id.swipe_card_tv_locking_option);

        mBgUpdateDone = false;
        updateBg(0);
    }

    private void setOptionVisibility(int leftVis, int rightVis) {
        if (leftVis != mLeftOption.getVisibility()) {
            mLeftOption.setVisibility(leftVis);
            mLeftOptionArrow.setVisibility(leftVis);
        }

        if (rightVis != mRightOption.getVisibility()) {
            mRightOption.setVisibility(rightVis);
            mRightOptionArrow.setVisibility(rightVis);
        }
    }

    private int getColor(int colorRes) {
        return ContextCompat.getColor(mTopView.getContext(), colorRes);
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
                mLeftOverlay.setBackgroundColor(getColor(R.color.malachite));
                mRightOverlay.setBackgroundColor(getColor(R.color.radical_red));

                mLeftAlpha = 1f;
                mRightAlpha = 1f - alpha / 2f;
            } else { // Locking right option
                mLockingOption = mTopQuestion.getQuestionOption2();
                mLeftOverlay.setBackgroundColor(getColor(R.color.radical_red));
                mRightOverlay.setBackgroundColor(getColor(R.color.malachite));

                mLeftAlpha = 1f - alpha / 2f;
                mRightAlpha = 1f;
            }
        } else if (mNeitherOptionAvailable && yPercent < 0f) { // Locking top option
            mLockingOption = mTopQuestion.getQuestionOption3();
            mLeftOverlay.setBackgroundColor(getColor(R.color.radical_red));
            mRightOverlay.setBackgroundColor(getColor(R.color.radical_red));

            mLeftAlpha = 1f - alpha / 2f;
            mRightAlpha = 1f - alpha / 2f;
        } else { // No locking
            mLockingOption = "";
            mLeftOverlay.setBackgroundColor(getColor(R.color.transparent));
            mRightOverlay.setBackgroundColor(getColor(R.color.transparent));
            alpha = 0f;

            mBgUpdateDone = false;
            updateBg(0);

            mLeftAlpha = 1f;
            mRightAlpha = 1f;
        }

        alpha = 1f - alpha;

        mTvLockingOption.setText(mLockingOption);

        mLeftOption.setAlpha(alpha);
        mRightOption.setAlpha(alpha);
        mLeftOptionArrow.setAlpha(alpha);
        mRightOptionArrow.setAlpha(alpha);
        /*mLeftImage.setAlpha(mLeftAlpha);
        mRightImage.setAlpha(mRightAlpha);*/

        alpha = 1f - alpha;
        /*mLeftOverlay.setAlpha(alpha / 2f);
        mRightOverlay.setAlpha(alpha / 2f);*/
        mTvLockingOption.setAlpha(alpha);

        Log.d("Image Alpha", mLeftAlpha + "  " + mRightAlpha);
    }

    static class ViewHolder {

        CardView cvMainCard;

        TextView tvQuestion;

        TextView tvContext;

        FrameLayout flLeftArea;

        FrameLayout flRightArea;

        HmImageView ivLeftOption;

        HmImageView ivRightOption;

        TextView tvLeftOption;

        TextView tvRightOption;

        ImageButton btnpowerupicon;

        TextView btnanswer1Percentage;

        TextView btnanswer2Percentage;

        TextView tvquestionPositivePoints;

        TextView tvquestionNegativePoints;

        CardView cardViewpoints;

        View viewPoints;

        TextView tvLockingOption;

        LinearLayout llOptionLabels;

        LinearLayout llQuestionDesc;

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
            btnpowerupicon = (ImageButton) rootView.findViewById(R.id.swipe_card_powerup_icon);
            btnanswer1Percentage = (TextView) rootView.findViewById(R.id.swipe_card_tv_left_poll);
            btnanswer2Percentage = (TextView) rootView.findViewById(R.id.swipe_card_answer2_percentage);
            tvquestionPositivePoints = (TextView) rootView.findViewById(R.id.swipe_card_question_positive_points);
            tvquestionNegativePoints = (TextView) rootView.findViewById(R.id.swipe_card_question_negative_points);
            viewPoints = rootView.findViewById(R.id.swipe_card_question_points_line);
            cardViewpoints = (CardView) rootView.findViewById(R.id.points_cardview);
            tvLockingOption = (TextView) rootView.findViewById(R.id.swipe_card_tv_locking_option);
            llOptionLabels = (LinearLayout) rootView.findViewById(R.id.swipe_card_ll_option_labels);
            llQuestionDesc = (LinearLayout) rootView.findViewById(R.id.swipe_card_ll_question_desc);
        }
    }
}
