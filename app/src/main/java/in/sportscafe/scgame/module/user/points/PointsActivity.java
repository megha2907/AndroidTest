package in.sportscafe.scgame.module.user.points;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.jeeva.android.Log;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.CustomViewPager;
import in.sportscafe.scgame.module.common.ScGameActivity;
import in.sportscafe.scgame.module.common.ViewPagerAdapter;
import in.sportscafe.scgame.module.TournamentFeed.dto.TournamentInfo;
import in.sportscafe.scgame.module.user.leaderboard.LeaderBoardFragment;
import in.sportscafe.scgame.module.user.leaderboard.dto.LeaderBoard;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.ChallengesTourSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.GroupsTourSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.TourSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.sports.SportsFragment;

/**
 * Created by Jeeva on 10/6/16.
 */
public class PointsActivity extends ScGameActivity implements PointsView, View.OnClickListener {

    private PointsPresenter mPointsPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

        PointsActivity.this.mPointsPresenter = PointsPresenterImpl.newInstance(PointsActivity.this);
        PointsActivity.this.mPointsPresenter.onCreatePoints( getIntent().getExtras());
    }

    @Override
    public void setName(String name) {
        ((TextView) findViewById(R.id.points_sp_name)).setText(name);
    }

    @Override
    public void initMyPosition(ViewPagerAdapter adapter, int selectedPosition) {
        Log.d("PointsActivity", "selectedPosition : " + selectedPosition);
        CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.points_tab_vp);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.points_tab_tl);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(selectedPosition);
    }

    @Override
    public void onClick(View view) {
        view.setEnabled(false);

        switch (view.getId()) {
            case R.id.points_back_icon:
                onBackPressed();
        }
    }
}