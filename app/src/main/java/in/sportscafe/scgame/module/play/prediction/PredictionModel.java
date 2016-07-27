package in.sportscafe.scgame.module.play.prediction;

import android.os.Bundle;

/**
 * Created by Jeeva on 20/5/16.
 */
public interface PredictionModel {

    void saveData(Bundle bundle);

    String getTournamentName();

    String getContestName();
}