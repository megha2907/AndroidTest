package in.sportscafe.nostragamus.module.play.myresults;

import com.jeeva.android.InAppView;

import in.sportscafe.nostragamus.module.resultspeek.dto.Match;

/**
 * Created by Jeeva on 15/6/16.
 */
public interface MyResultsView extends InAppView {

    void setAdapter(MyResultsAdapter myResultsAdapter);

    void navigateToHome();

    void setNumberofPowerups(int numberofReplayPowerups, int numberofFlipPowerups);

    void setMatchDetails(Match match);

    void openReplayDialog();

    void navigatetoPlay(Match match);

    void openFlipDialog();

    void takeScreenShot();

    void setMatchName(String matchName);

    void setToolbarHeading(String result);

    void showResultsToBeDeclaredView(Boolean playedFirstMatch, String matchEndTime);

    void updateAnswers(Match match);

    void setUserRank(Match match);
}