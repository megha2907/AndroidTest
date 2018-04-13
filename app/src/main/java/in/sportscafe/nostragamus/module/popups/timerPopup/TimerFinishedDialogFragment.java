package in.sportscafe.nostragamus.module.popups.timerPopup;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.PopUpDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimerFinishedDialogFragment extends PopUpDialogFragment implements View.OnClickListener {

    private static final String TAG = TimerFinishedDialogFragment.class.getSimpleName();

    public interface TimerFinishedFragmentListener {
        void onActionButtonClicked();
    }

    private TimerFinishedFragmentListener mDialogFinishListener;
    private TimerFinishDialogScreenData mScreenData;

    public void setSuccessListener(TimerFinishedFragmentListener listener) {
        mDialogFinishListener = listener;
    }

    public static TimerFinishedDialogFragment newInstance(TimerFinishDialogScreenData screenData,
                                                          TimerFinishedFragmentListener listener) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.BundleKeys.TIMER_FINISHED_SCREEN_DATA, Parcels.wrap(screenData));

        TimerFinishedDialogFragment fragment = new TimerFinishedDialogFragment();
        fragment.setSuccessListener(listener);
        fragment.setArguments(bundle);
        return fragment;
    }

    public TimerFinishedDialogFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timer_finished, container, false);
        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {
        rootView.findViewById(R.id.time_finish_action_button).setOnClickListener(this);
        rootView.findViewById(R.id.popup_bg).setOnClickListener(this);
        rootView.findViewById(R.id.popup_cross_btn).setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setCancelable(false);
        initMembers();
        initValues();
    }

    private void initMembers() {
        Bundle args = getArguments();
        if (args != null) {
            mScreenData = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.TIMER_FINISHED_SCREEN_DATA));
        }
    }

    private void initValues() {
        View rootView = getView();
        if (mScreenData != null && rootView != null) {

            TextView dialogTitleTextView = (TextView) rootView.findViewById(R.id.timer_finish_dialog_title_textView);
            TextView msgTextView = (TextView) rootView.findViewById(R.id.timer_finish_dialog_message_textView);
            TextView subMessageTextView = (TextView) rootView.findViewById(R.id.timer_finish_dialog_sub_message_textView);
            HmImageView iconImageView = (HmImageView) rootView.findViewById(R.id.timer_finish_dialog_icon_imgView);

            if (!TextUtils.isEmpty(mScreenData.getDialogTitle())) {
                dialogTitleTextView.setText(mScreenData.getDialogTitle());
            }

            if (!TextUtils.isEmpty(mScreenData.getMessage())) {
                msgTextView.setText(mScreenData.getMessage());
            }

            if (!TextUtils.isEmpty(mScreenData.getSubMessage())) {
                subMessageTextView.setText(mScreenData.getSubMessage());
            }

            if (mScreenData.getIconResource() != 0) {
                iconImageView.setImageResource(mScreenData.getIconResource());

            } else if (!TextUtils.isEmpty(mScreenData.getIconImageUrl())) {
                iconImageView.setImageUrl(mScreenData.getIconImageUrl());

                HmImageView errIconImgView = (HmImageView) rootView.findViewById(R.id.err_indicator_icn_imgView);
                errIconImgView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.time_finish_action_button:
            case R.id.popup_cross_btn:
            case R.id.popup_bg:
                if (mDialogFinishListener != null) {
                    mDialogFinishListener.onActionButtonClicked();
                }
                dismiss();
                break;
        }
    }

}
