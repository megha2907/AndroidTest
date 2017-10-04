package in.sportscafe.nostragamus.module.navigation.help.dummygame;

import android.content.Context;
import android.os.Bundle;

import in.sportscafe.nostragamus.module.navigation.help.dummygame.tindercard.FlingCardListener;
import in.sportscafe.nostragamus.module.resultspeek.dto.Question;

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