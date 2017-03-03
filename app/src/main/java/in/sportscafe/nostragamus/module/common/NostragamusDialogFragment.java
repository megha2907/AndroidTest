package in.sportscafe.nostragamus.module.common;

import android.os.Bundle;
import android.view.Window;

import com.jeeva.android.BaseDialogFragment;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;

/**
 * Created by Jeeva on 6/4/16.
 */
public class NostragamusDialogFragment extends BaseDialogFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.transparent);
        window.getAttributes().windowAnimations = R.style.DialogAnimation;
        window.getAttributes().dimAmount = 0.7f;
        setCancelable(false);
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