package in.sportscafe.scgame.module.user.points;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.ScGameActivity;
import in.sportscafe.scgame.module.user.leaderboard.LeaderBoardFragment;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.scgame.module.user.sportselection.dto.Sport;

/**
 * Created by Jeeva on 10/6/16.
 */
public class PointsActivity extends ScGameActivity implements PointsView, View.OnClickListener {

    private Spinner mSpGroup;

    private Spinner mSpSport;

    private TextView mTvWeek;

    private TextView mTvMonth;

    private TextView mTvAllTime;

    private PointsPresenter mPointsPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

        mSpGroup = (Spinner) findViewById(R.id.points_sp_group);
        mSpGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPointsPresenter.onGroupItemSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        mSpSport = (Spinner) findViewById(R.id.points_sp_sport);
        mSpSport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPointsPresenter.onSportItemSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        mTvWeek = (TextView) findViewById(R.id.points_tv_week);
        mTvMonth = (TextView) findViewById(R.id.points_tv_month);
        mTvAllTime = (TextView) findViewById(R.id.points_tv_all);

        LeaderBoardFragment leaderBoardFragment = new LeaderBoardFragment();

        final OnLeaderBoardUpdateListener listener = leaderBoardFragment;

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.points_fl_leaderboard, leaderBoardFragment).commit();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PointsActivity.this.mPointsPresenter = PointsPresenterImpl.newInstance(PointsActivity.this);
                PointsActivity.this.mPointsPresenter.onCreatePoints(listener, getIntent().getExtras());
            }
        }, 300);
    }

    @Override
    public void onClick(View view) {
        mTvAllTime.setEnabled(true);
        mTvMonth.setEnabled(true);
        mTvWeek.setEnabled(true);
        view.setEnabled(false);

        switch (view.getId()) {
            case R.id.points_tv_week:
                mPointsPresenter.onWeekClicked();
                break;
            case R.id.points_tv_month:
                mPointsPresenter.onMonthClicked();
                break;
            case R.id.points_tv_all:
                mPointsPresenter.onAllTimeClicked();
                break;
        }
    }

    @Override
    public void setGroupAdapter(ArrayAdapter<GroupInfo> groupAdapter, int initialGroupPosition) {
        if(null == groupAdapter) {
            mSpGroup.setVisibility(View.GONE);
        } else {
            mSpGroup.setAdapter(groupAdapter);
            mSpGroup.setSelection(initialGroupPosition);
        }
    }

    @Override
    public void setSportAdapter(ArrayAdapter<Sport> sportAdapter, int initialSportPosition) {
        mSpSport.setAdapter(sportAdapter);
        mSpSport.setSelection(initialSportPosition);
    }

    public void onBack(View view) {
        onBackPressed();
    }
}