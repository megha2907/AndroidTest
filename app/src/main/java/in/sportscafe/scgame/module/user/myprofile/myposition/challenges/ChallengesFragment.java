package in.sportscafe.scgame.module.user.myprofile.myposition.challenges;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import in.sportscafe.scgame.AppSnippet;
import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.ScGameFragment;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.BaseSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.ChallengesSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.ChallengesTourSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.GroupsTourSummary;
import in.sportscafe.scgame.module.user.points.PointsActivity;

/**
 * Created by Deepanshi on 13/10/16.
 */
public class ChallengesFragment extends ScGameFragment implements ChallengesLayout.OnRankClickListener, View.OnClickListener {

    private static final String KEY_CHALLENGES_SUMMARY_LIST = "keyChallengesSummaryList";

    public static ChallengesFragment newInstance(List<ChallengesSummary> challengesSummaryList) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_CHALLENGES_SUMMARY_LIST, (Serializable) challengesSummaryList);

        ChallengesFragment fragment = new ChallengesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_challenges, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        updateRank((List<ChallengesSummary>) getArguments().getSerializable(KEY_CHALLENGES_SUMMARY_LIST));
    }

    public List<BaseSummary> updateRank(List<ChallengesSummary> challengesSummaryList) {

        if (challengesSummaryList.isEmpty()){
            showChallengesSummaryEmpty();
        }

        if (null == challengesSummaryList) {
            challengesSummaryList = new ArrayList<>();
        }
        List<BaseSummary> tourSummaryList = new ArrayList<>();

        LinearLayout myLeagueParent = (LinearLayout) findViewById(R.id.challenges_ll);

        List<BaseSummary> tempTourSummaryList;

        ChallengesTourSummary newTourSummary;
        for (ChallengesSummary challengesSummary : challengesSummaryList) {
            tempTourSummaryList = new ArrayList<>();
            for (ChallengesTourSummary oldTourSummary : challengesSummary.getTourSummaryList()) {
                newTourSummary = new ChallengesTourSummary(oldTourSummary.getTournamentId(), oldTourSummary.getTournamentName(),oldTourSummary.getTournamentImageUrl(),
                        oldTourSummary.getRank(), oldTourSummary.getRankChange(), challengesSummary.getOverallRank(),
                        challengesSummary.getOverallRankChange(), challengesSummary.getChallengeId(), challengesSummary.getChallengeName(),oldTourSummary.getChallengePhoto());
                newTourSummary.setSummaryList(tempTourSummaryList);
                tempTourSummaryList.add(newTourSummary);
                myLeagueParent.addView(getLeagueView(myLeagueParent, newTourSummary));
            }

        }

        return tourSummaryList;
    }


    private View getLeagueView(ViewGroup parent, ChallengesTourSummary challengesTourSummary) {
        View convertView = getLayoutInflater().inflate(R.layout.inflater_challenges_tournaments_row, parent, false);
        convertView.setOnClickListener(this);

        convertView.setTag(challengesTourSummary);

        ((TextView) convertView.findViewById(R.id.challenges_tournaments_row_tv_name))
                .setText(challengesTourSummary.getChallengeName());

        TextView mTournamentRankTextView = (TextView) convertView.findViewById(R.id.challenges_tournaments_row_overall_rank);
        ImageView mTournamentRankStatus = (ImageView) convertView.findViewById(R.id.challenges_tournaments_row_rank_status);

        if (null == challengesTourSummary.getOverallRankChange()) {
            mTournamentRankStatus.setVisibility(View.INVISIBLE);
        } else if (challengesTourSummary.getOverallRankChange() < 0) {
            mTournamentRankStatus.setImageResource(R.drawable.status_arrow_down);
        } else {
            mTournamentRankStatus.setImageResource(R.drawable.status_arrow_up);
        }

        if (null == challengesTourSummary.getOverallRank()) {
            mTournamentRankTextView.setText("-");
        } else {
            String rank = AppSnippet.ordinal(challengesTourSummary.getRank());
            mTournamentRankTextView.setText(rank);
        }


        return convertView;
    }

    private void showChallengesSummaryEmpty() {

        TextView noSport= (TextView)findViewById(R.id.no_challenges_summary_tv);
        noSport.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClickRank(ChallengesTourSummary tourSummary) {
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.BundleKeys.GROUP_ID, 0);
        bundle.putInt(Constants.BundleKeys.SPORT_ID, 0);
        bundle.putInt(Constants.BundleKeys.CHALLENGE_ID, tourSummary.getChallengeId());
        navigateToPointsActivity(bundle);
    }

    private void navigateToPointsActivity(Bundle bundle) {
        Intent intent = new Intent(getContext(), PointsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        ChallengesTourSummary challengesTourSummary = (ChallengesTourSummary) v.getTag();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BundleKeys.LEADERBOARD_KEY, "challenge");
        bundle.putInt(Constants.BundleKeys.CHALLENGE_ID, challengesTourSummary.getChallengeId());
        bundle.putSerializable(Constants.BundleKeys.TOURNAMENT_SUMMARY,(ChallengesTourSummary) v.getTag());
        Intent intent = new Intent(v.getContext(), PointsActivity.class);
        intent.putExtras(bundle);
        v.getContext().startActivity(intent);

    }
}