package in.sportscafe.nostragamus.module.user.points;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.CustomViewPager;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.user.leaderboard.LeaderBoardModelImpl;
import in.sportscafe.nostragamus.module.user.sportselection.dto.Sport;

import static in.sportscafe.nostragamus.R.id.view;

/**
 * Created by Jeeva on 10/6/16.
 */
public class PointsActivity extends NostragamusActivity implements PointsView, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private PointsPresenter mPointsPresenter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

        initSortingSpinner();

        this.mPointsPresenter = PointsPresenterImpl.newInstance(PointsActivity.this);
        this.mPointsPresenter.onCreatePoints(getIntent().getExtras());
    }

    private void initSortingSpinner() {
        List categories = new ArrayList();
        categories.add("By Rank");
        categories.add("By Accuracy");

        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, categories) {

            public View getView(int position, View convertView, ViewGroup parent) {
                return setExternalFont(super.getView(position, convertView, parent));
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                return setExternalFont(super.getDropDownView(position, convertView, parent));
            }
        };

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.custom_radio_btn_spinner);

        AppCompatSpinner spinner = (AppCompatSpinner) findViewById(R.id.points_sp_sport);
        spinner.setOnItemSelectedListener(this);
        spinner.setAdapter(dataAdapter);
    }

    private View setExternalFont(View spinnerView) {
        ((TextView) spinnerView).setTypeface(Typeface.createFromAsset(
                getContext().getAssets(),
                "fonts/roboto/RobotoCondensed-Regular.ttf"
        ));
        return spinnerView;
    }

    @Override
    public void setName(String name) {
        ((TextView) findViewById(R.id.points_sp_name)).setText(name);
    }

    @Override
    public void initMyPosition(ViewPagerAdapter adapter, int selectedPosition) {
        mViewPager = (CustomViewPager) findViewById(R.id.points_tab_vp);
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.points_tab_tl);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.setCurrentItem(selectedPosition);
    }

    @Override
    public void setIcon(String icon) {
        ((HmImageView) findViewById(R.id.points_group_icon)).setImageUrl(icon);
    }

    @Override
    public void onClick(View view) {
        view.setEnabled(false);

        switch (view.getId()) {
            case R.id.points_back_icon:
                LeaderBoardModelImpl.SORT_TYPE = 0;
                onBackPressed();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String item = parent.getItemAtPosition(position).toString();
        // hide selection text
        ((TextView) view).setText(null);

        ((TextView) findViewById(R.id.sorting_tv)).setText(item);

        if (mViewPager != null) {
            LeaderBoardModelImpl.SORT_TYPE = position;
            ((ViewPagerAdapter) mViewPager.getAdapter()).getItem(mViewPager.getCurrentItem()).setUserVisibleHint(true);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.LEADERBOARD_DETAIL;
    }
}