package in.sportscafe.nostragamus.module.play.powerup.info;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class PowerupBankInfoViewPagerFragment extends NostragamusFragment {

    public interface InfoScreens {
        int FIRST_TIME_VISITOR_SCREEN = 1;
        int ALWAYS_VISIBLE_SCREEN = 2;
    }

    private LinearLayout mContainer;

    public PowerupBankInfoViewPagerFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_powerup_bank_info_view_pager, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView) {
        mContainer = (LinearLayout) rootView.findViewById(R.id.powerup_bank_info_text_container_linear);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initContentToShow();
    }

    private void initContentToShow() {
        int screenInfo = InfoScreens.ALWAYS_VISIBLE_SCREEN;

        Bundle args = getArguments();
        if (args != null) {
            screenInfo = args.getInt(Constants.BundleKeys.POWERUP_BANK_INFO_SCREEN, InfoScreens.ALWAYS_VISIBLE_SCREEN);
        }

        inflatLayout(screenInfo);
    }

    private void inflatLayout(int screenInfo) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        switch (screenInfo) {
            case InfoScreens.FIRST_TIME_VISITOR_SCREEN:
                view = inflater.inflate(R.layout.powerup_bank_info_first_time_visitor_layout, null);
                break;

            default:
            case InfoScreens.ALWAYS_VISIBLE_SCREEN:
                view = inflater.inflate(R.layout.powerup_bank_info_always_visible_layout, null);
                break;
        }
        mContainer.addView(view);
    }

    /**
     * Update values
     * @param args
     */
    public void updateInfoTexts(Bundle args) {
        if (mContainer != null && mContainer.getChildCount() > 0 && args != null) {

            String challengeName = args.getString(Constants.BundleKeys.CHALLENGE_NAME, "");
            int maxLimit = args.getInt(Constants.BundleKeys.MAX_TRANSFER_COUNT, -1);

            String msg = "";
            if (maxLimit > 0) {
                msg = msg + "Max of " + maxLimit + " Powerup each can be added";
            }
            if (!TextUtils.isEmpty(challengeName)) {
                msg = msg + " to " + challengeName;
            }

            if (!TextUtils.isEmpty(msg)) {
                View view = mContainer.getChildAt(0);
                TextView msgTextView = (TextView) view.findViewById(R.id.powerup_bank_info_max_msg_textView);
                msgTextView.setText(msg);
            }
        }
    }
}
