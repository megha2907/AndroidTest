package in.sportscafe.nostragamus.module.navigation.wallet.addMoney;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;

public class AddWalletMoneyFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = AddWalletMoneyFragment.class.getSimpleName();

    private AddWalletMoneyFragmentListener mFragmentListener;
    private EditText mAmountEditText;

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
        rootView.findViewById(R.id.add_money_50_textView).setOnClickListener(this);
        rootView.findViewById(R.id.add_money_100_textView).setOnClickListener(this);
        rootView.findViewById(R.id.add_money_250_textView).setOnClickListener(this);
        rootView.findViewById(R.id.wallet_add_amount_button).setOnClickListener(this);

        mAmountEditText = (EditText) rootView.findViewById(R.id.wallet_add_amount_editText);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    private void initialize() {
        showWalletBalance();
    }

    private void showWalletBalance() {
        if (getView() != null) {
            double amount = WalletHelper.getDepositAmount();
            if (amount > 0) {
                TextView balanceTextView = (TextView) getView().findViewById(R.id.wallet_add_money_amount_textView);
                balanceTextView.setText(WalletHelper.getFormattedStringOfAmount(amount));
            }
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
        }
    }

    private void onAddAmountClicked() {
        double amount = AddMoneyWalletHelper.validateAddMoneyAmountValid(mAmountEditText.getText().toString().trim());
        if (amount > 0) {
            AddMoneyWalletHelper.initTransaction(this, amount);
        } else {
            if (mAmountEditText != null) {
                mAmountEditText.setError("Please enter amount");
            }
        }
    }


    private void onAddMoney250Clicked() {
        mAmountEditText.setText("250");
        setEditTextSelection();
    }

    private void onAddMoney100Clicked() {
        mAmountEditText.setText("100");
        setEditTextSelection();
    }

    private void onAddMoney50Clicked() {
        mAmountEditText.setText("50");
        setEditTextSelection();
    }

    private void setEditTextSelection() {
        int length = mAmountEditText.getText().length();
        mAmountEditText.setSelection(length, length);
    }
}
