package in.sportscafe.nostragamus.module.play.prediction;

import android.os.Bundle;

import com.jeeva.android.InAppView;

import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.tindercard.SwipeFlingAdapterView;

/**
 * Created by Jeeva on 20/5/16.
 */
public interface PredictionView extends InAppView {

    void setContestName(String leftContestName, String rightContestName,
                        String leftContestImageUrl, String rightContestImageUrl,
                        String matchStage);

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

    void navigateToFeed();

    void goBack();

    void hideLeftRightIndicator();

    void hideNeitherIndicator();

    void navigateToHome();

    void navigateToDummyGame();

    void navigateToBankTransfer(Bundle bundle);

    void showBankInfo();

    void showPopUp(String popUpType);

    void showFirstMatchPlayedPopUp(String firstMatchPlayed, Bundle bundle);

    void takeScreenshotAndShare();

    void navigateToResults(Bundle bundle);

    void showPowerUpBankActivity(Bundle args);
}