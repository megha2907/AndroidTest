package in.sportscafe.nostragamus.module.user.sportselection;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Jeeva on 27/5/16.
 */
public interface SportSelectionModel {

    RecyclerView.Adapter getSportsSelectionAdapter(Context context);

    void saveSelectedSports();

    boolean isUserLoggedIn();
}