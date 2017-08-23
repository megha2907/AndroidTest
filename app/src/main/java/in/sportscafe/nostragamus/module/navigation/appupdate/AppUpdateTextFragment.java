package in.sportscafe.nostragamus.module.navigation.appupdate;

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

/**
 * Created by deepanshi on 6/2/17.
 */

public class AppUpdateTextFragment extends NostragamusFragment {

    private static final String APP_UPDATE_DATA = "appUpdate";

    private HmImageView mIvUpdateIn;

    private HmImageView mIvUpdateOut;

    public static AppUpdateTextFragment newInstance(AppUpdateDto appUpdateDto) {
        Bundle args = new Bundle();
        args.putParcelable(APP_UPDATE_DATA, Parcels.wrap(appUpdateDto));

        AppUpdateTextFragment fragment = new AppUpdateTextFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_app_text, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AppUpdateDto appUpdateDto = Parcels.unwrap(getArguments().getParcelable(APP_UPDATE_DATA));

        ((TextView) findViewById(R.id.app_update_tv_title)).setText(appUpdateDto.getTitle());
        ((TextView) findViewById(R.id.app_update_tv_desc)).setText(appUpdateDto.getDesc());

        mIvUpdateIn = (HmImageView) findViewById(R.id.update_app_iv_image_in);
        mIvUpdateIn.setImageUrl(appUpdateDto.getImageUrl());
        mIvUpdateIn.setAlpha(0.1f);
        mIvUpdateIn.animate().alpha(1).setDuration(1000);
    }
}