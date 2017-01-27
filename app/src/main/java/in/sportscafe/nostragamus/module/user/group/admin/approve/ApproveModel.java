package in.sportscafe.nostragamus.module.user.group.admin.approve;

import android.content.Context;

/**
 * Created by Jeeva on 2/7/16.
 */
public interface ApproveModel {

    ApproveAdapter init(Context context, Integer groupId, ApproveFragment.OnApproveAcceptedListener listener);
}