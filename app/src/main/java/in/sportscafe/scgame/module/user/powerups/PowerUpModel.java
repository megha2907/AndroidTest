package in.sportscafe.scgame.module.user.powerups;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

/**
 * Created by deepanshi on 23/8/16.
 */
public interface PowerUpModel {

    RecyclerView.Adapter getPowerUpAdapter(Context context);

}