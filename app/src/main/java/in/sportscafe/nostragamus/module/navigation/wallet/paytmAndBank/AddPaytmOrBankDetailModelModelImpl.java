package in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.common.ErrorResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto.AddBankDetailsRequest;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto.AddUserPaymentDetailsResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto.AddUserPaymentPaytmRequest;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 23/03/17.
 */
public class AddPaytmOrBankDetailModelModelImpl implements AddPaytmDetailModel, AddBankDetailModel {

    private static final String TAG = AddPaytmOrBankDetailModelModelImpl.class.getSimpleName();
    private PaytmOrBankDetailModelListener paytmOrBankDetailModelListener;

    private AddPaytmOrBankDetailModelModelImpl(@NonNull PaytmOrBankDetailModelListener listener) {
        paytmOrBankDetailModelListener = listener;
    }

    public static AddPaytmOrBankDetailModelModelImpl newInstance(PaytmOrBankDetailModelListener listener) {
        return new AddPaytmOrBankDetailModelModelImpl(listener);
    }

    @Override
    public void savePaytmDetails(String mobileNumber) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            AddUserPaymentPaytmRequest paytmRequest = new AddUserPaymentPaytmRequest();
            paytmRequest.setPaymentMode(Constants.AddUserPaymentDetailsPaymentModes.PAYTM);
            paytmRequest.setMobile(mobileNumber);

            MyWebService.getInstance().addUserPaymentPaytmDetails(paytmRequest).enqueue(getUserPaymentCallBack());
        } else {
            paytmOrBankDetailModelListener.onNoInternet();
        }
    }

    @Override
    public void savePaymentBankDetails(String accountHolderName, String accNumber, String ifsCode) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            AddBankDetailsRequest bankRequest = new AddBankDetailsRequest();
            bankRequest.setPaymentMode(Constants.AddUserPaymentDetailsPaymentModes.BANK);
            bankRequest.setAccountNo(accNumber);
            bankRequest.setName(accountHolderName);
            bankRequest.setIfscCode(ifsCode);

            MyWebService.getInstance().addUserPaymentBankDetails(bankRequest).enqueue(getUserPaymentCallBack());
        } else {
            paytmOrBankDetailModelListener.onNoInternet();
        }
    }

    @NonNull
    private NostragamusCallBack<AddUserPaymentDetailsResponse> getUserPaymentCallBack() {
        return new NostragamusCallBack<AddUserPaymentDetailsResponse>() {
            @Override
            public void onResponse(Call<AddUserPaymentDetailsResponse> call, Response<AddUserPaymentDetailsResponse> response) {
                super.onResponse(call, response);

                if (response.code() == 400 && response.errorBody() != null) {
                    Log.d(TAG, "Response code 400");
                    ErrorResponse errorResponse = null;

                    try {
                        errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                    } catch (Exception e) {e.printStackTrace();}
                    if (paytmOrBankDetailModelListener != null) {
                        if (errorResponse != null) {
                            paytmOrBankDetailModelListener.onServerSentError(errorResponse.getError());
                        } else {
                            paytmOrBankDetailModelListener.onAddDetailFailed();
                        }
                    }

                } else {
                    if (response.isSuccessful() && response.body() != null) {
                        onPaytmOrBankDetailsAddedSuccessfully();

                    } else {
                        paytmOrBankDetailModelListener.onAddDetailFailed();
                    }
                }
            }
        };
    }

    private void onPaytmOrBankDetailsAddedSuccessfully() {
        paytmOrBankDetailModelListener.onAddDetailSuccess();
    }

    public interface PaytmOrBankDetailModelListener {
        void onAddDetailSuccess();
        void onNoInternet();
        void onAddDetailFailed();
        void onServerSentError(String errorMsg);
    }
}