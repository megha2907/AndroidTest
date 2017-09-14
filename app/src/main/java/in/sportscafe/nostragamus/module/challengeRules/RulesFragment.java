package in.sportscafe.nostragamus.module.challengeRules;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.Log;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
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
    private ImageView IvContestRuleThree;

    private int powerUp2xCount = 0;
    private int powerUpNonNegsCount = 0;
    private int powerUpPlayerPollCount = 0;

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
        IvContestRuleThree = (ImageView) findViewById(R.id.contest_rule_three_iv);
        TvPowerUpsRuleOne = (TextView) findViewById(R.id.powerup_rule_one);
        TvPowerUpsRuleTwo = (TextView) findViewById(R.id.powerup_rule_two);
        TvCancelledRuleOne = (TextView) findViewById(R.id.cancelled_rule_one);
    }

    private void setInfo(Contest contest) {

        showContestRuleOne(contest);
        showContestRuleTwo(contest);
        showContestRuleThree(contest);

        showPowerUpsRuleOne(contest);
        showPowerUpsRuleTwo(contest);

        showCancelledRuleOne(contest);
    }

    private void showContestRuleOne(Contest contest) {

        tournamentsList = contest.getTournaments();

        if (tournamentsList != null && tournamentsList.size() > 0) {
            TvContestRuleOne.setText("This challenge contains matches from - " + tournamentsList.toString().replaceAll("[\\[\\](){}]", "")
                    + " and " + String.valueOf(contest.getTotalMatches()) + " matches in total");

//            TvContestRuleOne.setText("This challenge has "+ String.valueOf(contest.getTotalMatches()) + " from "
//                    +String.valueOf(contest.getTotalMatches())+" exciting games in "+String.valueOf(tournamentsList.size())
//                    +" tournaments - "+ tournamentsList.toString().replaceAll("[\\[\\](){}]", ""));
        } else {
            if (getView() != null) {
                getView().findViewById(R.id.contest_rule_one_rl).setVisibility(View.GONE);
            }
        }
    }

    private void showContestRuleTwo(Contest contest) {
        TvContestRuleTwo.setText("You can make and edit your predictions half an hr before the matches start");
    }

    private void showContestRuleThree(Contest contest) {
        if (contest.getContestTypeInfo() != null && contest.getContestModeInfo()!=null) {
            TvContestRuleThree.setText(contest.getContestTypeInfo().getName()
                    + " - " + contest.getContestTypeInfo().getDescription());

            if (contest.getContestModeInfo().getName().equalsIgnoreCase(Constants.ContestType.GUARANTEED)) {
                IvContestRuleThree.setBackgroundResource(R.drawable.guaranteed_icon);
            } else if (contest.getContestModeInfo().getName().equalsIgnoreCase(Constants.ContestType.POOL)) {
                IvContestRuleThree.setBackgroundResource(R.drawable.pool_icon);
            }else if (contest.getContestModeInfo().getName().equalsIgnoreCase(Constants.ContestType.NON_GUARANTEED)) {
                IvContestRuleThree.setBackgroundResource(R.drawable.no_guarantee_icon);
            }

        } else {
            if (getView() != null) {
                getView().findViewById(R.id.contest_rule_three_rl).setVisibility(View.GONE);
            }
        }
    }

    private void showPowerUpsRuleOne(Contest contest) {
        if (contest.getPowerUpsMap() != null) {
            setPowerUps(contest.getPowerUpsMap(), contest);
        } else {
            if (getView() != null) {
                getView().findViewById(R.id.powerups_rule_one_rl).setVisibility(View.GONE);
            }
        }
    }

    private void setPowerUps(HashMap<String, Integer> powerUpsMap, Contest contest) {

        if (powerUpsMap.get(Constants.Powerups.XX) != null) {
            powerUp2xCount = powerUpsMap.get(Constants.Powerups.XX);
        } else {
            if (getView() != null) {
                getView().findViewById(R.id.powerups_rule_one_rl).setVisibility(View.GONE);
            }
        }
        if (powerUpsMap.get(Constants.Powerups.NO_NEGATIVE) != null) {
            powerUpNonNegsCount = powerUpsMap.get(Constants.Powerups.NO_NEGATIVE);
        } else {
            if (getView() != null) {
                getView().findViewById(R.id.powerups_rule_one_rl).setVisibility(View.GONE);
            }
        }
        if (powerUpsMap.get(Constants.Powerups.AUDIENCE_POLL) != null) {
            powerUpPlayerPollCount = powerUpsMap.get(Constants.Powerups.AUDIENCE_POLL);
        } else {
            if (getView() != null) {
                getView().findViewById(R.id.powerups_rule_one_rl).setVisibility(View.GONE);
            }
        }

        LinearLayout powerUpLayout = (LinearLayout) findViewById(R.id.powerup_ll);
        ImageView powerUp2xImageView = (ImageView) findViewById(R.id.powerup_2x);
        ImageView powerUpNoNegativeImageView = (ImageView) findViewById(R.id.powerup_noNeg);
        ImageView powerUpAudienceImageView = (ImageView) findViewById(R.id.powerup_audience);

        TextView powerUp2xTextView = (TextView) findViewById(R.id.powerup_2x_count);
        TextView powerUpNoNegativeTextView = (TextView) findViewById(R.id.powerup_noNeg_count);
        TextView powerUpAudienceTextView = (TextView) findViewById(R.id.powerup_audience_count);


        if (powerUp2xCount == 0 && powerUpNonNegsCount == 0 && powerUpPlayerPollCount == 0) {
            powerUpLayout.setVisibility(View.GONE);
        } else {
            powerUpLayout.setVisibility(View.VISIBLE);

            if (powerUp2xCount != 0) {
                powerUp2xImageView.setBackgroundResource(R.drawable.double_powerup_small);
                powerUp2xImageView.setVisibility(View.VISIBLE);
                powerUp2xTextView.setText(String.valueOf(powerUp2xCount));
            } else {
                powerUp2xImageView.setVisibility(View.GONE);
                powerUp2xTextView.setVisibility(View.GONE);
            }

            if (powerUpNonNegsCount != 0) {
                powerUpNoNegativeImageView.setBackgroundResource(R.drawable.no_negative_powerup_small);
                powerUpNoNegativeImageView.setVisibility(View.VISIBLE);
                powerUpNoNegativeTextView.setText(String.valueOf(powerUpNonNegsCount));
            } else {
                powerUpNoNegativeImageView.setVisibility(View.GONE);
                powerUpNoNegativeTextView.setVisibility(View.GONE);
            }

            if (powerUpPlayerPollCount != 0) {
                powerUpAudienceImageView.setBackgroundResource(R.drawable.audience_poll_powerup_small);
                powerUpAudienceImageView.setVisibility(View.VISIBLE);
                powerUpAudienceTextView.setText(String.valueOf(powerUpPlayerPollCount));
            } else {
                powerUpAudienceImageView.setVisibility(View.GONE);
                powerUpAudienceTextView.setVisibility(View.GONE);
            }

            int totalPowerUps = powerUp2xCount + powerUpNonNegsCount + powerUpPlayerPollCount;

            TvPowerUpsRuleOne.setText("You have " + String.valueOf(totalPowerUps)
                    + " powerups to use across " + String.valueOf(contest.getTotalMatches())
                    + " matches. Use them to score higher!");

        }

    }

    private void showPowerUpsRuleTwo(Contest contest) {
        TvPowerUpsRuleTwo.setText("You can transfer a maximum of " + String.valueOf(contest.getMaxTransferPowerUps()) + " powerups each from the bank");
    }


    private void showCancelledRuleOne(Contest contest) {
        TvCancelledRuleOne.setText("Questions will be cancelled in case a player is injured or does not play.In these cases," +
                " half the points would be awarded for the questions.");
    }

    private void showEmptyScreen() {


    }

}
