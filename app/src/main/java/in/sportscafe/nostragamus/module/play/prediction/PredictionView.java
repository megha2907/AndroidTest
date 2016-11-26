package in.sportscafe.nostragamus.module.play.prediction;

import android.os.Bundle;

import com.jeeva.android.InAppView;

import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.tindercard.SwipeFlingAdapterView;

/**
 * Created by Jeeva on 20/5/16.
 */
public interface PredictionView extends InAppView {

    void setTournamentName(String tournamentName);

    void setRightArrowAnimation();

    void setContestName(String contestName);

    void setAdapter(PredictionAdapter predictionAdapter,
                    SwipeFlingAdapterView.OnSwipeListener<Question> swipeListener);


    void hidePass();

    void showNoNegativeAlert();

    void showLastQuestionAlert();

    void navigateToResult(Bundle bundle);

    void swipeCardToTop();

    void dismissPowerUp();


    void updateAudiencePollPowerup();

    void navigateToAllDone(Bundle bundle);

    void setLeftOption(String questionOption1);

    void setRightOption(String questionOption2);

    void setMatchStage(String matchStage);

    void setTournamentPhoto(String tournamentPhoto);

    void setNumberofCards(int itemsInAdapter, int initialCount);

    void setNumberofPowerups(int numberof2xPowerups, int numberofAudiencePollPowerups, int numberofNonegsPowerups);
}