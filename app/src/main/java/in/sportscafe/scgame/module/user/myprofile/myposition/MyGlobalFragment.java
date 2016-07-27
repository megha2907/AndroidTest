package in.sportscafe.scgame.module.user.myprofile.myposition;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.common.ScGameFragment;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.GroupSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.RankSummary;
import in.sportscafe.scgame.module.user.points.PointsActivity;
import in.sportscafe.scgame.module.user.sportselection.dto.Sport;

/**
 * Created by Jeeva on 13/6/16.
 */
public class MyGlobalFragment extends ScGameFragment implements MyPositionLayout.OnRankClickListener {

    private static final String KEY_GLOBAL_SUMMARY = "keyGlobalSummary";

    public static MyGlobalFragment newInstance(GroupSummary globalSummary) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_GLOBAL_SUMMARY, globalSummary);

        MyGlobalFragment fragment = new MyGlobalFragment();
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

        updateRank((GroupSummary) getArguments().getSerializable(KEY_GLOBAL_SUMMARY));
    }

    public void updateRank(GroupSummary groupSummary) {
        if (null == groupSummary) {
            groupSummary = new GroupSummary(0L, "Global");
        }

        Map<Integer, RankSummary> rankSummaryMap = new HashMap<>();
        for (RankSummary rankSummary : groupSummary.getRanks()) {
            rankSummaryMap.put(rankSummary.getSportId(), rankSummary);
        }

        RankSummary rankSummary;
        LinearLayout llMyPositionParent = (LinearLayout) findViewById(R.id.my_position_parent);
        for (Sport sport : ScGameDataHandler.getInstance().getGlbFollowedSports()) {
            if (rankSummaryMap.containsKey(sport.getId())) {
                rankSummary = rankSummaryMap.get(sport.getId());
            } else {
                rankSummary = new RankSummary(sport.getId(), sport.getName());
            }

            rankSummary.setGroupId(0L);
            llMyPositionParent.addView(new MyPositionLayout(getContext(), rankSummary, this));
        }
    }

    @Override
    public void onClickRank(RankSummary rankSummary) {
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.BundleKeys.GROUP_ID, 0);
        bundle.putInt(Constants.BundleKeys.SPORT_ID, rankSummary.getSportId());

        navigateToPointsActivity(bundle);
    }

    private void navigateToPointsActivity(Bundle bundle) {
        Intent intent = new Intent(getContext(), PointsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}