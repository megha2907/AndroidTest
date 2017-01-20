package in.sportscafe.nostragamus.module.user.lblanding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import in.sportscafe.nostragamus.Constants.LBLandingType;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.user.sportselection.dto.Sport;

/**
 * Created by deepanshi on 1/19/17.
 */

public class LBLandingFragment extends NostragamusFragment implements LBLandingView {

    private LinearLayout mLlLandingHolder;

    private TextView mTvTitle;

    private LBLandingPresenter mLbLandingPresenter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_lb_landing, container, false);
        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initToolBar();

        this.mLbLandingPresenter = LBLandingPresenterImpl.newInstance(this);
        this.mLbLandingPresenter.onCreateLeaderBoard();

        mLlLandingHolder = (LinearLayout) findViewById(R.id.lb_landing_ll_holder);
    }


    @Override
    public void initMyPosition(LBLandingSummary lbSummary) {
        List<LBLanding> lbLandingList = lbSummary.getSports();
        if (null != lbLandingList && lbLandingList.size() > 0) {
            for (LBLanding lbLanding : lbSummary.getSports()) {
                lbLanding.setImgUrl(Sport.getSportImageUrl(lbLanding.getName(), 200, 200));
            }

            addLandingRow(
                    lbLandingList,
                    LBLandingType.SPORT_TYPE,
                    true
            );
        }

        lbLandingList = lbSummary.getGroups();
        if (null != lbLandingList && lbLandingList.size() > 0) {
            addLandingRow(
                    lbLandingList,
                    LBLandingType.GROUP_TYPE,
                    false
            );
        }

        lbLandingList = lbSummary.getChallenges();
        if (null != lbLandingList && lbLandingList.size() > 0) {
            addLandingRow(
                    lbLandingList,
                    LBLandingType.CHALLENGE_TYPE,
                    true
            );
        }
    }

    private void addLandingRow(List<LBLanding> lbList, int lbLandingType, boolean needPadding) {
        View landingRow = getLayoutInflater().inflate(R.layout.inflater_lblanding_row, mLlLandingHolder, false);
        mLlLandingHolder.addView(landingRow);

        ((TextView) landingRow.findViewById(R.id.lblanding_tv_category_name)).setText(getCategoryName(lbLandingType));

        RecyclerView recyclerView = (RecyclerView) landingRow.findViewById(R.id.lblanding_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        LBLandingAdapter lbLandingAdapter = new LBLandingAdapter(getContext(), lbLandingType, needPadding);
        lbLandingAdapter.addAll(lbList);
        recyclerView.setAdapter(lbLandingAdapter);
    }

    private String getCategoryName(int lbLandingType) {
        switch (lbLandingType) {
            case LBLandingType.SPORT_TYPE:
                return getResources().getString(R.string.sports);
            case LBLandingType.GROUP_TYPE:
                return getResources().getString(R.string.groups);
            case LBLandingType.CHALLENGE_TYPE:
                return getResources().getString(R.string.challenges);
        }
        return "";
    }


    public void initToolBar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar((Toolbar) findViewById(R.id.lb_landing_toolbar));

        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

}