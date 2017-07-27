package in.sportscafe.nostragamus.module.store.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepanshi on 7/26/17.
 */

public class StoreApiResponse {

    @JsonProperty("data")
    private List<StoreSections> storeSections = new ArrayList<>();

    @JsonProperty("data")
    public List<StoreSections> getStoreSections() {
        return storeSections;
    }

    @JsonProperty("data")
    public void setStoreSections(List<StoreSections> storeSections) {
        this.storeSections = storeSections;
    }

}
