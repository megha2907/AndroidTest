package in.sportscafe.nostragamus.module.navigation.wallet.addMoney.lowBalance;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
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
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeConfig;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.AddMoneyWalletHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddMoneyOnLowBalanceFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = AddMoneyOnLowBalanceFragment.class.getSimpleName();

    private AddMoneyOnLowBalanceFragmentListener mFragmentListener;
    private EditText mAmountEditText;

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
        rootView.findViewById(R.id.add_money_50_textView).setOnClickListener(this);
        rootView.findViewById(R.id.add_money_100_textView).setOnClickListener(this);
        rootView.findViewById(R.id.add_money_250_textView).setOnClickListener(this);
        rootView.findViewById(R.id.low_bal_add_amount_button).setOnClickListener(this);

        mAmountEditText = (EditText) rootView.findViewById(R.id.low_bal_add_amount_editText);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initBalanceDetails();
    }

    private void initBalanceDetails() {
        Bundle args = getArguments();
        if (args != null && args.containsKey(Constants.BundleKeys.CHALLENGE_CONFIG)) {
            ChallengeConfig challengeConfig = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.CHALLENGE_CONFIG));

            if (challengeConfig != null) {
                double totalBal = WalletHelper.getTotalBalance();
                int entryFee = challengeConfig.getEntryFee();
                double diff = totalBal - entryFee;
                if (diff < 0) {
                    mLowBalanceDifferenceAmount = -diff;
                    showDetailsOnUi(totalBal, entryFee, challengeConfig.getConfigName());
                } else {
                    Log.d(TAG, "Low balance difference not proper : " + diff);
                    showMessage(Constants.Alerts.SOMETHING_WRONG);
                }
            }

        } else {
            if (mFragmentListener != null) {
                mFragmentListener.onBackClicked();
            }
        }
    }

    private void showDetailsOnUi(double totalBal, int entryFee, String configName) {
        // Show on UI
        View view = getView();
        if (view != null && getActivity() != null) {
            TextView totalBalTextView = (TextView) view.findViewById(R.id.low_bal_amount_textView);
            TextView entryFeeTextView = (TextView) view.findViewById(R.id.low_bal_contest_entry_fee_textView);
            TextView lowBalMsgTextView = (TextView) view.findViewById(R.id.low_bal_details_msg_textView);

            if (totalBal > 0) {
                totalBalTextView.setText(WalletHelper.getFormattedStringOfAmount(totalBal));
            }
            if (entryFee > 0) {
                entryFeeTextView.setText(WalletHelper.getFormattedStringOfAmount(entryFee));
            }

            setMessageText(configName, lowBalMsgTextView);

            if (mAmountEditText != null) {
                mAmountEditText.setText(String.valueOf(mLowBalanceDifferenceAmount));
                setEditTextSelection();
            }
        }
    }

    private void setMessageText(String configName, TextView lowBalMsgTextView) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String msg1 = "Low balance! You need to add ";
        SpannableString spannable1 = new SpannableString(msg1);

        SpannableString spannable2 = null;
        if (mLowBalanceDifferenceAmount > 0) {
            String msg2 = WalletHelper.getFormattedStringOfAmount(mLowBalanceDifferenceAmount);
            spannable2 = new SpannableString(msg2);
            spannable2.setSpan(new AbsoluteSizeSpan(16), 0, msg2.length(), 0);
        } else {
            spannable2 = new SpannableString("");
        }

        String msg3 = " more to wallet to join " + ((!TextUtils.isEmpty(configName)) ? configName : "");
        SpannableString spannable3 = new SpannableString(msg3);

        builder.append(spannable1).append(spannable2).append(spannable3);
        lowBalMsgTextView.setText(builder, TextView.BufferType.SPANNABLE);
    }

    private void onAddMoney250Clicked() {
        double amt = WalletHelper.addMoreAmount(mAmountEditText.getText().toString().trim(), 250);
        mAmountEditText.setText(String.valueOf(amt));
        setEditTextSelection();
    }

    private void onAddMoney100Clicked() {
        double amt = WalletHelper.addMoreAmount(mAmountEditText.getText().toString().trim(), 100);
        mAmountEditText.setText(String.valueOf(amt));
        setEditTextSelection();
    }

    private void onAddMoney50Clicked() {
        double amt = WalletHelper.addMoreAmount(mAmountEditText.getText().toString().trim(), 50);
        mAmountEditText.setText(String.valueOf(amt));
        setEditTextSelection();
    }

    private void setEditTextSelection() {
        int length = mAmountEditText.getText().length();
        mAmountEditText.setSelection(length, length);
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

            case R.id.low_bal_add_amount_button:
                onAddAmountClicked();
                break;
        }
    }

    private void onAddAmountClicked() {
        double amount = AddMoneyWalletHelper.validateAddMoneyAmountValid(mAmountEditText.getText().toString().trim());
        if (amount > 0 && amount >= mLowBalanceDifferenceAmount) {
            AddMoneyWalletHelper.initTransaction(this, amount);
        } else {
            if (mAmountEditText != null) {
                mAmountEditText.setError("Please enter min amount required to continue");
            }
        }
    }

    /**
     * Has been invoked by AddMoneyWalletHelper once the transaction has been completed.
     */
    public void onAddMoneySuccess() {
        if (mFragmentListener != null) {
            mFragmentListener.onSuccess(getArguments());
        }
    }
}
