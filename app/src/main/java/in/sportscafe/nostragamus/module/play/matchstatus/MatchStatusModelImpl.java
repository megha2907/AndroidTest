package in.sportscafe.nostragamus.module.play.matchstatus;

import android.os.Bundle;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.module.feed.dto.Match;

/**
 * Created by Jeeva on 20/5/16.
 */
public class MatchStatusModelImpl implements MatchStatusModel {

    private Match matchDetails;

    private Bundle mMatchBundle;

    private MatchStatusModelImpl() {
    }

    public static MatchStatusModel newInstance() {
        return new MatchStatusModelImpl();
    }

    @Override
    public void init(Bundle bundle) {
        this.mMatchBundle = bundle;
        this.matchDetails = (Match) bundle.getSerializable(Constants.BundleKeys.CONTEST_QUESTIONS);
    }

    @Override
    public Match getMatchDetails() {
        return matchDetails;
    }

    @Override
    public Bundle getQuestions() {
        return mMatchBundle;
    }
}