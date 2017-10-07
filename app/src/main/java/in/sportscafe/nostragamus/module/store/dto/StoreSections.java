package in.sportscafe.nostragamus.module.store.dto;

import com.google.gson.annotations.SerializedName;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepanshi on 7/25/17.
 */

@Parcel
public class StoreSections {

    @SerializedName("section_id")
    private int sectionId;

    @SerializedName("section_name")
    private String sectionName;

    @SerializedName("section_desc")
    private String sectionDesc;

    @SerializedName("products")
    private List<StoreItems> storeItemsList = new ArrayList<>();

    @SerializedName("section_id")
    public int getSectionId() {
        return sectionId;
    }

    @SerializedName("section_id")
    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    @SerializedName("section_name")
    public String getSectionName() {
        return sectionName;
    }

    @SerializedName("section_name")
    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    @SerializedName("section_desc")
    public String getSectionDesc() {
        return sectionDesc;
    }

    @SerializedName("section_desc")
    public void setSectionDesc(String sectionDesc) {
        this.sectionDesc = sectionDesc;
    }

    @SerializedName("products")
    public List<StoreItems> getStoreItemsList() {
        return storeItemsList;
    }

    @SerializedName("products")
    public void setStoreItemsList(List<StoreItems> storeItemsList) {
        this.storeItemsList = storeItemsList;
    }

}
