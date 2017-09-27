package in.sportscafe.nostragamus.module.prediction.playScreen;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sportscafe.nostracardstack.cardstack.CardDirection;
import com.sportscafe.nostracardstack.cardstack.CardStack;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.play.myresults.dto.MyResultScreenData;
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
import in.sportscafe.nostragamus.module.prediction.playScreen.helper.PredictionUiHelper;
import in.sportscafe.nostragamus.module.prediction.powerupBank.PowerupBankTransferToPlayActivity;
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
    private PowerUp mPowerUp;
    private PlayScreenDataDto mPlayScreenData;
    private ProgressDialog mProgressDialog;
    private TextView mCounterTextView;
    private TextView mNextTextView;

    public PredictionFragment() {}

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
        ImageView powerUpBankImgView = (ImageView) rootView.findViewById(R.id.prediction_powerup_bank_imgView);
        ImageView helpPlayImgView = (ImageView) rootView.findViewById(R.id.prediction_help_play_imgView);
        RelativeLayout doublerPowerup = (RelativeLayout) rootView.findViewById(R.id.prediction_doubler_Layout);
        RelativeLayout noNegPowerup = (RelativeLayout) rootView.findViewById(R.id.prediction_noNeg_Layout);
        RelativeLayout playerPollPowerup = (RelativeLayout) rootView.findViewById(R.id.prediction_player_poll_Layout);
        Button thirdOptionButton = (Button) rootView.findViewById(R.id.prediction_third_option_button);
        mThirdOptionLayout = (LinearLayout) rootView.findViewById(R.id.prediction_third_option_layout);
        mCounterTextView = (TextView) rootView.findViewById(R.id.prediction_question_counter_textView);
        mNextTextView = (TextView) rootView.findViewById(R.id.prediction_question_next_textView);

        mNextTextView.setOnClickListener(this);
        powerUpBankImgView.setOnClickListener(this);
        helpPlayImgView.setOnClickListener(this);
        doublerPowerup.setOnClickListener(this);
        noNegPowerup.setOnClickListener(this);
        playerPollPowerup.setOnClickListener(this);
        backButton.setOnClickListener(this);
        thirdOptionButton.setOnClickListener(this);

        /* Card stack */
        mCardStack = (CardStack) rootView.findViewById(R.id.prediction_cardStack);
        mCardStack.setContentResource(R.layout.prediction_card_layout);
        mCardStack.setCanSwipe(true);
        mCardStack.setListener(getCardEventsListener());
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
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initMembers();
        initHeading();
        animateTopBottomLayouts();
        loadQuestions();
        updatePowerUpDetails(mPowerUp);
    }

    private void initMembers() {
        mProgressDialog = new ProgressDialog(this.getContext());

        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey(Constants.BundleKeys.PLAY_SCREEN_DATA)) {
                mPlayScreenData = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.PLAY_SCREEN_DATA));
            }

            if (mPlayScreenData != null) {
                mUiHelper = new PredictionUiHelper();
                mSavePredictionAnswerProvider = new SavePredictionAnswerProvider();
                mPowerUp = mPlayScreenData.getPowerUp();
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
                onAllQuestionApiSuccessResponse(questionsResponse);
            }

            @Override
            public void onError(int status) {
                handleError(status);
            }
        };
    }

    private void onAllQuestionApiSuccessResponse(PredictionAllQuestionResponse questionsResponse) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing() &&
                mCardStack != null && questionsResponse != null) {

            if (questionsResponse.getQuestions() != null && !questionsResponse.getQuestions().isEmpty()) {
                mQuestionsCardAdapter = new PredictionQuestionsCardAdapter(getContext(),
                        questionsResponse.getQuestions(),
                        getQuestionsCardAdapterListener());

                mCardStack.setAdapter(mQuestionsCardAdapter);

                showQuestionsCounter();
                showNeitherIfRequired();
            } else {
                Log.d(TAG, "Questions list null/empty");
                handleErrorAndFinishActivity();
            }
        }
    }

    private void showQuestionsCounter() {
        if (getView() != null && mQuestionsCardAdapter != null) {
            String questionNumCounter = getTopVisibleCardPosition() + "/" + mQuestionsCardAdapter.getCount();
            mCounterTextView.setText(questionNumCounter);

            RelativeLayout relativeLayout = (RelativeLayout) getView().findViewById(R.id.prediction_questions_info_parent);
            relativeLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initHeading() {
        if (getView() != null && getActivity() != null) {
            TextView headingParty1TextView = (TextView) getView().findViewById(R.id.prediction_heading_party1_textView);
            TextView headingParty2TextView = (TextView) getView().findViewById(R.id.prediction_heading_party2_textView);
            TextView subHeadingTextView = (TextView) getView().findViewById(R.id.prediction_sub_heading_textView);

            if (mPlayScreenData != null) {
                String party1 = mPlayScreenData.getMatchPartyTitle1();
                String party2 = mPlayScreenData.getMatchPartyTitle2();

                if (!TextUtils.isEmpty(party1) && !TextUtils.isEmpty(party2)) {
                    headingParty1TextView.setText(party1);
                    headingParty2TextView.setText(party2);
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
                    topLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.prediction_top_view_anim));
                    bottomLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.prediction_bottom_view_anim));
                    topLayout.setVisibility(View.VISIBLE);
                    bottomLayout.setVisibility(View.VISIBLE);
                }
            }, 500);
        }
    }

    private void showNeitherIfRequired() {
        int pos = getTopVisibleCardPosition();
        if (mQuestionsCardAdapter != null && pos < mQuestionsCardAdapter.getCount()) {
            PredictionQuestion question = mQuestionsCardAdapter.getItem(pos);
            if (question != null && !TextUtils.isEmpty(question.getQuestionOption3())) {
                makeThirdOptionVisible(true);
            } else {
                makeThirdOptionVisible(false);
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

    private void makeThirdOptionVisible(boolean shouldVisible) {
        if (getView() != null && getActivity() != null) {
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

            case R.id.prediction_question_next_textView:
                onNextClicked();
                break;

            case R.id.prediction_powerup_bank_imgView:
                onPowerUpBankClicked();
                break;

            case R.id.prediction_help_play_imgView:
                onHelpPlayClicked();
                break;
        }
    }

    public void onPowerUpBankClicked() {
        Intent intent = new Intent(getActivity(), PowerupBankTransferToPlayActivity.class);
        if (getArguments() != null) {
            intent.putExtras(getArguments());
        }
        getActivity().startActivityForResult(intent, POWER_UP_BANK_ACTIVITY_REQUEST_CODE);
    }

    public void onHelpPlayClicked() {
        Intent intent = new Intent(getActivity(), GamePlayHelpActivity.class);
        getActivity().startActivityForResult(intent, GAME_PLAY_HELP_ACTIVITY);
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
        if (canApplyPowerUp(PowerUpEnum.PLAYER_POLL)) {

            int questionId = getTopVisibleCardQuestionId();
            if (mPlayScreenData != null && questionId >= 0) {

                showProgress();
                PredictionPlayersPollDataProvider playersPollDataProvider = new PredictionPlayersPollDataProvider();
                playersPollDataProvider.getPlayersPoll(questionId, mPlayScreenData.getRoomId(), new PredictionPlayersPollDataProvider.PlayersPollDataProviderListener() {
                    @Override
                    public void onData(int status, @Nullable PlayerPollResponse playersPolls) {
                        hideProgress();
                        onPlayerPollSuccess(playersPolls);
                    }

                    @Override
                    public void onError(int status) {
                        hideProgress();
                        handleError(status);
                    }
                });
            } else {
                Log.e(TAG, "QuestionId / roomId can not be found for players-poll info");
                handleError(-1);
            }
        } else {
            showAlertAsNoPowerUp();
        }
    }

    private void showAlertAsNoPowerUp() {
        AlertsHelper.showYesNoAlert(getContext(),
                "No Powerup!", "Do you want to add more?",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onPowerUpBankClicked();
            }
        }, null);
    }

    private boolean canApplyPowerUp(PowerUpEnum powerUpEnum) {
        boolean canApply = false;

        if (mPowerUp != null) {
            switch (powerUpEnum) {
                case DOUBLER:
                    if (mPowerUp.getPlayerPoll() > 0) {
                        canApply = true;
                    }
                    break;

                case NO_NEGATIVE:
                    if (mPowerUp.getNoNegative() > 0) {
                        canApply = true;
                    }
                    break;

                case PLAYER_POLL:
                    if (mPowerUp.getPlayerPoll() > 0) {
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
            PredictionQuestion question = mQuestionsCardAdapter.getItem(cardPos);
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
        if (getView() != null && getActivity() != null && playersPolls != null &&
                playersPolls.getPlayersPollList() != null &&
                playersPolls.getPlayersPollList().size() == 2) {    // for both the options

            List<PlayersPoll> playersPollList = playersPolls.getPlayersPollList();

            String poll1Str = playersPollList.get(0).getAnswerPercentage();
            String poll2Str = playersPollList.get(1).getAnswerPercentage();
            int pollPercentForOption1 = Integer.parseInt(poll1Str.replaceAll("%",""));
            int pollPercentForOption2 = Integer.parseInt(poll2Str.replaceAll("%", ""));

            if (mCardStack != null && mCardStack.getTopView() != null && mQuestionsCardAdapter != null) {
                View view = mCardStack.getTopView();
                TextView playerPollOption1TextView = (TextView) view.findViewById(R.id.prediction_card_player_poll_1_textView);
                TextView playerPollOption2TextView = (TextView) view.findViewById(R.id.prediction_card_player_poll_2_textView);

                if (pollPercentForOption1 > 0 && pollPercentForOption2 > 0) {
                    playerPollOption1TextView.setText(playersPollList.get(0).getAnswerPercentage());
                    playerPollOption2TextView.setText(playersPollList.get(1).getAnswerPercentage());

                    playerPollOption1TextView.setVisibility(View.VISIBLE);
                    playerPollOption2TextView.setVisibility(View.VISIBLE);

                    mQuestionsCardAdapter.applyOrRemovePowerUp(PowerUpEnum.PLAYER_POLL, getTopVisibleCardPosition(), true);
                    usePowerUp(true, PowerUpEnum.PLAYER_POLL);

                    /* Set Minority Answer */
                    int pos = getTopVisibleCardPosition();
                    if (mQuestionsCardAdapter != null && pos < mQuestionsCardAdapter.getCount()) {
                        PredictionQuestion question = mQuestionsCardAdapter.getItem(pos);
                        if (question != null) {
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
        }  else {
            handleError(-1);
        }
    }

    private void onNoNegativeClicked() {
        if (canApplyPowerUp(PowerUpEnum.NO_NEGATIVE)) {

            if (mCardStack != null && mCardStack.getTopView() != null && mUiHelper != null) {
                View topView = mCardStack.getTopView();
                final RelativeLayout powerUpParent = (RelativeLayout) topView.findViewById(R.id.prediction_card_powerup_layout);
                if (powerUpParent != null) {
                    if (canAddPowerUp(powerUpParent, PowerUpEnum.NO_NEGATIVE)) {
                        applyPowerUp(powerUpParent, PowerUpEnum.NO_NEGATIVE, powerUpParent.getChildCount());
                    }
                }
            }
        } else {
            showAlertAsNoPowerUp();
        }
    }

    private void onDoublerPowerUpClicked() {
        if (canApplyPowerUp(PowerUpEnum.DOUBLER)) {

            if (mCardStack != null && mCardStack.getTopView() != null && mUiHelper != null) {
                View topView = mCardStack.getTopView();
                final RelativeLayout powerUpParent = (RelativeLayout) topView.findViewById(R.id.prediction_card_powerup_layout);
                if (powerUpParent != null) {
                    if (canAddPowerUp(powerUpParent, PowerUpEnum.DOUBLER)) {
                        applyPowerUp(powerUpParent, PowerUpEnum.DOUBLER, powerUpParent.getChildCount());
                    }
                }
            }
        } else {
            showAlertAsNoPowerUp();
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

    private boolean canAddPowerUp(RelativeLayout powerUpParent, PowerUpEnum powerUpEnum) {
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

    /**
     * Performs action when card is swiped out
     * @return
     */
    @NonNull
    public CardStack.CardEventListener getCardEventsListener() {
        return new CardStack.CardEventListener() {
            @Override
            public boolean swipeEnd(int direction, float distance) {
                boolean endSwipe = false;
                if (distance > CARD_SWIPE_DISTANCE_TO_END_SWIPE) {
                    endSwipe = true;
                }
                return endSwipe;
            }

            @Override
            public boolean swipeStart(int i, float v) {
                return true;
            }

            @Override
            public boolean swipeContinue(int i, float v, float v1) {
                return true;
            }

            @Override
            public void discarded(int index, int direction) {

                int position = index - 1;   // Index position is not 0 based

                boolean isMatchCompleted = false;
                if (position == (mQuestionsCardAdapter.getCount() - 1)) {
                    isMatchCompleted = true;
                }

                switch (direction) {
                    case CardDirection.LEFT:
                        saveAnswer(position, Constants.AnswerIds.LEFT, isMatchCompleted);
                        break;

                    case CardDirection.RIGHT:
                        saveAnswer(position, Constants.AnswerIds.RIGHT, isMatchCompleted);
                        break;

                    case CardDirection.UP:
                        saveAnswer(position, Constants.AnswerIds.NEITHER, isMatchCompleted);
                        break;

                    case CardDirection.DOWN:
                        if (mQuestionsCardAdapter != null && mCardStack != null) {
                            PredictionQuestion question = mQuestionsCardAdapter.getItem(index);
                            mCardStack.shuffle(question);
                        }
                        break;
                }

                if (position+1 == mQuestionsCardAdapter.getCount()-1) {
                    hideNextButton();
                }
            }

            @Override
            public void topCardTapped() {
                Log.d("Card", "Top card tapped");
            }
        };
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
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * On success full completion of choice for all the answers
     */
    private void onMatchCompleted() {
        Log.d(TAG, "Match Completed");
        MyResultScreenData data = new MyResultScreenData();
        if (mPlayScreenData != null) {
            data.setRoomId(mPlayScreenData.getRoomId());
            data.setChallengeId(mPlayScreenData.getChallengeId());
            data.setMatchId(mPlayScreenData.getMatchId());
        }

        Bundle args = getArguments();
        if (args == null) {
            args = new Bundle();
        }
        args.putParcelable(Constants.BundleKeys.MY_RESULT_SCREEN_DATA, Parcels.wrap(data));

        if (mFragmentListener != null) {
            mFragmentListener.onMatchCompleted(args);
        }
    }

    private void saveAnswer(int cardIndexPos, final int answerId, final boolean isMatchCompleted) {
        if (mSavePredictionAnswerProvider == null) {
            mSavePredictionAnswerProvider = new SavePredictionAnswerProvider();
        }

        if (mQuestionsCardAdapter != null && cardIndexPos < mQuestionsCardAdapter.getCount() && mPlayScreenData != null) {
            PredictionQuestion question = mQuestionsCardAdapter.getItem(cardIndexPos);
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
                                onAnswerSavedSuccessfully(isMatchCompleted, answerResponse);
                            }

                            @Override
                            public void onError(int status) {
                                hideProgress();

                                AlertsHelper.showAlert(getContext(), "Error", "Could not save your prediction, try again!",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                /* Undo drag as answer is failed to save */
                                                if (mCardStack != null) {
                                                    mCardStack.undo();
                                                }
                                            }
                                        });
                            }
                        });
            }
        } else {
            Log.e(TAG, "Can not save answer without required info");
            handleError(-1);
        }
    }

    private void onAnswerSavedSuccessfully(boolean isMatchCompleted, AnswerResponse answerResponse) {
        if (answerResponse != null && answerResponse.getAnswerResponseData() != null && mQuestionsCardAdapter != null) {
            /* Remove Question from Adapter-list */
//            mQuestionsCardAdapter.onSuccessfulAnswer(mQuestionsCardAdapter.getItem(getTopVisibleCardPosition()));

            if (isMatchCompleted) {
                /* On success response for the last question only allows to consider match as completed */
                onMatchCompleted();

            } else {
                showQuestionsCounter();
                showNeitherIfRequired();

                if (answerResponse.getAnswerResponseData().getPowerUp() != null) {
                    mPowerUp = answerResponse.getAnswerResponseData().getPowerUp();
                    updatePowerUpDetails(mPowerUp);
                }
            }
        }
    }

    private void handleError(int status) {
        switch (status) {
            case Constants.DataStatus.NO_INTERNET:
                AlertsHelper.showAlert(getContext(), "No Internet", "Please tern ON internet to continue", null);
                break;

            default:
                AlertsHelper.showAlert(getContext(), "Error", Constants.Alerts.SOMETHING_WRONG, null);
                break;
        }
    }

    /**
     * Handles Power-up related counters & ui
     * @param isPowerUpApplied - true means applied (reduce), false means removed (increase)
     * @param powerUpEnum - which power-up used
     */
    private void usePowerUp(boolean isPowerUpApplied, PowerUpEnum powerUpEnum) {
        if (mPowerUp != null) {
            int counter = 0;
            switch (powerUpEnum) {
                case DOUBLER:
                    counter = isPowerUpApplied ? mPowerUp.getDoubler() - 1 : mPowerUp.getDoubler() + 1;
                    if (counter >= 0) {
                        mPowerUp.setDoubler(counter);
                    }
                    updatePowerUpDetails(mPowerUp);
                    break;

                case NO_NEGATIVE:
                    counter = isPowerUpApplied ? mPowerUp.getNoNegative() - 1 : mPowerUp.getNoNegative() + 1;
                    if (counter >= 0) {
                        mPowerUp.setNoNegative(counter);
                    }
                    updatePowerUpDetails(mPowerUp);
                    break;

                case PLAYER_POLL:
                    if (isPowerUpApplied) {
                        counter = mPowerUp.getPlayerPoll() - 1;
                    } else {
                        // PlayerPoll can  NOT be removed
                    }
                    if (counter >= 0) {
                        mPowerUp.setPlayerPoll(counter);
                    }
                    updatePowerUpDetails(mPowerUp);
                    break;
            }
        }
    }

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

            updatePointsOnCard();
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
                negativeTextView.setText("- " + String.valueOf(question.getNegativePoints()) + " PTS");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (!getActivity().isFinishing()) {
            switch (requestCode) {
                case POWER_UP_BANK_ACTIVITY_REQUEST_CODE:
                    // show icons
                    updatePowerUpDetails(mPowerUp);
                    break;


                case GAME_PLAY_HELP_ACTIVITY:
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
                PowerUp powerUp = Parcels.unwrap(intent.getExtras().getParcelable(Constants.BundleKeys.POWERUPS));
                if (powerUp != null) {
                    mPowerUp = powerUp;
                    updatePowerUpDetails(mPowerUp);
                }
            }
        }
    };
}
