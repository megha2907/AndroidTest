package in.sportscafe.nostragamus.module.common;

import android.os.Bundle;

import com.jeeva.android.InAppFragment;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;

/**
 * Created by Jeeva on 6/4/16.
 */
public class NostragamusFragment extends InAppFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        NostragamusAnalytics.getInstance().startFragmentTrack(getActivity(),
                AppSnippet.getClassName(getClass()));
    }

    @Override
    public void onStop() {
        super.onStop();
        NostragamusAnalytics.getInstance().stopFragmentTrack(getActivity(),
                AppSnippet.getClassName(getClass()));
    }
}