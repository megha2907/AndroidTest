package in.sportscafe.nostragamus.module.allchallenges.challenge;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jeeva.android.InAppView;

/**
 * Created by Jeeva on 17/02/17.
 */

public interface ChallengeView extends InAppView {

    void setAdapter(RecyclerView.Adapter adapter);
}