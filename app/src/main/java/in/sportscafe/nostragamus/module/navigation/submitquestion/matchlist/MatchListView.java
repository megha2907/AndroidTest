package in.sportscafe.nostragamus.module.navigation.submitquestion.matchlist;

import com.jeeva.android.InAppView;

/**
 * Created by deepanshi on 10/5/16.
 */
public interface MatchListView extends InAppView {

    void setAdapter(MatchListAdapter matchesAdapter);
}