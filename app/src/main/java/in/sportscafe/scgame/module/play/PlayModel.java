package in.sportscafe.scgame.module.play;

import android.os.Bundle;

/**
 * Created by Jeeva on 14/7/16.
 */
public interface PlayModel {

    void getAllQuestions();

    void skipCurrentMatch();

    Bundle getNextContestQuestions();

    boolean isNextContest();
}