package in.sportscafe.nostragamus.module.user.group.allgroups;

import android.support.v7.widget.RecyclerView;

import com.jeeva.android.View;

/**
 * Created by deepanshi on 9/27/16.
 */

public interface AllGroupsView extends View {

    void goBackWithSuccessResult();

    void navigateToHomeActivity();

    void setAdapter(RecyclerView.Adapter adapter);

    void showGroupsEmpty();
}
