package in.sportscafe.nostragamus.module.play.dummygame;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import in.sportscafe.nostragamus.Constants.ScreenNames;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.play.dummygame.DGAnimation.AnimationType;
import in.sportscafe.nostragamus.module.play.dummygame.DGTextFragment.TextType;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.webservice.MyWebService;


public class DummyGameActivity extends NostragamusActivity implements DGPlayFragment.OnDGPlayActionListener,
        DGTextFragment.OnDGTextActionListener {

    interface InstructionType {
        String TEXT = "text";
        String QUESTION = "question";
        String POWERUP = "powerup";
        String ANIMATE_POWERUP = "animatePowerup";
    }

    interface ActionType {
        String GOTO_NEXT = "next";
        String EXIT = "exit";
        String GOTO_POWERUPS = "powerups";
    }

    private List<DGInstruction> mInstructionList;

    private int mLastReadInstruction = 13;

    private DGPlayFragment mDummyGamePlayFragment;

    private DGTextFragment mDummyGameTextFragment;

    private Integer mLastScoredPoints;

    private int mPowerUpsInstructionPos = 0;

    @Override
    public String getScreenName() {
        return ScreenNames.DUMMY_GAME;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_game);

        mInstructionList = getDummyGameInstructions();
        if (null != mInstructionList) {
            readNextInstruction();
        }
    }

    private void readNextInstruction() {
        if (mLastReadInstruction + 1 < mInstructionList.size()) {
            handleInstruction(mInstructionList.get(++mLastReadInstruction));
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
                mPowerUpsInstructionPos = mLastReadInstruction;
                addDummyPlay(instruction.getQuestion(), instruction.getQuestionType());
                break;
            case InstructionType.ANIMATE_POWERUP:
                mDummyGamePlayFragment.animatePowerUps();
                break;
        }

        checkWaitingTime(instruction.getWaitingTime());
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
            getFragmentTransaction().replace(
                    R.id.dummy_game_fl_play_holder,
                    mDummyGamePlayFragment = DGPlayFragment.newInstance(dummyQuestion, questionType)
            ).commit();
        } else {
            mDummyGamePlayFragment.addQuestion(dummyQuestion, questionType);
        }
    }

    private void removeDummyPlay() {
        if (null != mDummyGamePlayFragment) {
            getFragmentTransaction().remove(mDummyGamePlayFragment).commit();
            mDummyGamePlayFragment = null;
        }
    }

    private void addDummyText(DGInstruction instruction) {
        if (null != mLastScoredPoints) {
            instruction.setScoredPoints(mLastScoredPoints);
            mLastScoredPoints = null;
        }

        if (null == mDummyGameTextFragment) {
            getFragmentTransaction().replace(
                    R.id.dummy_game_fl_text_holder,
                    mDummyGameTextFragment = DGTextFragment.newInstance(instruction)
            ).commit();
        } else {
            mDummyGameTextFragment.applyInstruction(instruction);
        }
    }

    private void removeDummyText() {
        if (null != mDummyGameTextFragment) {
            getFragmentTransaction().remove(mDummyGameTextFragment).commit();
            mDummyGameTextFragment = null;
        }
    }

    @Override
    public void onPlayed(Integer scoredPoints) {
        mLastScoredPoints = scoredPoints;
        readNextInstruction();
    }

    @Override
    public void on2xApplied() {
        mDummyGameTextFragment.hideBottomText();
//        addDummyText(getPowerUpInstruction("You just used a 2x powerup.\nSwipe to confirm your answer!"));
        addDummyText(getPowerUpInstruction("You have just used a Double X powerup. It doubles " +
                "the points for any question!\n" +
                "Swipe to confirm your answer!"));
    }

    @Override
    public void onNonegsApplied() {
        mDummyGameTextFragment.hideBottomText();
        addDummyText(getPowerUpInstruction("You have just used a No Negative powerup. It cuts down " +
                "the negative marking for any question!\n" +
                "Swipe to confirm your answer!"));
    }

    @Override
    public void onPollApplied() {
        mDummyGameTextFragment.hideBottomText();
        addDummyText(getPowerUpInstruction("You have just used an Audience poll. It will tell" +
                "you how others have answered this question\n" +
                "Swipe to confirm your answer!"));
    }

    @Override
    public void onActionClicked(String actionType) {
        switch (actionType) {
            case ActionType.GOTO_NEXT:
                readNextInstruction();
                break;
            case ActionType.GOTO_POWERUPS:
                mLastReadInstruction = mPowerUpsInstructionPos - 2;
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
    }

    private DGInstruction getPowerUpInstruction(String powerUpText) {
        DGInstruction instruction = new DGInstruction();
        instruction.setName(powerUpText);
        instruction.setTextType(TextType.TOP_TEXT);
        instruction.setType(InstructionType.TEXT);

        DGAnimation animation = new DGAnimation();
        animation.setType(AnimationType.ALPHA);
        animation.setStart(0);
        animation.setEnd(1);
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
            return MyWebService.getInstance().getObjectFromJson(
                    json,
                    new TypeReference<List<DGInstruction>>() {
                    }
            );
        }
        return null;
    }

    private FragmentTransaction getFragmentTransaction() {
        return getSupportFragmentManager().beginTransaction();
    }
}