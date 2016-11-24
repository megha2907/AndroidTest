package in.sportscafe.scgame.module.user.group.admin.approve;

import android.content.Context;

/**
 * Created by Jeeva on 2/7/16.
 */
public interface ApproveModel {

    ApproveAdapter init(Context context, long groupId, ApproveFragment.OnApproveAcceptedListener listener);
}