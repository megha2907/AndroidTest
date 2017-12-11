package in.sportscafe.nostragamus.module.navigation.wallet.addMoney;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByPaymentCoupon.AddMoneyThroughPaymentCouponFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByPaymentCoupon.AddMoneyThroughPaymentCouponFragmentListener;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByPaytm.AddMoneyThroughPaytmFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByPaytm.AddMoneyThroughPaytmFragmentListener;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.PaytmTransactionSuccessDialogFragment;

public class AddWalletMoneyFragment extends BaseFragment implements View.OnClickListener,
        AddMoneyThroughPaytmFragmentListener, AddMoneyThroughPaymentCouponFragmentListener {

    private static final String TAG = AddWalletMoneyFragment.class.getSimpleName();

    private AddWalletMoneyFragmentListener mFragmentListener;

    public AddWalletMoneyFragment() {
    }

    public AddWalletMoneyFragmentListener getFragmentListener() {
        return mFragmentListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddWalletMoneyFragmentListener) {
            mFragmentListener = (AddWalletMoneyFragmentListener) context;
        } else {
            throw new RuntimeException("Activity must implement " +
                    AddWalletMoneyFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_wallet_money, container, false);
        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {
        rootView.findViewById(R.id.back_button).setOnClickListener(this);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    private void initialize() {
        loadAddMoneyByPaytmFragment();
        showWalletBalance();
    }

    private void loadAddMoneyByPaytmFragment() {
        if (getActivity() != null) {
            AddMoneyThroughPaytmFragment fragment = new AddMoneyThroughPaytmFragment();
            fragment.setFragmentListener(this);

            FragmentManager fragmentManager = getChildFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.add_money_fragment_container,
                    fragment, fragment.getClass().getSimpleName()).commit();
            fragmentManager.executePendingTransactions();
        }
    }

    private void showWalletBalance() {
        if (getView() != null) {
            /* Showing ONLY deposit money while adding into wallet */
            double amount = WalletHelper.getDepositAmount();
            TextView balanceTextView = (TextView) getView().findViewById(R.id.wallet_add_money_amount_textView);
            balanceTextView.setText(WalletHelper.getFormattedStringOfAmount(amount));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_button:
                if (mFragmentListener != null) {
                    mFragmentListener.onBackClicked();
                }
                break;
        }
    }

    @Override
    public void onPaymentCouponFragmentBackPressed() {

    }

    @Override
    public void onPaytmMoneyAddSuccess() {
        if (mFragmentListener != null) {
            mFragmentListener.onSuccess();
        }
    }

    @Override
    public void onPaymentCouponSuccess(int moneyAdded) {
        PaytmTransactionSuccessDialogFragment successDialogFragment =
                PaytmTransactionSuccessDialogFragment.newInstance(1200, moneyAdded,
                        getSuccessActionListener());

        successDialogFragment.show(getChildFragmentManager(), "SUCCESS_DIALOG");
    }

    private PaytmTransactionSuccessDialogFragment.IPaytmSuccessActionListener
    getSuccessActionListener() {
        return new PaytmTransactionSuccessDialogFragment.IPaytmSuccessActionListener() {
            @Override
            public void onBackToHomeClicked() {
                if (mFragmentListener != null) {
                    mFragmentListener.onSuccess();
                }
            }
        };
    }

    @Override
    public void launchPaymentCouponFragment(Bundle args) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {
            AddMoneyThroughPaymentCouponFragment fragment = new AddMoneyThroughPaymentCouponFragment();
            fragment.setFragmentListener(this);

            FragmentManager manager = getChildFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_left_from_right, R.anim.activity_anim_stay, R.anim.activity_anim_stay, R.anim.slide_right_from_right);
            ft.add(R.id.add_money_fragment_container, fragment).addToBackStack(null).commit();

            if (manager.getBackStackEntryCount() > 0) {
                manager.popBackStackImmediate();
            } else {
                manager.executePendingTransactions();
            }
        }
    }

    public void onBackPressed() {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {

            Fragment fragment = getChildFragmentManager().findFragmentById(R.id.add_money_fragment_container);
            if (fragment != null && fragment instanceof AddMoneyThroughPaymentCouponFragment) {
                FragmentManager manager = getChildFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                ft.setCustomAnimations(R.anim.activity_anim_stay, R.anim.slide_right_from_right);
                ft.remove(fragment);
                ft.commit();
                manager.popBackStackImmediate();
            } else {
                getActivity().finish();
            }
        }
    }

}
