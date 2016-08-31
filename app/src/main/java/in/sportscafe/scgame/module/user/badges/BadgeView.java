package in.sportscafe.scgame.module.user.badges;

import android.support.v7.widget.RecyclerView;

import com.jeeva.android.View;

/**
 * Created by Deepanshi on 23/5/16.
 */
public interface BadgeView extends View {

    void setAdapter(RecyclerView.Adapter adapter);
}