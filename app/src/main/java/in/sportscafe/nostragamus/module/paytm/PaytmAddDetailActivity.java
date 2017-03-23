package in.sportscafe.nostragamus.module.paytm;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import in.sportscafe.nostragamus.Constants.ScreenNames;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;

/**
 * Created by Jeeva on 23/03/17.
 */
public class PaytmAddDetailActivity extends NostragamusActivity implements PaytmAddDetailView {

    private EditText mEtNumber;

    private EditText mEtConfirmNumber;

    private EditText mEtEmail;

    private EditText mEtConfirmEmail;

    private PaytmAddDetailPresenter mPaytmAddDetailPresenter;

    @Override
    public String getScreenName() {
        return ScreenNames.PAYTM_ADD_DETAIL;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm_add_detail);

        mEtNumber = (EditText) findViewById(R.id.paytm_add_detail_et_number);
        mEtConfirmNumber = (EditText) findViewById(R.id.paytm_add_detail_et_confirm_number);
        mEtEmail = (EditText) findViewById(R.id.paytm_add_detail_et_email);
        mEtConfirmEmail = (EditText) findViewById(R.id.paytm_add_detail_et_confirm_email);

        this.mPaytmAddDetailPresenter = PaytmAddDetailPresenterImpl.newInstance(this);
        this.mPaytmAddDetailPresenter.onCreatePaytmAddDetail();
    }

    public void onClickSave(View view) {
        mPaytmAddDetailPresenter.onClickSave(
                getTrimmedText(mEtNumber),
                getTrimmedText(mEtConfirmNumber),
                getTrimmedText(mEtEmail),
                getTrimmedText(mEtConfirmEmail)
        );
    }

    public void onClickSkip(View view) {
        mPaytmAddDetailPresenter.onClickSkip();
    }

    @Override
    public void goBack() {
        onBackPressed();
    }
}