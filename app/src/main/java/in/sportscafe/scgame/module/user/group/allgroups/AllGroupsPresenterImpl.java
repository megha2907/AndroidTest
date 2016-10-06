package in.sportscafe.scgame.module.user.group.allgroups;

import in.sportscafe.scgame.Constants;


/**
 * Created by deepanshi on 9/27/16.
 */

public class AllGroupsPresenterImpl implements AllGroupsPresenter, AllGroupsModelImpl.AllGroupsModelListener {

    private AllGroupsView mAllGroupsView;

    private AllGroupsModel mAllGroupsModel;

    private boolean mFromProfile = false;

    public AllGroupsPresenterImpl(AllGroupsView AllGroupsView) {
        this.mAllGroupsView = AllGroupsView;
        this.mAllGroupsModel = AllGroupsModelImpl.newInstance(this);
    }

    public static AllGroupsPresenter newInstance(AllGroupsView AllGroupsView) {
        return new AllGroupsPresenterImpl(AllGroupsView);
    }

    @Override
    public void onCreateAllGroupsAdapter() {
        mAllGroupsView.setAdapter(mAllGroupsModel
                .getAllGroupsAdapter(mAllGroupsView.getContext()));
    }

    @Override
    public void onClickNext() {
        mAllGroupsView.showProgressbar();
    }

    @Override
    public void onNoInternet() {
        mAllGroupsView.dismissProgressbar();
        mAllGroupsView.showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onFailed(String message) {
        mAllGroupsView.dismissProgressbar();
        mAllGroupsView.showMessage(message);
    }
}