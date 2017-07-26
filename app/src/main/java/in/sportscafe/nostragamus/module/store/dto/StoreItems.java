package in.sportscafe.nostragamus.module.store.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import in.sportscafe.nostragamus.module.allchallenges.dto.ConfigRewardDetails;

/**
 * Created by deepanshi on 7/25/17.
 */

@Parcel
public class StoreItems {

    @JsonProperty("product_id")
    private Integer productId;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("product_image")
    private String productImage;

    @JsonProperty("product_desc")
    private String productDesc;

    @JsonProperty("product_category")
    private String productCategory;

    @JsonProperty("product_price")
    private Integer productPrice;

//    @JsonProperty("inventory")
//    private Integer productInventory;

    @JsonProperty("sale_info")
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

}
