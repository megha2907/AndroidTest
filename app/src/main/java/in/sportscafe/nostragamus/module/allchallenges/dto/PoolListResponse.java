package in.sportscafe.nostragamus.module.allchallenges.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 31/03/17.
 */
public class PoolListResponse {

    @JsonProperty("data")
    private List<Pool> poolList = new ArrayList<>();

    @JsonProperty("data")
    public List<Pool> getPoolList() {
        return poolList;
    }

    @JsonProperty("data")
    public void setPoolList(List<Pool> poolList) {
        this.poolList = poolList;
    }
}