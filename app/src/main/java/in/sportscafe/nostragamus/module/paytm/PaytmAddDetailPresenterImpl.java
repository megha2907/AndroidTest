package in.sportscafe.nostragamus.module.paytm;

import in.sportscafe.nostragamus.Constants.Alerts;

/**
 * Created by Jeeva on 23/03/17.
 */
public class PaytmAddDetailPresenterImpl implements PaytmAddDetailPresenter,
        PaytmAddDetailModelImpl.OnPaytmAddDetailModelListener {

    private PaytmAddDetailView mPaytmAddDetailView;

    private PaytmAddDetailModel mPaytmAddDetailModel;

    private PaytmAddDetailPresenterImpl(PaytmAddDetailView view) {
        this.mPaytmAddDetailView = view;
        this.mPaytmAddDetailModel = PaytmAddDetailModelImpl.newInstance(this);
    }

    public static PaytmAddDetailPresenter newInstance(PaytmAddDetailView view) {
        return new PaytmAddDetailPresenterImpl(view);
    }

    @Override
    public void onCreatePaytmAddDetail() {

    }

    @Override
    public void onClickSave(String mobNumber, String confirmMobNumber, String email, String confirmEmail) {
        mPaytmAddDetailModel.savePaytmDetails(mobNumber, confirmMobNumber, email, confirmEmail);
    }

    @Override
    public void onClickSkip() {
        mPaytmAddDetailView.goBack();
    }

    @Override
    public void onApiCallStarted() {
        mPaytmAddDetailView.showProgressbar();
    }

    @Override
    public boolean onApiCallStopped() {
        return mPaytmAddDetailView.dismissProgressbar();
    }

    @Override
    public void onInvalidNumber() {
        mPaytmAddDetailView.showMessage(Alerts.INVALID_MOBILE_NUMBER);
    }

    @Override
    public void onConfirmNumberVary() {
        mPaytmAddDetailView.showMessage(Alerts.CONFIRM_MOBILE_NUMBER_VARY);
    }

    @Override
    public void onInvalidEmail() {
        mPaytmAddDetailView.showMessage(Alerts.INVALID_EMAIL);
    }

    @Override
    public void onConfirmEmailVary() {
        mPaytmAddDetailView.showMessage(Alerts.CONFIRM_EMAIL_VARY);
    }

    @Override
    public void onAddDetailSuccess() {
        mPaytmAddDetailView.showMessage(Alerts.PAYTM_ADD_DETAIL_SUCCESS);
        mPaytmAddDetailView.goBack();
    }

    @Override
    public void onNoInternet() {
        mPaytmAddDetailView.showMessage(Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onAddDetailFailed() {
        mPaytmAddDetailView.showMessage(Alerts.API_FAIL);
    }
}