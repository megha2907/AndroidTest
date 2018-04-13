package in.sportscafe.nostragamus.module.prediction.playScreen;


import android.animation.Animator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.ads.conversiontracking.AdWordsConversionReporter;
import com.jeeva.android.widgets.CustomProgressbar;
import com.sportscafe.nostracardstack.cardstack.CardDirection;
import com.sportscafe.nostracardstack.cardstack.CardStack;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.cache.CacheManagementHelper;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.customViews.CustomSnackBar;
import in.sportscafe.nostragamus.module.inPlay.ui.ResultsScreenDataDto;
import in.sportscafe.nostragamus.module.nostraHome.ui.NostraHomeActivity;
import in.sportscafe.nostragamus.module.prediction.gamePlayHelp.GamePlayHelpActivity;
import in.sportscafe.nostragamus.module.prediction.playScreen.adapter.PredictionQuestionAdapterListener;
import in.sportscafe.nostragamus.module.prediction.playScreen.adapter.PredictionQuestionsCardAdapter;
import in.sportscafe.nostragamus.module.prediction.playScreen.dataProvider.PredictionPlayersPollDataProvider;
import in.sportscafe.nostragamus.module.prediction.playScreen.dataProvider.PredictionQuestionsDataProvider;
import in.sportscafe.nostragamus.module.prediction.playScreen.dataProvider.SavePredictionAnswerProvider;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.AnswerResponse;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PlayScreenDataDto;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PlayerPollResponse;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PlayersPoll;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PowerUp;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PowerUpEnum;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PredictionAllQuestionResponse;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PredictionQuestion;
import in.sportscafe.nostragamus.module.prediction.playScreen.helper.PlayerPollHelper;
import in.sportscafe.nostragamus.module.prediction.playScreen.helper.PredictionUiHelper;
import in.sportscafe.nostragamus.module.prediction.powerupBank.PowerupBankTransferToPlayActivity;
import in.sportscafe.nostragamus.module.prediction.powerupBank.dto.PowerupBankTransferScreenData;
import in.sportscafe.nostragamus.module.resultspeek.FeedWebView;
import in.sportscafe.nostragamus.utils.AlertsHelper;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PredictionFragment extends NostraBaseFragment implements View.OnClickListener {

    private static final String TAG = PredictionFragment.class.getSimpleName();
    private static final int CARD_SWIPE_DISTANCE_TO_END_SWIPE = 300;
    private final static int POWER_UP_BANK_ACTIVITY_REQUEST_CODE = 99;
    private final static int GAME_PLAY_HELP_ACTIVITY = 98;

    private PredictionFragmentListener mFragmentListener;
    private CardStack mCardStack;
    private PredictionQuestionsCardAdapter mQuestionsCardAdapter;
    private SavePredictionAnswerProvider mSavePredictionAnswerProvider;
    private LinearLayout mThirdOptionLayout;
    private PredictionUiHelper mUiHelper;
    private PowerUp mCurrentPowerUp;
    private PlayScreenDataDto mPlayScreenData;
    private CustomProgressbar mProgressDialog;
    private TextView mCounterTextView;
    private LinearLayout mNextTextView;
    private ImageView mPowerUpImageView;
    private ImageView mGamePlayImageView;
    private Button mThirdOptionButton;
    private int mTotalQuestions = 0;
    private int mCurrentQuestionPos = 1;

    public PredictionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_prediction, container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PredictionFragmentListener) {
            mFragmentListener = (PredictionFragmentListener) context;
        } else {
            throw new RuntimeException(PredictionActivity.class.getSimpleName() + " must implement "
                    + PredictionFragmentListener.class.getSimpleName());
        }
    }

    private void initView(View rootView) {
        ImageView backButton = (ImageView) rootView.findViewById(R.id.prediction_back_btn);
        mPowerUpImageView = (ImageView) rootView.findViewById(R.id.prediction_powerup_bank_imgView);
        mGamePlayImageView = (ImageView) rootView.findViewById(R.id.prediction_help_play_imgView);
        RelativeLayout doublerPowerup = (RelativeLayout) rootView.findViewById(R.id.prediction_doubler_Layout);
        RelativeLayout noNegPowerup = (RelativeLayout) rootView.findViewById(R.id.prediction_noNeg_Layout);
        RelativeLayout playerPollPowerup = (RelativeLayout) rootView.findViewById(R.id.prediction_player_poll_Layout);
        Button thirdOptionButton = (Button) rootView.findViewById(R.id.prediction_third_option_button);
        mThirdOptionLayout = (LinearLayout) rootView.findViewById(R.id.prediction_third_option_layout);
        mCounterTextView = (TextView) rootView.findViewById(R.id.prediction_question_counter_textView);
        mNextTextView = (LinearLayout) rootView.findViewById(R.id.prediction_next_layout);
        mThirdOptionButton = (Button) rootView.findViewById(R.id.prediction_third_option_button);

        mNextTextView.setOnClickListener(this);
        mPowerUpImageView.setOnClickListener(this);
        mGamePlayImageView.setOnClickListener(this);
        doublerPowerup.setOnClickListener(this);
        noNegPowerup.setOnClickListener(this);
        playerPollPowerup.setOnClickListener(this);
        backButton.setOnClickListener(this);
        thirdOptionButton.setOnClickListener(this);

        /* Card stack */
        mCardStack = (CardStack) rootView.findViewById(R.id.prediction_cardStack);
        mCardStack.setContentResource(R.layout.prediction_card_layout);
        mCardStack.setCanSwipe(true);
        mCardStack.setListener(getCardSwipeEventsListener());
    }

    @NonNull
    private PredictionQuestionAdapterListener getQuestionsCardAdapterListener() {
        return new PredictionQuestionAdapterListener() {
            @Override
            public void onLeftOptionClicked(int pos) {
                if (mCardStack != null) {
                    mCardStack.discardTopOnButtonClick(CardDirection.LEFT);
                }
            }

            @Override
            public void onRightOptionClicked(int pos) {
                if (mCardStack != null) {
                    mCardStack.discardTopOnButtonClick(CardDirection.RIGHT);
                }
            }

            @Override
            public void onWebLinkClicked(String url) {
                openWebView(url);
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initMembers();
        initHeading();
        initPowerUpBankEnabled();
        animateTopBottomLayouts();
    }

    private void initPowerUpBankEnabled() {
        if (mPlayScreenData != null && mPlayScreenData.getMaxPowerUpTransferLimit() == 0) {
            mPowerUpImageView.setImageResource(R.drawable.powerup_bank_disabled_playscreen_img);
        }
    }

    private void initMembers() {
        mProgressDialog = new CustomProgressbar(this.getContext());

        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey(Constants.BundleKeys.PLAY_SCREEN_DATA)) {
                mPlayScreenData = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.PLAY_SCREEN_DATA));
            }

            if (mPlayScreenData != null) {
                mUiHelper = new PredictionUiHelper();
                mSavePredictionAnswerProvider = new SavePredictionAnswerProvider();
            } else {
                handleErrorAndFinishActivity();
            }
        } else {
            handleErrorAndFinishActivity();
        }
    }

    private void handleErrorAndFinishActivity() {
        Log.e(TAG, "No Contest details passed");
        AlertsHelper.showAlert(getContext(), "Error", "Can not continue, please try again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mFragmentListener != null) {
                    mFragmentListener.onBackClicked();
                }
            }
        });
    }

    private void loadQuestions() {
        if (mPlayScreenData != null && getActivity() != null) {
            showProgress();
            PredictionQuestionsDataProvider dataProvider = new PredictionQuestionsDataProvider();
            dataProvider.getAllQuestions(mPlayScreenData.getMatchId(), mPlayScreenData.getRoomId(), getAllQuestionsApiListener());
        } else {
            handleErrorAndFinishActivity();
        }
    }

    @NonNull
    private PredictionQuestionsDataProvider.QuestionDataProviderListener getAllQuestionsApiListener() {
        return new PredictionQuestionsDataProvider.QuestionDataProviderListener() {
            @Override
            public void onData(int status, @Nullable PredictionAllQuestionResponse questionsResponse) {
                hideProgress();
                onAllQuestionApiSuccessResponse(questionsResponse);
            }

            @Override
            public void onError(int status) {
                hideProgress();
                handleError(null, status);
            }
        };
    }

    private void onAllQuestionApiSuccessResponse(PredictionAllQuestionResponse questionsResponse) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing() &&
                mCardStack != null && questionsResponse != null) {

            if (questionsResponse.getQuestions() != null && !questionsResponse.getQuestions().isEmpty()) {
                /* PowerUps */
                if (questionsResponse.getPowerUp() != null) {
                    mCurrentPowerUp = questionsResponse.getPowerUp();
                    updatePowerUpDetails(mCurrentPowerUp);
                }

                /* Questions */
                mQuestionsCardAdapter = new PredictionQuestionsCardAdapter(getContext(),
                        questionsResponse.getQuestions(),
                        getQuestionsCardAdapterListener());

                mCardStack.setAdapter(mQuestionsCardAdapter);

                /* update ui */
                mTotalQuestions = questionsResponse.getQuestions().size();
                showQuestionsCounter();
                showNeitherIfRequired();
                showPowerUpsIfApplied();
                if (mTotalQuestions == 1) {
                    hideNextButton();
                }

            } else {
                Log.d(TAG, "Questions list null/empty");
                handleErrorAndFinishActivity();
            }
        }
    }

    private void showQuestionsCounter() {
        if (getView() != null && mQuestionsCardAdapter != null) {
//            int currentPos = mQuestionsCardAdapter.getPosition(mQuestionsCardAdapter.getItem(getTopVisibleCardPosition())) + 1; // default pos is 0 based, so add 1

            if (mCurrentQuestionPos < 0 || mCurrentQuestionPos > mTotalQuestions) {
                mCurrentQuestionPos = 1;
            }

            if (mCurrentQuestionPos > 0) {
                String questionNumCounter = String.valueOf(mCurrentQuestionPos) + "/" + String.valueOf(mTotalQuestions);
                mCounterTextView.setText(questionNumCounter);

                RelativeLayout relativeLayout = (RelativeLayout) getView().findViewById(R.id.prediction_questions_info_parent);
                relativeLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initHeading() {
        if (getView() != null && getActivity() != null) {
            TextView headingParty1TextView = (TextView) getView().findViewById(R.id.prediction_heading_party1_textView);
            TextView headingParty2TextView = (TextView) getView().findViewById(R.id.prediction_heading_party2_textView);
            TextView vsTextView = (TextView) getView().findViewById(R.id.prediction_heading_vs_textView);
            TextView subHeadingTextView = (TextView) getView().findViewById(R.id.prediction_sub_heading_textView);

            if (mPlayScreenData != null) {
                String party1 = mPlayScreenData.getMatchPartyTitle1();
                String party2 = mPlayScreenData.getMatchPartyTitle2();

                if (!TextUtils.isEmpty(party1) && !TextUtils.isEmpty(party2)) {
                    headingParty1TextView.setText(party1);
                    headingParty2TextView.setText(party2);

                } else if (!TextUtils.isEmpty(party1) && TextUtils.isEmpty(party2)) {   // Single party
                    headingParty1TextView.setText(party1);
                    vsTextView.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(mPlayScreenData.getSubTitle())) {
                    subHeadingTextView.setText(mPlayScreenData.getSubTitle());
                }
            }
        }
    }

    private void animateTopBottomLayouts() {
        if (getView() != null && getActivity() != null) {
            final RelativeLayout topLayout = (RelativeLayout) getView().findViewById(R.id.prediction_top_layout);
            final LinearLayout bottomLayout = (LinearLayout) getView().findViewById(R.id.prediction_bottom_layout);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation animation = AnimationUtils.loadAnimation(topLayout.getContext(), R.anim.prediction_top_view_anim);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            topLayout.setVisibility(View.VISIBLE);

                            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.prediction_bottom_view_anim);
                            anim.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    bottomLayout.setVisibility(View.VISIBLE);

                                    loadQuestions();
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });

                            bottomLayout.startAnimation(anim);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    topLayout.startAnimation(animation);
                }
            }, 100);

            /*topLayout.clearAnimation();
            topLayout.animate().translationYBy(topLayout.getHeight()).setDuration(1000).
                    setInterpolator(new LinearInterpolator()).
                    setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            topLayout.setVisibility(View.VISIBLE);

                            bottomLayout.clearAnimation();
                            bottomLayout.animate().translationYBy(bottomLayout.getHeight()).setDuration(1000).
                                    setInterpolator(new LinearInterpolator()).
                                    setListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            bottomLayout.setVisibility(View.VISIBLE);
                                            loadQuestions();
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {

                                        }
                                    }).start();

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).start();*/

            /*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation animation = AnimationUtils.loadAnimation(topLayout.getContext(), R.anim.prediction_top_view_anim);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            topLayout.setVisibility(View.VISIBLE);
                            bottomLayout.clearAnimation();
                            bottomLayout.startAnimation(AnimationUtils.loadAnimation(bottomLayout.getContext(), R.anim.prediction_bottom_view_anim));
                            bottomLayout.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    topLayout.clearAnimation();
                    topLayout.startAnimation(animation);
                }
            }, 100);*/
        }
    }

    private void showNeitherIfRequired() {
        int pos = getTopVisibleCardPosition();
        if (mQuestionsCardAdapter != null && pos < mQuestionsCardAdapter.getCount()) {
            PredictionQuestion question = mQuestionsCardAdapter.getItem(pos);

            if (question != null && !TextUtils.isEmpty(question.getQuestionOption3())) {
                makeThirdOptionVisible(true, question.getQuestionOption3());
            } else {
                makeThirdOptionVisible(false, getString(R.string.prediction_neither_button_text));
            }
        }
    }

    private void showPowerUpsIfApplied() {
        int nextCardPos = getTopVisibleCardPosition();
        if (nextCardPos < mQuestionsCardAdapter.getCount()) {
            PredictionQuestion question = mQuestionsCardAdapter.getItem(nextCardPos);

            if (question != null && question.getPowerUp() != null && mCardStack != null && mCardStack.getTopView() != null) {
                View topView = mCardStack.getTopView();
                final RelativeLayout powerUpParent = (RelativeLayout) topView.findViewById(R.id.prediction_card_powerup_layout);
                if (powerUpParent != null) {

                    /* Doubler */
                    if (question.getPowerUp().getDoubler() == 1 && canAddPowerUpCheckUiLayout(powerUpParent, PowerUpEnum.DOUBLER)) {
                        View powerUpView = mUiHelper.getPowerUpView(getContext(), PowerUpEnum.DOUBLER);
                        if (powerUpView != null) {
                            setPowerUpRemoveListener(powerUpParent, powerUpView);
                            powerUpParent.addView(powerUpView, powerUpParent.getChildCount(),
                                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                        }
                    }

                    /* No Negative */
                    if (question.getPowerUp().getNoNegative() == 1 && canAddPowerUpCheckUiLayout(powerUpParent, PowerUpEnum.NO_NEGATIVE)) {
                        View powerUpView = mUiHelper.getPowerUpView(getContext(), PowerUpEnum.NO_NEGATIVE);
                        if (powerUpView != null) {
                            setPowerUpRemoveListener(powerUpParent, powerUpView);
                            powerUpParent.addView(powerUpView, powerUpParent.getChildCount(),
                                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                        }
                    }

                    if (powerUpParent.getChildCount() == 2) {
                        float moveUpto = getContext().getResources().getDimension(R.dimen.dp_30);
                        View view = powerUpParent.getChildAt(0);
                        view.setTranslationX(-moveUpto);

                        view = powerUpParent.getChildAt(1);
                        view.setTranslationX(moveUpto);
                    }
                }
            }
        }

    }

    private int getTopVisibleCardPosition() {
        int pos = -1;
        if (mCardStack != null) {
            pos = mCardStack.getCurrIndex();
        }
        return pos;
    }

    private void makeThirdOptionVisible(boolean shouldVisible, String buttonText) {
        if (getView() != null && getActivity() != null) {

            mThirdOptionButton.setText(buttonText);
            if (shouldVisible) {
                mThirdOptionLayout.setVisibility(View.VISIBLE);
            } else {
                mThirdOptionLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.prediction_back_btn:
                if (mFragmentListener != null) {
                    mFragmentListener.onBackClicked();
                }
                break;

            case R.id.prediction_doubler_Layout:
                onDoublerPowerUpClicked();
                break;

            case R.id.prediction_noNeg_Layout:
                onNoNegativeClicked();
                break;

            case R.id.prediction_player_poll_Layout:
                onPlayerPollClicked();
                break;

            case R.id.prediction_third_option_button:
                onThirdOptionClicked();
                break;

            case R.id.prediction_next_layout:
                onNextClicked();
                break;

            case R.id.prediction_powerup_bank_imgView:
                if (mPlayScreenData.getMaxPowerUpTransferLimit() == 0 && getView() != null) {
                    CustomSnackBar msgBar = CustomSnackBar.make(getView(), "POWERUP BANK DISABLED", CustomSnackBar.DURATION_LONG);
                    msgBar.setImageResource(R.drawable.powerup_bank_disabled);
                    msgBar.show();

                } else {
                    onPowerUpBankClicked();
                    NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.PLAY, Constants.AnalyticsClickLabels.POWER_UP_BANK);
                }
                break;

            case R.id.prediction_help_play_imgView:
                onHelpPlayClicked();
                NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.PLAY, Constants.AnalyticsClickLabels.HELP);
                break;
        }
    }

    public void onPowerUpBankClicked() {
        mUiHelper.hidePowerUpBankAndHelpButtons(mPowerUpImageView, mGamePlayImageView);

        PowerupBankTransferScreenData bankScreenData = new PowerupBankTransferScreenData();
        bankScreenData.setRoomId(mPlayScreenData.getRoomId());
        bankScreenData.setChallengeId(mPlayScreenData.getChallengeId());
        bankScreenData.setSubTitle(mPlayScreenData.getSubTitle());

        Bundle args = new Bundle();
        args.putParcelable(Constants.BundleKeys.POWERUP_BANK_TRANSFER_SCREEN_DATA, Parcels.wrap(bankScreenData));

        Intent intent = new Intent(getActivity(), PowerupBankTransferToPlayActivity.class);
        intent.putExtras(args);
        startActivityForResult(intent, POWER_UP_BANK_ACTIVITY_REQUEST_CODE);
    }

    public void onHelpPlayClicked() {
        mUiHelper.hidePowerUpBankAndHelpButtons(mPowerUpImageView, mGamePlayImageView);
        Intent intent = new Intent(getActivity(), GamePlayHelpActivity.class);
        startActivityForResult(intent, GAME_PLAY_HELP_ACTIVITY);
    }

    private void onNextClicked() {
        if (mCardStack != null) {
            mCardStack.discardTopOnButtonClick(CardDirection.DOWN);
        }
    }

    private void onThirdOptionClicked() {
        if (mCardStack != null) {
            mCardStack.discardTopOnButtonClick(CardDirection.UP);
        }
    }

    private void onPlayerPollClicked() {
        if (isEnoughPowerUpAvailableToApply(PowerUpEnum.PLAYER_POLL)) {
            if (canAddPlayerPollPowerUpCheckUiLayout()) {

                int questionId = getTopVisibleCardQuestionId();
                if (mPlayScreenData != null && questionId >= 0) {

                    showProgress();
                    PredictionPlayersPollDataProvider playersPollDataProvider = new PredictionPlayersPollDataProvider();
                    playersPollDataProvider.getPlayersPoll(questionId, mPlayScreenData.getRoomId(),
                            new PredictionPlayersPollDataProvider.PlayersPollDataProviderListener() {
                                @Override
                                public void onData(int status, @Nullable PlayerPollResponse playersPolls) {
                                    hideProgress();
                                    onPlayerPollSuccess(playersPolls);
                                }

                                @Override
                                public void onError(int status) {
                                    hideProgress();
                                    handleError(null, status);
                                }

                                @Override
                                public void onServerSentError(String msg) {
                                    hideProgress();
                                    if (TextUtils.isEmpty(msg)) {
                                        msg = Constants.Alerts.SOMETHING_WRONG;
                                    }
                                    handleError(msg, -1);
                                }
                            });
                } else {
                    Log.e(TAG, "QuestionId / roomId can not be found for players-poll info");
                    handleError(null, -1);
                }
            } else {
                Log.d(TAG, "Player poll already applied on the card");
            }
        } else {
            onPowerUpBankClicked();
        }
    }

    private boolean isEnoughPowerUpAvailableToApply(PowerUpEnum powerUpEnum) {
        boolean canApply = false;

        if (mCurrentPowerUp != null) {
            switch (powerUpEnum) {
                case DOUBLER:
                    if (mCurrentPowerUp.getDoubler() > 0) {
                        canApply = true;
                    }
                    break;

                case NO_NEGATIVE:
                    if (mCurrentPowerUp.getNoNegative() > 0) {
                        canApply = true;
                    }
                    break;

                case PLAYER_POLL:
                    if (mCurrentPowerUp.getPlayerPoll() > 0) {
                        canApply = true;
                    }
                    break;
            }
        }

        return canApply;
    }

    private int getTopVisibleCardQuestionId() {
        int questionId = -1;
        int cardPos = getTopVisibleCardPosition();
        if (cardPos >= 0 && mQuestionsCardAdapter != null && cardPos < mQuestionsCardAdapter.getCount()) {
            PredictionQuestion question = mQuestionsCardAdapter.getItem(cardPos);
            if (question != null) {
                questionId = question.getQuestionId();
            }
        }

        return questionId;
    }

    /**
     * Players Poll can NOT be removed once applied
     *
     * @param playersPolls
     */
    private void onPlayerPollSuccess(PlayerPollResponse playersPolls) {
        /* NOTE: same logic for PredictionCardAdapter.preFillIfPlayerPollAlreadyApplied() tobe followed */

        if (getView() != null && getActivity() != null && playersPolls != null &&
                playersPolls.getPlayersPollList() != null && !playersPolls.getPlayersPollList().isEmpty()) {

            List<PlayersPoll> playersPollList = playersPolls.getPlayersPollList();

            /* Add response to question so that it can be shown next time if card is shuffled */
            if (mQuestionsCardAdapter != null) {
                PredictionQuestion question = mQuestionsCardAdapter.getItem(getTopVisibleCardPosition());
                if (question != null) {
                    question.setPlayersPollList(playersPollList);
                }
            }

            String leftPollAnswer = PlayerPollHelper.getLeftAnswerString(playersPollList);
            String rightPollAnswer = PlayerPollHelper.getRightAnswerString(playersPollList);

            if (mCardStack != null && mCardStack.getTopView() != null && mQuestionsCardAdapter != null) {
                View view = mCardStack.getTopView();
                TextView playerPollOption1TextView = (TextView) view.findViewById(R.id.prediction_card_player_poll_1_textView);
                TextView playerPollOption2TextView = (TextView) view.findViewById(R.id.prediction_card_player_poll_2_textView);

                playerPollOption1TextView.setText(leftPollAnswer);
                playerPollOption2TextView.setText(rightPollAnswer);

                playerPollOption1TextView.setVisibility(View.VISIBLE);
                playerPollOption2TextView.setVisibility(View.VISIBLE);

                mQuestionsCardAdapter.applyOrRemovePowerUp(PowerUpEnum.PLAYER_POLL, getTopVisibleCardPosition(), true);
                usePowerUp(true, PowerUpEnum.PLAYER_POLL);

                    /* Set Minority Answer */
                int pos = getTopVisibleCardPosition();
                if (mQuestionsCardAdapter != null && pos < mQuestionsCardAdapter.getCount()) {
                    PredictionQuestion question = mQuestionsCardAdapter.getItem(pos);
                    if (question != null) {
                        PlayerPollHelper.setMinorityAnswerId(question, leftPollAnswer, rightPollAnswer);
                    }
                }
            }
        } else {
            handleError("Not enough responses for Audience Poll", -1);
        }
    }

    private void onNoNegativeClicked() {
        if (isEnoughPowerUpAvailableToApply(PowerUpEnum.NO_NEGATIVE)) {

            if (mCardStack != null && mCardStack.getTopView() != null && mUiHelper != null) {
                View topView = mCardStack.getTopView();
                final RelativeLayout powerUpParent = (RelativeLayout) topView.findViewById(R.id.prediction_card_powerup_layout);
                if (powerUpParent != null) {
                    if (canAddPowerUpCheckUiLayout(powerUpParent, PowerUpEnum.NO_NEGATIVE)) {
                        applyPowerUp(powerUpParent, PowerUpEnum.NO_NEGATIVE, powerUpParent.getChildCount());
                    }
                }
            }
        } else {
            onPowerUpBankClicked();
        }
    }

    private void onDoublerPowerUpClicked() {
        if (isEnoughPowerUpAvailableToApply(PowerUpEnum.DOUBLER)) {

            if (mCardStack != null && mCardStack.getTopView() != null && mUiHelper != null) {
                View topView = mCardStack.getTopView();
                final RelativeLayout powerUpParent = (RelativeLayout) topView.findViewById(R.id.prediction_card_powerup_layout);
                if (powerUpParent != null) {
                    if (canAddPowerUpCheckUiLayout(powerUpParent, PowerUpEnum.DOUBLER)) {
                        applyPowerUp(powerUpParent, PowerUpEnum.DOUBLER, powerUpParent.getChildCount());
                    }
                }
            }
        } else {
            onPowerUpBankClicked();
        }
    }

    private void applyPowerUp(RelativeLayout powerUpParent, PowerUpEnum powerUpEnum, int childCount) {
        final View powerUpView = mUiHelper.getPowerUpView(getContext(), powerUpEnum);
        if (powerUpView != null) {
            setPowerUpRemoveListener(powerUpParent, powerUpView);
            powerUpParent.addView(powerUpView, childCount,
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mUiHelper.showPowerUpAnimation(powerUpView, powerUpParent);

            mQuestionsCardAdapter.applyOrRemovePowerUp(powerUpEnum, getTopVisibleCardPosition(), true);
            usePowerUp(true, powerUpEnum);
        }
    }

    private void setPowerUpRemoveListener(final RelativeLayout powerUpParent, final View powerUpView) {
        powerUpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUiHelper.dismissPowerUpAnimation(powerUpView, new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (mQuestionsCardAdapter != null) {
                            PowerUpEnum powerUpEnum = (PowerUpEnum) powerUpView.getTag();
                            powerUpParent.removeView(powerUpView);
                            mUiHelper.animateOtherPowerupWhenAnyOneRemoved(powerUpParent);

                            switch (powerUpEnum) {
                                case DOUBLER:
                                    mQuestionsCardAdapter.applyOrRemovePowerUp(PowerUpEnum.DOUBLER, getTopVisibleCardPosition(), false);
                                    usePowerUp(false, PowerUpEnum.DOUBLER);
                                    break;

                                case NO_NEGATIVE:
                                    mQuestionsCardAdapter.applyOrRemovePowerUp(PowerUpEnum.NO_NEGATIVE, getTopVisibleCardPosition(), false);
                                    usePowerUp(false, PowerUpEnum.NO_NEGATIVE);
                                    break;

                                case PLAYER_POLL:
                                    // PLAYER Poll can NEVER be removed once applied
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        });
    }

    /**
     * Check that powerup already applied & UI is created for the same question/card
     * so that multi-tap can be prevented
     *
     * @param powerUpParent
     * @param powerUpEnum
     * @return
     */
    private boolean canAddPowerUpCheckUiLayout(RelativeLayout powerUpParent, PowerUpEnum powerUpEnum) {
        boolean canAdd = true;
        if (powerUpParent.getChildCount() > 0) {
            for (int temp = 0; temp < powerUpParent.getChildCount(); temp++) {
                PowerUpEnum powerupView = (PowerUpEnum) powerUpParent.getChildAt(temp).getTag();
                if (powerupView == powerUpEnum) {
                    canAdd = false;
                }
            }
        }
        return canAdd;
    }

    private boolean canAddPlayerPollPowerUpCheckUiLayout() {
        boolean canAdd = true;
        if (mCardStack != null && mCardStack.getTopView() != null) {
            View view = mCardStack.getTopView();
            TextView playerPollOption1TextView = (TextView) view.findViewById(R.id.prediction_card_player_poll_1_textView);
            TextView playerPollOption2TextView = (TextView) view.findViewById(R.id.prediction_card_player_poll_2_textView);

            if (playerPollOption1TextView.getVisibility() == View.VISIBLE ||
                    playerPollOption2TextView.getVisibility() == View.VISIBLE) {
                canAdd = false;
            }
        }
        return canAdd;
    }

    /**
     * Performs action when card is swiped out
     *
     * @return
     */
    @NonNull
    public CardStack.CardEventListener getCardSwipeEventsListener() {
        return new CardStack.CardEventListener() {
            @Override
            public boolean swipeEnd(int direction, float distance) {
                /* While dragging, based on chosen direct, intented answer was shown; which should be removed now */
                if (mCardStack != null) {
                    View view = mCardStack.getTopView();
                    if (view != null) {
                        TextView dragChoiceTextView = (TextView) view.findViewById(R.id.prediction_drag_choice_textView);
                        LinearLayout optionsLayout = (LinearLayout) view.findViewById(R.id.prediction_options_parent);

                        dragChoiceTextView.setText("");
                        optionsLayout.setVisibility(View.VISIBLE);
                    }
                }

                boolean endSwipe = false;
                if (distance > CARD_SWIPE_DISTANCE_TO_END_SWIPE) {
                    endSwipe = true;
                }
                return endSwipe;
            }

            @Override
            public boolean swipeStart(int i, float v) {
                /* Show intented answer based on direction of swipe */
                if (mCardStack != null) {
                    View view = mCardStack.getTopView();
                    if (view != null) {
                        LinearLayout optionsLayout = (LinearLayout) view.findViewById(R.id.prediction_options_parent);
                        optionsLayout.setVisibility(View.INVISIBLE);
                    }
                }
                return true;
            }

            @Override
            public boolean swipeContinue(int direction, float v, float v1) {
                if (mCardStack != null && mQuestionsCardAdapter != null) {
                    View view = mCardStack.getTopView();
                    PredictionQuestion question = mQuestionsCardAdapter.getItem(getTopVisibleCardPosition());

                    if (view != null && question != null) {
                        TextView dragChoiceTextView = (TextView) view.findViewById(R.id.prediction_drag_choice_textView);
                        switch (direction) {
                            case CardDirection.LEFT:
                                dragChoiceTextView.setText(question.getQuestionOption1());
                                break;

                            case CardDirection.RIGHT:
                                dragChoiceTextView.setText(question.getQuestionOption2());
                                break;

                            case CardDirection.UP:
                                if (!TextUtils.isEmpty(question.getQuestionOption3())) {
                                    dragChoiceTextView.setText(question.getQuestionOption3());
                                }
                                break;

                            case CardDirection.DOWN:
                                break;
                        }
                    }
                }
                return true;
            }

            @Override
            public void discarded(int index, int direction) {
                boolean isMatchCompleted = false;
                if (index == (mQuestionsCardAdapter.getCount() - 1)) {
                    isMatchCompleted = true;
                }

                switch (direction) {
                    case CardDirection.LEFT:
                        saveAnswer(index, Constants.AnswerIds.LEFT, isMatchCompleted);
                        break;

                    case CardDirection.RIGHT:
                        saveAnswer(index, Constants.AnswerIds.RIGHT, isMatchCompleted);
                        break;

                    case CardDirection.UP:
                        if (shouldAllowThirdOption(index)) {
                            saveAnswer(index, Constants.AnswerIds.NEITHER, isMatchCompleted);
                        } else {
                            mCardStack.undo();
                            showQuestionsCounter();
                            showNeitherIfRequired();
                            showPowerUpsIfApplied();
                        }
                        break;

                    case CardDirection.DOWN:
                        if (mQuestionsCardAdapter != null && mCardStack != null) {
                            PredictionQuestion question = mQuestionsCardAdapter.getItem(index);
                            mCardStack.shuffle(question);
                            mCurrentQuestionPos++;
                            showQuestionsCounter();
                            showNeitherIfRequired();
                            showPowerUpsIfApplied();
                        }
                        break;
                }

                if ((index + 1) == (mCardStack.getAdapter().getCount() - 1)) {
                    hideNextButton();
                }
            }

            @Override
            public void topCardTapped() {
                Log.d("Card", "Top card tapped");
            }
        };
    }

    private boolean shouldAllowThirdOption(int index) {
        boolean isThirdOption = false;
        if (mQuestionsCardAdapter != null && mCardStack != null && index < mQuestionsCardAdapter.getCount()) {
            PredictionQuestion question = mQuestionsCardAdapter.getItem(index);
            if (question != null && !TextUtils.isEmpty(question.getQuestionOption3())) {
                isThirdOption = true;
            }
        }
        return isThirdOption;
    }

    private void hideNextButton() {
        mNextTextView.setVisibility(View.INVISIBLE);
    }

    private void showProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.show();
        }
    }

    private void hideProgress() {
        if (getActivity() != null) {
            if (!getActivity().isFinishing() && mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }

    /**
     * On success full completion of choice for all the answers
     */
    private void onMatchCompleted() {

        if (getContext() != null && getContext().getApplicationContext() != null) {
        /* AdWords conversion Report, on match completed */
            AdWordsConversionReporter.reportWithConversionId(getContext().getApplicationContext(),
                    "934797470", "tIBCCIK8pHEQnsHfvQM", "1.00", true);

            Log.d(TAG, "Match Completed");

        /* Fetch Inplay data and save into DB */
            new CacheManagementHelper().fetchInplayDataAndSaveIntoDb(getContext().getApplicationContext());
        }

        ResultsScreenDataDto data = new ResultsScreenDataDto();
        if (mPlayScreenData != null) {
            data.setRoomId(mPlayScreenData.getRoomId());
            data.setChallengeId(mPlayScreenData.getChallengeId());
            data.setMatchId(mPlayScreenData.getMatchId());
            data.setMatchStatus(mPlayScreenData.getMatchStatus());
            data.setChallengeName(mPlayScreenData.getChallengeName());
            data.setPlayingPseudoGame(mPlayScreenData.isPlayingPseudoGame());
            data.setChallengeStartTime(mPlayScreenData.getChallengeStartTime());
            if (mPlayScreenData.getInPlayContestDto() != null) {
                data.setInPlayContestDto(mPlayScreenData.getInPlayContestDto());
                data.setSubTitle(mPlayScreenData.getInPlayContestDto().getContestName());
            }
        }

        Bundle args = getArguments();
        if (args == null) {
            args = new Bundle();
        }
        args.putParcelable(Constants.BundleKeys.RESULTS_SCREEN_DATA, Parcels.wrap(data));
        args.putInt(Constants.BundleKeys.SCREEN_LAUNCH_REQUEST, NostraHomeActivity.LaunchedFrom.SHOW_IN_PLAY);

        if (mFragmentListener != null && mPlayScreenData != null) {
            if (mPlayScreenData.isPlayingPseudoGame()) {
                mFragmentListener.onMatchCompleted(args);
            } else {
                mFragmentListener.onBackClicked();
            }
        }
    }

    private void saveAnswer(int cardIndexPos, final int answerId, final boolean isMatchCompleted) {
        if (mSavePredictionAnswerProvider == null) {
            mSavePredictionAnswerProvider = new SavePredictionAnswerProvider();
        }

        if (mQuestionsCardAdapter != null && cardIndexPos < mQuestionsCardAdapter.getCount() && mPlayScreenData != null) {
            final PredictionQuestion question = mQuestionsCardAdapter.getItem(cardIndexPos);
            if (question != null) {
                showProgress();

                mSavePredictionAnswerProvider.savePredictionAnswer(question.getPowerUp(),
                        mPlayScreenData.getRoomId(),
                        question.getMatchId(),
                        question.getQuestionId(),
                        answerId,
                        TimeUtils.getCurrentTime(Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE, Constants.DateFormats.GMT),
                        isMatchCompleted,
                        question.isMinorityAnswer(answerId),
                        new SavePredictionAnswerProvider.SavePredictionAnswerListener() {
                            @Override
                            public void onData(int status, @Nullable AnswerResponse answerResponse) {
                                hideProgress();
                                onAnswerSavedSuccessfully(isMatchCompleted, question, answerResponse);
                            }

                            @Override
                            public void onError(int status) {
                                hideProgress();
                                /* Undo card */
                                if (mCardStack != null) {
                                    mCardStack.undo();
                                }
                                handleError("Could not save your prediction, try again!", -1);
                            }

                            @Override
                            public void onServerSentError(String error) {
                                hideProgress();
                                /* Undo card */
                                if (mCardStack != null) {
                                    mCardStack.undo();
                                }

                                String errorMsg = "Could not save your prediction, try again!";
                                if (!TextUtils.isEmpty(error)) {
                                    errorMsg = error;
                                }
                                handleError(errorMsg, -1);
                            }
                        });
            }
        } else {
            Log.e(TAG, "Can not save answer without required info");
            handleError(null, -1);
        }
    }

    private void onAnswerSavedSuccessfully(boolean isMatchCompleted, PredictionQuestion question, AnswerResponse answerResponse) {
        if (answerResponse != null && answerResponse.getAnswerResponseData() != null && mQuestionsCardAdapter != null) {
            if (isMatchCompleted) {
                /* On success response for the last question only allows to consider match as completed */
                onMatchCompleted();

            } else {
//                mCurrentQuestionPos++;
                mTotalQuestions--;
                showQuestionsCounter();
                showNeitherIfRequired();
                showPowerUpsIfApplied();
                question.setAnsweredSuccessfully(true);

                if (answerResponse.getAnswerResponseData().getPowerUp() != null) {
                    /* NOTE: Do not update powerup current status as per server response,
                      * As powerups could have applied locally to other questions & have shuffled (Those powerups remain with those questions) */

                    /*mCurrentPowerUp = answerResponse.getAnswerResponseData().getPowerUp();
                    updatePowerUpDetails(mCurrentPowerUp, true);*/
                }
            }
        }
    }

    /**
     * If msg is passed then msg will be shown ;
     * else status should be passed
     *
     * @param msg
     * @param status
     */
    private void handleError(String msg, int status) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {
            if (!TextUtils.isEmpty(msg)) {
                CustomSnackBar.make(getView(), msg, CustomSnackBar.DURATION_LONG).show();

            } else {
                switch (status) {
                    case Constants.DataStatus.NO_INTERNET:
                        CustomSnackBar.make(getView(), Constants.Alerts.NO_INTERNET_CONNECTION, CustomSnackBar.DURATION_LONG).show();
                        break;

                    default:
                        CustomSnackBar.make(getView(), Constants.Alerts.SOMETHING_WRONG, CustomSnackBar.DURATION_LONG).show();
                        break;
                }
            }
        }
    }

    /**
     * Handles Power-up related counters & ui
     *
     * @param isPowerUpApplied - true means applied (reduce), false means removed (increase)
     * @param powerUpEnum      - which power-up used
     */
    private void usePowerUp(boolean isPowerUpApplied, PowerUpEnum powerUpEnum) {
        if (mCurrentPowerUp != null) {
            int counter = 0;
            switch (powerUpEnum) {
                case DOUBLER:
                    counter = isPowerUpApplied ? mCurrentPowerUp.getDoubler() - 1 : mCurrentPowerUp.getDoubler() + 1;
                    if (counter >= 0) {
                        mCurrentPowerUp.setDoubler(counter);
                    }
                    updatePowerUpDetails(mCurrentPowerUp);
                    break;

                case NO_NEGATIVE:
                    counter = isPowerUpApplied ? mCurrentPowerUp.getNoNegative() - 1 : mCurrentPowerUp.getNoNegative() + 1;
                    if (counter >= 0) {
                        mCurrentPowerUp.setNoNegative(counter);
                    }
                    updatePowerUpDetails(mCurrentPowerUp);
                    break;

                case PLAYER_POLL:
                    if (isPowerUpApplied) {
                        counter = mCurrentPowerUp.getPlayerPoll() - 1;
                    } else {
                        // PlayerPoll can  NOT be removed
                    }
                    if (counter >= 0) {
                        mCurrentPowerUp.setPlayerPoll(counter);
                    }
                    updatePowerUpDetails(mCurrentPowerUp);
                    break;
            }
        }
    }

    /**
     * Updates powerUp counter on UI
     *
     * @param powerUp - powerup to update
     */
    private void updatePowerUpDetails(PowerUp powerUp) {

        if (getView() != null && getActivity() != null && !getActivity().isFinishing() && powerUp != null) {
            View view = getView();
            TextView doublerTextView = (TextView) view.findViewById(R.id.prediction_doubler_counter_textView);
            TextView noNegTextView = (TextView) view.findViewById(R.id.prediction_noNeg_counter_textView);
            TextView playerPollTextView = (TextView) view.findViewById(R.id.prediction_player_poll_counter_textView);

            if (powerUp.getDoubler() < 0) {
                powerUp.setDoubler(0);
            }
            if (powerUp.getNoNegative() < 0) {
                powerUp.setNoNegative(0);
            }
            if (powerUp.getPlayerPoll() < 0) {
                powerUp.setPlayerPoll(0);
            }

            doublerTextView.setText(String.valueOf(powerUp.getDoubler()));
            noNegTextView.setText(String.valueOf(powerUp.getNoNegative()));
            playerPollTextView.setText(String.valueOf(powerUp.getPlayerPoll()));

            changeCounterColorBaseOnAvailablePowerUps(powerUp, doublerTextView, noNegTextView, playerPollTextView);

            updatePointsOnCard();
        }
    }

    private void changeCounterColorBaseOnAvailablePowerUps(PowerUp powerUp, TextView doublerTextView,
                                                           TextView noNegTextView, TextView playerPollTextView) {
        if (powerUp != null) {
            if (powerUp.getDoubler() <= 0) {
                doublerTextView.setBackgroundResource(R.drawable.prediction_powerup_counter_0_bg);
                doublerTextView.setText("+");
            } else {
                doublerTextView.setBackgroundResource(R.drawable.prediction_powerup_counter_bg);
            }

            if (powerUp.getNoNegative() <= 0) {
                noNegTextView.setBackgroundResource(R.drawable.prediction_powerup_counter_0_bg);
                noNegTextView.setText("+");
            } else {
                noNegTextView.setBackgroundResource(R.drawable.prediction_powerup_counter_bg);
            }

            if (powerUp.getPlayerPoll() <= 0) {
                playerPollTextView.setBackgroundResource(R.drawable.prediction_powerup_counter_0_bg);
                playerPollTextView.setText("+");
            } else {
                playerPollTextView.setBackgroundResource(R.drawable.prediction_powerup_counter_bg);
            }
        }
    }

    private void updatePointsOnCard() {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing() &&
                mQuestionsCardAdapter != null && mCardStack != null) {

            PredictionQuestion question = mQuestionsCardAdapter.getItem(getTopVisibleCardPosition());
            if (question != null) {
                View view = mCardStack.getTopView();
                TextView positiveTextView = (TextView) view.findViewById(R.id.prediction_card_positive_textView);
                TextView negativeTextView = (TextView) view.findViewById(R.id.prediction_card_negative_textView);

                positiveTextView.setText("+ " + String.valueOf(question.getPositivePoints()) + " PTS");
                negativeTextView.setText(String.valueOf(question.getNegativePoints()) + " PTS");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (!getActivity().isFinishing()) {
            switch (requestCode) {
                case POWER_UP_BANK_ACTIVITY_REQUEST_CODE:
                case GAME_PLAY_HELP_ACTIVITY:
                    mUiHelper.showPowerUpBankAndHelpButtons(mPowerUpImageView, mGamePlayImageView);
                    break;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mPowerUpUpdatedReceiver,
                new IntentFilter(Constants.IntentActions.ACTION_POWERUPS_UPDATED));
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mPowerUpUpdatedReceiver);
        super.onStop();
    }

    /**
     * As per UI design, powerups need to be updated when Bank-Activity there
     */
    BroadcastReceiver mPowerUpUpdatedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getExtras() != null) {
                PowerUp powerUpsAddedAsPerRequest = Parcels.unwrap(intent.getExtras().getParcelable(Constants.BundleKeys.BANK_TRANSFER_POWERUPS));
                updatePowerUpOnSuccessfulBankTransfer(powerUpsAddedAsPerRequest);
            }
        }
    };

    private void updatePowerUpOnSuccessfulBankTransfer(PowerUp transferredPowerUp) {
        if (transferredPowerUp != null) {
            if (mCurrentPowerUp != null) {
                mCurrentPowerUp.setDoubler(mCurrentPowerUp.getDoubler() + transferredPowerUp.getDoubler());
                mCurrentPowerUp.setNoNegative(mCurrentPowerUp.getNoNegative() + transferredPowerUp.getNoNegative());
                mCurrentPowerUp.setPlayerPoll(mCurrentPowerUp.getPlayerPoll() + transferredPowerUp.getPlayerPoll());
            } else {
                mCurrentPowerUp = transferredPowerUp;
            }
            updatePowerUpDetails(mCurrentPowerUp);
        }
    }

    private void openWebView(String url) {
        if (!TextUtils.isEmpty(url) && getActivity() != null && !getActivity().isFinishing()) {

            Intent webLinkIntent = new Intent(getActivity(), FeedWebView.class);
            webLinkIntent.putExtra("url", url);
            getActivity().startActivity(webLinkIntent);

        } else {
            com.jeeva.android.Log.d(TAG, "Url empty");
        }
    }

}
