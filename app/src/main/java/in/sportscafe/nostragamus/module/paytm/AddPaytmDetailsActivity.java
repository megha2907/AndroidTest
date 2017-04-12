package in.sportscafe.nostragamus.module.paytm;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.ScreenNames;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;

/**
 * Created by Jeeva on 23/03/17.
 */
public class AddPaytmDetailsActivity extends NostragamusActivity {

    private EditText mMobileNoEditText;
    private EditText mConfirmMobileNoEditText;
    private Toolbar mtoolbar;

    @Override
    public String getScreenName() {
        return ScreenNames.PAYTM_ADD_DETAIL;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm_add_detail);

        initToolBar();

        mMobileNoEditText = (EditText) findViewById(R.id.paytm_add_detail_et_number);
        mConfirmMobileNoEditText = (EditText) findViewById(R.id.paytm_add_detail_et_confirm_number);
    }

    public void onClickSave(View view) {
        String mobNo = getTrimmedText(mMobileNoEditText);
        String confirmMobNumber = getTrimmedText(mConfirmMobileNoEditText);

        if (mobNo.length() != 10) {
            mMobileNoEditText.setError("Enter 10 digit number");
            return;
        }

        if (!mobNo.equalsIgnoreCase(confirmMobNumber)) {
            mConfirmMobileNoEditText.setError("Numbers don't match");
            return;
        }

        showProgressbar();
        AddPaytmOrBankDetailModelModelImpl detailsModel = AddPaytmOrBankDetailModelModelImpl.newInstance(getCallBackListener());
        detailsModel.savePaytmDetails(mobNo);

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
                "Your Paytm details have been saved",
                getString(R.string.ok),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

        alertDialog.show();
    }
    public void onClickSkip(View view) {
        onBackPressed();
    }

    public void initToolBar() {
        mtoolbar = (Toolbar) findViewById(R.id.paytm_toolbar);
        mtoolbar.setTitle("PayTm Details");
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationIcon(R.drawable.back_icon_grey);
        mtoolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }
}