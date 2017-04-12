package in.sportscafe.nostragamus.module.paytm;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;

public class AddPaymentBankActivity extends NostragamusActivity {

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.ADD_PAYMENT_BANK_DETAILS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment_bank);

        initToolBar();
        initViews();

    }

    private void initViews() {

    }

    public void onAddBankSaveClicked(View view) {
        EditText accHolderNameEditText = (EditText) findViewById(R.id.add_bank_acc_holder_name_edittext);
        EditText accNumberEditText = (EditText) findViewById(R.id.add_bank_acc_number_edittext);
        EditText ifscodeEditText = (EditText) findViewById(R.id.add_bank_ifsc_edittext);

        String name = getTrimmedText(accHolderNameEditText);
        String accNumber = getTrimmedText(accNumberEditText);
        String ifsCode = getTrimmedText(ifscodeEditText);

        if (name.length() == 0) {
            accHolderNameEditText.setError("Please provide account holder's name");
        } else if (accNumber.length() == 0) {
            accNumberEditText.setError("Please provide account number");
        } else if (ifsCode.length() == 0) {
            ifscodeEditText.setError("Please provide IFSC code");
        } else {
            // Make api call to save details

            showProgressbar();
            AddPaytmOrBankDetailModelModelImpl detailsModel = AddPaytmOrBankDetailModelModelImpl.newInstance(getCallBackListener());
            detailsModel.savePaymentBankDetails(name, accNumber, ifsCode);
        }
    }

    public void onAddBankSkipClicked(View view) {
        onBackPressed();
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
        AlertDialog alertDialog = AppSnippet.getAlertDialog(this,
                "Details Saved",
                "Your bank details have been saved",
                getString(R.string.ok),
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        alertDialog.show();
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
                        finish();
                    }
                }
        );
    }


}
