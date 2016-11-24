package in.sportscafe.scgame.module.user.points;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.squareup.picasso.Picasso;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.common.CustomViewPager;
import in.sportscafe.scgame.module.common.ScGameActivity;
import in.sportscafe.scgame.module.common.ViewPagerAdapter;
import in.sportscafe.scgame.module.user.sportselection.dto.Sport;

/**
 * Created by Jeeva on 10/6/16.
 */
public class PointsActivity extends ScGameActivity implements PointsView, View.OnClickListener {

    private PointsPresenter mPointsPresenter;

    private int mSelectedSportId;

     private Bundle mbundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

        mbundle=getIntent().getExtras();

        PointsActivity.this.mPointsPresenter = PointsPresenterImpl.newInstance(PointsActivity.this);
        PointsActivity.this.mPointsPresenter.onCreatePoints( getIntent().getExtras());
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

        mSelectedSportId = mbundle.getInt(Constants.BundleKeys.SPORT_ID);

        ImageView pointsIcon= (ImageView) findViewById(R.id.points_group_icon);

        if (mSelectedSportId!=0) {
            for (Sport sport : ScGameDataHandler.getInstance().getAllSports()) {
                if (sport.getId() == mSelectedSportId) {
                    Log.i("SPORTS","INSIDE");
                    pointsIcon.setBackgroundResource(sport.getImageResource());
                }

            }
        }
        else if (null==icon || icon.isEmpty()) {
            Log.i("NULL","INSIDE");
            pointsIcon.setImageDrawable(null);
            pointsIcon.setImageResource(R.drawable.placeholder_icon);
        }
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