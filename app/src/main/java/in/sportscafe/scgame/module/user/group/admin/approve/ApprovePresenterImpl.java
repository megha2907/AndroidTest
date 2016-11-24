package in.sportscafe.scgame.module.user.group.admin.approve;

import in.sportscafe.scgame.Constants;

/**
 * Created by Jeeva on 2/7/16.
 */
public class ApprovePresenterImpl implements ApprovePresenter, ApproveModelImpl.OnApproveModelListener {

    private ApproveView mApproveView;

    private ApproveModel mApproveModel;

    private ApprovePresenterImpl(ApproveView approveView) {
        this.mApproveView = approveView;
        this.mApproveModel = ApproveModelImpl.newInstance(this);
    }

    public static ApprovePresenter newInstance(ApproveView approveView) {
        return new ApprovePresenterImpl(approveView);
    }

    @Override
    public void onCreateApprove(long groupId, ApproveFragment.OnApproveAcceptedListener listener) {
        mApproveView.setAdapter(mApproveModel.init(mApproveView.getContext(), groupId, listener));
    }

    @Override
    public void onApprovalSuccess(String message) {
        mApproveView.dismissProgressbar();
        mApproveView.showMessage(message);
    }

    @Override
    public void onNoInternet() {
        mApproveView.dismissProgressbar();
        mApproveView.showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onEmpty() {
        mApproveView.dismissProgressbar();
        mApproveView.showInAppMessage(Constants.Alerts.APPROVE_EMPTY);
    }

    @Override
    public void onApprovalFailed(String message) {
        mApproveView.dismissProgressbar();
        mApproveView.showMessage(message);
    }
}