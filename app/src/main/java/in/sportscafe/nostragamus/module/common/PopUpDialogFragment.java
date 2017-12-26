package in.sportscafe.nostragamus.module.common;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;
import android.view.Window;

import com.jeeva.android.BaseDialogFragment;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;

/**
 * Created by deepanshi on 8/30/17.
 */

public class PopUpDialogFragment extends BaseDialogFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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

    public static void showDialogAllowingStateLoss(FragmentManager fragmentManager, DialogFragment dialogFragment, String tag) {

        if (fragmentManager != null && dialogFragment != null) {
            try {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.add(dialogFragment, tag);
                ft.commitAllowingStateLoss();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

