package in.sportscafe.nostragamus.module.store.buy;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sandip on 27/07/17.
 */

public class BuyRequest {

    @JsonProperty("product_id")
    private Integer productId;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}
