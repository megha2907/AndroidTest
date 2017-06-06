package in.sportscafe.nostragamus.module.navigation.settings.about;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeeva.android.BaseFragment;

import in.sportscafe.nostragamus.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = AboutFragment.class.getSimpleName();

    private AboutFragmentListener mAboutFragmentListener;

    public AboutFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof AboutActivity) {
            mAboutFragmentListener = (AboutFragmentListener) context;
        } else {
            throw new RuntimeException("Activity must implement " + TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {
        rootView.findViewById(R.id.about_terms_of_services_layout).setOnClickListener(this);
        rootView.findViewById(R.id.about_nostragamus_layout).setOnClickListener(this);
        rootView.findViewById(R.id.about_privacy_layout).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.about_terms_of_services_layout:
                if (mAboutFragmentListener != null) {
                    mAboutFragmentListener.onTermsOfServiceClicked();
                }
                break;

            case R.id.about_nostragamus_layout:
                if (mAboutFragmentListener != null) {
                    mAboutFragmentListener.onAboutNostragamusClicked();
                }
                break;

            case R.id.about_privacy_layout:
                if (mAboutFragmentListener != null) {
                    mAboutFragmentListener.onPrivacyClicked();
                }
                break;
        }
    }
}
