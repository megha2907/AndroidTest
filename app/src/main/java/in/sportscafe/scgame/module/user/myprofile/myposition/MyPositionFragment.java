package in.sportscafe.scgame.module.user.myprofile.myposition;

import android.os.Bundle;

import in.sportscafe.scgame.module.common.AbstractTabFragment;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.LbSummary;

/**
 * Created by Jeeva on 14/6/16.
 */
public class MyPositionFragment extends AbstractTabFragment {

    private static final String KEY_LB_SUMMARY = "keyLbSummary";

    public static MyPositionFragment newInstance(LbSummary lbSummary) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_LB_SUMMARY, lbSummary);

        MyPositionFragment fragment = new MyPositionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public ViewPagerAdapter getAdapter() {
        LbSummary lbSummary = (LbSummary) getArguments().getSerializable(KEY_LB_SUMMARY);

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        pagerAdapter.addFragment(MyLeaguesFragment.newInstance(lbSummary.getGroups()), "My Groups");
        pagerAdapter.addFragment(MyGlobalFragment.newInstance(lbSummary.getGlobal()), "Global");
        return pagerAdapter;
    }

}