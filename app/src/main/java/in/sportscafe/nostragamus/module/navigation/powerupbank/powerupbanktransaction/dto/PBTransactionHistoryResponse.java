package in.sportscafe.nostragamus.module.navigation.powerupbank.powerupbanktransaction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.navigation.powerupbank.powerupbanktransaction.dto.PBTransactionHistory;

/**
 * Created by deepanshi on 7/29/17.
 */

@Parcel
public class PBTransactionHistoryResponse {

    @JsonProperty("data")
    private List<PBTransactionHistory> pbTransactionHistoryList = new ArrayList<>();

    @JsonProperty("data")
    public List<PBTransactionHistory> getPbTransactionHistoryList() {
        return pbTransactionHistoryList;
    }

    @JsonProperty("data")
    public void setPbTransactionHistoryList(List<PBTransactionHistory> pbTransactionHistoryList) {
        this.pbTransactionHistoryList = pbTransactionHistoryList;
    }

}

