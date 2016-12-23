package in.sportscafe.nostragamus.module.user.sportselection.profilesportselection;

import android.support.v7.widget.RecyclerView;

import com.jeeva.android.View;

/**
 * Created by deepanshi on 12/8/16.
 */

public interface ProfileSportSelectionView extends View {

    void setAdapter(RecyclerView.Adapter adapter);

    void showToast();
}
