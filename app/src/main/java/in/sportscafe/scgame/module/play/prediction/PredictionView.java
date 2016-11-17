package in.sportscafe.scgame.module.play.prediction;

import android.content.DialogInterface;
import android.os.Bundle;

import com.jeeva.android.InAppView;
import com.jeeva.android.View;

import in.sportscafe.scgame.module.play.prediction.dto.Question;
import in.sportscafe.scgame.module.play.tindercard.FlingCardListener;
import in.sportscafe.scgame.module.play.tindercard.SwipeFlingAdapterView;

/**
 * Created by Jeeva on 20/5/16.
 */
public interface PredictionView extends InAppView {

    void setTournamentName(String tournamentName);

    void setContestName(String contestName);

    void setAdapter(PredictionAdapter predictionAdapter,
                    SwipeFlingAdapterView.OnSwipeListener<Question> swipeListener);


    void hidePass();

    void showNoNegativeAlert();

    void showLastQuestionAlert();

    void navigateToResult(Bundle bundle);

    void swipeCardToTop();

    void dismissPowerUp();

    void navigateToAllDone(Bundle bundle);

    void setLeftOption(String questionOption1);

    void setRightOption(String questionOption2);

    void setMatchStage(String matchStage);

    void setTournamentPhoto(String tournamentPhoto);

    void setNumberofCards(int itemsInAdapter);
}