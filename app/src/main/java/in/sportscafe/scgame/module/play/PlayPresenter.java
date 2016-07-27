package in.sportscafe.scgame.module.play;

import android.content.Intent;

/**
 * Created by Jeeva on 20/5/16.
 */
public interface PlayPresenter {

    void onCreatePlay();

    void onClickSkip();

    void onGetResultBack(int resultCode, Intent data);
}