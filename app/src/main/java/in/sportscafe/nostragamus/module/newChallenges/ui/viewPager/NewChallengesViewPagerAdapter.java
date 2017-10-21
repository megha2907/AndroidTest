package in.sportscafe.nostragamus.module.newChallenges.ui.viewPager;


import android.content.Context;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import java.util.ArrayList;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.newChallenges.dto.SportsTab;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewChallengesViewPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<NewChallengesViewPagerFragment> tabFragments;

    public NewChallengesViewPagerAdapter(FragmentManager fm, ArrayList<NewChallengesViewPagerFragment> fragments) {
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

                LinearLayout parentLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.challenge_tab, null);

                TextView tabTextView = (TextView) parentLayout.findViewById(R.id.tab_name);
                if (sportsTab.getChallengeCount() > 0) {
                    tabTextView.setText(sportsTab.getSportsName() + " (" + String.valueOf(sportsTab.getChallengeCount()) + ")");
                }else {
                    tabTextView.setText(sportsTab.getSportsName());
                }
                HmImageView tabImageView = (HmImageView) parentLayout.findViewById(R.id.tab_iv);

                if (sportsTab.getChallengeCount()==0){
                    tabImageView.setBackground(ContextCompat.getDrawable(context, sportsTab.getSportIconUnSelectedDrawable()));
                }else {
                    tabImageView.setBackground(ContextCompat.getDrawable(context, sportsTab.getSportIconDrawable()));
                }

                return parentLayout;
            }
        }
        return null;
    }
}