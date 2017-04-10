package in.sportscafe.nostragamus.module.paytm;

import android.support.annotation.NonNull;
import android.util.Patterns;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 23/03/17.
 */
public class PaytmAddDetailModelImpl implements PaytmAddDetailModel {

    private OnPaytmAddDetailModelListener mAddDetailModelListener;

    public PaytmAddDetailModelImpl(OnPaytmAddDetailModelListener listener) {
        mAddDetailModelListener = listener;
    }

    public static PaytmAddDetailModel newInstance(OnPaytmAddDetailModelListener listener) {
        return new PaytmAddDetailModelImpl(listener);
    }

    @Override
    public void savePaytmDetails(String mobNumber, String confirmMobNumber, String email, String confirmEmail) {
        if (mobNumber.length() == 0 || !Patterns.PHONE.matcher(mobNumber).matches()) {
            mAddDetailModelListener.onInvalidNumber();
            return;
        }

        if (!mobNumber.equalsIgnoreCase(confirmMobNumber)) {
            mAddDetailModelListener.onConfirmNumberVary();
            return;
        }

        if (email.length() == 0 || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mAddDetailModelListener.onInvalidEmail();
            return;
        }

        if (!email.equalsIgnoreCase(confirmEmail)) {
            mAddDetailModelListener.onConfirmEmailVary();
            return;
        }

//        callAddDetailApi(mobNumber, email);
    }

    private void callAddDetailApi(UserPaymentModes userPaymentModes, String mobileNumber,
                                  String accountNo, String name, String ifsCode) {
        if (!Nostragamus.getInstance().hasNetworkConnection()) {
            mAddDetailModelListener.onNoInternet();
            return;
        }

        mAddDetailModelListener.onApiCallStarted();

        switch (userPaymentModes) {
            case BANK:
                AddUserPaymentBankRequest bankRequest = new AddUserPaymentBankRequest();
                bankRequest.setPaymentMode(Constants.AddUserPaymentDetailsPaymentModes.BANK);
                bankRequest.setAccountNo(accountNo);
                bankRequest.setName(name);
                bankRequest.setIfscCode(ifsCode);

                MyWebService.getInstance().addUserPaymentBankDetails(bankRequest).enqueue(getUserPaymentCallBack());
                break;
            case PAYTM:
                AddUserPaymentPaytmRequest paytmRequest = new AddUserPaymentPaytmRequest();
                paytmRequest.setPaymentMode(Constants.AddUserPaymentDetailsPaymentModes.BANK);
                paytmRequest.setMobile(mobileNumber);

                MyWebService.getInstance().addUserPaymentPaytmDetails(paytmRequest).enqueue(getUserPaymentCallBack());
                break;
        }

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!mAddDetailModelListener.onApiCallStopped()) {
                    return;
                }

                mAddDetailModelListener.onAddDetailSuccess();
            }
        }, 3000);*/
    }

    @NonNull
    private NostragamusCallBack<AddUserPaymentDetailsResponse> getUserPaymentCallBack() {
        return new NostragamusCallBack<AddUserPaymentDetailsResponse>() {
            @Override
            public void onResponse(Call<AddUserPaymentDetailsResponse> call, Response<AddUserPaymentDetailsResponse> response) {
                super.onResponse(call, response);

                if (!mAddDetailModelListener.onApiCallStopped()) {
                    return;
                }

                if (response.isSuccessful()) {
                    mAddDetailModelListener.onAddDetailSuccess();
                } else {
                    mAddDetailModelListener.onAddDetailFailed();
                }

            }
        };
    }

    public interface OnPaytmAddDetailModelListener {

        void onApiCallStarted();

        boolean onApiCallStopped();

        void onInvalidNumber();

        void onConfirmNumberVary();

        void onInvalidEmail();

        void onConfirmEmailVary();

        void onAddDetailSuccess();

        void onNoInternet();

        void onAddDetailFailed();
    }
}