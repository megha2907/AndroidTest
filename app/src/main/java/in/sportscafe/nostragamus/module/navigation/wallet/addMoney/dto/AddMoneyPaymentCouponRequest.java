package in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sc on 28/11/17.
 */

public class AddMoneyPaymentCouponRequest {

    @SerializedName("coupon_code")
    private String couponCode;

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
}
