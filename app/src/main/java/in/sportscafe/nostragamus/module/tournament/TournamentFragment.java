package in.sportscafe.nostragamus.module.tournament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.CustomViewPager;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;

/**
 * Created by deepanshi on 11/14/16.
 */

public class TournamentFragment extends NostragamusFragment implements TournamentView {

    private Toolbar mtoolbar;

    private ImageView mLogo;

    private TournamentPresenter mtournamentPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tournament, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mtournamentPresenter = TournamentPresenterImpl.newInstance(this,getChildFragmentManager());
        this.mtournamentPresenter.onCreateTournaments();

    }

    @Override
    public void initMyPosition(ViewPagerAdapter adapter, int selectedPosition,String[] sportsArray) {

        CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.tournament_tab_vp);
        viewPager.setAdapter(adapter);

//        NavigationTabStrip mTopNavigationTabStrip = (NavigationTabStrip) findViewById(R.id.nts_bottom);
//        mTopNavigationTabStrip.setTabIndex(1, true);
//        mTopNavigationTabStrip.setViewPager(viewPager);
//        mTopNavigationTabStrip.setTitles(sportsArray);
//        mTopNavigationTabStrip.setHorizontalScrollBarEnabled(true);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tournament_tab_tl);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(selectedPosition);
    }

    @Override
    public void setAdapter(ViewPagerAdapter adapter) {

    }

    public void initToolBar() {
        mtoolbar = (Toolbar) findViewById(R.id.tournament_toolbar);
        mLogo = (ImageView) mtoolbar.findViewById(R.id.toolbar_logo);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


}
