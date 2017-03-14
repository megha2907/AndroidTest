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
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.webservice.MyWebService;


public class DummyGameActivity extends NostragamusActivity implements DummyGamePlayFragment.OnDGPlayActionListener,
        DummyGameTextFragment.OnDGTextActionListener {

    private static final String TRY_OTHER_POWERUPS = "Try other powerups";

    private static final int POWERUPS_INSTRUCTION = 8;

    interface InstructionType {
        String TEXT = "text";
        String ACTION1 = "action1";
        String ACTION2 = "action2";
        String QUESTION = "question";
        String POWERUP = "powerup";
    }

    private List<DGInstruction> mInstructionList;

    private int mLastReadInstruction = -1;

    private DummyGamePlayFragment mDummyGamePlayFragment;

    private DummyGameTextFragment mDummyGameTextFragment;

    private Integer mLastScoredPoints;

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
            case InstructionType.ACTION1:
            case InstructionType.ACTION2:
                addDummyText(instruction);
                break;
            case InstructionType.QUESTION:
                addDummyPlay(instruction.getQuestion());
                break;
            case InstructionType.POWERUP:
                addDummyPlay(null);
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

    private void addDummyPlay(Question dummyQuestion) {
        if (null == mDummyGamePlayFragment) {
            getFragmentTransaction().replace(
                    R.id.dummy_game_fl_play_holder,
                    mDummyGamePlayFragment = DummyGamePlayFragment.newInstance(dummyQuestion)
            ).commit();
        } else {
            mDummyGamePlayFragment.addQuestion(dummyQuestion);
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
                    mDummyGameTextFragment = DummyGameTextFragment.newInstance(instruction)
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
        addDummyText(getPowerUpInstruction("You just used a 2x powerup.\nSwipe to confirm your answer!"));
    }

    @Override
    public void onNonegsApplied() {
        addDummyText(getPowerUpInstruction("You just used a No Negative powerup.\nSwipe to confirm your answer!"));
    }

    @Override
    public void onPollApplied() {
        addDummyText(getPowerUpInstruction("You just used a Audience Poll powerup.\nSwipe to confirm your answer!"));
    }

    @Override
    public void onActionClicked(String actionText) {
        if (TRY_OTHER_POWERUPS.equalsIgnoreCase(actionText)) {
            mLastReadInstruction = POWERUPS_INSTRUCTION - 1;
        }
        readNextInstruction();
    }

    private void startFresh() {
        removeDummyPlay();
        removeDummyText();
    }

    private DGInstruction getPowerUpInstruction(String powerUpText) {
        DGInstruction instruction = new DGInstruction();
        instruction.setName(powerUpText);
        instruction.setAlignment(DummyGameTextFragment.Alignment.TOP);
        instruction.setType(InstructionType.TEXT);
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