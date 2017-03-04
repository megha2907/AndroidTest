package in.sportscafe.nostragamus.module.play.prediction;

import android.os.Bundle;

import java.util.List;

import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.tindercard.FlingCardListener;

/**
 * Created by Jeeva on 20/5/16.
 */
public interface PredictionPresenter {

    void onCreatePrediction(Bundle bundle);

    void setFlingListener(FlingCardListener topCardListener);

    void onClick2xPowerup();

    void onClickNonegsPowerup();

    void onClickPollPowerup();

    void onDummyGameStart();

    void onDummyGameEnd();

    void onClickBack();

    void onClickSkip();

    void onClickBankTransfer();

    void onBankInfoDismiss();

    void onChallengeInfoUpdated(Bundle bundle);
}