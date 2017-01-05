package in.sportscafe.nostragamus.module.user.playerbadges;

import android.support.v7.widget.RecyclerView;

import com.jeeva.android.View;

/**
 * Created by deepanshi on 1/5/17.
 */

public interface PlayerBadgeView extends View{

    void setAdapter(RecyclerView.Adapter adapter);

    void showBadgesEmpty();
}
