package in.sportscafe.nostragamus.module.paytm;

import android.support.annotation.NonNull;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 23/03/17.
 */
public class AddPaytmOrBankDetailModelModelImpl implements AddPaytmDetailModel, AddPaymentBankDetailModel {

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
            AddUserPaymentBankRequest bankRequest = new AddUserPaymentBankRequest();
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
                if (response != null && response.isSuccessful()) {
                    onPaytmOrBankDetailsAddedSuccessfully();

                } else {
                    paytmOrBankDetailModelListener.onAddDetailFailed();
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
    }
}