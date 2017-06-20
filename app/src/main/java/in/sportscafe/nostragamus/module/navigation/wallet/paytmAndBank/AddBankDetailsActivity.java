package in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.user.login.dto.UserPaymentInfo;
import in.sportscafe.nostragamus.module.user.login.dto.UserPaymentInfoBankDto;

public class AddBankDetailsActivity extends NostragamusActivity implements View.OnFocusChangeListener {

    private EditText mAccHolderNameEditText;
    private EditText mAccNumberEditText;
    private EditText mIfscodeEditText;

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.ADD_PAYMENT_BANK_DETAILS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment_bank);

        initToolBar();
        initViews();
        getUserPaymentInfoIfAvailable();
    }

    private void getUserPaymentInfoIfAvailable() {
        if (getIntent() != null &&
                getIntent().getExtras() != null &&
                getIntent().getExtras().containsKey(Constants.BundleKeys.USER_PAYMENT_INFO_PARCEL)) {

            UserPaymentInfo userPaymentInfo = Parcels.unwrap(getIntent().getExtras().getParcelable(Constants.BundleKeys.USER_PAYMENT_INFO_PARCEL));
            if (userPaymentInfo != null && userPaymentInfo.getBank() != null) {
                UserPaymentInfoBankDto bank = userPaymentInfo.getBank();

                if (!TextUtils.isEmpty(bank.getName())) {
                    mAccHolderNameEditText.setText(bank.getName());
                    mAccHolderNameEditText.setSelection(mAccHolderNameEditText.getText().toString().length());
                }
                if (!TextUtils.isEmpty(bank.getAccountNo())) {
                    mAccNumberEditText.setText(bank.getAccountNo());
                    mAccNumberEditText.setSelection(mAccNumberEditText.getText().toString().length());
                }
                if (!TextUtils.isEmpty(bank.getIfscCode())) {
                    mIfscodeEditText.setText(bank.getIfscCode());
                    mIfscodeEditText.setSelection(mIfscodeEditText.getText().toString().length());
                }
            }
        }
    }

    private void initViews() {
        mAccHolderNameEditText = (EditText) findViewById(R.id.add_bank_acc_holder_name_edittext);
        mAccNumberEditText = (EditText) findViewById(R.id.add_bank_acc_number_edittext);
        mIfscodeEditText = (EditText) findViewById(R.id.add_bank_ifsc_edittext);

        mAccHolderNameEditText.setOnFocusChangeListener(this);
        mAccNumberEditText.setOnFocusChangeListener(this);
        mIfscodeEditText.setOnFocusChangeListener(this);
    }

    public void onAddBankSaveClicked(View view) {
        hideSoftKeyboard();

        String name = getTrimmedText(mAccHolderNameEditText);
        String accNumber = getTrimmedText(mAccNumberEditText);
        String ifsCode = getTrimmedText(mIfscodeEditText);

        if (name.length() == 0) {
            mAccHolderNameEditText.setError("Please provide account holder's name");
        } else if (accNumber.length() == 0) {
            mAccNumberEditText.setError("Please provide account number");
        } else if (ifsCode.length() == 0) {
            mIfscodeEditText.setError("Please provide IFSC code");
        } else {
            // Make api call to save details

            showProgressbar();
            AddPaytmOrBankDetailModelModelImpl detailsModel = AddPaytmOrBankDetailModelModelImpl.newInstance(getCallBackListener());
            detailsModel.savePaymentBankDetails(name, accNumber, ifsCode);
        }
    }

    private AddPaytmOrBankDetailModelModelImpl.PaytmOrBankDetailModelListener getCallBackListener() {

        return new AddPaytmOrBankDetailModelModelImpl.PaytmOrBankDetailModelListener() {
            @Override
            public void onAddDetailSuccess() {
                dismissProgressbar();
                onApiSuccess();
            }

            @Override
            public void onNoInternet() {
                dismissProgressbar();
                showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
            }

            @Override
            public void onAddDetailFailed() {
                dismissProgressbar();
                showMessage(Constants.Alerts.API_FAIL);
            }
        };
    }

    private void onApiSuccess() {
        /*AlertDialog alertDialog = AppSnippet.getAlertDialog(this,
                "Details Saved",
                "Your bank details have been saved",
                getString(R.string.ok),
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishThisAndGotoPaymentHome();
            }
        });

        alertDialog.show();*/

        showMessage("Your bank details have been saved");
        finishThisAndGotoPaymentHome();
    }

    private void finishThisAndGotoPaymentHome() {
        hideSoftKeyboard();

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.bank_toolbar);
        toolbar.setTitle("Bank Details");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_icon_grey);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideSoftKeyboard();
                        finish();
                    }
                }
        );
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.add_bank_acc_holder_name_edittext:
                TextView textView = (TextView) findViewById(R.id.add_bank_accHolder_head_textview);
                if (hasFocus) {
                    textView.setTextColor(ContextCompat.getColor(this, R.color.white));
                } else {
                    textView.setTextColor(ContextCompat.getColor(this, R.color.white_60));
                }
                break;

            case R.id.add_bank_acc_number_edittext:
                textView = (TextView) findViewById(R.id.add_bank_accNumber_head_textview);
                if (hasFocus) {
                    textView.setTextColor(ContextCompat.getColor(this, R.color.white));
                } else {
                    textView.setTextColor(ContextCompat.getColor(this, R.color.white_60));
                }
                break;

            case R.id.add_bank_ifsc_edittext:
                textView = (TextView) findViewById(R.id.add_bank_ifsc_head_textview);
                if (hasFocus) {
                    textView.setTextColor(ContextCompat.getColor(this, R.color.white));
                } else {
                    textView.setTextColor(ContextCompat.getColor(this, R.color.white_60));
                }
                break;
        }
    }
}
