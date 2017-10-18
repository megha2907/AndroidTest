package in.sportscafe.nostragamus.module.navigation.help.dummygame;

import android.os.Bundle;

import in.sportscafe.nostragamus.module.navigation.help.dummygame.tindercard.FlingCardListener;
import in.sportscafe.nostragamus.module.resultspeek.dto.Question;

/**
 * Created by Jeeva on 20/5/16.
 */
public interface DGPlayPresenter {

    void onCreatePrediction(Bundle bundle);

    void setFlingListener(FlingCardListener topCardListener);

    void updatePowerups();

    void onClick2xPowerup();

    void onClickNonegsPowerup();

    void onClickPollPowerup();

    void onGetQuestions(Question question, String questionType);
}