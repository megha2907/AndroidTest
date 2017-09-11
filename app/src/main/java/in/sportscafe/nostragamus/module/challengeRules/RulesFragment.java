package in.sportscafe.nostragamus.module.challengeRules;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.Log;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.challenge.ChallengeFragment;
import in.sportscafe.nostragamus.module.contest.dto.Contest;
import in.sportscafe.nostragamus.module.user.myprofile.dto.Tournament;

/**
 * Created by deepanshi on 9/8/17.
 */

public class RulesFragment extends BaseFragment {

    private static final String TAG = RulesFragment.class.getSimpleName();

    private TextView TvContestRuleOne;
    private TextView TvContestRuleTwo;
    private TextView TvContestRuleThree;
    private TextView TvPowerUpsRuleOne;
    private TextView TvPowerUpsRuleTwo;
    private TextView TvCancelledRuleOne;
    private LinearLayout LlContestsTournaments;

    private List<String> tournamentsList = new ArrayList<>();

    public RulesFragment() {

    }

    public static RulesFragment newInstance(Contest contest) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.BundleKeys.CONTEST, Parcels.wrap(contest));
        RulesFragment fragment = new RulesFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rules, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();
        openBundle();
    }

    private void openBundle() {
        Bundle args = getArguments();
        if (args != null) {
            Contest contest = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.CONTEST));
            if (contest != null) {
                setInfo(contest);
            }
        } else {
            showEmptyScreen();
        }
    }

    private void initViews() {
        TvContestRuleOne = (TextView) findViewById(R.id.contest_rule_one);
        TvContestRuleTwo = (TextView) findViewById(R.id.contest_rule_two);
        TvContestRuleThree = (TextView) findViewById(R.id.contest_rule_three);
        TvPowerUpsRuleOne = (TextView) findViewById(R.id.powerup_rule_one);
        TvPowerUpsRuleTwo = (TextView) findViewById(R.id.powerup_rule_two);
        TvCancelledRuleOne = (TextView) findViewById(R.id.cancelled_rule_one);
        LlContestsTournaments = (LinearLayout) findViewById(R.id.contest_rule_tournaments_ll);
    }

    private void setInfo(Contest contest) {

        tournamentsList = contest.getTournaments();

        if (tournamentsList != null && tournamentsList.size() > 0) {
            TvContestRuleOne.setText("This challenge contains matches from - " + tournamentsList.toString().replaceAll("[\\[\\](){}]", "") + " and " +
                    String.valueOf(contest.getTotalMatches()) + " predictions in total");
        }

        TvContestRuleTwo.setText("You can make and edit your predictions half an hr before the matches start");

        TvContestRuleThree.setText(contest.getContestTypeInfo().getType() + " - " + contest.getContestTypeInfo().getDescription());

//        TvPowerUpsRuleOne.setText("You have "+ String.valueOf(mTotalPowerUps) +" to use across "+ String.valueOf(contest.getTotalMatches()) +" matches. Use them to score higher!");

        TvPowerUpsRuleTwo.setText("You can transfer a maximum of " + String.valueOf(contest.getMaxTransferPowerUps()) + " powerups each from the bank");

        TvCancelledRuleOne.setText("Questions will be cancelled in case a player is injured or does not play.In these cases," +
                " half the points would be awarded for the questions.");
    }

    private void showEmptyScreen() {


    }

}
