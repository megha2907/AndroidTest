package in.sportscafe.nostragamus.module.user.group.allgroups;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

/**
 * Created by deepanshi on 9/27/16.
 */

public interface AllGroupsModel {

    void init();

    RecyclerView.Adapter getAllGroupsAdapter(Context context);
}
