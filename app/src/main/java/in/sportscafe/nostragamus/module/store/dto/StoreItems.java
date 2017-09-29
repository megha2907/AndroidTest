package in.sportscafe.nostragamus.module.store.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.HashMap;


/**
 * Created by deepanshi on 7/25/17.
 */

@Parcel
public class StoreItems {

    @SerializedName("product_id")
    private Integer productId;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("product_image")
    private String productImage;

    @SerializedName("product_desc")
    private String productDesc;

    @SerializedName("product_category")
    private String productCategory;

    @SerializedName("product_price")
    private Integer productPrice;

    @SerializedName("product")
    private HashMap<String, Integer> powerUps = new HashMap<>();

//    @SerializedName("inventory")
//    private Integer productInventory;

    @SerializedName("sale_info")
    private ProductSaleInfo productSaleInfo;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
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

    public Integer getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Integer productPrice) {
        this.productPrice = productPrice;
    }

//    public Integer getProductInventory() {
//        return productInventory;
//    }
//
//    public void setProductInventory(Integer productInventory) {
//        this.productInventory = productInventory;
//    }

    public ProductSaleInfo getProductSaleInfo() {
        return productSaleInfo;
    }

    public void setProductSaleInfo(ProductSaleInfo productSaleInfo) {
        this.productSaleInfo = productSaleInfo;
    }

    public HashMap<String, Integer> getPowerUps() {
        return powerUps;
    }

    public void setPowerUps(HashMap<String, Integer> powerUps) {
        this.powerUps = powerUps;
    }

}
