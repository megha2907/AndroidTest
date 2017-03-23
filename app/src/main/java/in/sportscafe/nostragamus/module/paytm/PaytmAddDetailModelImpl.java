package in.sportscafe.nostragamus.module.paytm;

import android.os.Handler;
import android.util.Patterns;

import in.sportscafe.nostragamus.Nostragamus;

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

        callAddDetailApi(mobNumber, email);
    }

    private void callAddDetailApi(String mobileNumber, String email) {
        if (!Nostragamus.getInstance().hasNetworkConnection()) {
            mAddDetailModelListener.onNoInternet();
            return;
        }

        mAddDetailModelListener.onApiCallStarted();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!mAddDetailModelListener.onApiCallStopped()) {
                    return;
                }

                mAddDetailModelListener.onAddDetailSuccess();
            }
        }, 3000);
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