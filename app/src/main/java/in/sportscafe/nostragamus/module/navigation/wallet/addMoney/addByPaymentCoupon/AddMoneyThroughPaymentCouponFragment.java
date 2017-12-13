package in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByPaymentCoupon;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.CountDownTimer;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto.VerifyPaymentCouponResponse;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddMoneyThroughPaymentCouponFragment extends BaseFragment implements View.OnClickListener {

    private AddMoneyThroughPaymentCouponFragmentListener mFragmentListener;
    private EditText mCouponCodeEditText;

    /**
     * Param used to identify that api call should be fired or not
     * If server sent any error ; then for next 15 secs, do not allow to make api call
     */
    private boolean mShouldMakeApiCall = true;

    public AddMoneyThroughPaymentCouponFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_money_through_payment_coupon, container, false);
        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {
        rootView.findViewById(R.id.wallet_add_amount_button).setOnClickListener(this);
        mCouponCodeEditText = (EditText) rootView.findViewById(R.id.wallet_add_payment_coupon_editText);
    }

    public AddMoneyThroughPaymentCouponFragmentListener getFragmentListener() {
        return mFragmentListener;
    }

    public void setFragmentListener(AddMoneyThroughPaymentCouponFragmentListener mFragmentListener) {
        this.mFragmentListener = mFragmentListener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wallet_add_amount_button:
                onAddAmountClicked();
                break;
        }
    }

    private void onAddAmountClicked() {
        if (getView() != null && mCouponCodeEditText != null) {
            TextView errorTextView = (TextView) getView().findViewById(R.id.add_payment_coupon_error_textView);
            String couponCode = mCouponCodeEditText.getText().toString().trim();

            if (!TextUtils.isEmpty(couponCode)) {

                if (couponCode.length() == 8) {

                    if (mShouldMakeApiCall) {

                        errorTextView.setVisibility(View.INVISIBLE);
                        onPaymentCouponAdded(couponCode);

                    } else {
                        showMessage("Please wait for sometimes to add amount");
                    }
                } else {
                    showError("Please Enter Correct Coupon Code");
                }
            } else {
                showError("Please Enter Coupon Code");
            }
        }
    }

    private void onPaymentCouponAdded(String couponCode) {
        showProgressbar();
        AddMoneyThoughPaymentCouponApiModelImpl.newInstance(getAddMoneyThroughPaymentCouponListener()).
                callAddMoneyPaymentCouponApi(couponCode);
    }

    private AddMoneyThoughPaymentCouponApiModelImpl.AddMoneyThroughPaymentCouponApiListener
    getAddMoneyThroughPaymentCouponListener() {
        return new AddMoneyThoughPaymentCouponApiModelImpl.AddMoneyThroughPaymentCouponApiListener() {

            @Override
            public void noInternet() {
                dismissProgressbar();
                showMessage(Constants.Alerts.NO_INTERNET_CONNECTION);
            }

            @Override
            public void onApiFailure() {
                dismissProgressbar();
                showMessage(Constants.Alerts.API_FAIL);
            }

            @Override
            public void onSuccessResponse(VerifyPaymentCouponResponse response) {
                dismissProgressbar();
                if (response != null && mFragmentListener != null) {
                    mFragmentListener.onPaymentCouponSuccess(response.getMoneyAdded());
                } else {
                    showMessage(Constants.Alerts.API_FAIL);
                }
            }

            @Override
            public void onServerSentError(String msg) {
                dismissProgressbar();
                setToWaitBeforeNextApiCallIfServerSentAnError();
                showError(msg);
            }
        };
    }

    private void setToWaitBeforeNextApiCallIfServerSentAnError() {
        mShouldMakeApiCall = false;
        new CountDownTimer(15000, 15000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // No action required
            }

            @Override
            public void onFinish() {
                mShouldMakeApiCall = true;
            }
        }.start();
    }

    private void showError(String msg) {
        if (getView() != null) {
            if (TextUtils.isEmpty(msg)) {
                msg = "Invalid Coupon Code!";
            }

            TextView errorTextView = (TextView) getView().findViewById(R.id.add_payment_coupon_error_textView);
            errorTextView.setText(msg);
            errorTextView.setVisibility(View.VISIBLE);
        }
    }
}
