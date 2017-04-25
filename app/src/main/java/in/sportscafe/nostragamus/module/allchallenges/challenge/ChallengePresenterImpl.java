package in.sportscafe.nostragamus.module.allchallenges.challenge;

import android.os.Bundle;

/**
 * Created by Jeeva on 17/02/17.
 */

public class ChallengePresenterImpl implements ChallengePresenter, ChallengeModelImpl.OnChallengeModelListener {

    private ChallengeView mChallengeView;

    private ChallengeModel mChallengeModel;

    private ChallengePresenterImpl(ChallengeView challengeView) {
        this.mChallengeView = challengeView;
        this.mChallengeModel = ChallengeModelImpl.newInstance(this);
    }

    public static ChallengePresenter newInstance(ChallengeView challengeView) {
        return new ChallengePresenterImpl(challengeView);
    }

    @Override
    public void onCreateChallenge(Bundle bundle, String tabName) {
        mChallengeModel.init(bundle, tabName);
        mChallengeView.setSwipeAdapter(mChallengeModel.getSwipeAdapter(mChallengeView.getContext()));
        mChallengeView.setListAdapter(mChallengeModel.getListAdapter(mChallengeView.getContext()));
    }

    @Override
    public void changeAdapterLayout(boolean swipeView) {
//        mChallengeModel.changeChallengesAdapterLayout(swipeView);
    }
}