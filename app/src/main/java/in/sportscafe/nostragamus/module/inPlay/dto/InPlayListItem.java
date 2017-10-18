package in.sportscafe.nostragamus.module.inPlay.dto;

import in.sportscafe.nostragamus.module.inPlay.adapter.InPlayAdapterItemType;

/**
 * Created by sandip on 13/09/17.
 */

public class InPlayListItem {

    private Object object;

    private int inPlayAdapterItemType = InPlayAdapterItemType.JOINED_CONTEST;

    public int getInPlayAdapterItemType() {
        return inPlayAdapterItemType;
    }

    public void setInPlayAdapterItemType(int inPlayAdapterItemType) {
        this.inPlayAdapterItemType = inPlayAdapterItemType;
    }

    public Object getItemData() {
        return object;
    }

    public void setItemData(Object object) {
        this.object = object;
    }
}
