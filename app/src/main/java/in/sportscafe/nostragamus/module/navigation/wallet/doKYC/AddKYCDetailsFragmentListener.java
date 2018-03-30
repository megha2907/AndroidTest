package in.sportscafe.nostragamus.module.navigation.wallet.doKYC;

/**
 * Created by deepanshi on 3/28/18.
 */

public interface AddKYCDetailsFragmentListener {
    void navigateToAddPhotoActivity(int requestCode);
    void startPermissionActivity();
    void handleError(String msg, int status);
    void finishThisAndGotoWalletScreen();
}
