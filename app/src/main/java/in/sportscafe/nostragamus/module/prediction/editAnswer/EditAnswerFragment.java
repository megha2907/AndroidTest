package in.sportscafe.nostragamus.module.prediction.editAnswer;


import android.animation.Animator;
import android.app.Activity;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.CustomProgressbar;
import com.sportscafe.nostracardstack.cardstack.CardDirection;
import com.sportscafe.nostracardstack.cardstack.CardStack;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.customViews.CustomSnackBar;
import in.sportscafe.nostragamus.module.prediction.editAnswer.adapter.EditAnswerPredictionAdapterListener;
import in.sportscafe.nostragamus.module.prediction.editAnswer.adapter.EditAnswerPredictionCardAdapter;
import in.sportscafe.nostragamus.module.prediction.editAnswer.dataProvider.GetQuestionForEditAnswerApiModelImpl;
import in.sportscafe.nostragamus.module.prediction.editAnswer.dataProvider.SaveEditAnswerApiModelImpl;
import in.sportscafe.nostragamus.module.prediction.editAnswer.dto.EditAnswerQuestion;
import in.sportscafe.nostragamus.module.prediction.editAnswer.dto.EditAnswerScreenData;
import in.sportscafe.nostragamus.module.prediction.editAnswer.dto.QuestionForEditAnswerResponse;
import in.sportscafe.nostragamus.module.prediction.editAnswer.dto.SaveEditAnswerResponse;
import in.sportscafe.nostragamus.module.prediction.gamePlayHelp.GamePlayHelpActivity;
import in.sportscafe.nostragamus.module.prediction.playScreen.dataProvider.PredictionPlayersPollDataProvider;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.AnswerPowerUpDto;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PlayerPollResponse;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PlayersPoll;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PowerUp;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PowerUpEnum;
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
public class EditAnswerFragment extends NostraBaseFragment implements View.OnClickListener {

    private static final String TAG = EditAnswerFragment.class.getSimpleName();
    private static final int CARD_SWIPE_DISTANCE_TO_END_SWIPE = 300;
    private final static int POWER_UP_BANK_ACTIVITY_REQUEST_CODE = 99;
    private final static int GAME_PLAY_HELP_ACTIVITY = 98;
    private final static int POWERUP_EDITED_ALERT_POPUP_REQUEST_CODE = 351;

    private EditAnswerFragmentListener mFragmentListener;
    private CardStack mCardStack;
    private EditAnswerPredictionCardAdapter mQuestionsCardAdapter;
    private LinearLayout mThirdOptionLayout;
    private PredictionUiHelper mUiHelper;
    private PowerUp mCurrentPowerUp;
    private AnswerPowerUpDto mAppliedPowerUp;
    private EditAnswerScreenData mEditAnswerScreenData;
    private CustomProgressbar mProgressDialog;
    private ImageView mPowerUpImageView;
    private ImageView mGamePlayImageView;
    private LinearLayout mThirdOptionButton;
    private TextView mMessageTextView;
    private boolean mIsFirstTimeEditing = false;

    public EditAnswerFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_answer, container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EditAnswerFragmentListener) {
            mFragmentListener = (EditAnswerFragmentListener) context;
        } else {
            throw new RuntimeException(EditAnswerFragment.class.getSimpleName() + " must implement "
                    + EditAnswerFragmentListener.class.getSimpleName());
        }
    }

    private void initView(View rootView) {
        ImageView backButton = (ImageView) rootView.findViewById(R.id.edit_answer_back_btn);
        mPowerUpImageView = (ImageView) rootView.findViewById(R.id.edit_answer_powerup_bank_imgView);
        mGamePlayImageView = (ImageView) rootView.findViewById(R.id.edit_answer_help_play_imgView);
        RelativeLayout doublerPowerup = (RelativeLayout) rootView.findViewById(R.id.edit_answer_doubler_Layout);
        RelativeLayout noNegPowerup = (RelativeLayout) rootView.findViewById(R.id.edit_answer_noNeg_Layout);
        RelativeLayout playerPollPowerup = (RelativeLayout) rootView.findViewById(R.id.edit_answer_player_poll_Layout);
        mThirdOptionLayout = (LinearLayout) rootView.findViewById(R.id.edit_answer_third_option_layout);
        mThirdOptionButton = (LinearLayout) rootView.findViewById(R.id.edit_answer_third_option_button);
        mMessageTextView = (TextView) rootView.findViewById(R.id.edit_answer_msg_textView);

        mPowerUpImageView.setOnClickListener(this);
        mGamePlayImageView.setOnClickListener(this);
        mThirdOptionButton.setOnClickListener(this);
        doublerPowerup.setOnClickListener(this);
        noNegPowerup.setOnClickListener(this);
        playerPollPowerup.setOnClickListener(this);
        backButton.setOnClickListener(this);


        /* Card stack */
        mCardStack = (CardStack) rootView.findViewById(R.id.edit_answer_cardStack);
        mCardStack.setContentResource(R.layout.edit_answer_prediction_card_layout);
        mCardStack.setCanSwipe(true);
        mCardStack.setListener(getCardSwipeEventsListener());
    }

    @NonNull
    private EditAnswerPredictionAdapterListener getQuestionsCardAdapterListener() {
        return new EditAnswerPredictionAdapterListener() {
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
        animateTopBottomLayouts();
    }

    private void initMembers() {
        mProgressDialog = new CustomProgressbar(this.getContext());

        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey(Constants.BundleKeys.EDIT_ANSWER_SCREEN_DATA)) {
                mEditAnswerScreenData = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.EDIT_ANSWER_SCREEN_DATA));
            }

            if (mEditAnswerScreenData != null) {
                mUiHelper = new PredictionUiHelper();
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
        if (mEditAnswerScreenData != null && getActivity() != null) {
            showProgress();
            GetQuestionForEditAnswerApiModelImpl question = new GetQuestionForEditAnswerApiModelImpl();

            question.fetchQuestionDetailsToEditAnswers(mEditAnswerScreenData.getMatchId(),
                    mEditAnswerScreenData.getQuestionId(),
                    mEditAnswerScreenData.getRoomId(),
                    getQuestionApiListener());
        } else {
            handleErrorAndFinishActivity();
        }
    }

    private GetQuestionForEditAnswerApiModelImpl.GetQuestionForEditAnswerApiListener getQuestionApiListener() {
        return new GetQuestionForEditAnswerApiModelImpl.GetQuestionForEditAnswerApiListener() {
            @Override
            public void noInternet() {
                hideProgress();
                handleError("", Constants.DataStatus.NO_INTERNET);
            }

            @Override
            public void onEditAnswerSuccessful(QuestionForEditAnswerResponse response) {
                hideProgress();
                onGetQuestionSuccessResponse(response);
            }

            @Override
            public void onApiFailure() {
                hideProgress();
                handleError("", Constants.DataStatus.FROM_SERVER_API_FAILED);
            }

            @Override
            public void onServerSentApi(String errorMsg) {
                hideProgress();
                if (TextUtils.isEmpty(errorMsg)) {
                    errorMsg = Constants.Alerts.SOMETHING_WRONG;
                }
                handleError(errorMsg, -1);
            }
        };
    }

    private void onGetQuestionSuccessResponse(QuestionForEditAnswerResponse questionsResponse) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing() &&
                mCardStack != null && questionsResponse != null) {

            if (questionsResponse.getQuestion() != null) {
                 /*PowerUps*/
                if (questionsResponse.getPowerUp() != null) {
                    mAppliedPowerUp = questionsResponse.getAppliedPowerUps();
                    mCurrentPowerUp = questionsResponse.getPowerUp();
                    updatePowerUpDetails(mCurrentPowerUp);
                }

                 /*Questions*/
                mQuestionsCardAdapter = new EditAnswerPredictionCardAdapter(getContext(),
                        getQuestionList(questionsResponse.getQuestion()),
                        getQuestionsCardAdapterListener());

                mCardStack.setAdapter(mQuestionsCardAdapter);

                /* update ui*/
                showNeitherIfRequired();
                showPowerUpsIfAppliedWhenQuestionLoaded();

            } else {
                Log.d(TAG, "Questions list null/empty");
                handleErrorAndFinishActivity();
            }
        }
    }

    private List<EditAnswerQuestion> getQuestionList(EditAnswerQuestion question) {
        List<EditAnswerQuestion> list = new ArrayList<>();
        list.add(question);
        return list;
    }

    private void initHeading() {
        if (getView() != null && getActivity() != null) {
            mIsFirstTimeEditing = NostragamusDataHandler.getInstance().isEditingAnswerFirstTime();
            if (mIsFirstTimeEditing) {
                mMessageTextView.setText("Add/Remove powerups to your answer. Then swipe & select your option to save new predictions!");
                NostragamusDataHandler.getInstance().setEditingAnswerFirstTime();
            }
        }
    }

    private void animateTopBottomLayouts() {
        if (getView() != null && getActivity() != null) {
            final RelativeLayout topLayout = (RelativeLayout) getView().findViewById(R.id.edit_answer_top_layout);
            final LinearLayout bottomLayout = (LinearLayout) getView().findViewById(R.id.edit_answer_bottom_layout);

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

                                    /* Wait for 1 seconds before first time loading */
                                    if (mIsFirstTimeEditing) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                loadQuestions();
                                            }
                                        }, 1000);
                                    } else {
                                        loadQuestions();
                                    }
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
        }
    }

    private void showNeitherIfRequired() {
        int pos = getTopVisibleCardPosition();
        if (mQuestionsCardAdapter != null && pos < mQuestionsCardAdapter.getCount()) {
            EditAnswerQuestion question = mQuestionsCardAdapter.getItem(pos);

            if (question != null && !TextUtils.isEmpty(question.getQuestionOption3())) {
                makeThirdOptionVisible(true, question.getQuestionOption3(), question.getChosenAnswerId());
            } else {
                makeThirdOptionVisible(false, getString(R.string.prediction_neither_button_text), question.getChosenAnswerId());
            }
        }
    }

    private void showPowerUpsIfApplied() {
        int nextCardPos = getTopVisibleCardPosition();
        if (nextCardPos < mQuestionsCardAdapter.getCount()) {
            EditAnswerQuestion question = mQuestionsCardAdapter.getItem(nextCardPos);

            if (question != null && question.getPowerUp() != null && mCardStack != null && mCardStack.getTopView() != null) {
                View topView = mCardStack.getTopView();
                final RelativeLayout powerUpParent = (RelativeLayout) topView.findViewById(R.id.prediction_card_powerup_layout);
                if (powerUpParent != null) {

                    /* Doubler */
                    if (question.getPowerUp().getDoubler() == 1 && canAddPowerUpCheckUiLayout(powerUpParent, PowerUpEnum.DOUBLER)) {

                        mQuestionsCardAdapter.applyOrRemovePowerUp(PowerUpEnum.DOUBLER, getTopVisibleCardPosition(), true);
                        usePowerUp(true, PowerUpEnum.DOUBLER);

                        View powerUpView = mUiHelper.getPowerUpView(getContext(), PowerUpEnum.DOUBLER);
                        if (powerUpView != null) {
                            setPowerUpRemoveListener(powerUpParent, powerUpView);
                            powerUpParent.addView(powerUpView, powerUpParent.getChildCount(),
                                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                        }
                    }

                    /* No Negative */
                    if (question.getPowerUp().getNoNegative() == 1 && canAddPowerUpCheckUiLayout(powerUpParent, PowerUpEnum.NO_NEGATIVE)) {

                        mQuestionsCardAdapter.applyOrRemovePowerUp(PowerUpEnum.NO_NEGATIVE, getTopVisibleCardPosition(), true);
                        usePowerUp(true, PowerUpEnum.NO_NEGATIVE);

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

    /**
     * When question loaded, show saved details (as per api) like powerups applied (in previous session), etc...
     */
    private void showPowerUpsIfAppliedWhenQuestionLoaded() {
        int nextCardPos = getTopVisibleCardPosition();
        if (nextCardPos < mQuestionsCardAdapter.getCount()) {
            EditAnswerQuestion question = mQuestionsCardAdapter.getItem(nextCardPos);

            if (question != null && question.getPowerUp() != null && mCardStack != null && mCardStack.getTopView() != null) {
                View topView = mCardStack.getTopView();
                final RelativeLayout powerUpParent = (RelativeLayout) topView.findViewById(R.id.prediction_card_powerup_layout);
                if (powerUpParent != null && mAppliedPowerUp != null) {

                    /* Doubler */
                    if (mAppliedPowerUp.isDoubler() && canAddPowerUpCheckUiLayout(powerUpParent, PowerUpEnum.DOUBLER)) {

                        mQuestionsCardAdapter.applyOrRemovePowerUp(PowerUpEnum.DOUBLER, getTopVisibleCardPosition(), true);
                        updatePowerUpDetails(mCurrentPowerUp);

                        View powerUpView = mUiHelper.getPowerUpView(getContext(), PowerUpEnum.DOUBLER);
                        if (powerUpView != null) {
                            setPowerUpRemoveListener(powerUpParent, powerUpView);
                            powerUpParent.addView(powerUpView, powerUpParent.getChildCount(),
                                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                        }
                    }

                    /* No Negative */
                    if (mAppliedPowerUp.isNoNegative() && canAddPowerUpCheckUiLayout(powerUpParent, PowerUpEnum.NO_NEGATIVE)) {

                        mQuestionsCardAdapter.applyOrRemovePowerUp(PowerUpEnum.NO_NEGATIVE, getTopVisibleCardPosition(), true);
                        updatePowerUpDetails(mCurrentPowerUp);

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

    private void makeThirdOptionVisible(boolean shouldVisible, String buttonText, int chosenAnswerId) {
        if (getView() != null && getActivity() != null) {

            TextView thirdOptionTextView = (TextView) getView().findViewById(R.id.edit_answer_third_option_textView);
            thirdOptionTextView.setText(buttonText);
            if (shouldVisible) {
                if (chosenAnswerId == Constants.AnswerIds.NEITHER) {
                    mThirdOptionButton.setBackgroundResource(R.drawable.edit_answer_third_option_chosen_bg);
                    ImageView imgView = (ImageView) getView().findViewById(R.id.edit_answer_third_option_imgView);
                    imgView.setVisibility(View.VISIBLE);
                }

                mThirdOptionLayout.setVisibility(View.VISIBLE);
            } else {
                mThirdOptionLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_answer_back_btn:
                onBackPressed();
                break;

            case R.id.edit_answer_doubler_Layout:
                onDoublerPowerUpClicked();
                break;

            case R.id.edit_answer_noNeg_Layout:
                onNoNegativeClicked();
                break;

            case R.id.edit_answer_player_poll_Layout:
                onPlayerPollClicked();
                break;

            case R.id.edit_answer_third_option_button:
                onThirdOptionClicked();
                break;

            case R.id.edit_answer_powerup_bank_imgView:
                onPowerUpBankClicked();
                break;

            case R.id.edit_answer_help_play_imgView:
                onHelpPlayClicked();
                break;
        }
    }

    public void onPowerUpBankClicked() {
        mUiHelper.hidePowerUpBankAndHelpButtons(mPowerUpImageView, mGamePlayImageView);

        PowerupBankTransferScreenData bankScreenData = new PowerupBankTransferScreenData();
        bankScreenData.setRoomId(mEditAnswerScreenData.getRoomId());
        bankScreenData.setChallengeId(mEditAnswerScreenData.getChallengeId());
        bankScreenData.setSubTitle(mEditAnswerScreenData.getSubTitle());

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

    private void onThirdOptionClicked() {
        if (mCardStack != null) {
            mCardStack.discardTopOnButtonClick(CardDirection.UP);
        }
    }

    private void onPlayerPollClicked() {
        if (isEnoughPowerUpAvailableToApply(PowerUpEnum.PLAYER_POLL)) {
            if (canAddPlayerPollPowerUpCheckUiLayout()) {

                int questionId = getTopVisibleCardQuestionId();
                if (mEditAnswerScreenData != null && questionId >= 0) {

                    showProgress();
                    PredictionPlayersPollDataProvider playersPollDataProvider = new PredictionPlayersPollDataProvider();
                    playersPollDataProvider.getPlayersPoll(questionId, mEditAnswerScreenData.getRoomId(),
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
        if (cardPos >= 0  && mQuestionsCardAdapter != null && cardPos < mQuestionsCardAdapter.getCount()) {
            EditAnswerQuestion question = mQuestionsCardAdapter.getItem(cardPos);
            if (question != null) {
                questionId = question.getQuestionId();
            }
        }

        return questionId;
    }

    /**
     * Players Poll can NOT be removed once applied
     * @param playersPolls
     */
    private void onPlayerPollSuccess(PlayerPollResponse playersPolls) {
        /* NOTE: same logic for PredictionCardAdapter.preFillIfPlayerPollAlreadyApplied() tobe followed */

        if (getView() != null && getActivity() != null && playersPolls != null &&
                playersPolls.getPlayersPollList() != null && !playersPolls.getPlayersPollList().isEmpty()) {

            List<PlayersPoll> playersPollList = playersPolls.getPlayersPollList();

            /* Add response to question so that it can be shown next time if card is shuffled */
            if (mQuestionsCardAdapter != null) {
                EditAnswerQuestion question = mQuestionsCardAdapter.getItem(getTopVisibleCardPosition());
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
                    EditAnswerQuestion question = mQuestionsCardAdapter.getItem(pos);
                    if (question != null) {
                        PlayerPollHelper.setMinorityAnswerIdForEditAnswer(question, leftPollAnswer, rightPollAnswer);
                    }
                }
            }
        }  else {
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

            onPowerUpModified();
        }
    }

    /**
     * Handle action if powerup edited...
     */
    private void onPowerUpModified() {
        if (isPowerupModified()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Animation alpha = new AlphaAnimation(0f, 1f);
                    alpha.setDuration(200);
                    mMessageTextView.setText("You seem to have edited powerups. Now, swipe your choice to save the prediction!");
                    mMessageTextView.startAnimation(alpha);
                }
            }, 500 /* Once powerup anim over, show this text */);
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

                            onPowerUpModified();
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
                    EditAnswerQuestion question = mQuestionsCardAdapter.getItem(getTopVisibleCardPosition());

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
                            showNeitherIfRequired();
                            showPowerUpsIfApplied();
                        }
                        break;

                    case CardDirection.DOWN:
                        if (mQuestionsCardAdapter != null && mCardStack != null) {
                            EditAnswerQuestion question = mQuestionsCardAdapter.getItem(index);
                            mCardStack.shuffle(question);
                            showNeitherIfRequired();
                            showPowerUpsIfApplied();
                        }
                        break;
                }

                if ((index + 1) == (mCardStack.getAdapter().getCount() - 1)) {
                    // Hide next button , not applicable here
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
        if (mQuestionsCardAdapter != null && mCardStack != null) {
            EditAnswerQuestion question = mQuestionsCardAdapter.getItem(index);
            if (question != null && !TextUtils.isEmpty(question.getQuestionOption3())) {
                isThirdOption = true;
            }
        }
        return isThirdOption;
    }

    private void showProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.show();
        }
    }

    private void hideProgress() {
        if (getActivity() != null){
            if (!getActivity().isFinishing() && mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }

    private void saveAnswer(int cardIndexPos, final int answerId, final boolean isMatchCompleted) {
        if (mQuestionsCardAdapter != null && cardIndexPos < mQuestionsCardAdapter.getCount() && mEditAnswerScreenData != null) {
            final EditAnswerQuestion question = mQuestionsCardAdapter.getItem(cardIndexPos);
            if (question != null) {
                showProgress();

                SaveEditAnswerApiModelImpl saveEditAnswerApiModel = new SaveEditAnswerApiModelImpl();
                saveEditAnswerApiModel.callChangeAnswerApi(
                        question.getMatchId(),
                        question.getQuestionId(),
                        answerId,
                        mEditAnswerScreenData.getRoomId(),
                        question.getPowerUp(),
                        TimeUtils.getCurrentTime(Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE, Constants.DateFormats.GMT),
                        question.isMinorityAnswer(answerId),
                        new SaveEditAnswerApiModelImpl.EditAnswerApiListener() {
                            @Override
                            public void noInternet() {
                                hideProgress();
                                handleError("", Constants.DataStatus.NO_INTERNET);
                            }

                            @Override
                            public void onEditAnswerSuccessful(SaveEditAnswerResponse response) {
                                hideProgress();
                                onAnswerSavedSuccessfully(response);
                            }

                            @Override
                            public void onApiFailure() {
                                hideProgress();

                                /* Undo card */
                                if (mCardStack != null) {
                                    mCardStack.undo();
                                }
                                showPowerUpsIfApplied();
                                handleError("Could not save your prediction, try again!", -1);
                            }

                            @Override
                            public void onServerSentApi(String errorMsg) {
                                hideProgress();

                                /* Undo card */
                                if (mCardStack != null) {
                                    mCardStack.undo();
                                }
                                showPowerUpsIfApplied();

                                if (TextUtils.isEmpty(errorMsg)) {
                                    errorMsg = "Could not save your prediction, try again!";
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

    private void onAnswerSavedSuccessfully(SaveEditAnswerResponse answerResponse) {
        if (answerResponse != null && answerResponse.getEditAnswerDataResponse() != null
                && answerResponse.getEditAnswerDataResponse().getPowerUp() != null) {
            Log.d(TAG, "Answer Edited successfully");

            if (mFragmentListener != null) {
                mFragmentListener.onAnswerEditSuccessful();
            }
        }
    }

    /**
     * If msg is passed then msg will be shown ;
     * else status should be passed
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
     * @param isPowerUpApplied - true means applied (reduce), false means removed (increase)
     * @param powerUpEnum - which power-up used
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
     * @param powerUp - powerup to update
     */
    private void updatePowerUpDetails(PowerUp powerUp) {

        if (getView() != null && getActivity() != null && !getActivity().isFinishing() && powerUp != null) {
            View view = getView();
            TextView doublerTextView = (TextView) view.findViewById(R.id.edit_answer_doubler_counter_textView);
            TextView noNegTextView = (TextView) view.findViewById(R.id.edit_answer_noNeg_counter_textView);
            TextView playerPollTextView = (TextView) view.findViewById(R.id.edit_answer_player_poll_counter_textView);

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

            EditAnswerQuestion question = mQuestionsCardAdapter.getItem(getTopVisibleCardPosition());
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

                case POWERUP_EDITED_ALERT_POPUP_REQUEST_CODE:
                    if (resultCode == Activity.RESULT_OK) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mFragmentListener != null) {
                                    mFragmentListener.onBackClicked();
                                }
                            }
                        }, 500);
                    }
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

    /**
     * Checks that powerup is modified or not
     * @return
     */
    private boolean isPowerupModified() {
        boolean isModified = false;

        EditAnswerQuestion question = mQuestionsCardAdapter.getItem(0); // as only one card always
        if (question != null) {
            if ((mAppliedPowerUp.isDoubler() && question.getPowerUp().getDoubler() < 1) ||
                    (!mAppliedPowerUp.isDoubler() && question.getPowerUp().getDoubler() > 0) ||
                    (mAppliedPowerUp.isNoNegative() && question.getPowerUp().getNoNegative() < 1) ||
                    (!mAppliedPowerUp.isNoNegative() && question.getPowerUp().getNoNegative() > 0)) {

                isModified = true;

            }
        }
        return isModified;
    }

    /**
     * check whether powerup is edited, if so, show dialog
     */
    public void onBackPressed() {
        if (mAppliedPowerUp != null && mQuestionsCardAdapter != null) {
            if (isPowerupModified()) {
                // show dialog
                showPowerupEditedDialogBeforeCancel();

            } else {
                if (mFragmentListener != null) {
                    mFragmentListener.onBackClicked();
                }
            }
        } else {
            if (mFragmentListener != null) {
                mFragmentListener.onBackClicked();
            }
        }

    }

    private void showPowerupEditedDialogBeforeCancel() {
//        AlertsHelper.showAlert(getContext(), "Msg", "Powrup edited", null);

        Intent intent = new Intent(getContext(), EditAnswerPowerUpModifiedPopupActivity.class);
        startActivityForResult(intent, POWERUP_EDITED_ALERT_POPUP_REQUEST_CODE);
    }


}
