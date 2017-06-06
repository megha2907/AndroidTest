package in.sportscafe.nostragamus.module.navigation.submitquestion.matchlist;

import android.os.Bundle;

import in.sportscafe.nostragamus.Constants.Alerts;

/**
 * Created by Jeeva on 15/6/16.
 */
public class MatchListPresenterImpl implements MatchListPresenter, MatchListModelImpl.OnMatchListModelListener {

    private MatchListView mMatchListView;

    private MatchListModel mMatchListModel;

    private MatchListPresenterImpl(MatchListView view) {
        this.mMatchListView = view;
        this.mMatchListModel = MatchListModelImpl.newInstance(this);
    }

    public static MatchListPresenter newInstance(MatchListView view) {
        return new MatchListPresenterImpl(view);
    }

    @Override
    public void onCreateMatchList(Bundle bundle) {
        mMatchListModel.init(mMatchListView.getContext(), bundle);
    }

    @Override
    public void onEmpty() {
        mMatchListView.dismissProgressbar();
        mMatchListView.showInAppMessage(Alerts.EMPTY_MATCHES);
    }

    @Override
    public void onAdapterCreated(MatchListAdapter adapter) {
        mMatchListView.setAdapter(adapter);
    }
}