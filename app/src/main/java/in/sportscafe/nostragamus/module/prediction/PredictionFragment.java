package in.sportscafe.nostragamus.module.prediction;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.sportscafe.nostracardstack.cardstack.CardStack;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.prediction.adapter.PredictionQuestionAdapterListener;
import in.sportscafe.nostragamus.module.prediction.adapter.PredictionQuestionsCardAdapter;
import in.sportscafe.nostragamus.module.prediction.dataProvider.PredictionPlayersPollDataProvider;
import in.sportscafe.nostragamus.module.prediction.dataProvider.PredictionQuestionsDataProvider;
import in.sportscafe.nostragamus.module.prediction.dto.PlayerPollResponse;
import in.sportscafe.nostragamus.module.prediction.dto.PlayersPoll;
import in.sportscafe.nostragamus.module.prediction.dto.PowerupEnum;
import in.sportscafe.nostragamus.module.prediction.dto.PredictionAllQuestionResponse;
import in.sportscafe.nostragamus.module.prediction.dto.PredictionQuestion;
import in.sportscafe.nostragamus.utils.AlertsHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class PredictionFragment extends NostraBaseFragment implements View.OnClickListener {

    private static final String TAG = PredictionFragment.class.getSimpleName();
    private MediaPlayer mp;
    private PredictionFragmentListener mFragmentListener;
    private CardStack mCardStack;
    private PredictionQuestionsCardAdapter mQuestionsCardAdapter;
    private LinearLayout mThirdOptionLayout;
    private PredictionUiHelper mUiHelper;

    private int mRoomId = -1; // RoomId mandatory for match along with matchId

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
        RelativeLayout doublerPowerup = (RelativeLayout) rootView.findViewById(R.id.prediction_doubler_Layout);
        RelativeLayout noNegPowerup = (RelativeLayout) rootView.findViewById(R.id.prediction_noNeg_Layout);
        RelativeLayout playerPollPowerup = (RelativeLayout) rootView.findViewById(R.id.prediction_player_poll_Layout);
        Button thirdOptionButton = (Button) rootView.findViewById(R.id.prediction_third_option_button);
        mThirdOptionLayout = (LinearLayout) rootView.findViewById(R.id.prediction_third_option_layout);

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

        mp = MediaPlayer.create(getContext(), R.raw.beep6);
    }

    @NonNull
    private PredictionQuestionAdapterListener getQuestionsCardAdapterListener() {
        return new PredictionQuestionAdapterListener() {
            @Override
            public void onLeftOptionClicked(int pos) {
                if (mCardStack != null) {
                    mCardStack.discardTopOnButtonClick(1);   // 1: left, 2: right, 3:top, 4:bottom
                }
            }

            @Override
            public void onRightOptionClicked(int pos) {
                if (mCardStack != null) {
                    mCardStack.discardTopOnButtonClick(2);
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
    }

    private void initMembers() {
        mUiHelper = new PredictionUiHelper();
    }

    private void loadQuestions() {
        PredictionQuestionsDataProvider dataProvider = new PredictionQuestionsDataProvider();
        dataProvider.getAllQuestions(10, 1, getAllQuestionsApiListener());
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
// TODO handle error
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

                showThirdLayoutIfRequired();
            } else {
                // TODO: NO question list
            }
        }
    }

    private void initHeading() {
        if (getView() != null && getActivity() != null) {
            TextView headingTextView = (TextView) getView().findViewById(R.id.prediction_heading_textView);
            TextView subHeadingTextView = (TextView) getView().findViewById(R.id.prediction_sub_heading_textView);

            headingTextView.setText("Test heading challenge now");
            subHeadingTextView.setText("sub heading of contest challenge");
        }
    }

    private void animateTopBottomLayouts() {
        if (getView() != null && getActivity() != null) {
            final LinearLayout topLayout = (LinearLayout) getView().findViewById(R.id.prediction_top_layout);
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

    @NonNull
    private CardStack.CardEventListener getCardEventsListener() {
        return new CardStack.CardEventListener() {
            @Override
            public boolean swipeEnd(int direction, float distance) {
                boolean endSwipe = false;
                if (distance > 300) {
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
                if (mp != null && !mp.isPlaying()) {
                    mp.start();
                }

                switch (direction) {
                    case 0:
//                        Toast.makeText(MainActivity.this, "direction 0", Toast.LENGTH_SHORT).show();
                        Log.d("Card", "0");
                        break;

                    case 1:
//                        Toast.makeText(MainActivity.this, "direction 1", Toast.LENGTH_SHORT).show();
                        Log.d("Card", "1");
                        break;

                    case 2:
//                        Toast.makeText(MainActivity.this, "direction 2", Toast.LENGTH_SHORT).show();
                        Log.d("Card", "2");
                        break;

                    case 3:
//                        Toast.makeText(MainActivity.this, "direction 4", Toast.LENGTH_SHORT).show();
                        Log.d("Card", "3");
                        break;
                }

                showThirdLayoutIfRequired();
            }

            @Override
            public void topCardTapped() {
                Log.d("Card", "Top card tapped");
            }
        };
    }

    private void showThirdLayoutIfRequired() {
        if (mCardStack != null && mQuestionsCardAdapter != null) {
            int pos = mCardStack.getCurrIndex(); //(int) mCardStack.getTopView().getTag();
            if (pos >= 0 && pos < mQuestionsCardAdapter.getCount()) {
                PredictionQuestion question = mQuestionsCardAdapter.getItem(pos);
                if (question != null && !TextUtils.isEmpty(question.getQuestionOption3())) {
                    makeThirdOptionVisible(true);
                } else {
                    makeThirdOptionVisible(false);
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
        }
    }

    private void onThirdOptionClicked() {
        if (mCardStack != null) {
            mCardStack.discardTopOnButtonClick(3);
        }
    }

    private void onPlayerPollClicked() {
        int questionId = 1; // TODO: change

        PredictionPlayersPollDataProvider playersPollDataProvider = new PredictionPlayersPollDataProvider();
        playersPollDataProvider.getPlayersPoll(questionId, mRoomId, new PredictionPlayersPollDataProvider.PlayersPollDataProviderListener() {
            @Override
            public void onData(int status, @Nullable PlayerPollResponse playersPolls) {
                onPlayerPollSuccess(playersPolls);

            }

            @Override
            public void onError(int status) {
                AlertsHelper.showAlert(getContext(), "Server Error", Constants.Alerts.SOMETHING_WRONG, null);
            }
        });
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

            int pollPercentForOption1 = Integer.parseInt(playersPollList.get(0).getAnswerPercentage());
            int pollPercentForOption2 = Integer.parseInt(playersPollList.get(1).getAnswerPercentage());

            if (mCardStack != null && mCardStack.getTopView() != null && mQuestionsCardAdapter != null) {
                View view = mCardStack.getTopView();
                TextView playerPollOption1TextView = (TextView) view.findViewById(R.id.prediction_card_player_poll_1_textView);
                TextView playerPollOption2TextView = (TextView) view.findViewById(R.id.prediction_card_player_poll_2_textView);

                if (pollPercentForOption1 > 0 && pollPercentForOption2 > 0) {
                    playerPollOption1TextView.setText(pollPercentForOption1);
                    playerPollOption2TextView.setText(pollPercentForOption2);

                    playerPollOption1TextView.setVisibility(View.VISIBLE);
                    playerPollOption2TextView.setVisibility(View.VISIBLE);

                    mQuestionsCardAdapter.applyOrRemovePowerUp(PowerupEnum.PLAYER_POLL, getTopVisibleCardPosition(), true);
                }
            }
        }  else {
            AlertsHelper.showAlert(getContext(), "Server Error", Constants.Alerts.SOMETHING_WRONG, null);
        }
    }

    private void onNoNegativeClicked() {
        if (mCardStack != null && mCardStack.getTopView() != null && mUiHelper != null) {
            View topView = mCardStack.getTopView();
            final RelativeLayout powerUpParent = (RelativeLayout) topView.findViewById(R.id.prediction_card_powerup_layout);
            if (powerUpParent != null) {
                if (canAddPowerUp(powerUpParent, PowerupEnum.NO_NEGATIVE)) {
                    // Add Powerup on UI

                    final View powerUpView = mUiHelper.getPowerUpView(getContext(), PowerupEnum.NO_NEGATIVE);
                    if (powerUpView != null) {
                        setPowerUpRemoveListener(powerUpParent, powerUpView);

                        powerUpParent.addView(powerUpView, powerUpParent.getChildCount(),
                                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        mUiHelper.showPowerUpAnimation(powerUpView, powerUpParent);

                        mQuestionsCardAdapter.applyOrRemovePowerUp(PowerupEnum.NO_NEGATIVE, getTopVisibleCardPosition(), true);
                    }
                }
            }
        }
    }

    private void onDoublerPowerUpClicked() {
        if (mCardStack != null && mCardStack.getTopView() != null && mUiHelper != null) {
            View topView = mCardStack.getTopView();
            final RelativeLayout powerUpParent = (RelativeLayout) topView.findViewById(R.id.prediction_card_powerup_layout);
            if (powerUpParent != null) {
                if (canAddPowerUp(powerUpParent, PowerupEnum.DOUBLER)) {
                    // Add Powerup on UI

                    final View powerUpView = mUiHelper.getPowerUpView(getContext(), PowerupEnum.DOUBLER);
                    if (powerUpView != null) {
                        setPowerUpRemoveListener(powerUpParent, powerUpView);
                        powerUpParent.addView(powerUpView, powerUpParent.getChildCount(),
                                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        mUiHelper.showPowerUpAnimation(powerUpView, powerUpParent);

                        mQuestionsCardAdapter.applyOrRemovePowerUp(PowerupEnum.DOUBLER, getTopVisibleCardPosition(), true);
                    }
                }
            }
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
                        PowerupEnum powerupEnum = (PowerupEnum) powerUpView.getTag();
                        powerUpParent.removeView(powerUpView);
                        mUiHelper.animateOtherPowerupWhenAnyOneRemoved(powerUpParent);

                        switch (powerupEnum) {
                            case DOUBLER:
                                mQuestionsCardAdapter.applyOrRemovePowerUp(PowerupEnum.DOUBLER, getTopVisibleCardPosition(), false);
                                break;

                            case NO_NEGATIVE:
                                mQuestionsCardAdapter.applyOrRemovePowerUp(PowerupEnum.NO_NEGATIVE, getTopVisibleCardPosition(), false);
                                break;

                            case PLAYER_POLL:
                                // PLAYER Poll can NEVER be removed once applied
                                break;
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        });
    }

    private boolean canAddPowerUp(RelativeLayout powerUpParent, PowerupEnum powerupEnum) {
        boolean canAdd = true;
        if (powerUpParent.getChildCount() > 0) {
            for (int temp = 0; temp < powerUpParent.getChildCount(); temp++) {
                PowerupEnum powerupView = (PowerupEnum) powerUpParent.getChildAt(temp).getTag();
                if (powerupView == powerupEnum) {
                    canAdd = false;
                }
            }
        }
        return canAdd;
    }
}
