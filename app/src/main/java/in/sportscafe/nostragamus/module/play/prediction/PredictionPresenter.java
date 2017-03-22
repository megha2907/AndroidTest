package in.sportscafe.nostragamus.module.play.prediction;

import android.os.Bundle;

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

    void onClickBack();

    void onClickBankTransfer();

    void onBankInfoDismiss();

    void onChallengeInfoUpdated(Bundle bundle);

    void onGetDummyGameResult();

    void onShake();
}