package in.sportscafe.nostragamus.module.play.dummygame;

import android.content.Context;
import android.os.Bundle;

import in.sportscafe.nostragamus.module.play.prediction.PredictionAdapter;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.tindercard.FlingCardListener;
import in.sportscafe.nostragamus.module.play.tindercard.SwipeFlingAdapterView;

/**
 * Created by Jeeva on 20/5/16.
 */
public interface DummyGamePlayModel {

    int get2xPowerupCount();

    int getNonegsPowerupCount();

    int getPollPowerupCount();

    PredictionAdapter getAdapter(Context context, Bundle bundle);

    void setFlingCardListener(FlingCardListener flingCardListener);

    void apply2xPowerup();

    void applyNonegsPowerup();

    void applyPollPowerup();

    SwipeFlingAdapterView.OnSwipeListener<Question> getSwipeListener();
}