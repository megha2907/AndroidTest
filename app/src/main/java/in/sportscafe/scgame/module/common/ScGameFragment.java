package in.sportscafe.scgame.module.common;

import com.jeeva.android.InAppFragment;
import com.moe.pushlibrary.MoEHelper;

/**
 * Created by Jeeva on 6/4/16.
 */
public class ScGameFragment extends InAppFragment {

    @Override
    public void onStart() {
        super.onStart();
        MoEHelper.getInstance(getContext()).onFragmentStart(getActivity(), getClass().getSimpleName());
    }

    @Override
    public void onStop() {
        super.onStop();
        MoEHelper.getInstance(getContext()).onFragmentStop(getActivity(), getClass().getSimpleName());
    }
}