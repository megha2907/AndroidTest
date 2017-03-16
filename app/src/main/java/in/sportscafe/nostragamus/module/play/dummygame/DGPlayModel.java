package in.sportscafe.nostragamus.module.play.dummygame;

import android.content.Context;
import android.os.Bundle;

import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.tindercard.FlingCardListener;

/**
 * Created by Jeeva on 20/5/16.
 */
public interface DGPlayModel {

    void init(Context context, Bundle bundle);

    void initAdapter(Context context, Question question, String questionType);

    int get2xPowerupCount();

    int getNonegsPowerupCount();

    int getPollPowerupCount();

    void setFlingCardListener(FlingCardListener flingCardListener);

    void apply2xPowerup();

    void applyNonegsPowerup();

    void applyPollPowerup();
}