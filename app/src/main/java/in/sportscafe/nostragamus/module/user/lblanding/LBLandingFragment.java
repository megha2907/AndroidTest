package in.sportscafe.nostragamus.module.user.lblanding;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.customfont.CustomEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.IntentActions;
import in.sportscafe.nostragamus.Constants.LBLandingType;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.fuzzylbs.FuzzyLbFragment;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoard;
import in.sportscafe.nostragamus.module.user.sportselection.dto.Sport;

/**
 * Created by deepanshi on 1/19/17.
 */

public class LBLandingFragment extends NostragamusFragment implements LBLandingView,
        AdapterView.OnItemSelectedListener, CustomEditText.EditTextImeBackListener {

    private LinearLayout mLlLandingHolder;

    private TextView mTvTitle;

    private LBLandingPresenter mLbLandingPresenter;

    private FuzzyLbFragment mFuzzyLbFragment;

    public static int SORT_TYPE = 0;

    private LBLandingSummary mlbLandingSummary;

    private TextView mTvDummySearch;

    private FrameLayout mFlFuzzyHolder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_lb_landing, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initToolBar();

        this.mLbLandingPresenter = LBLandingPresenterImpl.newInstance(this);
        this.mLbLandingPresenter.onCreateLeaderBoard();

        mLlLandingHolder = (LinearLayout) findViewById(R.id.lb_landing_ll_holder);

        getChildFragmentManager().beginTransaction().replace(
                R.id.lb_landing_fl_fuzzy_holder,
                mFuzzyLbFragment = FuzzyLbFragment.newInstance()
        ).commit();

        mFlFuzzyHolder = (FrameLayout) findViewById(R.id.lb_landing_fl_fuzzy_holder);

        mTvDummySearch = (TextView) findViewById(R.id.fuzzy_players_tv_search);
        findViewById(R.id.lb_landing_rl_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFuzzy();
            }
        });

        mFlFuzzyHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFuzzy();
            }
        });
    }

    @Override
    public void sortLeaderBoards() {

        AppCompatSpinner spinner = (AppCompatSpinner) findViewById(R.id.lb_landing_sp);
        spinner.setOnItemSelectedListener(this);
        List categories = new ArrayList();
        categories.add("By Date Modified");//default
        categories.add("By Rank");
        categories.add("By Rank Change");
        categories.add("By Played Matches");


        ArrayAdapter dataAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, categories) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                Typeface externalFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto/RobotoCondensed-Regular.ttf");
                ((TextView) v).setTypeface(externalFont);

                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);

                Typeface externalFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto/RobotoCondensed-Regular.ttf");
                ((TextView) v).setTypeface(externalFont);

                return v;
            }
        };


        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.custom_radio_btn_spinner);
        spinner.setAdapter(dataAdapter);

    }

    @Override
    public void initMyPosition(LBLandingSummary lbSummary) {

        mLlLandingHolder.removeAllViews();

        mlbLandingSummary = lbSummary;

        List<LbLanding> lbLandingList = lbSummary.getLeaderBoardItems();


        if (null != lbLandingList && lbLandingList.size() > 0) {

            for (int i=0 ; i < lbLandingList.size() ; i++) {

                for (LbLanding lbLanding : lbSummary.getLeaderBoardItems()) {
                    lbLanding.setImgUrl(Sport.getSportImageUrl(lbLanding.getName(), 200, 200));
                }

                addLandingRow(
                        lbLandingList,
                        LBLandingType.SPORT,
                        false
                );
            }

        }

//        lbLandingList = lbSummary.getChallenges();
//
//        if (null != lbLandingList && lbLandingList.size() > 0) {
//            addLandingRow(
//                    lbLandingList,
//                    LBLandingType.CHALLENGE,
//                    false
//            );
//        }
//
//        lbLandingList = lbSummary.getGroups();
//
//        if (null != lbLandingList && lbLandingList.size() > 0) {
//            addLandingRow(
//                    lbLandingList,
//                    LBLandingType.GROUP,
//                    false
//            );
//        }


    }

    private void addLandingRow(List<LbLanding> lbList, String lbLandingType, boolean needPadding) {
        LBLandingRow landingRow = (LBLandingRow) getLayoutInflater().inflate(
                R.layout.inflater_lblanding_row,
                mLlLandingHolder,
                false
        );
        mLlLandingHolder.addView(landingRow);
        landingRow.init(lbList, lbLandingType, needPadding);
    }


    public void initToolBar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar((Toolbar) findViewById(R.id.lb_landing_toolbar));
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mFuzzyLbClickReceiver,
                new IntentFilter(IntentActions.ACTION_FUZZY_LB_CLICK));
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mFuzzyLbClickReceiver);
    }

    BroadcastReceiver mFuzzyLbClickReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if(null != bundle && bundle.containsKey(BundleKeys.LB_LANDING_KEY)) {
                closeFuzzy();
            }
            mLbLandingPresenter.onFuzzyLbClick(bundle);
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String item = parent.getItemAtPosition(position).toString();
        // hide selection text
        ((TextView) view).setText(null);

        if (mlbLandingSummary != null) {
            mLbLandingPresenter.sortLeaderBoardList(position);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public boolean onBack() {
        if(mFlFuzzyHolder.getVisibility() == View.VISIBLE) {
            closeFuzzy();
            return true;
        }
        return false;
    }

    private boolean mBackListenerSet = false;

    private void openFuzzy() {
        mFlFuzzyHolder.setVisibility(View.VISIBLE);
        mFuzzyLbFragment.showKeyboard();

        if(!mBackListenerSet) {
            mBackListenerSet = true;
            mFuzzyLbFragment.setOnEditTextImeBackListener(this);
        }
    }

    private void closeFuzzy() {
        mTvDummySearch.setText(mFuzzyLbFragment.getSearchKey());
        mFuzzyLbFragment.hideKeyboard();
        mFlFuzzyHolder.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onImeBack(CustomEditText ctrl, String text) {
        onBack();
    }
}