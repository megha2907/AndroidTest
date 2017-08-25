package in.sportscafe.nostragamus.module.challenges;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;

import java.util.ArrayList;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.challenges.viewPager.ChallengeViewPagerAdapter;
import in.sportscafe.nostragamus.module.challenges.viewPager.ChallengeViewPagerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChallengesFragment extends BaseFragment {

    public ChallengesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_challenges2, container, false);
        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {
        TabLayout challengesTabLayout = (TabLayout) rootView.findViewById(R.id.challenge_tabs);
        ViewPager challengesViewPager = (ViewPager) rootView.findViewById(R.id.challenge_viewPager);

        challengesTabLayout.setupWithViewPager(challengesViewPager, true);

        int bucketCount = 10;
        setupViewPager(bucketCount);

        // TODO: sports count
        for (int temp = 0; temp < bucketCount; temp++) {
            challengesTabLayout.addTab(challengesTabLayout.newTab());
        }

        for (int temp = 0; temp < challengesTabLayout.getTabCount(); temp++) {
            challengesTabLayout.getTabAt(temp).setCustomView(getTab(challengesTabLayout));
        }

    }

    private void setupViewPager(int bucketCount) {
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        for (int temp = 0; temp < bucketCount; temp++) {
            fragmentList.add(new ChallengeViewPagerFragment());
        }

        ChallengeViewPagerAdapter viewPagerAdapter = new ChallengeViewPagerAdapter(getChildFragmentManager(), fragmentList);


    }

    private TextView getTab(TabLayout tabLayout) {
        TextView tabTextView = (TextView) LayoutInflater.from(tabLayout.getContext()).inflate(R.layout.challenge_tab, null);
        tabTextView.setText("ONE");
        tabTextView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.add_members_icon, 0, 0);
        return tabTextView;
    }

}
