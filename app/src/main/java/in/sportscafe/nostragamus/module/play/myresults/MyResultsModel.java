package in.sportscafe.nostragamus.module.play.myresults;

import android.content.Context;
import android.os.Bundle;

import java.io.File;

import in.sportscafe.nostragamus.module.feed.dto.Match;

/**
 * Created by Jeeva on 15/6/16.
 */
public interface MyResultsModel {

    void init(Bundle bundle);

    void getMyResultsData(Context context);

    void callReplayPowerupApplied();

    void showFlipQuestion();

    String getMatchResult();

    int getMatchPoints();

    String getMatchName();

    void getShareText();

    String getMatchEndTime();
}