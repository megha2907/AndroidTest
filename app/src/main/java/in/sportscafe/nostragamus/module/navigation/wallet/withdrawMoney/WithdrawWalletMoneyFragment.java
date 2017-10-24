package in.sportscafe.nostragamus.module.navigation.wallet.withdrawMoney;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;

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
        rootView.findViewById(R.id.wallet_withdraw_next_button).setOnClickListener(this);

        mAmountEditText = (EditText) rootView.findViewById(R.id.wallet_withdraw_amount_editText);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initialize();
    }

    private void initialize() {
        showWalletBalance();
        initMessages();
    }

    private void initMessages() {
        UserInfo userInfo = Nostragamus.getInstance().getServerDataManager().getUserInfo();
        if (userInfo != null && userInfo.getInfoDetails() != null && getView() != null) {
            LinearLayout msgLayout = (LinearLayout) getView().findViewById(R.id.withdraw_first_time_msg_layout);
            TextView msgTextView = (TextView) getView().findViewById(R.id.withdraw_first_time_msg_textView);

            Integer walletInit = userInfo.getInfoDetails().getWalletInit();
            Boolean isFirstWithdrawalDone =  userInfo.getInfoDetails().getFirstWithdrawDone();

            /* If first withdrawal made, do not show this msg;
             * be careful as any of the value can be null (null considered as withdrawal NOT done) */

            String msg = "Your first withdrawal needs to be ₹ 100 or more, \nOr else ";
            if (walletInit == null || walletInit < 0) {
                msg = msg + " transaction fee will be applied";
                msgTextView.setText(msg);
                msgLayout.setVisibility(View.GONE);     // IF no wallet_init available, then should not be shown

            } else if (walletInit == 0) {
                msg = "You can withdraw without any transaction fee";
                msgTextView.setText(msg);
                msgLayout.setVisibility(View.VISIBLE);

            } else {
                if (isFirstWithdrawalDone == null || !isFirstWithdrawalDone) {
                    msg = msg + WalletHelper.getFormattedStringOfAmount(walletInit) + " transaction fee will be applied";
                    msgTextView.setText(msg);
                    msgLayout.setVisibility(View.VISIBLE);

                } else {
                    msgLayout.setVisibility(View.GONE);
                }
            }
        }
    }

    private void showWalletBalance() {
        if (getView() != null) {
            double winningAmount = WalletHelper.getWinningAmount();
            TextView balanceTextView = (TextView) getView().findViewById(R.id.wallet_withdraw_money_amount_textView);
            balanceTextView.setText(WalletHelper.getFormattedStringOfAmount(winningAmount));
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

            case R.id.wallet_withdraw_next_button:
                onWithdrawNextButtonClicked();
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

    private void onWithdrawNextButtonClicked() {
        double withdrawalAmount = validateAmount();
        if (getView() != null) {
            TextView errorTextView = (TextView) getView().findViewById(R.id.withdraw_amt_error_textView);

            if (withdrawalAmount > 0) {
                errorTextView.setVisibility(View.GONE);

                if (withdrawalAmount <= WalletHelper.getWinningAmount()) {

                    Bundle args = new Bundle();
                    args.putDouble(Constants.BundleKeys.WALLET_WITHDRAWAL_AMT, withdrawalAmount);
                    if (mFragmentListener != null) {
                        mFragmentListener.onWithdrawButtonClicked(args);
                    }
                } else {
                    showMessage("Sorry, You can not withdraw more than your winnings");
                }
            } else {
                errorTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setEditTextSelection() {
        int length = mAmountEditText.getText().length();
        mAmountEditText.setSelection(length, length);
    }
}
