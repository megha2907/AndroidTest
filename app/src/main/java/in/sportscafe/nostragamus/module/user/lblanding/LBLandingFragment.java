package in.sportscafe.nostragamus.module.user.lblanding;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.user.leaderboardsummary.LeaderBoardSummaryPresenter;
import in.sportscafe.nostragamus.module.user.leaderboardsummary.LeaderBoardSummaryPresenterImpl;
import in.sportscafe.nostragamus.module.user.leaderboardsummary.LeaderBoardSummaryView;
import in.sportscafe.nostragamus.module.user.myprofile.myposition.dto.LbSummary;

/**
 * Created by deepanshi on 1/19/17.
 */

public class LBLandingFragment extends NostragamusFragment implements LeaderBoardSummaryView {

    private RecyclerView horizontal_recycler_view;
    private LBSportsAdapter mAdapter;

    private Toolbar mtoolbar;
    private TextView mTitle;

    private LeaderBoardSummaryPresenter mleaderBoardSummaryPresenter;


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

        this.mleaderBoardSummaryPresenter = LeaderBoardSummaryPresenterImpl.newInstance(this);
        this.mleaderBoardSummaryPresenter.onCreateLeaderBoard();
        horizontal_recycler_view = (RecyclerView) findViewById(R.id.lb_landing_rv);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        horizontal_recycler_view.setLayoutManager(horizontalLayoutManagaer);
    }


    @Override
    public void initMyPosition(LbSummary lbSummary) {
        mAdapter = new LBSportsAdapter(lbSummary.getSports());
        horizontal_recycler_view.setAdapter(mAdapter);
    }


    public void initToolBar() {
        Typeface tftitle = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato/Lato-Regular.ttf");
        mtoolbar = (Toolbar) findViewById(R.id.lb_landing_toolbar);
        mTitle = (TextView) mtoolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("LEADERBOARDS");
        mTitle.setTypeface(tftitle);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

}