package in.sportscafe.nostragamus.module.store.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 7/25/17.
 */


@Parcel
public class ProductSaleInfo {

    @SerializedName("sale")
    private boolean saleOn;

    @SerializedName("sale_price")
    private int salePrice;

    @SerializedName("sale_percentage")
    private float salePercentage;

    @SerializedName("sale_description")
    private String saleDesc;

    public boolean getSaleOn() {
        return saleOn;
    }

    public void setSaleOn(boolean saleOn) {
        this.saleOn = saleOn;
    }

    public int getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice;
    }

    public float getSalePercentage() {
        return salePercentage;
    }

    public void setSalePercentage(float salePercentage) {
        this.salePercentage = salePercentage;
    }

    public String getSaleDesc() {
        return saleDesc;
    }

    public void setSaleDesc(String saleDesc) {
        this.saleDesc = saleDesc;
    }

}
