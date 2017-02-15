package in.sportscafe.nostragamus.module.user.sportselection.profilesportselection;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

/**
 * Created by deepanshi on 12/8/16.
 */

public interface ProfileSportSelectionModel {

    RecyclerView.Adapter getSportsSelectionAdapter(Context context);

    void saveSelectedSports();
}
