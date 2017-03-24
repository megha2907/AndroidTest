package in.sportscafe.nostragamus.module.question.tourlist;

import android.os.Bundle;
import android.support.design.widget.TabLayout;

import in.sportscafe.nostragamus.Constants.ScreenNames;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.CustomViewPager;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;

/**
 * Created by deepanshi on 11/14/16.
 */
public class TourListActivity extends NostragamusActivity implements TourListView {

    private TourListPresenter mTournamentPresenter;

    @Override
    public String getScreenName() {
        return ScreenNames.QUESTION_ADD_TOUR_LIST;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_list);

        this.mTournamentPresenter = TourListPresenterImpl.newInstance(this, getSupportFragmentManager());
        this.mTournamentPresenter.onCreateTournaments();
    }

    @Override
    public void setAdapter(ViewPagerAdapter adapter) {
        CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.tournament_tab_vp);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tournament_tab_tl);
        tabLayout.setupWithViewPager(viewPager);
    }
}