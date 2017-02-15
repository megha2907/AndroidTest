package in.sportscafe.nostragamus.module.user.group.allgroups;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by deepanshi on 9/27/16.
 */

public interface AllGroupsModel {

    void init(Bundle bundle);

    boolean isAllGroups();

    void getAllGroups();

    RecyclerView.Adapter getGroupsAdapter(Context context, List<AllGroups> groupsList);

    void saveSelectedItem(Bundle bundle);

    void updateGroupInfo(Bundle bundle);

    void addNewGroup(Bundle bundle);
}