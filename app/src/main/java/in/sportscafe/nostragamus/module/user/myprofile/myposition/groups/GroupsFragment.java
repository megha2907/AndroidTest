package in.sportscafe.nostragamus.module.user.myprofile.myposition.groups;

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
import in.sportscafe.nostragamus.module.user.group.groupselection.GroupSelectionFragment;
import in.sportscafe.nostragamus.module.user.myprofile.myposition.dto.BaseSummary;
import in.sportscafe.nostragamus.module.user.myprofile.myposition.dto.GroupSummary;
import in.sportscafe.nostragamus.module.user.myprofile.myposition.dto.GroupsTourSummary;
import in.sportscafe.nostragamus.module.user.points.PointsActivity;
import in.sportscafe.nostragamus.module.user.sportselection.profilesportselection.ProfileSportSelectionFragment;

/**
 * Created by Jeeva on 13/6/16.
 */
public class GroupsFragment extends NostragamusFragment implements GroupsLayout.OnRankClickListener {

    private static final String KEY_GROUP_SUMMARY = "keyGroupSummary";

    private GroupsAdapter mAdapter;


    public static GroupsFragment newInstance(List<GroupSummary> groupSummaryList) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_GROUP_SUMMARY,(Serializable) groupSummaryList);


        GroupsFragment fragment = new GroupsFragment();
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

        RecyclerView  mrecyclerView = (RecyclerView) findViewById(R.id.my_position_rv);
        mAdapter = new GroupsAdapter(getContext(), updateRank((List<GroupSummary>) getArguments().getSerializable(KEY_GROUP_SUMMARY)));
        mAdapter.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
            @Override
            public void onListItemExpanded(int position) {
            }

            @Override
            public void onListItemCollapsed(int position) {

            }
        });

        mrecyclerView.setAdapter(mAdapter);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
    }

    public List<BaseSummary> updateRank(List<GroupSummary> groupSummaryList) {

        if(groupSummaryList.isEmpty()) {
            showGroupSummaryEmpty();
        }

        if (null == groupSummaryList) {
            groupSummaryList = new ArrayList<>();
        }
        List<BaseSummary> tourSummaryList = new ArrayList<>();
        List<BaseSummary> tempTourSummaryList;

        GroupsTourSummary newTourSummary = null;
        for (GroupSummary groupSummary : groupSummaryList) {
            tempTourSummaryList = new ArrayList<>();

            for (GroupsTourSummary oldTourSummary : groupSummary.getTourSummaryList()) {
                newTourSummary = new GroupsTourSummary(oldTourSummary.getTournamentId(), oldTourSummary.getTournamentName(),
                        oldTourSummary.getTournamentImageUrl(), oldTourSummary.getRank() ,oldTourSummary.getRankChange(),
                        groupSummary.getOverallRank(),groupSummary.getOverallRankChange(),groupSummary.getGroupId(),
                        groupSummary.getGroupName(),oldTourSummary.getGroupIcon());
                newTourSummary.setSummaryList(tempTourSummaryList);

                tempTourSummaryList.add(newTourSummary);
            }
            if (null!=newTourSummary){
                tourSummaryList.add(newTourSummary);
            }
            newTourSummary=null;
        }

        if(tourSummaryList.isEmpty()){
            showGroupSummaryEmpty();
        }

        return tourSummaryList;
    }

    private void showGroupSummaryEmpty() {

        TextView noSport= (TextView)findViewById(R.id.no_summary_tv);
        noSport.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClickRank(GroupsTourSummary groupsTourSummary) {
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.BundleKeys.GROUP_ID, 0);
        bundle.putInt(Constants.BundleKeys.SPORT_ID, groupsTourSummary.getTournamentId());
        navigateToPointsActivity(bundle);
    }

    private void navigateToPointsActivity(Bundle bundle) {
        Intent intent = new Intent(getContext(), PointsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}