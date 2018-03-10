package in.sportscafe.nostragamus.module.navigation.help.dummygame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AnalyticsActions;
import in.sportscafe.nostragamus.Constants.ScreenNames;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.prediction.playScreen.PredictionActivity;
import in.sportscafe.nostragamus.module.resultspeek.dto.Question;
import in.sportscafe.nostragamus.webservice.MyWebService;


public class DummyGameActivity extends NostragamusActivity implements DGPlayFragment.OnDGPlayActionListener,
        DGTextFragment.OnDGTextActionListener, DGPowerUpInfoFragment.DGPowerupInfoFragmentListener,
        DGPowerupTriedFragment.DgPowerUpTriedFragmentListener{

    public static final int DELAY_TO_PERFORM_BACK_PRESS = 1000;

    interface InstructionType {
        String TEXT = "text";
        String QUESTION = "question";
        String POWERUP = "powerup";
        String ANIMATE_POWERUP = "animatePowerup";
        String POWERUP_INFO = "powerupInfo";
        String POWERUP_TRIED = "powerupTried";
    }

    interface ActionType {
        String GOTO_NEXT = "next";
        String EXIT = "exit";
        String GOTO_POWERUPS = "powerups";
        String GOTO_POWERUP_INFO = "powerupInfo";
    }

    private List<DGInstruction> mInstructionList;

    private int mLastReadInstruction = 0;

    private DGPlayFragment mDummyGamePlayFragment;

    private DGTextFragment mDummyGameTextFragment;

    private DGPowerUpInfoFragment mDummyGamePowerUpInfoFragment;

    private DGPowerupTriedFragment mDummyGamePowerUpTriedFragment;

    private Integer mLastScoredPoints;

    private int mPowerUpsInstructionPos = 0;

    private boolean mIsBackAlreadyPressed = false;

    @Override
    public String getScreenName() {
        return ScreenNames.DUMMY_GAME;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_game);
        setImmersiveFullScreenMode();

        NostragamusAnalytics.getInstance().trackDummyGame(AnalyticsActions.STARTED);

        mInstructionList = getDummyGameInstructions();
        if (null != mInstructionList) {
            readNextInstruction();
        }
    }

    private void readNextInstruction() {
        if (mLastReadInstruction < mInstructionList.size()) {
            handleInstruction(mInstructionList.get(mLastReadInstruction++));
        } else {
            onBackPressed();
        }
    }

    private void handleInstruction(DGInstruction instruction) {
        if (instruction.isFreshStart()) {
            startFresh();
        }

        switch (instruction.getType()) {
            case InstructionType.TEXT:
                addDummyText(instruction);
                break;

            case InstructionType.QUESTION:
                addDummyPlay(instruction.getQuestion(), instruction.getQuestionType());
                break;

            case InstructionType.POWERUP:
                mPowerUpsInstructionPos = mLastReadInstruction - 1;
                addDummyPlay(instruction.getQuestion(), instruction.getQuestionType());
                break;

            case InstructionType.ANIMATE_POWERUP:
                mDummyGamePlayFragment.animatePowerUps();
                break;

            case InstructionType.POWERUP_INFO:
                addPowerupInfo(instruction);
                return;

            case InstructionType.POWERUP_TRIED:
                addPowerUpTried(instruction);
                return;
        }

        checkWaitingTime(instruction.getWaitingTime());
    }

    private void addPowerUpTried(DGInstruction instruction) {
        if (null != mLastScoredPoints) {
            instruction.setScoredPoints(mLastScoredPoints);
            mLastScoredPoints = null;
        }

        if (null == mDummyGamePowerUpTriedFragment) {
            mDummyGamePowerUpTriedFragment = new DGPowerupTriedFragment();
            mDummyGamePowerUpTriedFragment.setInstruction(instruction);

            commitTransaction(getFragmentTransaction().replace(
                    R.id.dummy_game_fl_play_holder,
                    mDummyGamePowerUpTriedFragment
            ));
        } else {
            mDummyGamePowerUpTriedFragment.applyInstruction(instruction);
        }
    }

    private void removeDummyPowerupTried() {
        if (null != mDummyGamePowerUpTriedFragment) {
            commitTransaction(getFragmentTransaction().remove(mDummyGamePowerUpTriedFragment));
            mDummyGamePowerUpTriedFragment = null;
        }
    }

    private void addPowerupInfo(DGInstruction instruction) {
        if (null != mLastScoredPoints) {
            instruction.setScoredPoints(mLastScoredPoints);
            mLastScoredPoints = null;
        }

        if (null == mDummyGamePowerUpInfoFragment) {
            mDummyGamePowerUpInfoFragment = new DGPowerUpInfoFragment();
            mDummyGamePowerUpInfoFragment.setInstruction(instruction);

            commitTransaction(getFragmentTransaction().replace(
                    R.id.dummy_game_fl_play_holder,
                    mDummyGamePowerUpInfoFragment
            ));
        } else {
            mDummyGamePowerUpInfoFragment.applyInstruction(instruction);
        }
    }

    private void removeDummyPowerupInfo() {
        if (null != mDummyGamePowerUpInfoFragment) {
            commitTransaction(getFragmentTransaction().remove(mDummyGamePowerUpInfoFragment));
            mDummyGamePowerUpInfoFragment = null;
        }
    }

    private void checkWaitingTime(Long waitingTime) {
        if (null != waitingTime) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    readNextInstruction();
                }
            }, waitingTime);
        } // else wait for user action
    }

    private void addDummyPlay(Question dummyQuestion, String questionType) {
        if (null == mDummyGamePlayFragment) {
            commitTransaction(getFragmentTransaction().replace(
                    R.id.dummy_game_fl_play_holder,
                    mDummyGamePlayFragment = DGPlayFragment.newInstance(dummyQuestion, questionType)
            ));
        } else {
            mDummyGamePlayFragment.addQuestion(dummyQuestion, questionType);
        }
    }

    private void removeDummyPlay() {
        if (null != mDummyGamePlayFragment) {
            commitTransaction(getFragmentTransaction().remove(mDummyGamePlayFragment));
            mDummyGamePlayFragment = null;
        }
    }

    private void addDummyText(DGInstruction instruction) {
        if (null != mLastScoredPoints) {
            instruction.setScoredPoints(mLastScoredPoints);
            mLastScoredPoints = null;
        }

        if (null == mDummyGameTextFragment) {
            commitTransaction(getFragmentTransaction().replace(
                    R.id.dummy_game_fl_text_holder,
                    mDummyGameTextFragment = DGTextFragment.newInstance(instruction)
            ));
        } else {
            if (getActivity() != null && mDummyGameTextFragment.isAdded()) {
                mDummyGameTextFragment.applyInstruction(instruction);
            }
        }
    }

    private void removeDummyText() {
        if (null != mDummyGameTextFragment) {
            commitTransaction(getFragmentTransaction().remove(mDummyGameTextFragment));
            mDummyGameTextFragment = null;
        }
    }

    private void commitTransaction(FragmentTransaction ft) {
        if (!isDestroyed()) {
            ft.commitAllowingStateLoss();
        }
    }

    @Override
    public void onPlayed(Integer scoredPoints) {
        mLastScoredPoints = scoredPoints;
        readNextInstruction();
    }

    @Override
    public void on2xApplied() {
//        mDummyGameTextFragment.hideBottomText();
        addDummyText(getPowerUpInstruction("You used a Doubler powerup - this doubles the points you gain (or lose). Now swipe!"));
//        addDummyText(getSwipeInstruction());
    }

    @Override
    public void onNonegsApplied() {
//        mDummyGameTextFragment.hideBottomText();
        addDummyText(getPowerUpInstruction("You used a No-Negatives powerup - this means NO points lost for a wrong prediction. Now swipe!"));
//        addDummyText(getSwipeInstruction());
    }

    @Override
    public void onPollApplied() {
//        mDummyGameTextFragment.hideBottomText();
        addDummyText(getPowerUpInstruction("You used an Audience Poll - this shows you how others predicted this question. Now swipe!"));
//        addDummyText(getSwipeInstruction());
    }

    @Override
    public void onRemovingPowerUps() {
//        mDummyGameTextFragment.showBottomText();
        addDummyText(getTapPowerPlayInstruction());
        addDummyText(getRemovePowerUpInstruction());
    }

    @Override
    public void onActionClicked(String actionType) {
        switch (actionType) {
            case ActionType.GOTO_NEXT:
                readNextInstruction();
                break;

            case ActionType.GOTO_POWERUPS:
                mLastReadInstruction = mPowerUpsInstructionPos;
                mInstructionList.get(mLastReadInstruction).setFreshStart(true);
                readNextInstruction();
                break;

            case ActionType.GOTO_POWERUP_INFO:
                readNextInstruction();
                break;

            case ActionType.EXIT:
                onBackPressed();
                break;
        }
    }

    private void startFresh() {
        removeDummyPlay();
        removeDummyText();
        removeDummyPowerupInfo();
        removeDummyPowerupTried();
    }

    private DGInstruction getPowerUpInstruction(String powerUpText) {
        DGInstruction instruction = new DGInstruction();
        instruction.setName(powerUpText);
        instruction.setTextType(DGTextFragment.TextType.BOTTOM_TEXT);
        instruction.setType(InstructionType.TEXT);

        DGAnimation animation = new DGAnimation();
        animation.setType(DGAnimation.AnimationType.ALPHA);
        animation.setStart(0);
        animation.setEnd(1);
        animation.setDuration(1500);

        instruction.setAnimation(animation);
        return instruction;
    }

    private DGInstruction getSwipeInstruction() {
        DGInstruction instruction = new DGInstruction();
        instruction.setName("");
        instruction.setTextType(DGTextFragment.TextType.TOP_TEXT);
        instruction.setType(InstructionType.TEXT);

        DGAnimation animation = new DGAnimation();
        animation.setType(DGAnimation.AnimationType.ALPHA);
        animation.setStart(0);
        animation.setEnd(1);
        animation.setDuration(1500);

        instruction.setAnimation(animation);
        return instruction;
    }

    private DGInstruction getTapPowerPlayInstruction() {
        DGInstruction instruction = new DGInstruction();
        instruction.setName("Tap on a powerup to apply it.");
        instruction.setTextType(DGTextFragment.TextType.BOTTOM_TEXT);
        instruction.setType(InstructionType.TEXT);

        DGAnimation animation = new DGAnimation();
        animation.setType(DGAnimation.AnimationType.ALPHA);
        animation.setStart(0);
        animation.setEnd(1);
        animation.setDuration(1500);

        instruction.setAnimation(animation);
        return instruction;
    }

    private DGInstruction getRemovePowerUpInstruction() {
        DGInstruction instruction = new DGInstruction();
        instruction.setTextType(DGTextFragment.TextType.TOP_TEXT);
        instruction.setType(InstructionType.TEXT);

        DGAnimation animation = new DGAnimation();
        animation.setType(DGAnimation.AnimationType.ALPHA);
        animation.setStart(1);
        animation.setEnd(0);
        animation.setDuration(1000);

        instruction.setAnimation(animation);
        return instruction;
    }

    private List<DGInstruction> getDummyGameInstructions() {
        String json = null;
        try {
            InputStream is = getAssets().open("json/dummy_game_flow.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (null != json) {
            /*return MyWebService.getInstance().getObjectFromJson(
                    json,
                    new TypeReference<List<DGInstruction>>() {
                    }
            );*/
            return new Gson().fromJson(json, new TypeReference<List<DGInstruction>>() {
            }.getType());
        }
        return null;
    }

    private FragmentTransaction getFragmentTransaction() {
        return getSupportFragmentManager().beginTransaction();
    }

    @Override
    public void onBackPressed() {
        if (mLastReadInstruction == mInstructionList.size()) {
            NostragamusAnalytics.getInstance().trackDummyGame(AnalyticsActions.COMPLETED);
        } else {
            NostragamusAnalytics.getInstance().trackDummyGame(AnalyticsActions.SKIPPED, mLastReadInstruction);
        }

        if (!mIsBackAlreadyPressed) {
            handleMultipleFastBackPressAndPerformBack();
        }
    }

    /**
     * In case user clicks so fast back button (it takes multiple back action) which is not accepted for Dummy-game back flow.
     * so fast back-press happens within fraction (a second) which is block for sometime to delay back-press behaviour.
     */
    private void handleMultipleFastBackPressAndPerformBack() {
        mIsBackAlreadyPressed = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                /* If args contains playscreenData then launch play screen and finish this as it could be firs time play for user */
                if (getIntent() != null && getIntent().getExtras() != null) {
                    Bundle bundle = getIntent().getExtras();
                    if (bundle.containsKey(Constants.BundleKeys.PLAY_SCREEN_DATA)) {
                        Intent predictionIntent = new Intent(getActivity(), PredictionActivity.class);
                        predictionIntent.putExtras(bundle);
                        predictionIntent.putExtra(Constants.BundleKeys.SCREEN_LAUNCHED_FROM_PARENT,
                                PredictionActivity.LaunchedFrom.DUMMY_GAME);
                        startActivity(predictionIntent);
                    }
                }

                DummyGameActivity.super.onBackPressed();
            }
        }, DELAY_TO_PERFORM_BACK_PRESS);
    }

    @Override
    public void onLearnPowerupClicked() {
        readNextInstruction();
    }

    @Override
    public void onGotItClicked() {
        onActionClicked(ActionType.EXIT);
    }
}