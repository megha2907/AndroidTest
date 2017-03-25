package in.sportscafe.nostragamus.module.question.tourlist;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

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

        initToolBar();

        this.mTournamentPresenter = TourListPresenterImpl.newInstance(this, getSupportFragmentManager());
        this.mTournamentPresenter.onCreateTournaments();
    }

    public void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tournament_toolbar);
        toolbar.setTitle("Select a Match");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_icon_grey);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }

    @Override
    public void setAdapter(ViewPagerAdapter adapter) {
        CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.tournament_tab_vp);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tournament_tab_tl);
        tabLayout.setupWithViewPager(viewPager);
    }
}