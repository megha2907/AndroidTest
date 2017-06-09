package in.sportscafe.nostragamus.module.navigation.wallet.payoutDetails.dto;

import org.parceler.Parcel;

/**
 * Created by sandip on 08/06/17.
 */
@Parcel
public class PayoutAddEditItemDto {

    private int viewType;

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
