package in.sportscafe.nostragamus.module.navigation.settings;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;

import static io.fabric.sdk.android.services.concurrency.AsyncTask.init;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends BaseFragment implements View.OnClickListener{

    private static final String TAG = SettingsFragment.class.getSimpleName();

    private SettingsFragmentListener mSettingFragmentListener;

    public SettingsFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof  SettingsActivity) {
            mSettingFragmentListener = (SettingsFragmentListener) context;
        } else {
            throw new RuntimeException("Activity must implement " + TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {
        rootView.findViewById(R.id.settings_about_layout).setOnClickListener(this);
        rootView.findViewById(R.id.settings_send_feedback_layout).setOnClickListener(this);
        rootView.findViewById(R.id.settings_logout_layout).setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initialize();
    }

    private void initialize() {
        setFeedbackText();
    }

    private void setFeedbackText() {
        View view = getView();
        if (view != null) {
            TextView feedbackTextView = (TextView) view.findViewById(R.id.feedback_textView);
            feedbackTextView.setText(Html.fromHtml(getString(R.string.feedback_string)));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.settings_about_layout:
                if (mSettingFragmentListener != null) {
                    mSettingFragmentListener.onAboutNostragamusClicked();
                }
                break;

            case R.id.settings_send_feedback_layout:
                if (mSettingFragmentListener != null) {
                    mSettingFragmentListener.onSendFeedbackClicked();
                }
                break;

            case R.id.settings_logout_layout:
                if (mSettingFragmentListener != null) {
                    mSettingFragmentListener.onLogoutClicked();
                }
                break;
        }
    }
}
