package in.sportscafe.nostragamus.module.user.myprofile.myposition.sports;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.user.myprofile.myposition.dto.BaseSummary;
import in.sportscafe.nostragamus.module.user.myprofile.myposition.dto.SportSummary;
import in.sportscafe.nostragamus.module.user.myprofile.myposition.dto.TourSummary;
import in.sportscafe.nostragamus.module.user.points.PointsActivity;

/**
 * Created by Jeeva on 13/6/16.
 */
public class SportsFragment extends NostragamusFragment implements SportsLayout.OnRankClickListener {

    private static final String KEY_SPORT_SUMMARY = "keySportSummary";

    private SportsAdapter mAdapter;

    public static SportsFragment newInstance(List<SportSummary> sportSummaryList) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_SPORT_SUMMARY,(Serializable) sportSummaryList);

        SportsFragment fragment = new SportsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_position, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

       // updateRank((List<SportSummary>) getArguments().getSerializable(KEY_SPORT_SUMMARY));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_position_rv);
        mAdapter = new SportsAdapter(getContext(),updateRank((List<SportSummary>) getArguments().getSerializable(KEY_SPORT_SUMMARY)));
        mAdapter.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
            @Override
            public void onListItemExpanded(int position) {
            }

            @Override
            public void onListItemCollapsed(int position) {

            }
        });

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
    }

    public List<BaseSummary> updateRank(List<SportSummary> sportSummaryList) {

        if(sportSummaryList.isEmpty()){
            showSportSummaryEmpty();
        }

        if (null == sportSummaryList) {
            sportSummaryList = new ArrayList<>();
        }

        List<BaseSummary> tourSummaryList = new ArrayList<>();
        List<BaseSummary> tempTourSummaryList;

        TourSummary newTourSummary = null;
            for (SportSummary sportSummary : sportSummaryList) {

                tempTourSummaryList = new ArrayList<>();
                for (TourSummary oldTourSummary : sportSummary.getTourSummaryList()) {
                    newTourSummary = new TourSummary(oldTourSummary.getTournamentId(),
                            oldTourSummary.getTournamentName(),oldTourSummary.getTournamentImageUrl(),oldTourSummary.getRank(),
                            oldTourSummary.getRankChange(),sportSummary.getOverallRank(),
                            sportSummary.getOverallRankChange(),sportSummary.getSportsId(),sportSummary.getSportsName(),oldTourSummary.getSportPhoto());
                    newTourSummary.setSummaryList(tempTourSummaryList);
                    tempTourSummaryList.add(newTourSummary);

                }
                if (null!=newTourSummary){
                    tourSummaryList.add(newTourSummary);
                }
                newTourSummary=null;

            }

        if(tourSummaryList.isEmpty()){
            showSportSummaryEmpty();
        }

        return tourSummaryList;

    }

    @Override
    public void showSportSummaryEmpty() {
        TextView noSport= (TextView)findViewById(R.id.no_summary_tv);
        noSport.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClickRank(TourSummary tourSummary) {
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.BundleKeys.GROUP_ID, 0);
        bundle.putInt(Constants.BundleKeys.SPORT_ID, tourSummary.getSportId());

        navigateToPointsActivity(bundle);
    }

    private void navigateToPointsActivity(Bundle bundle) {
        Intent intent = new Intent(getContext(), PointsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}