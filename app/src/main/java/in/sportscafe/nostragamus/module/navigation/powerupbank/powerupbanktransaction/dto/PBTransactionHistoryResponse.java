package in.sportscafe.nostragamus.module.navigation.powerupbank.powerupbanktransaction.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.navigation.powerupbank.powerupbanktransaction.dto.PBTransactionHistory;

/**
 * Created by deepanshi on 7/29/17.
 */

@Parcel
public class PBTransactionHistoryResponse {

    @SerializedName("data")
    private List<PBTransactionHistory> pbTransactionHistoryList = new ArrayList<>();

    @SerializedName("data")
    public List<PBTransactionHistory> getPbTransactionHistoryList() {
        return pbTransactionHistoryList;
    }

    @SerializedName("data")
    public void setPbTransactionHistoryList(List<PBTransactionHistory> pbTransactionHistoryList) {
        this.pbTransactionHistoryList = pbTransactionHistoryList;
    }

}

