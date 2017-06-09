package in.sportscafe.nostragamus.module.navigation.wallet.withdrawMoney;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jeeva.android.BaseFragment;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.AddWalletMoneyFragmentListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class WithdrawWalletMoneyFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = WithdrawWalletMoneyFragment.class.getSimpleName();

    private WithdrawWalletMoneyFragmentListener mFragmentListener;
    private EditText mAmountEditText;

    public WithdrawWalletMoneyFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WithdrawWalletMoneyFragmentListener) {
            mFragmentListener = (WithdrawWalletMoneyFragmentListener) context;
        } else {
            throw  new RuntimeException("Activity must implement " +
                    WithdrawWalletMoneyFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_withdraw_wallet_money, container, false);
        initRoot(view);
        return view;
    }

    private void initRoot(View rootView) {
        rootView.findViewById(R.id.back_button).setOnClickListener(this);
        rootView.findViewById(R.id.wallet_withdraw_amount_button).setOnClickListener(this);

        mAmountEditText = (EditText) rootView.findViewById(R.id.wallet_withdraw_amount_editText);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_button:
                if (mFragmentListener != null) {
                    mFragmentListener.onBackClicked();
                }
                break;

            case R.id.wallet_withdraw_amount_button:
                onWithdrawButtonClicked();
                break;
        }
    }

    private double validateAmount() {
        double amount = 0;

        String str = mAmountEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(str)) {
            try {
                amount = Double.parseDouble(str);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }

        return amount;
    }

    private void onWithdrawButtonClicked() {
        double amount = validateAmount();
        if (amount > 99) {

            Bundle args = new Bundle();
            args.putDouble(Constants.BundleKeys.WALLET_WITHDRAWAL_AMT, amount);

            if (mFragmentListener != null) {
                mFragmentListener.onWithdrawButtonClicked(args);
            }
        } else {
            if (getView() != null) {
                Snackbar.make(getView(), "Please enter amount more or 100", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void setEditTextSelection() {
        int length = mAmountEditText.getText().length();
        mAmountEditText.setSelection(length, length);
    }
}
