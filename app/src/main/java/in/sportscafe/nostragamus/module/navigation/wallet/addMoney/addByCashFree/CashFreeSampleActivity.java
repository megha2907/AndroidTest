package in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByCashFree;

import com.jeeva.android.BaseActivity;
import com.jeeva.android.Log;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;

import com.gocashfree.cashfreesdk.CFClientInterface;
import com.gocashfree.cashfreesdk.CFPaymentService;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_APP_ID;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_NOTIFY_URL;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_ID;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_NOTE;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_PAYMENT_MODES;


import in.sportscafe.nostragamus.R;
import io.fabric.sdk.android.services.network.HttpRequest;

/**
 * Created by deepanshi on 2/14/18.
 */

public class CashFreeSampleActivity extends BaseActivity implements CFClientInterface {


     private long orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashfree_sample);
        // Generate random orderID
        long range = 100 * 1000 * 1000 - 1;
        Random r = new Random();

        // Between 100,000,000 and 199,999,999
        orderId = (long) (r.nextDouble() * range) + range + 1;

    }

    public void doPayment(View view) {


        /*
         * checksumUrl is the path to your hosted checksum calculation script.
         * For instructions on how to create checksumUrl script look at the
         * following url: http://docs.gocashfree.com/docs/v1/?php#mobile-app.
         */
        String checksumUrl = "http://api-stage02.sportscafe.in:4030/v3/game/cashfree/checksum?type=sort";

        /*
         * appId will be shared to you by CashFree over mail. This is a unique
         * identifier for your app. Please replace this appId with your appId.
         * Also, as explained below you will need to change your appId to prod
         * credentials before publishing your app.
         */
        String appId = "374123e1b8eb30b2ec8838d473";

        /*
         * stage allows you to switch between sandboxed and production servers
         * for CashFree Payment Gateway. The possible values are
         *
         * 1. TEST: Use the Test server. You can use this service while integrating
         *      and testing the CashFree PG. No real money will be deducted from the
         *      cards and bank accounts you use this stage. This mode is thus ideal
         *      for use during the development. You can use the cards provided here
         *      while in this stage: http://docs.gocashfree.com/docs/v1/#test-data
         *
         * 2. PROD: Once you have completed the testing and integration and successfully
         *      integrated the CashFree PG, use this value for stage variable. Then
         *      real credit/debit cards etc. can be used on CashFree PG as now
         *      the CashFreeSDK will be using production server. Ensure that the value of
         *      stage variable is PROD before publishing your app. When you switch to
         *      PROD you will need to update the appId to the Prod Credentials (which
         *      we will email to you separately).
         */
        String stage = "STAGE";

        Map<String, String> params = new HashMap<>();

        // Change this to reflect your own APP_ID. Refer CashfreeSDK Documentation or contact .
        params.put(PARAM_APP_ID, appId);
        params.put(PARAM_ORDER_ID, String.valueOf(orderId));
        params.put(PARAM_ORDER_AMOUNT, "10");
        params.put(PARAM_ORDER_NOTE, "Test");
        params.put(PARAM_CUSTOMER_NAME, "Deepanshi bajaj");
        params.put(PARAM_CUSTOMER_PHONE,"9582550819");
        params.put(PARAM_CUSTOMER_EMAIL, "deepanshi1503@gmail.com");
        params.put(PARAM_NOTIFY_URL, "http://api-stage02.sportscafe.in:4030/v3/game/cashfree/callback");
        params.put(PARAM_PAYMENT_MODES, "wallet");


        CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();

        // stage identifies whether you want trigger test or production service
        cfPaymentService.doPayment(this, params, checksumUrl, this, stage);
    }

    @Override
    public void onSuccess(Map<String, String> map) {
        Log.d("CFSDKSample", "Payment Success");
        for (Map.Entry entry : map.entrySet()) {
            Log.d("CFSKDSample Success-->", entry.getKey() + " " + entry.getValue());
        }
    }

    @Override
    public void onFailure(Map<String, String> map) {
        Log.d("CFSDKSample", "Payment Failure");

        for (Map.Entry entry : map.entrySet()) {
            Log.d("CFSKDSample Failure-->", entry.getKey() + " " + entry.getValue());
        }

    }

    @Override
    public void onNavigateBack() {
        Log.d("CFSDKSample", "Back Pressed");
    }

}
