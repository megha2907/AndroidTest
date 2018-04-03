package in.sportscafe.nostragamus.module.navigation.wallet.doKYC;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.popups.PopUpDialogActivity;

/**
 * Created by deepanshi on 3/30/18.
 */

public class KYCVerificationInProgressPopup extends PopUpDialogActivity implements View.OnClickListener {

    private static final String TAG = KYCVerificationInProgressPopup.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyc_verification_popup);
        initView();
    }

    private void initView() {
        (findViewById(R.id.popup_cross_btn)).setOnClickListener(this);
        (findViewById(R.id.popup_back_button)).setOnClickListener(this);
        (findViewById(R.id.popup_bg)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_cross_btn:
                onBackPressed();
                break;

            case R.id.popup_bg:
                onBackPressed();
                break;

            case R.id.popup_back_button:
                onBackPressed();
                break;
        }
    }

}