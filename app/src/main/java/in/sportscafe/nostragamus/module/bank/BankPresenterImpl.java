package in.sportscafe.nostragamus.module.bank;

import android.os.Bundle;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AnalyticsLabels;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;

/**
 * Created by Jeeva on 01/03/17.
 */
public class BankPresenterImpl implements BankPrensenter, BankModelImpl.OnBankModelListener {

    private BankView mBankView;

    private BankModel mBankModel;

    private BankPresenterImpl(BankView bankView) {
        this.mBankView = bankView;
        this.mBankModel = BankModelImpl.newInstance(this);
    }

    public static BankPrensenter newInstance(BankView bankView) {
        return new BankPresenterImpl(bankView);
    }

    @Override
    public void onCreateBank(Bundle bundle) {
        mBankModel.init(bundle);
    }

    @Override
    public void onClickReferFriend() {
        mBankView.navigateToReferFriend();
    }

    @Override
    public void onGet2xPowerUp(int count, boolean runningLow) {
        mBankView.set2xCount(count, runningLow);
    }

    @Override
    public void onGetNonegsPowerUp(int count, boolean runningLow) {
        mBankView.setNonegsCount(count, runningLow);
    }

    @Override
    public void onGetPollPowerUp(int count, boolean runningLow) {
        mBankView.setPollCount(count, runningLow);
    }
}