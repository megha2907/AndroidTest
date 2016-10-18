package in.sportscafe.scgame.module.user.myprofile.myposition;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.common.ScGameFragment;
import in.sportscafe.scgame.module.TournamentFeed.dto.TournamentInfo;
import in.sportscafe.scgame.module.user.group.groupinfo.GroupInfoActivity;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.GroupSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.GroupsTourSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.RankSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.SportSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.TourSummary;
import in.sportscafe.scgame.module.user.points.PointsActivity;

/**
 * Created by Jeeva on 13/6/16.
 */
public class MyLeaguesFragment extends ScGameFragment implements MyGroupPositionLayout.OnRankClickListener,
        View.OnClickListener {

    private static final int CODE_GROUP_INFO = 23;

    private static final String KEY_GROUP_SUMMARY_LIST = "keyGroupSummaryList";

    public static MyLeaguesFragment newInstance(List<GroupSummary> groupSummaryList) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_GROUP_SUMMARY_LIST, (Serializable) groupSummaryList);

        MyLeaguesFragment fragment = new MyLeaguesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_my_leagues, container, false);
        return convertView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateRank((List<GroupSummary>) getArguments().getSerializable(KEY_GROUP_SUMMARY_LIST));
    }

    public void updateRank(List<GroupSummary> groupSummaryList) {
        if(null == groupSummaryList) {
            groupSummaryList = new ArrayList<>();
        }

        Map<Long, GroupSummary> grpSummaryMap = new HashMap<>();
        List<String> grpSportKeys = new ArrayList<>();
        for (GroupSummary groupSummary : groupSummaryList) {
            grpSummaryMap.put(groupSummary.getGroupId(), groupSummary);
           for (GroupsTourSummary tourSummary : groupSummary.getTourSummaryList()) {
                grpSportKeys.add(groupSummary.getGroupId() + "" + tourSummary.getTournamentId());
            }
        }

        List<GroupInfo> grpInfoList = new ArrayList<>(ScGameDataHandler.getInstance().getGrpInfoMap().values());
        LinearLayout myLeagueParent = (LinearLayout) findViewById(R.id.my_leagues_ll_positions);

        GroupSummary groupSummary;
        Long groupId;
        for (GroupInfo groupInfo : grpInfoList) {
            groupId = groupInfo.getId();

            if(grpSummaryMap.containsKey(groupId)) {
                groupSummary = grpSummaryMap.get(groupId);
            } else {
                groupSummary = new GroupSummary(groupId, groupInfo.getName());
            }

            for (TournamentInfo tournamentInfo : groupInfo.getFollowedTournaments()) {
                if(!grpSportKeys.contains(groupId + "" + tournamentInfo.getTournamentId())) {
                    //groupSummary.addRank(new GroupsTourSummary(tournamentInfo.getTournamentId(), tournamentInfo.getTournamentName(),tournamentInfo.getTournamentName(),tournamentInfo.getSportsName(),tournamentInfo.getTournamentId(),tournamentInfo.getTournamentId(),tournamentInfo.getTournamentId(),tournamentInfo.getTournamentId(),tournamentInfo.getTournamentId()));
                }
            }

            myLeagueParent.addView(getLeagueView(myLeagueParent, groupSummary));
        }
    }

    private View getLeagueView(ViewGroup parent, GroupSummary groupSummary) {
        View convertView = getLayoutInflater().inflate(R.layout.inflater_league_row, parent, false);
        convertView.findViewById(R.id.league_row_ibtn_options).setOnClickListener(this);

        ((TextView) convertView.findViewById(R.id.league_row_tv_name))
                .setText(groupSummary.getGroupName());

        ((Button) convertView.findViewById(R.id.league_row_circle_icon))
                .setText(groupSummary.getGroupName().substring(0,1));

        convertView.findViewById(R.id.league_row_ibtn_options).setTag(groupSummary);
//
//        List<RankSummary> ranks = groupSummary.getRanks();
//        if (null != ranks) {
//            LinearLayout llMyPositionParent = (LinearLayout) convertView.findViewById(R.id.league_row_ll_positions);
//            for (RankSummary rankSummary : ranks) {
//                rankSummary.setGroupId(groupSummary.getGroupId());
//                llMyPositionParent.addView(new MyPositionLayout(getContext(), rankSummary, this));
//            }
//        }
        return convertView;
    }

    @Override
    public void onClickRank(GroupsTourSummary tourSummary,GroupSummary groupSummary) {
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.BundleKeys.GROUP_ID, groupSummary.getGroupId());
        bundle.putInt(Constants.BundleKeys.SPORT_ID, tourSummary.getTournamentId());

        navigateToPointsActivity(bundle);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.league_row_ibtn_options:
                Bundle bundle = new Bundle();
                bundle.putLong(Constants.BundleKeys.GROUP_ID, ((GroupSummary) view.getTag()).getGroupId());

                navigateToGroupInfo(bundle);
                break;
        }
    }

    private void navigateToGroupInfo(Bundle bundle) {
        Intent intent = new Intent(getContext(), GroupInfoActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, CODE_GROUP_INFO);
    }

    private void navigateToPointsActivity(Bundle bundle) {
        Intent intent = new Intent(getContext(), PointsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }


}