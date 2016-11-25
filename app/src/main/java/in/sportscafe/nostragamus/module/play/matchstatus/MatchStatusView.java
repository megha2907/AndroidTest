package in.sportscafe.nostragamus.module.play.matchstatus;

import android.os.Bundle;

import com.jeeva.android.InAppView;

/**
 * Created by Jeeva on 20/5/16.
 */
public interface MatchStatusView extends InAppView {

    void setTournament(String tournament);

    void setStartTime(String startTime);

    void setTimeLeft(String timeLeft);

    void setContestName(String contestName);

    void setTournamentImageTeam1(String tournamentImageTeam1);

    void setTournamentImageTeam2(String tournamentImageTeam2);

    void navigateToPrediction(Bundle bundle);
}