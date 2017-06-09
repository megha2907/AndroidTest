package in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank;

/**
 * Created by deepanshi on 3/21/17.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto.GenerateOrderRequest;

/**
 * This is the sample app which will make use of the PG SDK. This activity will
 * show the usage of Paytm PG SDK API's.
 **/

public class TestPaytmActivity extends Activity implements Constants {

    private static final String TAG = TestPaytmActivity.class.getSimpleName();

    /*@Override
    public String getScreenName() {
        return TestPaytmActivity.class.getSimpleName();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm);
        initOrderId();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    //This is to refresh the order id: Only for the Sample App's purpose.
    @Override
    protected void onStart(){
        super.onStart();
        initOrderId();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    private void initOrderId() {
        String orderId = getOrderId();
        EditText orderIdEditText = (EditText) findViewById(R.id.order_id);
        orderIdEditText.setText(orderId);
    }

    @NonNull
    private String getOrderId() {
        Random r = new Random(System.currentTimeMillis());
        return "ORDER" + (1 + r.nextInt(2)) * 10000 + r.nextInt(10000);
    }

    public void onStartTransaction(View view) {

        /* These 3 values change for each transaction, need to be provided explicitly
        AND
        Should be passed same for checksum-generation and for paytm-param list*/
        final String orderId = getOrderId();
        final String custId = "C001";
        final String txnAmount = "1";

        GenerateOrderRequest paytmCheckSumRequest = getGenerateCheckSumRequest();
        /*paytmCheckSumRequest.setoRDERID(orderId);
        paytmCheckSumRequest.setCUSTID(custId);
        paytmCheckSumRequest.setTXNAMOUNT(txnAmount);*/


        /*MyWebService.newInstance().getGenerateOrderRequest(paytmCheckSumRequest)
                .enqueue(new NostragamusCallBack<GenerateOrderResponse>() {
                    @Override
                    public void onResponse(Call<GenerateOrderResponse> call, Response<GenerateOrderResponse> response) {
                        super.onResponse(call, response);

                        if (response != null && response.isSuccessful() && response.body() != null) {
                            GenerateOrderResponse generateOrderResponse = response.body();
                            if (generateOrderResponse != null && !TextUtils.isEmpty(generateOrderResponse.getCHECKSUMHASH())) {
                                onSuccessResponse(generateOrderResponse.getCHECKSUMHASH(), orderId, custId, txnAmount);

                            } else {
                                com.jeeva.android.Log.d(TAG, "CheckSumHash can not be empty!");
//                                showMessage(Alerts.API_FAIL);
                            }

                        } else {
                            com.jeeva.android.Log.d(TAG, "Generate checksum Response not successful / null");
//                            showMessage(Alerts.API_FAIL);
                        }
                    }
                });*/

    }

    /**
     * Object with default constant values / params for api-request
     * @return Generate checksum request
     */
    @NonNull
    private GenerateOrderRequest getGenerateCheckSumRequest() {
        GenerateOrderRequest paytmCheckSumRequest = new GenerateOrderRequest();
        /*paytmCheckSumRequest.setRequestType(PaytmParamValues.REQUEST_TYPE_DEFAULT);
        paytmCheckSumRequest.setEMAIL(PaytmParamValues.EMAIL_VALUE);
        paytmCheckSumRequest.setMOBILENO(PaytmParamValues.MOBILE_NO_VALUE);
        paytmCheckSumRequest.setCALLBACKURL(PaytmParamValues.CALLBACK_URL_VALUE);
        paytmCheckSumRequest.setCHANNELID(PaytmParamValues.CHANNEL_ID_VALUE);
        paytmCheckSumRequest.setINDUSTRYTYPEID(PaytmParamValues.INDUSTRY_TYPE_ID_VALUE);
        paytmCheckSumRequest.setWEBSITE(PaytmParamValues.WEBSITE_VALUE);
        paytmCheckSumRequest.setMID(PaytmParamValues.MID_VALUE);*/
        return paytmCheckSumRequest;
    }

    private void onSuccessResponse(String checkSumHash, String orderId, String custId, String amount) {
        PaytmPGService paytmPGService = PaytmPGService.getStagingService();

        if (BuildConfig.DEBUG) {
            paytmPGService.enableLog(this);

        } else {
             /* NOTE: Production service results into real transaction which results into originally payment done. * */
//            Service = PaytmPGService.getProductionService();
        }

        PaytmOrder order = new PaytmOrder(getParams(checkSumHash, orderId, custId, amount));

        /*PaytmMerchant merchant = new PaytmMerchant("https://api-stage.sportscafe.in/v2/game/generateChecksum",
                "https://api-stage.sportscafe.in/v2/game/verifyChecksum");*/

        paytmPGService.initialize(order, null);

        com.jeeva.android.Log.d(TAG, "Starting Paytm Transaction ...");
        paytmPGService.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        Log.d(TAG, "UI Error Occurred");
                    }

                    @Override
                    public void onTransactionResponse(Bundle bundle) {
                        Log.d(TAG, "Payment Transaction is successful " + bundle);
                        Toast.makeText(getApplicationContext(), "Payment Transaction is successful ",
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void networkNotAvailable() {
                        Log.d(TAG, "Network not available");
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        Log.d(TAG, "Client authentication failed");
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                        Log.d(TAG, "Error Loading page");
                    }

                    @Override
                    public void onBackPressedCancelTransaction() {
                        Log.d(TAG, "Back pressed cancel transaction");
                    }

                    @Override
                    public void onTransactionCancel(String s, Bundle bundle) {
                        Log.d(TAG, "Transaction cancelled");
                    }
                });

    }

    /**
     * A map having parameters to pass for Paytm transaction
     * @param checkSumHash - server generated checksumHash
     * @param orderId - Unique order id for each request
     * @param custId - Customer id
     * @param amount - Transaction amount
     * @return - map having params
     */
    private Map<String, String> getParams(String checkSumHash, String orderId, String custId, String amount) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PaytmParamsKeys.REQUEST_TYPE, PaytmParamValues.REQUEST_TYPE_DEFAULT);
        paramMap.put(PaytmParamsKeys.CHANNEL_ID, PaytmParamValues.CHANNEL_ID_VALUE);
        paramMap.put(PaytmParamsKeys.INDUSTRY_TYPE_ID, PaytmParamValues.INDUSTRY_TYPE_ID_VALUE);
        paramMap.put(PaytmParamsKeys.WEBSITE, PaytmParamValues.WEBSITE_VALUE);
        paramMap.put(PaytmParamsKeys.MID, PaytmParamValues.MID_VALUE);
        paramMap.put(PaytmParamsKeys.CALLBACK_URL, PaytmParamValues.CALLBACK_URL_VALUE);
        paramMap.put(PaytmParamsKeys.EMAIL, PaytmParamValues.EMAIL_VALUE);
        paramMap.put(PaytmParamsKeys.MOBILE_NO, PaytmParamValues.MOBILE_NO_VALUE);

        paramMap.put(PaytmParamsKeys.CHECKSUMHASH, checkSumHash);
        paramMap.put(PaytmParamsKeys.ORDER_ID, orderId);
        paramMap.put(PaytmParamsKeys.CUST_ID, custId);
        paramMap.put(PaytmParamsKeys.TXN_AMOUNT, amount);
        return paramMap;
    }
}

