package in.sportscafe.nostragamus.module.store.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepanshi on 7/25/17.
 */

@Parcel
public class StoreSections {

    @JsonProperty("section_id")
    private Integer sectionId;

    @JsonProperty("section_name")
    private String sectionName;

    @JsonProperty("section_desc")
    private String sectionDesc;

    @JsonProperty("products")
    private List<StoreItems> storeItemsList = new ArrayList<>();

    @JsonProperty("section_id")
    public Integer getSectionId() {
        return sectionId;
    }

    @JsonProperty("section_id")
    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    @JsonProperty("section_name")
    public String getSectionName() {
        return sectionName;
    }

    @JsonProperty("section_name")
    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    @JsonProperty("section_desc")
    public String getSectionDesc() {
        return sectionDesc;
    }

    @JsonProperty("section_desc")
    public void setSectionDesc(String sectionDesc) {
        this.sectionDesc = sectionDesc;
    }

    @JsonProperty("products")
    public List<StoreItems> getStoreItemsList() {
        return storeItemsList;
    }

    @JsonProperty("products")
    public void setStoreItemsList(List<StoreItems> storeItemsList) {
        this.storeItemsList = storeItemsList;
    }

}
