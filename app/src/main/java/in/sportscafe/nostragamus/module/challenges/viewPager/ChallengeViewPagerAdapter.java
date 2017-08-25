package in.sportscafe.nostragamus.module.challenges.viewPager;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChallengeViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> tabFragments;

    public ChallengeViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        tabFragments = fragments;
    }

    @Override
    public int getCount() {
        return (tabFragments != null) ? tabFragments.size() : 0;
    }

    @Override
    public Fragment getItem(int position) {
        if (tabFragments != null) {
            return tabFragments.get(position);
        }
        return null;
    }
}
