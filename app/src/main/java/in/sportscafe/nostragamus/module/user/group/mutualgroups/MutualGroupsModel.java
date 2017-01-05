package in.sportscafe.nostragamus.module.user.group.mutualgroups;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;

/**
 * Created by deepanshi on 1/4/17.
 */

public interface MutualGroupsModel {

    void init(Bundle bundle);

    RecyclerView.Adapter getMutualGroupsAdapter(Context context);
}
