package in.sportscafe.nostragamus.module.navigation.help.howtoplay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.navigation.help.howtoplay.dto.HowToPlay;

/**
 * Created by deepanshi on 2/22/18.
 */

public class HowToPlaySlidesFragment extends NostragamusFragment {

    private static final String HOW_TO_PLAY_DATA = "howToPlay";

    private HmImageView howToPlayImage;

    public static HowToPlaySlidesFragment newInstance(HowToPlay howToPlay) {
        Bundle args = new Bundle();
        args.putParcelable(HOW_TO_PLAY_DATA, Parcels.wrap(howToPlay));

        HowToPlaySlidesFragment fragment = new HowToPlaySlidesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_how_to_play_item, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        HowToPlay howToPlay = Parcels.unwrap(getArguments().getParcelable(HOW_TO_PLAY_DATA));

        ((TextView) findViewById(R.id.how_to_play_tv_title)).setText(howToPlay.getHeading());
        ((TextView) findViewById(R.id.how_to_play_tv_desc)).setText(howToPlay.getDesc());

        howToPlayImage = (HmImageView) findViewById(R.id.how_to_play_iv_image);
        howToPlayImage.setImageUrl(howToPlay.getImageUrl());
        howToPlayImage.setAlpha(0.1f);
        howToPlayImage.animate().alpha(1).setDuration(1000);
    }
}
