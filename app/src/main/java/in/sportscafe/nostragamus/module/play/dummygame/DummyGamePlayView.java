package in.sportscafe.nostragamus.module.play.dummygame;

import com.jeeva.android.InAppView;

import in.sportscafe.nostragamus.module.play.prediction.PredictionAdapter;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.tindercard.SwipeFlingAdapterView;

/**
 * Created by Jeeva on 20/5/16.
 */
public interface DummyGamePlayView extends InAppView {

    void setAdapter(PredictionAdapter predictionAdapter,
                    SwipeFlingAdapterView.OnSwipeListener<Question> swipeListener);

    void showNeither();

    void hideNeither();

    void setNeitherOption(String neitherOption);

    void set2xPowerupCount(int count, boolean reverse);

    void setNonegsPowerupCount(int count, boolean reverse);

    void setPollPowerupCount(int count, boolean reverse);

    void notifyTopView();

    void showPowerups();

    void hidePowerups();

    void onPlayDone();
}