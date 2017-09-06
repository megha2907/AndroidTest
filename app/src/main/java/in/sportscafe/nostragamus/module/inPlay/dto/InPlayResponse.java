package in.sportscafe.nostragamus.module.inPlay.dto;

import org.parceler.Parcel;

import in.sportscafe.nostragamus.module.inPlay.adapter.InPlayAdapterItemType;
import in.sportscafe.nostragamus.module.newChallenges.adapter.NewChallengeAdapterItemType;

/**
 * Created by deepanshi on 9/6/17.
 */

@Parcel
public class InPlayResponse {

    private int inPlayAdapterItemType = InPlayAdapterItemType.IN_PLAY;

    public int getInPlayAdapterItemType() {
        return inPlayAdapterItemType;
    }

    public void setInPlayAdapterItemType(int inPlayAdapterItemType) {
        this.inPlayAdapterItemType = inPlayAdapterItemType;
    }
}
