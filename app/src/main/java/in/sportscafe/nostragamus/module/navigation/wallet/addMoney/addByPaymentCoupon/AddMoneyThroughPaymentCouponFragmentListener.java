package in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByPaymentCoupon;

/**
 * Created by sc on 28/11/17.
 */

public interface AddMoneyThroughPaymentCouponFragmentListener {
    void onPaymentCouponFragmentBackPressed();
    void onPaytmMoneyAddSuccess();
    void onPaymentCouponSuccess(int moneyAdded);
}
