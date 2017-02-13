package in.sportscafe.nostragamus.module.user.group.allgroups;

import android.os.Bundle;
import android.view.View;

import java.util.List;

import in.sportscafe.nostragamus.Constants.Alerts;


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
    public void onCreateAllGroups(Bundle bundle) {
        mAllGroupsModel.init(bundle);
    }

    @Override
    public void onGetGroupsSuccess(List<AllGroups> groupsList) {
        mAllGroupsView.setAdapter(mAllGroupsModel.getGroupsAdapter(mAllGroupsView.getContext(), groupsList));
    }

    @Override
    public void onGetGroupsFailed(String message) {
        mAllGroupsView.showMessage(message);
    }

    @Override
    public void onGroupsEmpty() {
        mAllGroupsView.showGroupsEmpty();
    }

    @Override
    public void onNoInternet() {
        mAllGroupsView.showMessage(Alerts.NO_NETWORK_CONNECTION, "RETRY",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAllGroupsModel.getAllGroups();
                    }
                });
    }

    @Override
    public void onApiCallStarted() {
        mAllGroupsView.showProgressbar();
    }

    @Override
    public boolean onApiCallStopped() {
        return mAllGroupsView.dismissProgressbar();
    }
}