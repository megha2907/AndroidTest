package in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByPaytm;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;

import com.jeeva.android.BaseFragment;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.module.contest.dto.Contest;
import in.sportscafe.nostragamus.module.contest.dto.JoinContestData;
import in.sportscafe.nostragamus.module.contest.ui.ContestsActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.AddMoneyWalletHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.selectPaymentMode.SelectPaymentModeActivity;
import in.sportscafe.nostragamus.module.nostraHome.ui.NostraHomeActivity;
import in.sportscafe.nostragamus.module.user.login.dto.BasicUserInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddMoneyThroughPaytmFragment extends BaseFragment implements View.OnClickListener {

    private static final String BLOCKED_CHARACTERS = ".~#^|$%&*!@_-+/:;!?,";

    private AddMoneyThroughPaytmFragmentListener mFragmentListener;
    private EditText mAmountEditText;
    private AddMoneyPaytmFragmentLaunchedFrom mLaunchedFrom = AddMoneyPaytmFragmentLaunchedFrom.ADD_MONEY_INTO_WALLET;
    private double mDifferenceAmount = 0;

    private InputFilter mAmountInputFilter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source != null && BLOCKED_CHARACTERS.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    public AddMoneyThroughPaytmFragment() {
    }

    public void setLaunchedFrom(AddMoneyPaytmFragmentLaunchedFrom launchedFrom) {
        this.mLaunchedFrom = launchedFrom;
    }

    public void setDifferenceAmount(double differenceAmount) {
        this.mDifferenceAmount = differenceAmount;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_money_through_paytm, container, false);
        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {
        rootView.findViewById(R.id.add_money_50_textView).setOnClickListener(this);
        rootView.findViewById(R.id.add_money_100_textView).setOnClickListener(this);
        rootView.findViewById(R.id.add_money_250_textView).setOnClickListener(this);
        rootView.findViewById(R.id.wallet_add_amount_button).setOnClickListener(this);
        rootView.findViewById(R.id.add_money_by_payment_coupon_button_textView).setOnClickListener(this);

        mAmountEditText = (EditText) rootView.findViewById(R.id.wallet_add_amount_editText);
        mAmountEditText.setFilters(new InputFilter[]{mAmountInputFilter});

        initPaymentCouponLayout(rootView);
    }

    private void initPaymentCouponLayout(View rootView) {
        if (BuildConfig.IS_ACL_VERSION) {
            LinearLayout paymentCouponLayout = (LinearLayout) rootView.findViewById(R.id.payment_coupon_button_layout);
            paymentCouponLayout.setVisibility(View.VISIBLE);
        }
    }

    public void setFragmentListener(AddMoneyThroughPaytmFragmentListener fragmentListener) {
        this.mFragmentListener = fragmentListener;
    }

    public AddMoneyThroughPaytmFragmentListener getFragmentListener() {
        return mFragmentListener;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initEditText();
    }

    private void initEditText() {
        switch (mLaunchedFrom) {
            case ADD_MONEY_LOW_BALANCE:
                if (mAmountEditText != null) {
                    mAmountEditText.setText(String.valueOf(mDifferenceAmount));
                    setEditTextSelection();
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_money_50_textView:
                onAddMoney50Clicked();
                break;

            case R.id.add_money_100_textView:
                onAddMoney100Clicked();
                break;

            case R.id.add_money_250_textView:
                onAddMoney250Clicked();
                break;

            case R.id.wallet_add_amount_button:
                onAddAmountClicked();
                break;

            case R.id.add_money_by_payment_coupon_button_textView:
                onPaymentCouponClicked();
                break;
        }
    }

    private void onPaymentCouponClicked() {
        if (mFragmentListener != null) {
            mFragmentListener.launchPaymentCouponFragment(null);
        }
    }

    private void onAddAmountClicked() {
        double amount = AddMoneyWalletHelper.validateAddMoneyAmountValid(mAmountEditText.getText().toString().trim());
        if (getView() != null) {
            TextView errorTextView = (TextView) getView().findViewById(R.id.add_wallet_money_amt_error_textView);
            if (amount > 0) {
                errorTextView.setVisibility(View.INVISIBLE);
                navigateToSelectPaymentMode(amount);
            } else {
                errorTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void navigateToSelectPaymentMode(double amount) {

        switch (mLaunchedFrom) {
            case ADD_MONEY_LOW_BALANCE:
                if (getActivity() != null && getArguments() != null && getArguments().containsKey(Constants.BundleKeys.JOIN_CONTEST_DATA)) {
                    JoinContestData joinContestData = Parcels.unwrap(getArguments().getParcelable(Constants.BundleKeys.JOIN_CONTEST_DATA));

                    Bundle args = new Bundle();
                    args.putParcelable(Constants.BundleKeys.JOIN_CONTEST_DATA, Parcels.wrap(joinContestData));
                    args.putDouble(Constants.BundleKeys.TRANSACTION_AMOUNT, amount);

                    Intent clearTaskIntent = new Intent(getContext().getApplicationContext(), SelectPaymentModeActivity.class);
                    clearTaskIntent.putExtra(Constants.BundleKeys.SCREEN_LAUNCHED_FROM_PARENT, SelectPaymentModeActivity.LaunchedFrom.ADD_MONEY_LOW_BALANCE);
                    clearTaskIntent.putExtras(args);
                    clearTaskIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(clearTaskIntent);
                }
                break;

            case ADD_MONEY_INTO_WALLET:
                if (getActivity() != null) {
                    Bundle bundle = new Bundle();
                    bundle.putDouble(Constants.BundleKeys.TRANSACTION_AMOUNT, amount);
                    Intent clearTaskIntent = new Intent(getContext().getApplicationContext(), SelectPaymentModeActivity.class);
                    clearTaskIntent.putExtra(Constants.BundleKeys.SCREEN_LAUNCHED_FROM_PARENT,SelectPaymentModeActivity.LaunchedFrom.ADD_MONEY_INTO_WALLET);
                    clearTaskIntent.putExtras(bundle);
                    clearTaskIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(clearTaskIntent);
                }
        }


    }

    private void onAddMoney250Clicked() {
        double amt = WalletHelper.addMoreAmount(mAmountEditText.getText().toString().trim(), 250);
        mAmountEditText.setText(String.valueOf((int) amt));
        setEditTextSelection();
    }

    private void onAddMoney100Clicked() {
        double amt = WalletHelper.addMoreAmount(mAmountEditText.getText().toString().trim(), 100);
        mAmountEditText.setText(String.valueOf((int) amt));
        setEditTextSelection();
    }

    private void onAddMoney50Clicked() {
        double amt = WalletHelper.addMoreAmount(mAmountEditText.getText().toString().trim(), 50);
        mAmountEditText.setText(String.valueOf((int) amt));
        setEditTextSelection();
    }

    private void setEditTextSelection() {
        int length = mAmountEditText.getText().length();
        mAmountEditText.setSelection(length, length);
    }


}
