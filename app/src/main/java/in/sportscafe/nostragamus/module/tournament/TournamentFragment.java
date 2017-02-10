package in.sportscafe.nostragamus.module.tournament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.CustomViewPager;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;

/**
 * Created by deepanshi on 11/14/16.
 */

public class TournamentFragment extends NostragamusFragment implements TournamentView {

    private TournamentPresenter mTournamentPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tournament, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mTournamentPresenter = TournamentPresenterImpl.newInstance(this,getChildFragmentManager());
        this.mTournamentPresenter.onCreateTournaments();
    }

    @Override
    public void setAdapter(ViewPagerAdapter adapter) {
        CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.tournament_tab_vp);
        viewPager.setAdapter(adapter);

//        NavigationTabStrip mTopNavigationTabStrip = (NavigationTabStrip) findViewById(R.id.nts_bottom);
//        mTopNavigationTabStrip.setTabIndex(1, true);
//        mTopNavigationTabStrip.setViewPager(viewPager);
//        mTopNavigationTabStrip.setTitles(sportsArray);
//        mTopNavigationTabStrip.setHorizontalScrollBarEnabled(true);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tournament_tab_tl);
        tabLayout.setupWithViewPager(viewPager);
    }
}