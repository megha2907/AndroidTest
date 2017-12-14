package in.sportscafe.nostragamus.module.navigation.wallet.addMoney.lowBalance;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.Log;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.contest.dto.JoinContestData;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.AddMoneyWalletHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByPaymentCoupon.AddMoneyThroughPaymentCouponFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByPaymentCoupon.AddMoneyThroughPaymentCouponFragmentListener;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByPaytm.AddMoneyPaytmFragmentLaunchedFrom;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByPaytm.AddMoneyThroughPaytmFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByPaytm.AddMoneyThroughPaytmFragmentListener;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.PaytmTransactionSuccessDialogFragment;
import in.sportscafe.nostragamus.module.store.dto.StoreItems;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddMoneyOnLowBalanceFragment extends BaseFragment implements View.OnClickListener,
        AddMoneyThroughPaytmFragmentListener, AddMoneyThroughPaymentCouponFragmentListener {

    private static final String TAG = AddMoneyOnLowBalanceFragment.class.getSimpleName();

    private AddMoneyOnLowBalanceFragmentListener mFragmentListener;
    private double mLowBalanceDifferenceAmount = 0d;

    public AddMoneyOnLowBalanceFragment() {}

    public AddMoneyOnLowBalanceFragmentListener getFragmentListener() {
        return mFragmentListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddMoneyOnLowBalanceFragmentListener) {
            mFragmentListener = (AddMoneyOnLowBalanceFragmentListener) context;
        } else {
            throw new RuntimeException("Activity must implement " +
                    AddMoneyOnLowBalanceFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_money_on_low_balance, container, false);
        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {
        rootView.findViewById(R.id.back_button).setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadAddMoneyByPaytmFragment();
        initBalanceDetails();
    }

    private void loadAddMoneyByPaytmFragment() {
        if (getActivity() != null) {
            AddMoneyThroughPaytmFragment fragment = new AddMoneyThroughPaytmFragment();
            fragment.setLaunchedFrom(AddMoneyPaytmFragmentLaunchedFrom.ADD_MONEY_LOW_BALANCE);
            fragment.setFragmentListener(this);

            FragmentManager fragmentManager = getChildFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.add_money_fragment_container,
                    fragment, fragment.getClass().getSimpleName()).commit();
            fragmentManager.executePendingTransactions();
        }
    }


    private void initBalanceDetails() {
        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey(Constants.BundleKeys.JOIN_CONTEST_DATA)) {
                setUiAsLowBalFromChallenge(args);
            } else if (args.containsKey(Constants.BundleKeys.STORE_ITEM)) {
                setUiAsLowBalFromBuyFromStore(args);
            }
        } else {
            if (mFragmentListener != null) {
                mFragmentListener.onBackClicked();
            }
        }
    }

    private void setUiAsLowBalFromBuyFromStore(Bundle args) {
        StoreItems storeItems = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.STORE_ITEM));
        if (storeItems != null && getView() != null) {

            double totalBal = WalletHelper.getTotalBalance();
            int productPrice = storeItems.getProductPrice();
            if (storeItems.getProductSaleInfo() != null && storeItems.getProductSaleInfo().getSaleOn()) {
                productPrice = storeItems.getProductSaleInfo().getSalePrice();
            }

            TextView productLabelTextView = (TextView) getView().findViewById(R.id.low_money_product_name_textView);
            if (!TextUtils.isEmpty(storeItems.getProductName())) {
                productLabelTextView.setText(storeItems.getProductName() + " Price");
            } else {
                productLabelTextView.setText("Powerup Price");
            }

            double diff = totalBal - productPrice;
            if (diff < 0) {
                mLowBalanceDifferenceAmount = -diff;
                showDetailsOnUi(totalBal, productPrice);
                setMessageText(storeItems.getProductName(), "buy");
            } else {
                Log.d(TAG, "Low balance difference not proper : " + diff);
                showMessage(Constants.Alerts.SOMETHING_WRONG);
            }
        }
    }

    private void setUiAsLowBalFromChallenge(Bundle args) {
        JoinContestData joinContestData = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.JOIN_CONTEST_DATA));
        if (joinContestData != null) {
            double totalBal = WalletHelper.getTotalBalance();
            double entryFee = joinContestData.getEntryFee();
            double diff = totalBal - entryFee;
            if (diff < 0) {
                mLowBalanceDifferenceAmount = -diff;
                showDetailsOnUi(totalBal, entryFee);
                setMessageText(joinContestData.getChallengeName(), "join");
            } else {
                Log.d(TAG, "Low balance difference not proper : " + diff);
                showMessage(Constants.Alerts.SOMETHING_WRONG);
            }
        }
    }

    private void showDetailsOnUi(double totalBal, double entryFee) {
        // Show on UI
        View view = getView();
        if (view != null && getActivity() != null) {
            TextView totalBalTextView = (TextView) view.findViewById(R.id.low_bal_amount_textView);
            TextView entryFeeTextView = (TextView) view.findViewById(R.id.low_bal_contest_entry_fee_textView);

            totalBalTextView.setText(WalletHelper.getFormattedStringOfAmount(totalBal));
            if (entryFee > 0) {
                entryFeeTextView.setText(WalletHelper.getFormattedStringOfAmount(entryFee));
            }

            sendLowMoneyDifferenceToPaytmFragment(mLowBalanceDifferenceAmount);
        }
    }

    private void sendLowMoneyDifferenceToPaytmFragment(double lowBalAmt) {
        if (getView() != null && getChildFragmentManager() != null) {
            Fragment fragment = getChildFragmentManager().findFragmentById(R.id.add_money_fragment_container);
            if (fragment != null && fragment instanceof AddMoneyThroughPaytmFragment) {
                ((AddMoneyThroughPaytmFragment) fragment).setDifferenceAmount(lowBalAmt);
            }
        }
    }

    private void setMessageText(String buyProductName, String operationStr) {
        if (getView() != null) {
            SpannableStringBuilder builder = new SpannableStringBuilder();
            String msg1 = "Low balance! Add at least ";
            SpannableString spannable1 = new SpannableString(msg1);

            SpannableString spannable2 = null;
            if (mLowBalanceDifferenceAmount > 0) {
                String msg2 = WalletHelper.getFormattedStringOfAmount(mLowBalanceDifferenceAmount);
                spannable2 = new SpannableString(msg2);
                spannable2.setSpan(new RelativeSizeSpan(1.20f), 0, msg2.length(), 0);
            } else {
                spannable2 = new SpannableString("");
            }

            String msg3 = " to " + operationStr + " " + ((!TextUtils.isEmpty(buyProductName)) ? buyProductName : "");
            SpannableString spannable3 = new SpannableString(msg3);

            builder.append(spannable1).append(spannable2).append(spannable3);

            TextView lowBalMsgTextView = (TextView) getView().findViewById(R.id.low_bal_details_msg_textView);
            lowBalMsgTextView.setText(builder, TextView.BufferType.SPANNABLE);
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

    @Override
    public void onPaytmMoneyAddSuccess() {
        if (mFragmentListener != null) {
            mFragmentListener.onSuccess(getArguments());
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
                    mFragmentListener.onSuccess(getArguments());
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
