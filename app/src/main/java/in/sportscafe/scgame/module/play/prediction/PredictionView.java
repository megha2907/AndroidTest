package in.sportscafe.scgame.module.play.prediction;

import android.os.Bundle;

import com.jeeva.android.View;

import in.sportscafe.scgame.module.play.prediction.dto.Question;
import in.sportscafe.scgame.module.play.tindercard.SwipeFlingAdapterView;

/**
 * Created by Jeeva on 20/5/16.
 */
public interface PredictionView extends View {

    void setTournamentName(String tournamentName);

    void setContestName(String contestName);

    void setAdapter(PredictionAdapter predictionAdapter,
                    SwipeFlingAdapterView.OnSwipeListener<Question> swipeListener);

    void hidePass();

    void showNoNegativeAlert();

    void showLastQuestionAlert();

    void navigateToResult(Bundle bundle);

    void swipeCardToTop();

    void dismisspowerup();



}