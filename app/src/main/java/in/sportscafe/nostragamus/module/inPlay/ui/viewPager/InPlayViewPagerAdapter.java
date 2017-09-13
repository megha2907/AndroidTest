package in.sportscafe.nostragamus.module.inPlay.ui.viewPager;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.newChallenges.dto.SportsTab;

/**
 * A simple {@link Fragment} subclass.
 */
public class InPlayViewPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<InPlayViewPagerFragment> tabFragments;

    public InPlayViewPagerAdapter(FragmentManager fm, ArrayList<InPlayViewPagerFragment> fragments) {
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

    public View getTabView(Context context, int position) {
        if (tabFragments != null && tabFragments.size() > position) {
            SportsTab sportsTab = tabFragments.get(position).getTabDetails();
            if (sportsTab != null) {

                TextView tabTextView = (TextView) LayoutInflater.from(context).inflate(R.layout.challenge_tab, null);
                tabTextView.setText(sportsTab.getSportsName());
                tabTextView.setCompoundDrawablesWithIntrinsicBounds(0, sportsTab.getSportIconDrawable(), 0, 0);
                return tabTextView;
            }
        }
        return null;
    }
}
