package in.sportscafe.nostragamus.module.whatsnew;

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
public class WhatsNewFragment extends NostragamusFragment {

    private static final String WHAT_NEW_DATA = "whatsNewData";

    public static WhatsNewFragment newInstance(OnBoardingDto onBoardingDto) {
        Bundle args = new Bundle();
        args.putParcelable(WHAT_NEW_DATA, Parcels.wrap(onBoardingDto));

        WhatsNewFragment fragment = new WhatsNewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_whatsnew, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        OnBoardingDto onBoardingDto = Parcels.unwrap(getArguments().getParcelable(WHAT_NEW_DATA));

        ((TextView) findViewById(R.id.onboard_tv_title)).setText(onBoardingDto.getTitle());
        ((TextView) findViewById(R.id.onboard_tv_desc)).setText(onBoardingDto.getDesc());
    }
}