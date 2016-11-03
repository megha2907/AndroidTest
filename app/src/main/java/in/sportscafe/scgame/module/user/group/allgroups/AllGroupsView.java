package in.sportscafe.scgame.module.user.group.allgroups;

import android.support.v7.widget.RecyclerView;

import com.jeeva.android.View;

/**
 * Created by deepanshi on 9/27/16.
 */

public interface AllGroupsView extends View {

    void navigateToJoinGroup();

    void onClick(android.view.View view);

    void goBackWithSuccessResult();

    void setAdapter(RecyclerView.Adapter adapter);

    void showGroupsEmpty();
}
