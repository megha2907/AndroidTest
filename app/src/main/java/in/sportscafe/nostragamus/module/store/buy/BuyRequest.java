package in.sportscafe.nostragamus.module.store.buy;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 27/07/17.
 */

public class BuyRequest {

    @SerializedName("product_id")
    private Integer productId;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}
