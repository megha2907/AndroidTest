package in.sportscafe.nostragamus.module.store.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 7/25/17.
 */


@Parcel
public class ProductSaleInfo {

    @SerializedName("sale")
    private Boolean saleOn;

    @SerializedName("sale_price")
    private Integer salePrice;

    @SerializedName("sale_percentage")
    private Integer salePercentage;

    @SerializedName("sale_description")
    private String saleDesc;

    public Boolean getSaleOn() {
        if (saleOn==null){
            return saleOn = false;
        }
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
        if (salePercentage==null){
            return salePercentage = 0;
        }
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
