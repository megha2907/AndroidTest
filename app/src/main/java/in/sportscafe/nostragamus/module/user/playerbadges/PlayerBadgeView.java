package in.sportscafe.nostragamus.module.user.playerbadges;

import android.support.v7.widget.RecyclerView;

import com.jeeva.android.InAppView;

/**
 * Created by deepanshi on 1/5/17.
 */

public interface PlayerBadgeView extends InAppView {

    void setAdapter(RecyclerView.Adapter adapter);
}