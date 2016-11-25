package in.sportscafe.nostragamus.module.play.matchstatus;

import android.os.Bundle;

import in.sportscafe.nostragamus.module.feed.dto.Match;

/**
 * Created by Jeeva on 20/5/16.
 */
public class MatchStatusPresenterImpl implements MatchStatusPresenter {

    private MatchStatusView mMatchStatusView;

    private MatchStatusModel mMatchStatusModel;

    public MatchStatusPresenterImpl(MatchStatusView predictionView) {
        this.mMatchStatusView = predictionView;
        this.mMatchStatusModel = MatchStatusModelImpl.newInstance();
    }

    public static MatchStatusPresenter newInstance(MatchStatusView predictionView) {
        return new MatchStatusPresenterImpl(predictionView);
    }

    @Override
    public void onCreateMatchStatus(Bundle bundle) {
        mMatchStatusModel.init(bundle);
        populateMatchDetails(mMatchStatusModel.getMatchDetails());
    }

    private void populateMatchDetails(Match matchDetails) {
        mMatchStatusView.setTournament(matchDetails.getTournamentName());
        mMatchStatusView.setContestName(matchDetails.getParties().get(0).getPartyName()+"  vs  "+matchDetails.getParties().get(1).getPartyName());
        mMatchStatusView.setStartTime(matchDetails.getStartTime());
        mMatchStatusView.setTimeLeft(matchDetails.getStartTime());
        mMatchStatusView.setTournamentImageTeam1(matchDetails.getParties().get(0).getPartyImageUrl());
        mMatchStatusView.setTournamentImageTeam2(matchDetails.getParties().get(1).getPartyImageUrl());
    }

    @Override
    public void onClickPlay() {
        mMatchStatusView.navigateToPrediction(mMatchStatusModel.getQuestions());
    }
}