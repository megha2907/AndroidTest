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
        for (LBLanding lbLanding : lbSummary.getSports()) {
            lbLanding.setImgUrl(Sport.getSportImageUrl(lbLanding.getName(), 200, 200));
        }

        mLlLandingHolder.addView(getLandingRow(
                getResources().getString(R.string.sports),
                lbSummary.getSports(),
                mLlLandingHolder,
                true
        ));

        mLlLandingHolder.addView(getLandingRow(
                getResources().getString(R.string.groups),
                lbSummary.getGroups(),
                mLlLandingHolder,
                false
        ));
    }

    private View getLandingRow(String categoryName, List<LBLanding> lbList, ViewGroup parent, boolean needPadding) {
        View landingRow = getLayoutInflater().inflate(R.layout.inflater_lblanding_row, parent, false);

        ((TextView) landingRow.findViewById(R.id.lblanding_tv_category_name)).setText(categoryName);

        RecyclerView recyclerView = (RecyclerView) landingRow.findViewById(R.id.lblanding_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        LBLandingAdapter lbLandingAdapter = new LBLandingAdapter(getContext(), needPadding);
        lbLandingAdapter.addAll(lbList);
        recyclerView.setAdapter(lbLandingAdapter);

        return landingRow;
    }


    public void initToolBar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar((Toolbar) findViewById(R.id.lb_landing_toolbar));

        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

}