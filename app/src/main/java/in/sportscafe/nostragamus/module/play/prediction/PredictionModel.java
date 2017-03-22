package in.sportscafe.nostragamus.module.play.prediction;

import android.content.Context;
import android.os.Bundle;

import java.util.List;

import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.tindercard.FlingCardListener;

/**
 * Created by Jeeva on 20/5/16.
 */
public interface PredictionModel {

    void init(Bundle bundle);

    String getTournamentName();

    String getMatchStage();

    String getTournamentPhoto();

    Integer getTournamentId();

    String getContestName();

    int get2xPowerupCount();

    int getNonegsPowerupCount();

    int getPollPowerupCount();

    void getAllQuestions();

    PredictionAdapter getAdapter(Context context, List<Question> questions);

    void setFlingCardListener(FlingCardListener flingCardListener);

    void apply2xPowerup();

    void applyNonegsPowerup();

    void applyPollPowerup();

    boolean isAnyQuestionAnswered();

    Bundle getChallengeInfoBundle();

    void updateChallengeInfoValues(Bundle bundle);

    boolean isDummyGameShown();

    void onDummyGameShown();

    boolean isQuestionAvailable();

    void getShareText(Context context);
}