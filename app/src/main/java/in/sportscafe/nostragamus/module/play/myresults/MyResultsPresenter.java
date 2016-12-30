package in.sportscafe.nostragamus.module.play.myresults;

import android.graphics.Bitmap;
import android.os.Bundle;

import java.io.File;

/**
 * Created by Jeeva on 15/6/16.
 */
public interface MyResultsPresenter {

    void onCreateMyResults(Bundle bundle);

    void onArticleScroll(int firstVisibleItemPosition, int childCount, int itemCount);

    void onPowerUp(String powerup);

    void onReplayPowerupApplied();

    void onFlipPowerupApplied();

    void onClickFbShare();

    void onGetScreenShot(File screenshotFile);
}