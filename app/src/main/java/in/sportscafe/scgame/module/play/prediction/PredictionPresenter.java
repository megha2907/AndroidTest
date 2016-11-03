package in.sportscafe.scgame.module.play.prediction;

import android.os.Bundle;

import java.util.List;

import in.sportscafe.scgame.module.play.prediction.dto.Question;

/**
 * Created by Jeeva on 20/5/16.
 */
public interface PredictionPresenter {

    void onCreatePrediction(Bundle bundle);

    void onPowerUp();

    void onSetQuestionOption(List<Question> questionList);
}