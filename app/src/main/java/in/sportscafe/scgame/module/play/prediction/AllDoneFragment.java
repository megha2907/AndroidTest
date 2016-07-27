package in.sportscafe.scgame.module.play.prediction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.ScGameFragment;

/**
 * Created by Jeeva on 16/6/16.
 */
public class AllDoneFragment extends ScGameFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_done, container, false);
    }
}