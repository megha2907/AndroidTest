package in.sportscafe.nostragamus.module.navigation.wallet.addMoney.selectPaymentMode;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.widgets.CustomProgressbar;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.navigation.NavigationFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.AddMoneyProcessListener;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.AddMoneyWalletHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByCashFree.AddMoneyByCashFreeHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByPaymentCoupon.AddMoneyThroughPaymentCouponFragmentListener;

/**
 * Created by deepanshi on 2/23/18.
 */

public class SelectPaymentModeFragment extends BaseFragment implements View.OnClickListener, AddMoneyProcessListener {

    private static final String TAG = NavigationFragment.class.getSimpleName();

    public SelectPaymentModeFragment() {
    }

    private SelectPaymentModeFragmentListener mSelectPaymentModeFragmentListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof SelectPaymentModeActivity) {
            mSelectPaymentModeFragmentListener = (SelectPaymentModeFragmentListener) context;
        } else {
            throw new RuntimeException("Activity must implement " + TAG);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_payment_mode, container, false);
        initRootView(view);
        return view;
    }

    private void initRootView(View view) {
        setListener(view);
    }

    private void setListener(View view) {
        view.findViewById(R.id.add_money_paytm_layout).setOnClickListener(this);
        view.findViewById(R.id.add_money_debit_card_layout).setOnClickListener(this);
        view.findViewById(R.id.add_money_credit_card_layout).setOnClickListener(this);
        view.findViewById(R.id.add_money_upi_layout).setOnClickListener(this);
        view.findViewById(R.id.add_money_wallet_layout).setOnClickListener(this);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public SelectPaymentModeFragmentListener getFragmentListener() {
        return mSelectPaymentModeFragmentListener;
    }

    public void setFragmentListener(SelectPaymentModeFragmentListener mFragmentListener) {
        this.mSelectPaymentModeFragmentListener = mFragmentListener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_money_paytm_layout:
                onAddMoneyByPaytmClicked();
                break;

            case R.id.add_money_debit_card_layout:
                onAddMoneyByDebitCardClicked();
                break;

            case R.id.add_money_credit_card_layout:
                onAddMoneyByCreditCardClicked();
                break;

            case R.id.add_money_upi_layout:
                onAddMoneyByUPIClicked();
                break;

            case R.id.add_money_wallet_layout:
                onAddMoneyByOtherWalletsClicked();
                break;


        }
    }

    private double getAmountDetails() {
        double transactionAmount = 0;
        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey(Constants.BundleKeys.TRANSACTION_AMOUNT)) {
                transactionAmount = args.getDouble(Constants.BundleKeys.TRANSACTION_AMOUNT);
            }
        }
        return transactionAmount;
    }

    private void onAddMoneyByOtherWalletsClicked() {
        if (getAmountDetails() > 0) {
            double amount = getAmountDetails();
            AddMoneyByCashFreeHelper.getOrderDetails(this, amount, Constants.SelectPaymentModes.WALLET, this);
            NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.PAYMENT_MODE,Constants.SelectPaymentModes.WALLET);
        }
    }

    private void onAddMoneyByUPIClicked() {
        if (getAmountDetails() > 0) {
            double amount = getAmountDetails();
            AddMoneyByCashFreeHelper.getOrderDetails(this, amount, Constants.SelectPaymentModes.UPI, this);
            NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.PAYMENT_MODE,Constants.SelectPaymentModes.UPI);
        }
    }

    private void onAddMoneyByCreditCardClicked() {
        if (getAmountDetails() > 0) {
            double amount = getAmountDetails();
            AddMoneyByCashFreeHelper.getOrderDetails(this, amount, Constants.SelectPaymentModes.CREDIT_CARD, this);
            NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.PAYMENT_MODE,Constants.SelectPaymentModes.CREDIT_CARD);
        }
    }

    private void onAddMoneyByDebitCardClicked() {
        if (getAmountDetails() > 0) {
            double amount = getAmountDetails();
            AddMoneyByCashFreeHelper.getOrderDetails(this, amount, Constants.SelectPaymentModes.DEBIT_CARD, this);
            NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.PAYMENT_MODE,Constants.SelectPaymentModes.DEBIT_CARD);
        }
    }

    private void onAddMoneyByPaytmClicked() {
        if (getAmountDetails() > 0) {
            double amount = getAmountDetails();
            AddMoneyWalletHelper.initTransaction(this, amount,this);
            NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.PAYMENT_MODE,Constants.SelectPaymentModes.PAYTM);
        }
    }

    @Override
    public void hideProgressBar() {
        if (getContext() != null) {
            CustomProgressbar.getProgressbar(getContext()).dismissProgress();
        }
    }

    @Override
    public void showProgressBar() {
        if (getContext() != null) {
            CustomProgressbar.getProgressbar(getContext()).show();
        }
    }
}
