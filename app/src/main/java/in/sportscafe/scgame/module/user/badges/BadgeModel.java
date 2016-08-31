package in.sportscafe.scgame.module.user.badges;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

/**
 * Created by deepanshi on 25/8/16.
 */
public interface BadgeModel {

    RecyclerView.Adapter getBadgeAdapter(Context context);
}
