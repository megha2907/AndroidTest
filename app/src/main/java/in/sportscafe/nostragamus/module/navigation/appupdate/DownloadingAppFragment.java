package in.sportscafe.nostragamus.module.navigation.appupdate;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jeeva.android.BaseFragment;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.OnDismissListener;

/**
 * Created by deepanshi on 6/14/17.
 */

public class DownloadingAppFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = DownloadingAppFragment.class.getSimpleName();

    private OnDismissListener mDismissListener;

    private static final int DISMISS_SCREEN = 58;

    private boolean mIsFirstBackPressed = false;
    public static final int DOUBLE_BACK_PRESSED_DELAY_ALLOWED = 3000;

    public DownloadingAppFragment() {
    }

    public static DownloadingAppFragment newInstance(String screenType) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BundleKeys.SCREEN, screenType);
        DownloadingAppFragment fragment = new DownloadingAppFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDismissListener) {
            mDismissListener = (OnDismissListener) context;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_downloading_app, container, false);
        initRootView(rootView, getArguments());
        return rootView;
    }

    private void initRootView(View rootView, Bundle bundle) {
        ImageView backBtn = (ImageView) rootView.findViewById(R.id.downloading_app_iv_back);
        backBtn.setOnClickListener(this);

        if (bundle.getString(Constants.BundleKeys.SCREEN) != null) {
            /* check if it's a What's NEW Screen or a Force Update Screen or a Normal Update Screen */
            if (bundle.getString(Constants.BundleKeys.SCREEN).equals(Constants.ScreenNames.APP_FORCE_UPDATE)) {
                backBtn.setVisibility(View.INVISIBLE);
            } else {
                backBtn.setVisibility(View.VISIBLE);
            }
        } else {
            backBtn.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.downloading_app_iv_back:
                mDismissListener.onDismiss(DISMISS_SCREEN, getArguments());
                break;

        }
    }
}
