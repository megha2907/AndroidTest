package in.sportscafe.nostragamus.module.play.powerup;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeeva.android.BaseFragment;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.play.powerup.info.PowerupBankInfoViewPagerFragment;
import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class PowerupBankTransferInfoFragment extends BaseFragment {

    private ViewPager mViewPager;
    private Fragment mFirstTimeVisitorFragment;
    private Fragment mAlwaysVisibleFragment;

    public PowerupBankTransferInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_powerup_bank_transfer_info, container, false);
        initRootView(view);
        return view;
    }

    private void initRootView(View view) {
        mFirstTimeVisitorFragment = getFirstTimeVisitorFragment();
        mAlwaysVisibleFragment = getAlwaysVisibleFragment();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(mFirstTimeVisitorFragment, "");
        viewPagerAdapter.addFragment(mAlwaysVisibleFragment, "");

        mViewPager = (ViewPager) view.findViewById(R.id.powerup_bank_info_viewPager);
        mViewPager.setAdapter(viewPagerAdapter);

        CircleIndicator circleIndicator = (CircleIndicator) view.findViewById(R.id.powerup_bank_info_pager_circle_indicator);
        circleIndicator.setViewPager(mViewPager);
    }

    private Fragment getAlwaysVisibleFragment() {
        Bundle args = getArguments();
        if (args == null) {
            args = new Bundle();
        }
        args.putInt(Constants.BundleKeys.POWERUP_BANK_INFO_SCREEN, PowerupBankInfoViewPagerFragment.InfoScreens.ALWAYS_VISIBLE_SCREEN);

        PowerupBankInfoViewPagerFragment fragment = new PowerupBankInfoViewPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Fragment getFirstTimeVisitorFragment() {
        Bundle args = getArguments();
        if (args == null) {
            args = new Bundle();
        }
        args.putInt(Constants.BundleKeys.POWERUP_BANK_INFO_SCREEN, PowerupBankInfoViewPagerFragment.InfoScreens.FIRST_TIME_VISITOR_SCREEN);

        PowerupBankInfoViewPagerFragment fragment = new PowerupBankInfoViewPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showAppropriateLayout();
    }

    private void showAppropriateLayout() {
        if (NostragamusDataHandler.getInstance().isBankInfoShown()) {
            mViewPager.setCurrentItem(1);   // Second layout to be shown always (but not for first time when user comes here)
        } else {
            NostragamusDataHandler.getInstance().setBankInfoShown(true);
        }
    }

    /**
     * Update info for view pager fragments
     * @param args
     */
    public void updateInfoDetails(Bundle args) {
        if (mAlwaysVisibleFragment != null) {
            ((PowerupBankInfoViewPagerFragment)mAlwaysVisibleFragment).updateInfoTexts(args);
        }
    }
}
