package in.sportscafe.nostragamus.module.privateContest.ui.createContest.dto;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by sc on 26/3/18.
 */
@Parcel
public class PrivateContestAdvancePrizeScreenData {

    private double entryFee;
    private int entries;
    private List<PrizeListItemDto> prizeListItemDtos;

    public double getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(double entryFee) {
        this.entryFee = entryFee;
    }

    public void setEntries(int entries) {
        this.entries = entries;
    }

    public int getEntries() {
        return entries;
    }

    public List<PrizeListItemDto> getPrizeListItemDtos() {
        return prizeListItemDtos;
    }

    public void setPrizeListItemDtos(List<PrizeListItemDto> prizeListItemDtos) {
        this.prizeListItemDtos = prizeListItemDtos;
    }
}
