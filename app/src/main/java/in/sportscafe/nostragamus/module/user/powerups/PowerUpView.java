package in.sportscafe.nostragamus.module.user.powerups;

import android.support.v7.widget.RecyclerView;

import com.jeeva.android.View;

/**
 * Created by Deepanshi on 23/5/16.
 */
public interface PowerUpView extends View {

    void setAdapter(RecyclerView.Adapter adapter);
}