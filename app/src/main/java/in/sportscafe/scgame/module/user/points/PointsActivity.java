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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.ScGameDataHandler;
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
import in.sportscafe.scgame.module.user.sportselection.dto.Sport;

/**
 * Created by Jeeva on 10/6/16.
 */
public class PointsActivity extends ScGameActivity implements PointsView, View.OnClickListener {

    private PointsPresenter mPointsPresenter;

    private Integer mSelectedSportId;

//    private Bundle mbundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

        PointsActivity.this.mPointsPresenter = PointsPresenterImpl.newInstance(PointsActivity.this);
        PointsActivity.this.mPointsPresenter.onCreatePoints( getIntent().getExtras());

//        mbundle=getIntent().getExtras();
    }

    @Override
    public void setName(String name) {
        ((TextView) findViewById(R.id.points_sp_name)).setText(name);
    }

    @Override
    public void initMyPosition(ViewPagerAdapter adapter, int selectedPosition) {
        CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.points_tab_vp);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.points_tab_tl);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(selectedPosition);
    }

    @Override
    public void setIcon(String icon) {

//        mSelectedSportId = mbundle.getInt(Constants.BundleKeys.SPORT_ID);

        ImageView pointsIcon= (ImageView) findViewById(R.id.points_group_icon);

        if (null==icon || icon.isEmpty()) {

            pointsIcon.setImageDrawable(null);
            pointsIcon.setImageResource(R.drawable.placeholder_icon);
        }
//        else if (mSelectedSportId!=0){
//
//            for (Sport sport: ScGameDataHandler.getInstance().getAllSports()){
//                if (sport.getId()==mSelectedSportId){
//
//                }
//                    pointsIcon.setBackgroundResource(sport.getImageResource());
//                }
//
//            }
        else
        {
            Picasso.with(getApplicationContext())
                    .load(icon)
                    .into(pointsIcon);

        }

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