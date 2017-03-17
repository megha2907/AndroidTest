package in.sportscafe.nostragamus.module.onboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;

/**
 * Created by Jeeva on 21/10/16.
 */
public class OnBoardingTextFragment extends NostragamusFragment {

    private static final String ONBOARD_DATA = "onboardData";

    public static OnBoardingTextFragment newInstance(OnBoardingDto onBoardingDto) {
        Bundle args = new Bundle();
        args.putParcelable(ONBOARD_DATA, Parcels.wrap(onBoardingDto));

        OnBoardingTextFragment fragment = new OnBoardingTextFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_onboard_text, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        OnBoardingDto onBoardingDto = Parcels.unwrap(getArguments().getParcelable(ONBOARD_DATA));

        ((TextView) findViewById(R.id.onboard_tv_title)).setText(onBoardingDto.getTitle());
        ((TextView) findViewById(R.id.onboard_tv_desc)).setText(onBoardingDto.getDesc());
    }
}