package in.sportscafe.nostragamus.module.play.prediction;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.jeeva.android.InAppView;

import java.util.HashMap;

import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.tindercard.SwipeFlingAdapterView;

import static android.R.attr.x;

/**
 * Created by Jeeva on 20/5/16.
 */
public interface PredictionView extends InAppView {

    void setContestName(String contestName, String matchStage);

    void setAdapter(PredictionAdapter predictionAdapter,
                    SwipeFlingAdapterView.OnSwipeListener<Question> swipeListener);

    void showShuffle();

    void hideShuffle();

    void showNeither();

    void hideNeither();

    void setNeitherOption(String neitherOption);

    void setNumberofCards(String numberofCards);

    void set2xPowerupCount(int count, boolean reverse);

    void setNonegsPowerupCount(int count, boolean reverse);

    void setPollPowerupCount(int count, boolean reverse);

    void notifyTopView();

    void changeBackgroundImage(Integer sportId);

    void navigateToFeed();

    void goBack();

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

    void navigateToDummyGame();

    void showLeftRightCoach();

    void showNeitherCoach();

    void showPowerupsCoach();

    boolean dismissCoach();

    void navigateToBankTransfer(Bundle bundle);

    void showBankInfo();

    void showPopUp(String popUpType);

    void showFirstMatchPlayedPopUp(String firstMatchPlayed, Bundle bundle);

    void takeScreenshotAndShare();
}