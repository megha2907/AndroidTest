package in.sportscafe.nostragamus.module.store.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepanshi on 7/26/17.
 */

public class StoreApiResponse {

    @SerializedName("data")
    private List<StoreSections> storeSections = new ArrayList<>();

    @SerializedName("data")
    public List<StoreSections> getStoreSections() {
        return storeSections;
    }

    @SerializedName("data")
    public void setStoreSections(List<StoreSections> storeSections) {
        this.storeSections = storeSections;
    }

}
