package in.sportscafe.nostragamus.module.user.group.mutualgroups;

import android.os.Bundle;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroupsView;

/**
 * Created by deepanshi on 1/4/17.
 */

public class MutualGroupsPresenterImpl   implements MutualGroupsPresenter, MutualGroupsModelImpl.MutualGroupsModelListener {

    private MutualGroupsView mmutualGroupsView;

    private MutualGroupsModel mMutualGroupsModel;

    private boolean mFromProfile = false;

    public MutualGroupsPresenterImpl(MutualGroupsView mutualGroupsView) {
        this.mmutualGroupsView = mutualGroupsView;
        this.mMutualGroupsModel = MutualGroupsModelImpl.newInstance(this);
    }

    public static MutualGroupsPresenter newInstance(MutualGroupsView mutualGroupsView) {
        return new MutualGroupsPresenterImpl(mutualGroupsView);
    }

    @Override
    public void onCreateMutualGroups(Bundle bundle) {
        mmutualGroupsView.showProgressbar();
        mMutualGroupsModel.init(bundle);
    }

    @Override
    public void onCreateMutualGroupsAdapter() {
        mmutualGroupsView.setAdapter(mMutualGroupsModel
                .getMutualGroupsAdapter(mmutualGroupsView.getContext()));
    }

    @Override
    public void onClickNext() {
        mmutualGroupsView.showProgressbar();
    }

    @Override
    public void onNoInternet() {
        mmutualGroupsView.dismissProgressbar();
        mmutualGroupsView.showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onFailed(String message) {
        mmutualGroupsView.dismissProgressbar();
        mmutualGroupsView.showMessage(message);
    }

    @Override
    public void onMutualGroupsEmpty() {
        mmutualGroupsView.showGroupsEmpty();
    }

    @Override
    public void ongetMutualGroupsSuccess() {
        mmutualGroupsView.dismissProgressbar();
        onCreateMutualGroupsAdapter();
    }

    @Override
    public void ongetMutualGroupsFailed(String message) {
        mmutualGroupsView.dismissProgressbar();
        mmutualGroupsView.showMessage(message);
    }
}