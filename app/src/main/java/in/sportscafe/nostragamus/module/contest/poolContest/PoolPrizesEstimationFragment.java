package in.sportscafe.nostragamus.module.contest.poolContest;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.contest.dto.PoolPrizeEstimationScreenData;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PoolPrizesEstimationFragment extends BaseFragment implements View.OnClickListener {

    private PoolPrizesEstimationFragmentListener mFragmentListener;
    protected PoolPrizeEstimationScreenData mScreenData;

    public PoolPrizesEstimationFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PoolPrizesEstimationFragmentListener) {
            mFragmentListener = (PoolPrizesEstimationFragmentListener) context;
        } else {
            throw new RuntimeException("Activity should implement " +
                    PoolPrizesEstimationFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_poll_contest_reward_calculation, container, false);
        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {
        rootView.findViewById(R.id.back_btn).setOnClickListener(this);
        rootView.findViewById(R.id.estimate_participants_number_button).setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initMembers();
        populateInitDetails();
    }

    private void initMembers() {
        Bundle args = getArguments();
        if (args != null) {
            mScreenData = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.POOL_PRIZE_ESTIMATION_SCREEN_DATA));
        }
    }

    private void populateInitDetails() {
        if (mScreenData != null && getView() != null) {

            // Show contest name on heading
            String contestName = mScreenData.getContestName();
            if (!TextUtils.isEmpty(contestName)) {
                TextView subHeadingTextView = (TextView) getView().findViewById(R.id.toolbar_heading_two);
                subHeadingTextView.setText(contestName);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                if (mFragmentListener != null) {
                    mFragmentListener.onBackClicked();
                }
                break;

            case R.id.estimate_participants_number_button:
                onParticipantsNumberButtonClicked(view);
                break;

        }
    }

    private void onParticipantsNumberButtonClicked(View view) {
        View popupView =  LayoutInflater.from(getContext()).inflate(R.layout.pool_prize_extimate_participants_number_window_layout, null, false);
        final PopupWindow popupWindow = new PopupWindow(popupView, 300, 500, true);

        /*Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
        btnDismiss.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popupWindow.dismiss();
            }});*/

        if (getView() != null) {
            LinearLayout anchorView = (LinearLayout) getView().findViewById(R.id.estimate_parent_layout);
            popupWindow.showAtLocation(anchorView, Gravity.CENTER, (int) view.getX(), (int) view.getY());
//            popupWindow.showAsDropDown(anchorView, 50, -30);
        }


    }
}
