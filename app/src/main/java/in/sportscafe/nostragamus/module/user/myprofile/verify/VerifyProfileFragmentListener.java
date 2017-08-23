package in.sportscafe.nostragamus.module.user.myprofile.verify;

/**
 * Created by deepanshi on 7/18/17.
 */

public interface VerifyProfileFragmentListener {
    void onVerifyPhoneNumber(String phoneNumber);

    void onVerifyOTP(String otp);

    void onResendOTP(String phoneNumber);
}
