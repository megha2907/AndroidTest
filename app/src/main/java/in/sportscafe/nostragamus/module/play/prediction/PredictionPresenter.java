package in.sportscafe.nostragamus.module.play.prediction;

import android.os.Bundle;

import java.util.List;

import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.tindercard.FlingCardListener;

/**
 * Created by Jeeva on 20/5/16.
 */
public interface PredictionPresenter {

    void onCreatePrediction(Bundle bundle);

    void onPowerUp(String powerup);

    void setFlingListener(FlingCardListener topCardListener);
}