package in.sportscafe.nostragamus.module.challengeCompleted.dto;

import in.sportscafe.nostragamus.module.challengeCompleted.adapter.CompletedChallengeAdapterItemType;
import in.sportscafe.nostragamus.module.inPlay.adapter.InPlayAdapterItemType;

/**
 * Created by deepanshi on 9/27/17.
 */

public class CompletedListItem {

    private Object object;

    private int completedAdapterItemType = CompletedChallengeAdapterItemType.COMPLETED_CONTEST;

    public int getCompletedAdapterItemType() {
        return completedAdapterItemType;
    }

    public void setCompletedAdapterItemType(int completedAdapterItemType) {
        this.completedAdapterItemType = completedAdapterItemType;
    }
    public Object getItemData() {
        return object;
    }

    public void setItemData(Object object) {
        this.object = object;
    }
}
