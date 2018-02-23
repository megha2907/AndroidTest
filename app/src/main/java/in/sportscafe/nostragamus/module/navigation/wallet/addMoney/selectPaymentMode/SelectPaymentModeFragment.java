package in.sportscafe.nostragamus.module.navigation.wallet.addMoney.selectPaymentMode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeeva.android.BaseFragment;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.NavigationFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.AddMoneyWalletHelper;

/**
 * Created by deepanshi on 2/23/18.
 */

public class SelectPaymentModeFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = NavigationFragment.class.getSimpleName();

    public SelectPaymentModeFragment() {
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
            AddMoneyWalletHelper.initTransaction(this, amount);
        }
    }

    private void onAddMoneyByUPIClicked() {
    }

    private void onAddMoneyByCreditCardClicked() {
    }

    private void onAddMoneyByDebitCardClicked() {
    }

    private void onAddMoneyByPaytmClicked() {
        if (getAmountDetails() > 0) {
            double amount = getAmountDetails();
            AddMoneyWalletHelper.initTransaction(this, amount);
        }
    }

    public void onBackPressed() {
    }
}
