package in.sportscafe.nostragamus.module.play.matchstatus;

import android.os.Bundle;

import in.sportscafe.nostragamus.module.feed.dto.Match;

/**
 * Created by Jeeva on 20/5/16.
 */
public interface MatchStatusModel {

    void init(Bundle bundle);

    Match getMatchDetails();

    Bundle getQuestions();
}