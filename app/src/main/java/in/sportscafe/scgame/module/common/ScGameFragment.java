package in.sportscafe.scgame.module.common;

import com.jeeva.android.InAppFragment;

import in.sportscafe.scgame.AppSnippet;
import in.sportscafe.scgame.module.analytics.ScGameAnalytics;

/**
 * Created by Jeeva on 6/4/16.
 */
public class ScGameFragment extends InAppFragment {

    @Override
    public void onStart() {
        super.onStart();
        ScGameAnalytics.getInstance().startFragmentTrack(getActivity(),
                AppSnippet.getClassName(getClass()));
    }

    @Override
    public void onStop() {
        super.onStop();
        ScGameAnalytics.getInstance().stopFragmentTrack(getActivity(),
                AppSnippet.getClassName(getClass()));
    }
}