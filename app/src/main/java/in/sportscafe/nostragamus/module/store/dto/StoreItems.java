package in.sportscafe.nostragamus.module.store.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.HashMap;

import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PowerUp;


/**
 * Created by deepanshi on 7/25/17.
 */

@Parcel
public class StoreItems {

    @SerializedName("product_id")
    private int productId;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("product_image")
    private String productImage;

    @SerializedName("product_desc")
    private String productDesc;

    @SerializedName("product_category")
    private String productCategory;

    @SerializedName("product_price")
    private int productPrice;

    @SerializedName("product")
    private PowerUp powerUp;

//    @SerializedName("inventory")
//    private int productInventory;

    @SerializedName("sale_info")
    private ProductSaleInfo productSaleInfo;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }


    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

//    public int getProductInventory() {
//        return productInventory;
//    }
//
//    public void setProductInventory(int productInventory) {
//        this.productInventory = productInventory;
//    }

    public ProductSaleInfo getProductSaleInfo() {
        return productSaleInfo;
    }

    public void setProductSaleInfo(ProductSaleInfo productSaleInfo) {
        this.productSaleInfo = productSaleInfo;
    }

    public PowerUp getPowerUps() {
        return powerUp;
    }

    public void setPowerUps(PowerUp powerUp) {
        this.powerUp = powerUp;
    }
}
