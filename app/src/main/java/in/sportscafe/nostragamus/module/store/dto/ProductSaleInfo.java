package in.sportscafe.nostragamus.module.store.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 7/25/17.
 */


@Parcel
public class ProductSaleInfo {

    @JsonProperty("sale")
    private Boolean saleOn;

    @JsonProperty("sale_price")
    private Integer salePrice;

    @JsonProperty("sale_percentage")
    private Integer salePercentage;

    @JsonProperty("sale_description")
    private String saleDesc;

    public Boolean getSaleOn() {
        return saleOn;
    }

    public void setSaleOn(Boolean saleOn) {
        this.saleOn = saleOn;
    }

    public Integer getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Integer salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getSalePercentage() {
        return salePercentage;
    }

    public void setSalePercentage(Integer salePercentage) {
        this.salePercentage = salePercentage;
    }

    public String getSaleDesc() {
        return saleDesc;
    }

    public void setSaleDesc(String saleDesc) {
        this.saleDesc = saleDesc;
    }

}
