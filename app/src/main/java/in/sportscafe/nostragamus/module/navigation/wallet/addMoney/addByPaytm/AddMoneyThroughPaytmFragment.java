package in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByPaytm;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import in.sportscafe.nostragamus.R;
import com.jeeva.android.BaseFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.AddMoneyWalletHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddMoneyThroughPaytmFragment extends BaseFragment implements View.OnClickListener {

    private AddMoneyThroughPaytmFragmentListener mFragmentListener;
    private EditText mAmountEditText;

    public AddMoneyThroughPaytmFragment() {
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
    }

    public void setFragmentListener(AddMoneyThroughPaytmFragmentListener fragmentListener) {
        this.mFragmentListener = fragmentListener;
    }

    public AddMoneyThroughPaytmFragmentListener getFragmentListener() {
        return mFragmentListener;
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
            TextView errorTextView = (TextView) getView().findViewById(R.id.add_payment_coupon_error_textView);

            if (amount > 0) {
                errorTextView.setVisibility(View.GONE);

                AddMoneyWalletHelper.initTransaction(this, amount);
            } else {
                /*if (mAmountEditText != null) {
                    mAmountEditText.setError("Please enter amount");
                }*/
                errorTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void onAddMoney250Clicked() {
        double amt = WalletHelper.addMoreAmount(mAmountEditText.getText().toString().trim(), 250);
        mAmountEditText.setText(String.valueOf((int)amt));
        setEditTextSelection();
    }

    private void onAddMoney100Clicked() {
        double amt = WalletHelper.addMoreAmount(mAmountEditText.getText().toString().trim(), 100);
        mAmountEditText.setText(String.valueOf((int)amt));
        setEditTextSelection();
    }

    private void onAddMoney50Clicked() {
        double amt = WalletHelper.addMoreAmount(mAmountEditText.getText().toString().trim(), 50);
        mAmountEditText.setText(String.valueOf((int)amt));
        setEditTextSelection();
    }

    private void setEditTextSelection() {
        int length = mAmountEditText.getText().length();
        mAmountEditText.setSelection(length, length);
    }


}
