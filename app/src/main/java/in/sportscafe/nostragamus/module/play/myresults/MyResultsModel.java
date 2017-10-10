package in.sportscafe.nostragamus.module.play.myresults;

import android.content.Context;
import android.os.Bundle;

import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestDto;
import in.sportscafe.nostragamus.module.resultspeek.dto.Match;


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

    InPlayContestDto getInPlayContest();

    String getMatchName();

    void getShareText();

    String getMatchEndTime();

    Match getMatch();
}