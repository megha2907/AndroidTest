package in.sportscafe.nostragamus.module.navigation.wallet.withdrawMoney;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

    public WithdrawWalletMoneyFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WithdrawWalletMoneyFragmentListener) {
            mFragmentListener = (WithdrawWalletMoneyFragmentListener) context;
        } else {
            throw new RuntimeException("Activity must implement " +
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
        final TextView receiveAmountTextView = (TextView) rootView.findViewById(R.id.receive_amt_textView);

        mAmountEditText = (EditText) rootView.findViewById(R.id.wallet_withdraw_amount_editText);
        mAmountEditText.setOnClickListener(this);

        UserInfo userInfo = Nostragamus.getInstance().getServerDataManager().getUserInfo();

        if (userInfo != null && userInfo.getInfoDetails() != null) {

            Boolean isFirstWithdrawalDone = userInfo.getInfoDetails().getFirstWithdrawDone();

            if (isFirstWithdrawalDone == null || !isFirstWithdrawalDone) {

                mAmountEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        if (!TextUtils.isEmpty(s.toString())) {
                            if (Integer.valueOf(s.toString()) > 20 && WalletHelper.getWinningAmount() > 20) {
                                if (Integer.valueOf(s.toString()) < 100) {
                                    receiveAmountTextView.setText("You will receive " + Constants.RUPEE_SYMBOL + String.valueOf(Integer.valueOf(s.toString()) - 20));
                                } else {
                                    receiveAmountTextView.setText("You will receive " + Constants.RUPEE_SYMBOL + String.valueOf(Integer.valueOf(s.toString())));
                                }
                                receiveAmountTextView.setVisibility(View.VISIBLE);
                                hideErrorText();
                            } else {
                                receiveAmountTextView.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

            }
        }

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
            TextView withdrawTextView = (TextView) getView().findViewById(R.id.withdraw_text_textView);

            Integer walletInit = userInfo.getInfoDetails().getWalletInit();
            Boolean isFirstWithdrawalDone = userInfo.getInfoDetails().getFirstWithdrawDone();

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
                    withdrawTextView.setText("Minimum Withdraw Amount is " + Constants.RUPEE_SYMBOL + "21");

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

            case R.id.wallet_withdraw_amount_editText:
                scrollView();
                break;
        }
    }

    private void scrollView() {
        if (getView() != null) {
            final ScrollView scrollView = (ScrollView) getView().findViewById(R.id.withdraw_money_sv);
            scrollView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    View lastChild = scrollView.getChildAt(scrollView.getChildCount() - 1);
                    int bottom = lastChild.getBottom() + scrollView.getPaddingBottom();
                    int sy = scrollView.getScrollY();
                    int sh = scrollView.getHeight();
                    int delta = bottom - (sy + sh);
                    scrollView.smoothScrollBy(0, delta);
                }
            }, 200);
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
        UserInfo userInfo = Nostragamus.getInstance().getServerDataManager().getUserInfo();
        if (userInfo != null && userInfo.getInfoDetails() != null && getView() != null) {

            Boolean isFirstWithdrawalDone = userInfo.getInfoDetails().getFirstWithdrawDone();

            /* Check If first withdrawal made , First Withdraw Amount Should be greater than 20 */
            if (isFirstWithdrawalDone == null || !isFirstWithdrawalDone) {

                if (withdrawalAmount > 20) {
                    hideErrorText();
                    if (withdrawalAmount <= WalletHelper.getWinningAmount()) {

                        Bundle args = new Bundle();
                        args.putDouble(Constants.BundleKeys.WALLET_WITHDRAWAL_AMT, withdrawalAmount);
                        if (mFragmentListener != null) {
                            mFragmentListener.onWithdrawButtonClicked(args);
                        }

                    } else {
                        setErrorText("You can't withdraw more than your winnings");
                    }
                } else if (withdrawalAmount > 0 && withdrawalAmount <= 20 && withdrawalAmount > WalletHelper.getWinningAmount()) {
                    setErrorText("You can't withdraw more than your winnings");
                } else if (withdrawalAmount > 0 && withdrawalAmount <= 20) {
                    setErrorText("Minimum Withdraw Amount is " + Constants.RUPEE_SYMBOL + "21");
                } else {
                    setErrorText("Please Enter a valid amount");
                }

            } else {
                if (withdrawalAmount > 0) {
                    hideErrorText();
                    if (withdrawalAmount <= WalletHelper.getWinningAmount()) {

                        Bundle args = new Bundle();
                        args.putDouble(Constants.BundleKeys.WALLET_WITHDRAWAL_AMT, withdrawalAmount);
                        if (mFragmentListener != null) {
                            mFragmentListener.onWithdrawButtonClicked(args);
                        }
                    } else {
                        setErrorText("You can't withdraw more than your winnings");
                    }
                } else {
                    setErrorText("Please Enter a valid amount");
                }
            }
        }
    }

    private void setErrorText(String errorText) {
        if (getView() != null) {
            if (mFragmentListener != null) {
                mFragmentListener.hideSoftKeyBoard();
            }
            TextView errorTextView = (TextView) getView().findViewById(R.id.withdraw_amt_error_textView);
            errorTextView.setText(errorText);
            errorTextView.setVisibility(View.VISIBLE);
            TextView receiveAmountTextView = (TextView) getView().findViewById(R.id.receive_amt_textView);
            receiveAmountTextView.setVisibility(View.GONE);
        }
    }

    private void hideErrorText() {
        if (getView() != null) {
            TextView errorTextView = (TextView) getView().findViewById(R.id.withdraw_amt_error_textView);
            errorTextView.setVisibility(View.GONE);
        }
    }

    private void setEditTextSelection() {
        int length = mAmountEditText.getText().length();
        mAmountEditText.setSelection(length, length);
    }
}
