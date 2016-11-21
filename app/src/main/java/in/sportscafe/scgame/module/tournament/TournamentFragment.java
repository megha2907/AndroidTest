package in.sportscafe.scgame.module.tournament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.CustomViewPager;
import in.sportscafe.scgame.module.common.ScGameFragment;
import in.sportscafe.scgame.module.common.ViewPagerAdapter;
import in.sportscafe.scgame.module.home.OnHomeActionListener;
import in.sportscafe.scgame.module.tournamentFeed.TournamentFeedPresenter;
import in.sportscafe.scgame.module.tournamentFeed.TournamentFeedPresenterImpl;
import in.sportscafe.scgame.module.user.points.PointsActivity;
import in.sportscafe.scgame.module.user.points.PointsPresenterImpl;

/**
 * Created by deepanshi on 11/14/16.
 */

public class TournamentFragment extends ScGameFragment implements TournamentView {

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
    public void initMyPosition(ViewPagerAdapter adapter, int selectedPosition) {
        CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.tournament_tab_vp);
        viewPager.setAdapter(adapter);

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
