package in.sportscafe.nostragamus.module.play.prediction;

import android.os.Bundle;
import android.view.View;

import com.jeeva.android.InAppView;

import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.tindercard.SwipeFlingAdapterView;

import static android.R.attr.x;

/**
 * Created by Jeeva on 20/5/16.
 */
public interface PredictionView extends InAppView {

    void setTournamentName(String tournamentName);

    void setContestName(String contestName);

    void setAdapter(PredictionAdapter predictionAdapter,
                    SwipeFlingAdapterView.OnSwipeListener<Question> swipeListener);

    void hideShuffle();

    void showNeither();

    void hideNeither();

    void setNeitherOption(String neitherOption);

    void setMatchStage(String matchStage);

    void setTournamentPhoto(String tournamentPhoto);

    void setNumberofCards(String numberofCards);

    void set2xPowerupCount(int count);

    void setNonegsPowerupCount(int count);

    void setPollPowerupCount(int count);

    void notifyTopView();

    void changeBackgroundImage(String sportName);

    void navigateToFeed(Bundle bundle);

    void goBack();

    void changeToDummyGameMode();

    void showDummyGameInfo();

    void hideDummyGameInfo();

    void enableLeftRightOptions();

    void disableLeftRightOptions();

    void showPowerups();

    void hidePowerups();

    void showLeftRightIndicator();

    void hideLeftRightIndicator();

    void showNeitherIndicator();

    void hideNeitherIndicator();

    void showPowerupsHint();

    void hidePowerupsHint();

    void navigateToHome();

    void showLeftRightCoach();

    void showNeitherCoach();

    void showPowerupsCoach();

    boolean dismissCoach();
}