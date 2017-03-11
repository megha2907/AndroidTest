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

    interface InstructionType {
        String TEXT = "text";
        String QUESTION = "question";
        String POWERUP = "powerup";
    }

    private List<DGInstruction> mInstructionList;

    private DummyGamePlayFragment mDummyGamePlayFragment;

    private DummyGameTextFragment mDummyGameTextFragment;

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
        if (mInstructionList.size() > 0) {
            handleInstruction(mInstructionList.get(0));
            mInstructionList.remove(0);
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
                addDummyPlay(instruction.getQuestion());
                break;
            case InstructionType.POWERUP:
                mDummyGamePlayFragment.showPowerups();
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
        getFragmentTransaction().replace(
                R.id.dummy_game_fl_play_holder,
                mDummyGamePlayFragment = DummyGamePlayFragment.newInstance(dummyQuestion)
        ).commit();
    }

    private void removeDummyPlay() {
        if (null != mDummyGamePlayFragment) {
            getFragmentTransaction().remove(mDummyGamePlayFragment).commit();
            mDummyGamePlayFragment = null;
        }
    }

    private void addDummyText(DGInstruction instruction) {
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
        if(null != mDummyGameTextFragment) {
            getFragmentTransaction().remove(mDummyGameTextFragment).commit();
            mDummyGameTextFragment = null;
        }
    }

    @Override
    public void onPlayed() {
        readNextInstruction();
    }

    @Override
    public void onActionClicked(String action) {
        readNextInstruction();
    }

    private void startFresh() {
        removeDummyPlay();
        removeDummyText();
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