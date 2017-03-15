package in.sportscafe.nostragamus.module.play.dummygame;

import android.os.Bundle;

import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.tindercard.FlingCardListener;

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

    void onGetQuestions(Question question);
}