package in.sportscafe.nostragamus.module.navigation.help;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = HelpFragment.class.getSimpleName();

    private HelpFragmentListener mHelpFragmentListener;

    public HelpFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof HelpActivity) {
            mHelpFragmentListener = (HelpFragmentListener) context;
        } else {
            throw new RuntimeException("Activity must implement " + TAG);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_help, container, false);
        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {
        rootView.findViewById(R.id.help_rules_layout).setOnClickListener(this);
        rootView.findViewById(R.id.help_faq_layout).setOnClickListener(this);
        rootView.findViewById(R.id.help_game_play_layout).setOnClickListener(this);
        rootView.findViewById(R.id.help_sample_game_play_layout).setOnClickListener(this);
        rootView.findViewById(R.id.settings_send_feedback_layout).setOnClickListener(this);
        rootView.findViewById(R.id.help_chat_layout).setOnClickListener(this);
        rootView.findViewById(R.id.help_how_to_play_layout).setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setFeedbackText();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.help_rules_layout:
                if (mHelpFragmentListener != null) {
                    mHelpFragmentListener.onRulesClicked();
                }
                break;

            case R.id.help_faq_layout:
                if (mHelpFragmentListener != null) {
                    mHelpFragmentListener.onFaqClicked();
                }
                break;

            case R.id.help_game_play_layout:
                if (mHelpFragmentListener != null) {
                    mHelpFragmentListener.onGamePlayClicked();
                }
                break;

            case R.id.help_sample_game_play_layout:
                if (mHelpFragmentListener != null) {
                    mHelpFragmentListener.onPlaySampleGameClicked();
                }
                break;
            case R.id.settings_send_feedback_layout:
                if (mHelpFragmentListener != null) {
                    mHelpFragmentListener.onSendFeedbackClicked();
                }
                break;
            case R.id.help_chat_layout:
                if (mHelpFragmentListener != null) {
                    mHelpFragmentListener.onChatClicked();
                }
                break;
            case R.id.help_how_to_play_layout:
                if (mHelpFragmentListener != null) {
                    mHelpFragmentListener.onHowToPlayClicked();
                }
                break;

        }
    }

    private void setFeedbackText() {
        View view = getView();
        if (view != null) {
            TextView feedbackTextView = (TextView) view.findViewById(R.id.feedback_textView);
            feedbackTextView.setText(Html.fromHtml(getString(R.string.feedback_string)));
        }
    }
}
