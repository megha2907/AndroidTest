package in.sportscafe.nostragamus.module.allchallenges.challenge;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Jeeva on 17/02/17.
 */

public interface ChallengeModel {

    void init(Bundle bundle);

    RecyclerView.Adapter getAdapter(Context context);

    void changeChallengesAdapterLayout(Boolean isSwipeView);
}