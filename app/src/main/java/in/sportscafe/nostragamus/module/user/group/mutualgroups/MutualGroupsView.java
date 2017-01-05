package in.sportscafe.nostragamus.module.user.group.mutualgroups;

import android.support.v7.widget.RecyclerView;
import com.jeeva.android.View;

/**
 * Created by deepanshi on 1/4/17.
 */

public interface MutualGroupsView extends View {

    void goBackWithSuccessResult();

    void navigateToHomeActivity();

    void setAdapter(RecyclerView.Adapter adapter);

    void showGroupsEmpty();
}
