package in.sportscafe.nostragamus.module.play.myresults;

import android.os.Bundle;

/**
 * Created by Jeeva on 15/6/16.
 */
public interface MyResultsPresenter {

    void onCreateMyResults(Bundle bundle);

    void onArticleScroll(int firstVisibleItemPosition, int childCount, int itemCount);

    void onPowerUp(String powerup);

    void onReplayPowerupApplied();

    void onFlipPowerupApplied();

}