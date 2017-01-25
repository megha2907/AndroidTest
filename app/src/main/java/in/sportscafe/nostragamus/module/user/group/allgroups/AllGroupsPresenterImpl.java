package in.sportscafe.nostragamus.module.user.group.allgroups;

import android.content.Context;
import android.os.Bundle;

import in.sportscafe.nostragamus.Constants;


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
    public void onCreateAllGroups() {
        mAllGroupsView.showProgressbar();
        mAllGroupsModel.init();
    }

    @Override
    public void onCreateAllGroupsAdapter() {
        Context context = mAllGroupsView.getContext();
        if(null != context) {
            mAllGroupsView.setAdapter(mAllGroupsModel.getAllGroupsAdapter(context));
        }
    }

    private void onCreateMutualGroupsAdapter() {
        mAllGroupsView.setAdapter(mAllGroupsModel
                .getMutualGroupsAdapter(mAllGroupsView.getContext()));
    }


    @Override
    public void onClickNext() {
        mAllGroupsView.showProgressbar();
    }

    @Override
    public void onCreateMutualGroups(Bundle bundle) {
        mAllGroupsView.showProgressbar();
        mAllGroupsModel.initMutualGroups(bundle);
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

    @Override
    public void onAllGroupsEmpty() {
            mAllGroupsView.showGroupsEmpty();
        }

    @Override
    public void ongetAllGroupsSuccess() {
        mAllGroupsView.dismissProgressbar();
        onCreateAllGroupsAdapter();
    }

    @Override
    public void ongetAllGroupsFailed(String message) {
        mAllGroupsView.dismissProgressbar();
        mAllGroupsView.showMessage(message);
    }

    @Override
    public void onMutualGroupsEmpty() {
        mAllGroupsView.dismissProgressbar();
        mAllGroupsView.showGroupsEmpty();
    }

    @Override
    public void ongetMutualGroupsSuccess() {
        mAllGroupsView.dismissProgressbar();
        onCreateMutualGroupsAdapter();
    }

}