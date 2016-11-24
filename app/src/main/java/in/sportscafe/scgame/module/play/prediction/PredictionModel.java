package in.sportscafe.scgame.module.play.prediction;

import android.os.Bundle;

import in.sportscafe.scgame.module.play.tindercard.FlingCardListener;

/**
 * Created by Jeeva on 20/5/16.
 */
public interface PredictionModel {

    void saveData(Bundle bundle);

    String getTournamentName();

    String getMatchStage();

    String getTournamentPhoto();

    String getContestName();

    void getAllQuestions();

    void updatePowerUps();

    void setFlingCardListener(FlingCardListener flingCardListener);
}